package vc;

import resources.Images;
import fileIO.DataCreator;
import fileIO.DriverCreator;
import fileIO.RideCreator;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import data.Data;
import util.ImageButton;


public class Menu extends Pane implements PaneInterface {
	
	private TextField searchbar;
	private ImageView searchbg, titleiv;
	private ImageButton searchbtn, plannerbtn, schedulebtn;
	
	private SearchPane searchPane;
	private PlannerPane plannerPane;
	private SchedulePane schedulePane;

	private Data data;
	
	public static final String font = "Arial"; //Poiret One
	
    public static Label debug_msg = new Label();
    private static boolean debug = false; //true if debugging messages should appear
     
	public Menu() {		
		data = DataCreator.getData();
		
		addContent();
		addEvents();
		addLayout();
	}
	
	public void addContent() {
		searchPane = new SearchPane(data);
		schedulePane = new SchedulePane(data);
		plannerPane = new PlannerPane(data);
		
		searchPane.setBackScreen(this);
		schedulePane.setBackScreen(this);
		plannerPane.setBackScreen(this);
		
		titleiv = new ImageView(Images.title);
		
		searchbar = new TextField();
		searchbg = new ImageView(Images.textfieldbg);
		searchbtn = new ImageButton(Images.searchbtn);
		
		schedulebtn = new ImageButton(Images.schedulebtn);
		plannerbtn = new ImageButton(Images.plannerbtn);
		
		this.getChildren().addAll(titleiv, searchbg, searchbar, searchbtn, schedulebtn, plannerbtn, debug_msg);	
	}
	
	
	public void addEvents() {
		searchbar.setOnMouseClicked(a->{
			searchbar.requestFocus();
		});
		searchbtn.setOnMouseClicked(a->{
			this.getScene().setRoot(searchPane);
			searchPane.requestFocus();
			searchPane.searchFor(searchbar.getText());
		});

		this.setOnMouseClicked(m->{
			if (searchbar.getSelectedText().length()==0)
				this.requestFocus();
		});
		searchbar.setOnKeyPressed(k->{
			if (k.getCode() == KeyCode.ENTER)
				searchbtn.getOnMouseClicked().handle(null);
		});	
		schedulebtn.setOnMouseClicked(a->{
			this.getScene().setRoot(schedulePane);
			schedulePane.requestFocus();	
		});
		plannerbtn.setOnMouseClicked(a->{
			this.getScene().setRoot(plannerPane);
			plannerPane.requestFocus();
		});
	}
	
	
	public void addLayout() {	
		
		debug_msg.setVisible(debug); 
		
		searchbtn.getIv().setPreserveRatio(true);
		searchbar.setPromptText("Enter keyword here");
		searchbar.setBackground(Background.EMPTY);
		
		titleiv.setPreserveRatio(true);
		
		schedulebtn.getIv().setPreserveRatio(true);
		plannerbtn.getIv().setPreserveRatio(true);
		
		this.setStyle("-fx-background-color: #efffff");
	} 
	
	
	public void resize(int width, int height, int k) {		
        int h = k*2, w = k*6;
        
        titleiv.setFitHeight(h*3);
        
		searchbar.setMinSize(width/2.5, k*1.8);
		searchbar.setMaxSize(width/3, k*1.8);
		searchbar.setFont(Font.font(Menu.font, k/1.3));
		
		searchbg.setFitWidth(searchbar.getMinWidth()+k/2);
		searchbg.setFitHeight(searchbar.getMinHeight()+k/4);
		
		searchbtn.resize(k*3, (int)(k*1.8));
		schedulebtn.resize(k*5, (int)(k*3));
		plannerbtn.resize(k*5, (int)(k*3));
		
        this.layout();
        double bar_width = searchbar.getLayoutBounds().getWidth()+searchbtn.getLayoutBounds().getWidth();
        
		searchbar.setLayoutX(width/2-bar_width/2);
		searchbar.setLayoutY(height/2+searchbar.getLayoutBounds().getHeight()/2);
		searchbg.setLayoutX(searchbar.getLayoutX()-k/2);
		searchbg.setLayoutY(searchbar.getLayoutY()-k/8);
		
		searchbtn.setLayoutX(searchbg.getLayoutX()+searchbg.getLayoutBounds().getWidth()+k/2);
		searchbtn.setLayoutY(searchbg.getLayoutY()+(searchbg.getLayoutBounds().getHeight()
				-searchbtn.getLayoutBounds().getHeight())/2);
		
		schedulePane.resize(width, height, k);
		searchPane.resize(width, height, k);
		plannerPane.resize(width, height, k);
		
		this.layout();
		
		searchbtn.setLayoutX(searchbg.getLayoutX()+searchbg.getLayoutBounds().getWidth()+k);
		searchbtn.setLayoutY(searchbg.getLayoutY()+(searchbg.getLayoutBounds().getHeight()
				-searchbtn.getLayoutBounds().getHeight())/2);
		
		schedulebtn.setLayoutX(width/2-schedulebtn.getLayoutBounds().getWidth()-k/2);
		schedulebtn.setLayoutY(searchbg.getLayoutY()+searchbg.getLayoutBounds().getHeight()+k);
		
		plannerbtn.setLayoutX(width/2+k/2);
		plannerbtn.setLayoutY(searchbg.getLayoutY()+searchbg.getLayoutBounds().getHeight()+k);
		
        titleiv.setLayoutX(width/2-titleiv.getLayoutBounds().getWidth()/2);
        titleiv.setLayoutY(k*4);
	}
		
	public void save() {
		DataCreator.save(data);
	}
	
	public void refresh() {
		data = DataCreator.getData();
		searchPane.changeData(data);
		plannerPane.changeData(data);
		schedulePane.changeData(data);
	}
	
}
