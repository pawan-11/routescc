package vc;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import person.Driver;
import util.Util;

public class DriverPV extends ProfileView<Driver> implements PaneInterface{

	
	private WeekCalendarView cv;
	private Label cal_header;

	protected static String [] field_names = {"name", "phone", "cell phone", "type", "age", "street", "town", 
			"postal code", "province", "birth date", "license expiry", "insurance expiry", "comments"};
	private LinkedHashMap<String, Label> field_labels;
	
	public DriverPV(Driver d) {
		super(d);
	}
	
	public DriverPV() {
		this(Driver.nullDriver);
		
	}
	
	public void changeProfile(Driver p) {
		super.helpChangeProfile(p);
		
		field_labels.get("cell phone").setText(p.getCellPhone());
		field_labels.get("age").setText(p.getAge()+"");
		field_labels.get("street").setText(p.getAddress().getStreet());
		field_labels.get("town").setText(p.getAddress().getTown());
		field_labels.get("postal code").setText(p.getAddress().getPostal());
		field_labels.get("province").setText(p.getAddress().getProvince());
		field_labels.get("birth date").setText(p.getBirthDate().toString());
		field_labels.get("license expiry").setText(p.getLicenseExpiry().toString());
		field_labels.get("insurance expiry").setText(p.getBirthDate().toString());
		field_labels.get("comments").setText(p.getComments());
		
		if (p.isInsuranceExpired())
			field_labels.get("insurance expiry").setTextFill(Color.RED);
		else 
			field_labels.get("insurance expiry").setTextFill(Color.BLACK);
		
		if (p.isLicenseExpired())
			field_labels.get("license expiry").setTextFill(Color.RED);
		else 
			field_labels.get("license expiry").setTextFill(Color.BLACK);
		
		if (p.getAge() >= 80)
			field_labels.get("age").setTextFill(Color.ORANGE);
		else 
			field_labels.get("license expiry").setTextFill(Color.BLACK);
		
		cv.changeCalendar(p.getWeekCalendar());
	}
	
	public void addContent() {
		field_labels = new LinkedHashMap<String, Label>();
		super.initContent();
		
		cv = new WeekCalendarView(7, 12, 13); //7 days, 12 slots and show times for all 12 of them
		
		header.setText("Driver Profile");
		cal_header = new Label("Availability");
		
		this.getChildren().addAll(gp, cal_header, info_header, header, cv, x);
	}
	
	public void addLayout() {
		super.initLayout();

		cal_header.setAlignment(Pos.CENTER);
		cal_header.setTextAlignment(TextAlignment.CENTER);
	}
	
	public void addEvents() {
		super.initEvents();
	}
	
	public void resize(int width, int height, int k) {
		super.help_resize(width, height, k);
		
		cv.resize(width/2, height-k*4, k);
		cal_header.setFont(Font.font(k*0.6));
		cal_header.setMinWidth(k*5);
		
		this.layout();
		
		gp.setMinSize(width-cv.getLayoutBounds().getWidth()-k*4, cv.getLayoutBounds().getHeight());
		gp.setMaxSize(gp.getMinWidth(), gp.getMinHeight());
		
		this.layout();
		
		cv.setLayoutX(width-cv.getLayoutBounds().getWidth()-k);
		cv.setLayoutY(height/2-cv.getLayoutBounds().getHeight()/2+k);
		
		gp.setLayoutX(k);
		gp.setLayoutY(cv.getLayoutY());
		
		cal_header.setLayoutX(cv.getLayoutX()+cv.getLayoutBounds().getWidth()/2-
				cal_header.getMinWidth()/2);
		cal_header.setLayoutY(cv.getLayoutY()-cal_header.getFont().getSize()-2);
		
		info_header.setLayoutX(gp.getLayoutX()+gp.getLayoutBounds().getWidth()/2-
				info_header.getMinWidth()/2);
		info_header.setLayoutY(gp.getLayoutY()-info_header.getFont().getSize()-2);
	}

	@Override
	public HashMap<String, Label> getFieldLabels() {
		return field_labels;
	}

	@Override
	public String[] getFields() {
		return field_names;
	}
}
