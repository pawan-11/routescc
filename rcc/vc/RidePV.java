package vc;

import person.Person;
import resources.Images;
import ride.Ride;
import util.ImageButton;
import util.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class RidePV extends Pane implements PaneInterface {
	
	protected Ride ride;
	//protected DriverPV dpv;
	//protected ClientPV cpv;
		
	protected GridPane gp;
	protected RowConstraints r;
	protected int gp_rows = 3;
	protected ImageButton x;
	protected Label info_header, header;
	protected static String [] field_names = {"start time", "end time", "ride date", 
			"pickup loc", "dest loc", "client", "driver", "description"};

	private LinkedHashMap<String, Label> field_labels;
	
	public RidePV() {
		addContent();
		addLayout();
		addEvents();
	}
	
	protected void changeProfile(Ride ride) {
		this.ride = ride;
		
		//dpv.changeProfile(ride.getDriver());
		//cpv.changeProfile(ride.getClient());
		
		field_labels.get("start time").setText(ride.getStartTime().toString());
		field_labels.get("end time").setText(ride.getEndTime().toString());
		field_labels.get("ride date").setText(Util.date_format.format(ride.getRideDate()));
		field_labels.get("pickup loc").setText(ride.getPickupLoc().toString());
		field_labels.get("dest loc").setText(ride.getDestLoc().toString());
		field_labels.get("client").setText(ride.getClient().getFullName());
		field_labels.get("driver").setText(ride.getDriver().getFullName());
		field_labels.get("description").setText(ride.getDescription());
	}
	
	public void addContent() {
		x = new ImageButton(Images.closebtn);
		
		field_labels = new LinkedHashMap<String, Label>();
		header = new Label("Ride Details");
		info_header = new Label("Information");
		
		gp = new GridPane();
				
		for (int i = 0; i < field_names.length; i++) {
			field_labels.put(field_names[i], new Label());
			Label field_title = new Label(Util.capitalize(field_names[i]));
		
			gp.add(field_title, 0, i);
			gp.add(field_labels.get(field_names[i]), 1, i);
		}
		
		this.getChildren().addAll(gp, info_header, header, x);
	}
	
	
	public void addLayout() {
		
		gp.setStyle("-fx-background-color: #dddeee; -fx-border-width: 0.5; -fx-border-color: black ;");
		this.setStyle("-fx-background-color: rgba(255, 255, 255, .98); -fx-border-width: 0.5; -fx-border-color: black ;");
		
		info_header.setAlignment(Pos.CENTER);
		info_header.setTextAlignment(TextAlignment.CENTER);
		header.setAlignment(Pos.CENTER);
		header.setTextAlignment(TextAlignment.CENTER);
		
		r = new RowConstraints();
		ColumnConstraints c = new ColumnConstraints();
		c.setHgrow(Priority.ALWAYS);
				
		for (int i = 0; i < field_names.length; i++) {
			gp.getRowConstraints().add(r);	
		}
		
		gp.setAlignment(Pos.BASELINE_LEFT);
		gp.getColumnConstraints().addAll(c, c);
		
		
	}
	
	public void addEvents() {
		x.setOnMouseClicked(m->{
			this.setVisible(false);
		});
		
	}
	
	public void resize(int width, int height, int k) {
		x.resize(k, k);
				
		x.setLayoutY(5);
		
		r.setMaxHeight(k);
		r.setPrefHeight(k);
		
		header.setFont(Font.font(k));
		header.setMinWidth(k*8);
		
		info_header.setFont(Font.font(k*0.6));
		info_header.setMinWidth(k*5);
		
		gp.setMinSize(width/1.5, height-k*4);
		gp.setMaxSize(gp.getMinWidth(), gp.getMinHeight());
	//	gp.setHgap(k);
	//	gp.setVgap(k);
		
		this.layout();
		
		gp.setLayoutX(width/2-gp.getLayoutBounds().getWidth()/2);//k
		gp.setLayoutY(height/2-gp.getLayoutBounds().getHeight()/2+k);
		
		header.setLayoutX(width/2-header.getMinWidth()/2);
		header.setLayoutY(k/2);
		info_header.setLayoutX(gp.getLayoutX()+gp.getLayoutBounds().getWidth()/2-
				info_header.getMinWidth()/2);
		info_header.setLayoutY(gp.getLayoutY()-info_header.getFont().getSize()-2);
		
		x.setLayoutX(width-x.getLayoutBounds().getWidth()-5);
		
		this.setMinSize(width, height);
		this.setMaxSize(width, height);
	}
	
	

}
