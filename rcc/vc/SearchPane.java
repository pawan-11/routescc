package vc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import person.Client;
import person.Driver;
import person.Person;
import resources.Images;
import data.Data;
import util.ImageButton;
import util.Util;
import vc.Menu;

public class SearchPane extends Mode {

	private final int max_cols = 5;

	private TextField searchbar;
	private ImageView searchbg;
	private ImageButton searchbtn;
	private GridPane results_view;
	private ScrollPane sp;
	private RowConstraints r;
	private ColumnConstraints more_c;
	private GridPane header;

	private DriverPV dpv;
	private ClientPV cpv;
	
	private ArrayList<Driver> d_results; //drivers in results of the last search
	private ArrayList<Client> c_results; //clients in search
	private String search_text;
	
	private final static String[] field_names = {"Name", "Phone", "Town/City", "Type"};
	private LinkedHashMap<String, Label> field_labels;
	
	
	public SearchPane(Data data) {
		super(data);	
		this.search_text = "";
		//searchFor(""); //displays every person
	}
	
	public void changeData(Data data) { 
		this.data = data;
		searchFor(search_text); //refreshes new data
	}
	
	@Override
	public void addContent() {
		super.initContent();

		field_labels = new LinkedHashMap<String, Label>();
		this.d_results = new ArrayList<Driver>();
		this.c_results = new ArrayList<Client>();
		
		dpv = new DriverPV();
		cpv = new ClientPV();
		
		searchbar = new TextField();
		searchbg = new ImageView(Images.textfieldbg);
		searchbtn = new ImageButton(Images.searchbtn);

		results_view = new GridPane();
		sp = new ScrollPane();
		sp.setContent(results_view);
		
		
		header = new GridPane();
		for (int i = 0; i < field_names.length; i++) {
			Label field = new Label(field_names[i]);
			field_labels.put(field_names[i], field);
			header.add(field, i, 0);
		}
		
		this.getChildren().addAll(sp, header, searchbg, searchbar, searchbtn, homeButton, cpv, dpv);
	}
	
	@Override
	public void addEvents() {
		super.initEvents();

		searchbar.setOnMouseClicked(a->{
			searchbar.requestFocus();
		});
		searchbtn.setOnMouseClicked(a->{
			searchFor(searchbar.getText());
		});
		this.setOnMouseClicked(m->{
			if (searchbar.getSelectedText().length()==0)
				this.requestFocus();
		});
		searchbar.setOnKeyPressed(k->{
			if (k.getCode() == KeyCode.ENTER) {
				searchFor(searchbar.getText());
			}
		});
		
	}

	@Override
	public void addLayout() {
		super.initLayout();

		dpv.setVisible(false);
		cpv.setVisible(false);
		
		searchbtn.getIv().setPreserveRatio(true);
		searchbar.setPromptText("Enter name here");
		searchbar.setBackground(Background.EMPTY);
		
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		
		results_view.setAlignment(Pos.TOP_LEFT);
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
		more_c.setFillWidth(true);
		more_c.setHgrow(Priority.SOMETIMES);
		results_view.getColumnConstraints().add(more_c);
		
		Iterator<Entry<String, Label>> iterator = field_labels.entrySet().iterator();
		for (int i = 0; i < field_labels.size(); i++) { //add layout to header labels
			Label title = iterator.next().getValue();
			title.setAlignment(Pos.CENTER);
			title.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			title.setStyle("-fx-background-color: #ffffff;");
			GridPane.setHalignment(title, HPos.LEFT);
		}
		
	}
	
	
	@Override
	public void resize(int width, int height, int k) {
		super.resize_help(width, height, k);

		dpv.resize(width, height, k);
		cpv.resize(width, height, k);
		
		searchbar.setMinSize(width/3, k*1.5);
		searchbar.setMaxSize(width/3, k*1.5);
		searchbar.setFont(Font.font(Menu.font, k/1.5));

		searchbg.setFitWidth(searchbar.getMinWidth()+k/2);
		searchbg.setFitHeight(searchbar.getMinHeight()+k/4);

		searchbtn.resize(k*4, (int)(k*1.5));

		this.layout();

		searchbar.setLayoutX(width/2-searchbar.getLayoutBounds().getWidth()/2);
		searchbar.setLayoutY(k+searchbar.getLayoutBounds().getHeight()/2);
		searchbg.setLayoutX(searchbar.getLayoutX()-k/2);
		searchbg.setLayoutY(searchbar.getLayoutY()-k/8);
		
		dpv.setLayoutX(width/2 - dpv.getLayoutBounds().getWidth()/2);
		dpv.setLayoutY(height/2 - dpv.getLayoutBounds().getHeight()/2);	
		cpv.setLayoutX(width/2 - cpv.getLayoutBounds().getWidth()/2);
		cpv.setLayoutY(height/2 - cpv.getLayoutBounds().getHeight()/2);
		
		this.layout();

		sp.setMinSize(width-k*4, height-searchbg.getLayoutY()-searchbg.getLayoutBounds().getHeight()-k*3);
		sp.setMaxSize(sp.getMinWidth(), sp.getMinHeight());
		
		this.layout();
		
		results_view.setMaxSize(sp.getLayoutBounds().getWidth(), sp.getLayoutBounds().getHeight());
		
		r.setPrefHeight(k);
		this.layout();
		
		header.setMinWidth(sp.getLayoutBounds().getWidth());
		
		this.layout();
		
		searchbtn.setLayoutX(searchbg.getLayoutX()+searchbg.getLayoutBounds().getWidth()+k);
		searchbtn.setLayoutY(searchbg.getLayoutY()+(searchbg.getLayoutBounds().getHeight()
				-searchbtn.getLayoutBounds().getHeight())/2);
		
		sp.setLayoutX(width/2-sp.getLayoutBounds().getWidth()/2);
		sp.setLayoutY(searchbg.getLayoutY()+searchbg.getLayoutBounds().getHeight()+k*2);
		
		header.setLayoutX(sp.getLayoutX());
		header.setLayoutY(sp.getLayoutY()-r.getPrefHeight());
	}


	public void searchFor(String name) {
		search_text = name;
		searchbar.setText(name);
		this.requestFocus();

		String[] names = name.split("\\s+"); //split by spaces

		this.d_results.clear();
		this.c_results.clear();
		
		this.results_view.getChildren().clear();
		this.results_view.getRowConstraints().clear();

		
		for (String sep_name: names) {
			List<Driver> drivers = data.searchDriver(sep_name);
			List<Client> clients = data.searchClient(sep_name);
			
			for (Driver d: drivers)
				if (!this.d_results.contains(d))
					this.d_results.add(d);
			for (Client c: clients)
				if (!this.c_results.contains(c))
					this.c_results.add(c);
		}
		
		for (int i = 0; i < d_results.size(); i++) {
			Driver d = d_results.get(i);
			addRow(i, d);
		}
		for (int i = 0; i < c_results.size(); i++) {
			Client c = c_results.get(i);
			addRow(i+d_results.size(), c);
		}
		sp.setVvalue(0); //scroll to top
	}
	
	/**
	 * Adds label with <text> at <row, col> position in results_view grid
	 * @param row
	 * @param col
	 * @param text
	 */
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
			Label title = field_labels.get(field_names[col]);
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

	/**
	 * Adds row with details of person <p> at row <row> in results_view grid
	 * @param row
	 * @param p
	 */
	private void addRow(int row, Person p) {
		addRow(row, p.getFirstName()+" "+p.getLastName(), p.getPhone(), p.getType());
	}
	
	private void addRow(int row, Driver p) {
		addRow(row, p.getFirstName()+" "+p.getLastName(), p.getPhone(), p.getAddress().getTown(), p.getType());
		
		ImageButton more = new ImageButton(Images.morebtn);
		more.setAlignment(Pos.CENTER);
		more.getIv().setPreserveRatio(true);
		
		more.setOnMouseClicked(m->{
			dpv.changeProfile(p);
			dpv.setVisible(true);
		});
		
		more_c.prefWidthProperty().addListener((c, old_w, new_w)->{
			more.resize((int)more_c.getPrefWidth()-2, (int)r.getPrefHeight()-2);
		});
		r.prefHeightProperty().addListener((c, old_h, new_h)->{
			more.resize((int)more_c.getPrefWidth()-2, (int)r.getPrefHeight()-2);
		});
		more.resize((int)more_c.getPrefWidth()-2, (int)r.getPrefHeight()-2);
		results_view.add(more, max_cols-1, row);
	}
	
	private void addRow(int row, Client p) {
		addRow(row, p.getFirstName()+" "+p.getLastName(), p.getPhone(), "", p.getType());
		
		ImageButton more = new ImageButton(Images.morebtn);
		more.setAlignment(Pos.CENTER);
		more.getIv().setPreserveRatio(true);
		
		more.setOnMouseClicked(m->{
			cpv.changeProfile(p);
			cpv.setVisible(true);
		});
		
		more_c.prefWidthProperty().addListener((c, old_w, new_w)->{
			more.resize((int)more_c.getPrefWidth()-2, (int)r.getPrefHeight()-2);
		});
		r.prefHeightProperty().addListener((c, old_h, new_h)->{
			more.resize((int)more_c.getPrefWidth()-2, (int)r.getPrefHeight()-2);
		});
		more.resize((int)more_c.getPrefWidth()-2, (int)r.getPrefHeight()-2);
		results_view.add(more, max_cols-1, row);
	}
	
	private void deleteRow(int row) {
		for (int i = row*max_cols; i < row*(max_cols+1); row++)
			results_view.getChildren().remove(i);

		results_view.getRowConstraints().remove(row);	
	}

}