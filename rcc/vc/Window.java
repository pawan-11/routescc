package vc;

import fileIO.DataCreator;
import fileIO.DriverCreator;
import fileIO.RideCreator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.util.Duration;
import resources.Images;
import util.Util;

public class Window extends Application {	
	
    private Stage stage;
    private Menu menu;
    private ContextMenu contextMenu;
    
	public void start(Stage stage) {
		this.stage = stage;
		this.menu = new Menu();
		
		contextMenu = new ContextMenu();
		contextMenu.setAutoFix(true);
		
		stage.setScene(new Scene(menu, 900, 600));
		stage.setTitle("Routes Connecting Communities");
		stage.centerOnScreen();
		stage.setResizable(true);
		stage.show();
		stage.getIcons().add(Images.title);
		
		menu.requestFocus();
		addEvents();
		resize();
	}	
	
	
	public void addEvents() {
		stage.setOnCloseRequest(e-> {
			Platform.exit();
		});
		
		Timeline t = new Timeline(new KeyFrame(Duration.millis(50), e->{
			resize();
		}));
		t.setCycleCount(1);
		stage.widthProperty().addListener((c, old, ne)->{
			resize();
		});
		stage.heightProperty().addListener((c, old, ne)->{
			resize();
		});
		
		stage.getScene().setOnKeyPressed(k->{
			if (k.getCode() == KeyCode.ESCAPE)
				stage.setFullScreen(false);		
			//else if (k.getCode() == KeyCode.F)
			//	stage.setFullScreen(true);		
			
		});
		
		MenuItem save = new MenuItem("Save to Files");
		save.setOnAction(m->{
			menu.save();
		});
		MenuItem load = new MenuItem("Load Files");
		load.setOnAction(m->{
			menu.refresh();
		});
		MenuItem folder = new MenuItem("Open Files Folder");
		folder.setOnAction(m->{
			menu.save();
			DataCreator.openFolder();
		});
		MenuItem drivers = new MenuItem("Open Drivers CSV");
		drivers.setOnAction(m->{
			menu.save();
			DriverCreator.openCsv();
		});
		MenuItem upcoming_rides = new MenuItem("Open Upcoming Rides CSV");
		upcoming_rides.setOnAction(m->{
			menu.save();
			RideCreator.openCsv(true);
		});
		MenuItem past_rides = new MenuItem("Open Past Rides CSV");
		past_rides.setOnAction(m->{
			menu.save();
			RideCreator.openCsv(false);
		});
		contextMenu.getItems().addAll(save, load, folder, drivers, upcoming_rides, past_rides);
		
		stage.getScene().setOnMouseClicked(m->{
			if (m.getButton() != MouseButton.SECONDARY)
				contextMenu.hide();
			else
				contextMenu.show(stage.getScene().getRoot(), m.getScreenX(), m.getScreenY()); 
		});
		
		stage.setOnCloseRequest(we->{   
			menu.save();
		});
	}
	
	
	private void resize() {
		int w = (int)stage.getScene().getWidth(), h = (int)stage.getScene().getHeight();
		w = w < 400?400:w;
		h = h < 300?300:h;
		int k = Util.getK(w, h);
		
		menu.resize(w, h, k);
	}
	
	public static void main(String[] args) {
		launch();
	}

}
