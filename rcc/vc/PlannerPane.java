package vc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import data.Data;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import resources.Images;
import ride.Ride;
import util.ImageButton;
import util.Util;
import util.WeekCalendar;

public class PlannerPane extends Mode {
	
	private final int max_cols = 6;
	
	private EditRidePV rpv;
	private Date date; //date of schedule

	private Label title, dateLbl;
	private ImageButton nextbtn, prevbtn;
	
	private ScrollPane sp;
	private GridPane header;
	private GridPane results_view;
	private RowConstraints r;
	private ColumnConstraints more_c;
	
	private List<Ride> ride_results;
	
	private final static String[] field_names = {"Pickup Time", "Dropoff Time", "Client", "Driver", "Town/City"};
	private LinkedHashMap<String, Label> field_labels;
	
	public PlannerPane(Data data) {
		super(data);	
		searchFor(new Date());
	}
	
	public void changeData(Data data) { 
		this.data = data;
		searchFor(date); //refreshes rides
	}
	
	@Override
	public void addContent() {
		super.initContent();
		
		field_labels = new LinkedHashMap<String, Label>();
		ride_results = new ArrayList<Ride>();
		
		rpv = new EditRidePV(this, data);
		
		title = new Label("Planner");
		
		dateLbl = new Label();
		
		nextbtn = new ImageButton(Images.nextbtn);
		prevbtn = new ImageButton(Images.prevbtn);
		
		results_view = new GridPane();
		sp = new ScrollPane();
		sp.setContent(results_view);
		
		header = new GridPane();	
		for (int i = 0; i < field_names.length; i++) {
			Label field = new Label(field_names[i]);
			field_labels.put(field_names[i], field);
			header.add(field, i, 0);
		}
		
		this.getChildren().addAll(sp, header, title, dateLbl, prevbtn, nextbtn, homeButton, rpv); //dateTitle
	}
	
	private void incrementDate(int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		
		searchFor(Util.trim_time(cal.getTime()));
	}
	
	@Override
	public void addEvents() {
		super.initEvents();

		Timeline prevDate = new Timeline();
		prevDate.setCycleCount(Animation.INDEFINITE);
		prevDate.getKeyFrames().add(new KeyFrame(Duration.millis(400), k->{
			incrementDate(-1);
			if (prevDate.getRate() < 10)
				prevDate.setRate(prevDate.getRate()+0.15);
		}));
		prevDate.setRate(0.8);
		prevDate.statusProperty().addListener(c->{
			if (prevDate.getStatus() == Animation.Status.PAUSED)
				prevDate.setRate(0.8);
		});
		
		Timeline nextDate = new Timeline();
		nextDate.setCycleCount(Animation.INDEFINITE);
		nextDate.getKeyFrames().add(new KeyFrame(Duration.millis(400), k->{
			incrementDate(1);
			if (nextDate.getRate() < 10)
				nextDate.setRate(nextDate.getRate()+0.15);
		}));
		nextDate.setRate(0.8);
		nextDate.statusProperty().addListener(c->{
			if (nextDate.getStatus() == Animation.Status.PAUSED)
				nextDate.setRate(0.8);
		});

		nextbtn.setOnMouseClicked(m->{
			incrementDate(1);
		});
		nextbtn.setOnMousePressed(m->{
			nextDate.play();
		});
		nextbtn.setOnMouseReleased(m->{
			nextDate.pause();
			nextDate.stop();
		});
		prevbtn.setOnMouseClicked(m->{
			incrementDate(-1);
		});
		prevbtn.setOnMousePressed(m->{
			prevDate.play();
		});
		prevbtn.setOnMouseReleased(m->{
			prevDate.pause();
			prevDate.stop();
		});
	}
	
	@Override
	public void addLayout() {
		super.initLayout();
	
		rpv.setVisible(false);	
		
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		
		results_view.setAlignment(Pos.BASELINE_CENTER);
		results_view.setHgap(1);
		results_view.setVgap(1);
		results_view.setStyle("-fx-background-color: #dddddd;");
		results_view.getColumnConstraints().clear();
		
		header.setAlignment(Pos.CENTER_LEFT);
		header.setHgap(1);
		header.setVgap(1);
		header.setStyle("-fx-background-color: #dddddd;");
		header.getColumnConstraints().clear();
		
		r = new RowConstraints();
		r.setFillHeight(true);
		r.setVgrow(Priority.ALWAYS);	
		header.getRowConstraints().add(r);
		
		for (int i = 0; i < max_cols-1; i++) {
			ColumnConstraints c = new ColumnConstraints();
			c.setFillWidth(true);
			c.setHgrow(Priority.ALWAYS);
			results_view.getColumnConstraints().add(c);	
		}
		
		more_c = new ColumnConstraints();
		more_c.setFillWidth(false);
		more_c.setHgrow(Priority.NEVER);
		results_view.getColumnConstraints().add(more_c);
		
		Iterator<Entry<String, Label>> iterator = field_labels.entrySet().iterator();
		for (int i = 0; i < field_labels.size(); i++) {
			
			Label title = iterator.next().getValue();
			title.setAlignment(Pos.CENTER);
			title.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			title.setStyle("-fx-background-color: #ffffff;");
			GridPane.setHalignment(title, HPos.LEFT);
		}
		
		title.setAlignment(Pos.CENTER);
		title.setTextAlignment(TextAlignment.CENTER);
		dateLbl.setAlignment(Pos.CENTER);
		dateLbl.setTextAlignment(TextAlignment.CENTER);
	
	}
	
	@Override
	public void resize(int width, int height, int k) {
		super.resize_help(width, height, k);
		
		rpv.resize(width, height, k);
		
		title.setMinSize(k*8, k*2);
		title.setFont(Font.font(Menu.font, k));
		title.setLayoutX(width/2-title.getMinWidth()/2);
		
		nextbtn.resize(k,k);
		prevbtn.resize(k,k);	
		
		this.layout();
		
		dateLbl.setMinSize(k*5, k*2);
		dateLbl.setFont(Font.font(Menu.font, k*0.7));
		dateLbl.setLayoutX(width/2-dateLbl.getMinWidth()/2);
		dateLbl.setLayoutY(title.getLayoutY()+k*2);
		nextbtn.setLayoutX(width/2+k*3);
		nextbtn.setLayoutY(dateLbl.getLayoutY()+nextbtn.getLayoutBounds().getHeight()/2);
		prevbtn.setLayoutX(width/2-k*3-prevbtn.getLayoutBounds().getWidth());
		prevbtn.setLayoutY(dateLbl.getLayoutY()+prevbtn.getLayoutBounds().getHeight()/2);
		
		rpv.setLayoutX(width/2 - rpv.getLayoutBounds().getWidth()/2);
		rpv.setLayoutY(height/2 - rpv.getLayoutBounds().getHeight()/2);	
		this.layout();
		sp.setMinSize(width-k*4, height-title.getMinHeight()-k*4);
		sp.setMaxSize(sp.getMinWidth(), sp.getMinHeight());
		
		this.layout();
		results_view.setMaxSize(sp.getLayoutBounds().getWidth(), sp.getLayoutBounds().getHeight());
		
		r.setPrefHeight(k);
		
		this.layout();
		
		header.setMinWidth(sp.getLayoutBounds().getWidth());
		this.layout();
		
		sp.setLayoutX(width/2-sp.getLayoutBounds().getWidth()/2);
		sp.setLayoutY(height-sp.getLayoutBounds().getHeight()-k);
		
		header.setLayoutX(sp.getLayoutX());
		header.setLayoutY(sp.getLayoutY()-r.getPrefHeight());
	}
	
	public void searchFor(Date date) {
		this.date = date;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		dateLbl.setText(WeekCalendar.weekdays[(cal.get(Calendar.DAY_OF_WEEK)+5)%7]+"\n"+Util.date_format.format(date));
		
		this.ride_results.clear();
		
		this.results_view.getChildren().clear();
		this.results_view.getRowConstraints().clear();
		
		this.ride_results.addAll(data.getEmptyRides(date)); //rides with no driver
		this.ride_results.addAll(data.getFilledRides(date));
		
		for (int i = 0; i < ride_results.size(); i++) {
			Ride r = ride_results.get(i);
			addRow(i, r);
		}
		
		sp.setVvalue(0); //scroll to top
		this.layout();
	}
	
	
	private void addCell(int row, int col, String text) {
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
			int c = 0;
			Label title = null;
			Iterator<Entry<String, Label>> iterator = field_labels.entrySet().iterator();
			while (c <= col && c < field_names.length) {
				title = iterator.next().getValue();
				
				c++;
			}
			title.minWidthProperty().unbind();
			title.minWidthProperty().bind(cell.widthProperty());
		}
	
	}
	
	/**
	 * Adds row of columns with text specified by <cols> array at row <row> in results_view grid
	 * @param row
	 * @param cols
	 */
	private void addRow(int row, String... cols) {

		for (int col = 0; col < cols.length; col++)
			addCell(row, col, cols[col]);
		
		if (row > results_view.getChildren().size()/max_cols)
			results_view.getRowConstraints().add(r);
	}
	
	private void addRow(int row, Ride ride) {
		addRow(row, ride.getStartTime()+"", ride.getEndTime()+"", 
				ride.getClient().getFullName(), ride.getDriver().getFullName(), ride.getPickupLoc().getTown());
		
		ImageButton more = new ImageButton(Images.morebtn);
		more.getIv().setPreserveRatio(true);
		
		more.setOnMouseClicked(m->{
			rpv.changeProfile(ride);
			rpv.setVisible(true);
		});
		
		more_c.maxWidthProperty().addListener((c, old_w, new_w)->{
			more.resize((int)more_c.getMaxWidth(), (int)r.getPrefHeight());
		});
		r.prefHeightProperty().addListener((c, old_h, new_h)->{
			more.resize((int)more_c.getPrefWidth(), (int)r.getPrefHeight());
		});
		more.resize((int)more_c.getMaxWidth(), (int)r.getPrefHeight());
		results_view.add(more, max_cols-1, row);
	}
	

}