package vc;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import resources.Images;
import data.Data;
import util.ImageButton;
import vc.Menu;

abstract public class Mode extends Pane implements PaneInterface {
	
	protected ImageButton homeButton;
	protected ImageView modebg;
	protected Data data;
	
	public Mode(Data data) {
		this.data = data;
		addContent();
		addEvents();
		addLayout();
	}
	
	abstract public void changeData(Data data);
	
	protected void initContent() {
		homeButton = new ImageButton(Images.homebtn);
		modebg = new ImageView();
	}
	
	
	protected void initEvents() {
		
	}
	
	public void setBackScreen(Menu menu) {
		homeButton.setOnMouseClicked(m-> {
			getScene().setRoot(menu);
			menu.requestFocus();
		});
	}
	
	protected void initLayout() {	
		this.setStyle("-fx-background-color: #efffff");
	}
	
	protected void resize_help(int width, int height, int k) {		
		homeButton.resize(k,k);
		
		this.layout();
		
		homeButton.setLayoutX(5);	
		homeButton.setLayoutY(5);	
	}
	
}
