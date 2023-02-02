package vc;

import person.Person;
import resources.Images;
import util.ImageButton;
import util.Util;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


abstract public class ProfileView<P extends Person> extends Pane implements PaneInterface {
	
	protected P p;

	protected GridPane gp;
	protected RowConstraints r;
	protected ImageButton x;
	protected Label info_header, header;
	
	public ProfileView(P p) {
		addContent();
		addLayout();
		addEvents();
		
		changeProfile(p);
	}
	
	protected void helpChangeProfile(P p) {
		this.p = p;
		
		HashMap<String, Label> field_labels = this.getFieldLabels();
		field_labels.get("name").setText(p.getFirstName()+", "+p.getLastName());
		field_labels.get("phone").setText(p.getPhone());
		field_labels.get("type").setText(p.getType());
	}
	
	protected void initContent() {
		x = new ImageButton(Images.closebtn);
		
		header = new Label();
		info_header = new Label("Information");
		
		gp = new GridPane();
		
		HashMap<String, Label> field_labels = this.getFieldLabels();
		String[] field_names = this.getFields();
		
		for (int i = 0; i < field_names.length; i++) {
			Label field_title = new Label(Util.capitalize(field_names[i]));
			Label field = new Label();
			
			field_title.setStyle("-fx-background-color: #ffffff;");
			field.setStyle("-fx-background-color: #eeeeee;");
			
			field_labels.put(field_names[i], field);
			gp.add(field_title, 0, i);
			gp.add(field_labels.get(field_names[i]), 1, i);
		}
	}
	
	
	protected void initLayout() {
		gp.setStyle("-fx-background-color: #dddeee; -fx-border-width: 0.5; -fx-border-color: black ;");
		this.setStyle("-fx-background-color: rgba(255, 255, 255, .98); -fx-border-width: 0.5; -fx-border-color: black ;");
		
		info_header.setAlignment(Pos.CENTER);
		info_header.setTextAlignment(TextAlignment.CENTER);
		header.setAlignment(Pos.CENTER);
		header.setTextAlignment(TextAlignment.CENTER);
		
		r = new RowConstraints();
		ColumnConstraints c = new ColumnConstraints();
		c.setHgrow(Priority.ALWAYS);
		
		for (int i = 0; i < this.getFields().length; i++) {
			gp.getRowConstraints().add(r);	
		}
		
		gp.setAlignment(Pos.BASELINE_LEFT);
		gp.getColumnConstraints().addAll(c, c);
	}
	
	protected void initEvents() {
		x.setOnMouseClicked(m->{
			this.setVisible(false);
		});
		
	}
	
	protected void help_resize(int width, int height, int k) {
		x.resize(k, k);
				
		x.setLayoutY(5);
		
		r.setMaxHeight(k);
		r.setPrefHeight(k);
		
		header.setFont(Font.font(k));
		header.setMinWidth(k*8);
		
		info_header.setFont(Font.font(k*0.6));
		info_header.setMinWidth(k*5);
		
	//	gp.setHgap(k);
	//	gp.setVgap(k);
		
		this.layout();
		
		header.setLayoutX(width/2-
				header.getMinWidth()/2);
		header.setLayoutY(k/2);
		
		x.setLayoutX(width-x.getLayoutBounds().getWidth()-5);
		
		this.setMinSize(width, height);
		this.setMaxSize(width, height);
	}
	
	abstract public void changeProfile(P p);
	abstract public HashMap<String, Label> getFieldLabels();
	abstract public String[] getFields();

}
