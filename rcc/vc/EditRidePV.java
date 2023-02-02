package vc;

import person.Driver;
import resources.Images;
import ride.Ride;
import util.ImageButton;
import util.Location;
import util.Time;
import util.Util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import data.Data;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class EditRidePV extends Pane implements PaneInterface {
	
	private int max_cols = 4; //driver name, town, more, assign
	private int drivers_cap = 10; //max drivers suggested for ride
	
	protected Ride ride;
	protected DriverPV dpv;
	protected ClientPV cpv;
		
	protected GridPane infogp, results_view, driver_header;
	protected RowConstraints r;
	private ColumnConstraints more_c, assign_c;
	
	protected ImageButton x, new_ride;
	protected Label info_header, header, driver_title;
	
	protected static String [] field_names = {"start time", "end time", "ride date", 
			"pickup loc", "dest loc", "client", "driver", "description"};

	private LinkedHashMap<String, TextField> fields;
	
	private final static String[] driver_fields = {"name", "town"};
	private LinkedHashMap<String, Label> driver_labels;
	
	private List<Driver> driver_results;
	private Data data;
	private PlannerPane pp;
	
	
	public EditRidePV(PlannerPane pp, Data data) {
		this.pp = pp;
		this.data = data;
		
		addContent();
		addLayout();
		addEvents();
	}
	
	
	protected void changeProfile(Ride ride) {
		this.ride = ride;
		
		dpv.changeProfile(ride.getDriver());
		cpv.changeProfile(ride.getClient());
		
		fields.get("start time").setText(ride.getStartTime().toString());
		fields.get("end time").setText(ride.getEndTime().toString());
		fields.get("ride date").setText(Util.date_format.format(ride.getRideDate()));
		fields.get("pickup loc").setText(ride.getPickupLoc().toString());
		fields.get("dest loc").setText(ride.getDestLoc().toString());
		fields.get("client").setText(ride.getClient().getFullName());
		fields.get("driver").setText(ride.getDriver().getFullName());
		fields.get("description").setText(ride.getDescription());
		
		if (ride.getDriver().getFullName().equals(Driver.nullDriver.getFullName()))
			fields.get("driver").setText("");
		updateFields();
	}
	
	public void addContent() {
		dpv = new DriverPV();
		cpv = new ClientPV();
		
		x = new ImageButton(Images.closebtn);
	//	new_ride = new ImageButton(Images.newbtn);
		
		driver_results = new ArrayList<Driver>();
		fields = new LinkedHashMap<String, TextField>();
		driver_labels = new LinkedHashMap<String, Label>();
		
		header = new Label("Ride Details");
		info_header = new Label("Information");
		driver_title = new Label("Suggested Drivers");
		
		infogp = new GridPane();
		results_view = new GridPane();
		driver_header = new GridPane();
		
		for (int i = 0; i < driver_fields.length; i++) {
			Label field = new Label(Util.capitalize(driver_fields[i]));
			
			driver_labels.put(driver_fields[i], field);
			driver_header.add(field, i, 0);
		}
		
		for (int i = 0; i < field_names.length; i++) {
			TextField tf = new TextField();
			tf.setOnMouseClicked(a->{
				tf.requestFocus();
			});
			fields.put(field_names[i], tf);
			
			Label field_title = new Label(Util.capitalize(field_names[i]));
			
			infogp.add(field_title, 0, i);
			infogp.add(tf, 1, i);		
		}
		fields.get("driver").setPromptText("Search Driver here and press Enter");
		
		this.getChildren().addAll(infogp, results_view, driver_header, info_header, driver_title, header, x, dpv, cpv);
	}
	
	
	public void addLayout() {
		dpv.setVisible(false);
		cpv.setVisible(false);
		
		infogp.setStyle("-fx-background-color: #dddeee; -fx-border-width: 0.5; -fx-border-color: black ;");
		results_view.setStyle("-fx-background-color: #dddeee; -fx-border-width: 0.5; -fx-border-color: black ;");
		this.setStyle("-fx-background-color: rgba(255, 255, 255, .98); -fx-border-width: 0.5; -fx-border-color: black ;");
		
		info_header.setAlignment(Pos.CENTER);
		info_header.setTextAlignment(TextAlignment.CENTER);
		header.setAlignment(Pos.CENTER);
		header.setTextAlignment(TextAlignment.CENTER);
		driver_title.setAlignment(Pos.CENTER);
		driver_title.setTextAlignment(TextAlignment.CENTER);
		
		r = new RowConstraints();
		ColumnConstraints c = new ColumnConstraints();
		c.setHgrow(Priority.ALWAYS);
				
		for (int i = 0; i < field_names.length; i++) {
			infogp.getRowConstraints().add(r);	
		}
		
		infogp.setAlignment(Pos.BASELINE_LEFT);
		infogp.getColumnConstraints().addAll(c, c);
		
		results_view.setAlignment(Pos.BASELINE_CENTER);
		results_view.setHgap(1);
		results_view.setVgap(1);
		results_view.setStyle("-fx-background-color: #dddddd;");
		results_view.getColumnConstraints().clear();
		
		driver_header.setAlignment(Pos.CENTER_LEFT);
		driver_header.setHgap(1);
		driver_header.setVgap(1);
		driver_header.setStyle("-fx-background-color: #dddddd;");
		driver_header.getColumnConstraints().clear();
		driver_header.getRowConstraints().add(r);
		
		for (int i = 0; i < max_cols-1; i++) {
			ColumnConstraints c2 = new ColumnConstraints();
			c2.setFillWidth(true);
			c2.setHgrow(Priority.ALWAYS);
			results_view.getColumnConstraints().add(c2);			
		}
		
		more_c = new ColumnConstraints();
		more_c.setFillWidth(true);
		more_c.setHgrow(Priority.SOMETIMES);
		assign_c = new ColumnConstraints();
		assign_c.setFillWidth(true);
		assign_c.setHgrow(Priority.SOMETIMES);
		results_view.getColumnConstraints().addAll(assign_c, more_c);
		
		Iterator<Entry<String, Label>> iterator = driver_labels.entrySet().iterator();
		for (int i = 0; i < driver_labels.size(); i++) { //add layout to header labels
			Label title = iterator.next().getValue();
			title.setAlignment(Pos.CENTER);
			title.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			title.setStyle("-fx-background-color: #ffffff;");
			GridPane.setHalignment(title, HPos.LEFT);
		}
			
	}
	
	public void addEvents() {
		x.setOnMouseClicked(m->{
			updateFields();
			this.setVisible(false);
		});
		this.setOnKeyPressed(k->{
			if (k.getCode() == KeyCode.ENTER) {
				this.requestFocus();
				updateFields();
			}
		});
	
		this.setOnMouseClicked(m->{
		//	this.requestFocus();
		});
	}

	
	public void resize(int width, int height, int k) {
		x.resize(k, k);
				
		x.setLayoutY(5);
		
		dpv.resize(width, height, k);
		cpv.resize(width, height, k);
		
		r.setMaxHeight(k);
		r.setPrefHeight(k*0.8);
		
		header.setFont(Font.font(k));
		header.setMinWidth(k*8);
		info_header.setFont(Font.font(k*0.6));
		info_header.setMinWidth(k*5);
		driver_title.setFont(Font.font(k*0.6));
		driver_title.setMinWidth(k*5);
		
		infogp.setMinSize(width/2, height-k*4);
		infogp.setMaxSize(infogp.getMinWidth(), infogp.getMinHeight());
		infogp.setVgap(k/3);
		infogp.setPadding(new Insets(k/3));
		
		this.layout();
		
		results_view.setMinSize(width-infogp.getLayoutBounds().getWidth()-k*4, infogp.getLayoutBounds().getHeight()-r.getPrefHeight());
		results_view.setMaxSize(results_view.getMinWidth(), results_view.getMinHeight());
		results_view.setPadding(new Insets(2));
		
		this.layout();	
		driver_header.setMinWidth(results_view.getLayoutBounds().getWidth());
		driver_header.setPadding(new Insets(2));
		
		dpv.setLayoutX(width/2 - dpv.getLayoutBounds().getWidth()/2);
		dpv.setLayoutY(height/2 - dpv.getLayoutBounds().getHeight()/2);	
		cpv.setLayoutX(width/2 - cpv.getLayoutBounds().getWidth()/2);
		cpv.setLayoutY(height/2 - cpv.getLayoutBounds().getHeight()/2);
		
		this.layout();
		
		infogp.setLayoutX(k);
		infogp.setLayoutY(height/2-infogp.getLayoutBounds().getHeight()/2+k);
		results_view.setLayoutX(width-results_view.getLayoutBounds().getWidth()-k);
		results_view.setLayoutY(infogp.getLayoutY()+r.getPrefHeight());
		
		header.setLayoutX(width/2-header.getMinWidth()/2);
		header.setLayoutY(k/2);
		info_header.setLayoutX(infogp.getLayoutX()+infogp.getLayoutBounds().getWidth()/2-
				info_header.getMinWidth()/2);
		info_header.setLayoutY(infogp.getLayoutY()-info_header.getFont().getSize()-5);
		driver_title.setLayoutX(results_view.getLayoutX()+results_view.getLayoutBounds().getWidth()/2-
				driver_title.getMinWidth()/2);
		
		driver_header.setLayoutX(results_view.getLayoutX());
		driver_header.setLayoutY(results_view.getLayoutY()-r.getPrefHeight());
		
		driver_title.setLayoutY(driver_header.getLayoutY()-driver_title.getFont().getSize()-5);

		x.setLayoutX(width-x.getLayoutBounds().getWidth()-5);
		
		this.setMinSize(width, height);
		this.setMaxSize(width, height);
	}
	
	
	
	private void updateFields() {
		String startTime = fields.get("start time").getText();
		String endTime = fields.get("end time").getText();
		String rideDate = fields.get("ride date").getText();
		String pickupLoc = fields.get("pickup loc").getText();
		String destLoc = fields.get("dest loc").getText();
		String client = fields.get("client").getText();
		String driver = fields.get("driver").getText();
		String desc = fields.get("description").getText();

		
		if (driver.equals("") || driver.equals(Driver.nullDriver.getFullName()))
			searchDrivers(ride);
		else
			searchDrivers(driver);
		
		ride.setDescription(desc);
		
		String names[] = client.split("\\s+", 2);
		
		ride.getClient().setLastName("");
		if (names.length > 0)
			ride.getClient().setFirstName(names[0]);
		if (names.length > 1)
			ride.getClient().setLastName(names[1]);
		
		try {
			ride.setPickupLoc(new Location(0,0, pickupLoc));
			ride.setDestLoc(new Location(0,0, destLoc));
			Time newStartTime = new Time();
			Time newEndTime = new Time();
			if (newStartTime.changeTime(startTime))
				ride.setStartTime(newStartTime);
			if (newEndTime.changeTime(endTime))
				ride.setEndTime(newEndTime);
			
			ride.setRideDate(Util.date_format.parse(rideDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		pp.changeData(data);
	}
	
	private void searchDrivers(Ride r) {
		this.requestFocus();
		
		this.driver_results.clear();
		
		this.results_view.getChildren().clear();
		this.results_view.getRowConstraints().clear();


		this.driver_results.addAll(data.findDrivers(r, drivers_cap));
	
		for (int i = 0; i < driver_results.size(); i++) {
			Driver d = driver_results.get(i);
			addRow(i, d);
		}
	}
	
	private void searchDrivers(String name) {
		this.requestFocus();

		String[] names = name.split("\\s+"); //split by spaces

		this.driver_results.clear();
		
		this.results_view.getChildren().clear();
		this.results_view.getRowConstraints().clear();

		for (String sep_name: names) {
			List<Driver> found = data.searchDriver(sep_name);
			int i = 0;
			while (i < found.size() && this.driver_results.size() < drivers_cap) {
				if (!this.driver_results.contains(found.get(i))) //no duplicate
					this.driver_results.add(found.get(i));
				i++;
			}

		}
		
		for (int i = 0; i < driver_results.size(); i++) {
			Driver d = driver_results.get(i);
			addRow(i, d);
		}
	}

	private void addCell(int row, int col, String text) { //assumes col is valid and in field names
		Label cell = new Label(text);
		cell.setAlignment(Pos.CENTER);
		cell.setTextAlignment(TextAlignment.CENTER);
		cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		cell.wrapTextProperty().set(true);		

		if (row%2 == col%2)
			cell.setStyle("-fx-background-color: #ffffff;");
		else 
			cell.setStyle("-fx-background-color: #eeeeee;");

		results_view.add(cell, col, row);
		
		if (row == 0) {				
			Label title = driver_labels.get(driver_fields[col]);
			
			title.minWidthProperty().unbind();
			title.minWidthProperty().bind(cell.widthProperty());
		}
		
	}
	
	private void addRow(int row, String... cols) {

		for (int col = 0; col < cols.length; col++)
			addCell(row, col, cols[col]);
		
		if (row > results_view.getChildren().size()/max_cols)
			results_view.getRowConstraints().add(r);
	}
	
	private void addRow(int row, Driver p) {
		addRow(row, p.getFirstName()+" "+p.getLastName(), p.getAddress().getTown());
		
		ImageButton more = new ImageButton(Images.morebtn);
		more.setAlignment(Pos.CENTER);
		more.getIv().setPreserveRatio(true);
		
		more.setOnMouseClicked(m->{
			dpv.changeProfile(p);
			dpv.setVisible(true);
		});
		
		more_c.prefWidthProperty().addListener((c, old_w, new_w)->{
			more.resize((int)more_c.getPrefWidth(), (int)r.getPrefHeight());
		});
		r.prefHeightProperty().addListener((c, old_h, new_h)->{
			more.resize((int)more_c.getPrefWidth(), (int)r.getPrefHeight());
		});
		more.resize((int)more_c.getPrefWidth(), (int)r.getPrefHeight());
		
		ImageButton assign = new ImageButton(Images.assignbtn);
		assign.setAlignment(Pos.CENTER);
		assign.getIv().setPreserveRatio(true);
		
		assign.setOnMouseClicked(m->{
			fields.get("driver").setText(p.getFullName());
			ride.assignDriver(p);
			updateFields();
		});
		
		assign_c.prefWidthProperty().addListener((c, old_w, new_w)->{
			assign.resize((int)assign_c.getPrefWidth(), (int)r.getPrefHeight());
		});
		r.prefHeightProperty().addListener((c, old_h, new_h)->{
			assign.resize((int)assign_c.getPrefWidth(), (int)r.getPrefHeight());
		});
		assign.resize((int)more_c.getPrefWidth(), (int)r.getPrefHeight());
		

		results_view.add(more, max_cols-2, row);
		results_view.add(assign, max_cols-1, row);
	}
	
	

}
