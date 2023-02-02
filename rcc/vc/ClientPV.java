package vc;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import person.Client;
import person.Driver;
import util.Util;

public class ClientPV extends ProfileView<Client> implements PaneInterface{

	protected static String [] field_names = {"name", "phone", "type"};
	private LinkedHashMap<String, Label> field_labels;
	
	public ClientPV(Client c) {
		super(c);
	}
	
	public ClientPV() {
		this(Client.nullClient);
		
	}
	
	public void changeProfile(Client c) {
		super.helpChangeProfile(c);
	
	}
	
	public void addContent() {
		field_labels = new LinkedHashMap<String, Label>();
		super.initContent();
		
		header.setText("Client Profile");
		this.getChildren().addAll(gp, info_header, header, x);
	}
	
	public void addLayout() {
		//gp_rows = 3;
		super.initLayout();
	}
	
	public void addEvents() {
		super.initEvents();
	}
	
	public void resize(int width, int height, int k) {
		super.help_resize(width, height, k);

		this.layout();
		
		gp.setMinSize(width/2-k*4, height-k*4);
		gp.setMaxSize(gp.getMinWidth(), gp.getMinHeight());
		
		this.layout();
		
		gp.setLayoutX(k);
		gp.setLayoutY(height/2-gp.getLayoutBounds().getHeight()/2+k);
		
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
