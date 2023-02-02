package vc;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import util.DayCalendar;
import util.Util;


public class DayCalendarView implements PaneInterface {

	private DayCalendar cal;
	private Label[] slots; 
	
	
	public DayCalendarView(int intervals) {
		slots = new Label[intervals];
		
		addContent();
		addLayout();
		addEvents();
	}
	
	public DayCalendarView(DayCalendar cal) {
		this(cal.getIntervals());
		changeCalendar(cal);
	}
	

	
	public void changeCalendar(DayCalendar cal) {
		
		assert(cal.getIntervals() == slots.length);
		if (cal.getIntervals() == slots.length) {
			this.cal = cal;
			
			for (int i = 0; i < slots.length; i++) {
				final int j = i;
					
				slots[i].setOnMousePressed(m->{
					cal.set(j, j, !cal.isBusy(j, j));
					update(j, j);
				});
			}
		
			update(0, cal.getIntervals()-1);
		}
		else {
			Util.print("calendar intervals are "+cal.getIntervals()+" but expeceted "+slots.length);
		}
	}
	
	@Override
	public void addContent() {
		//slots managed by the parent
		for (int i = 0; i < slots.length; i++)
			slots[i] = new Label();
		
	}
	
	@Override
	public void addLayout() {
		
		//this.setStyle("-fx-background-color: blue; -fx-border-width: 0.5; -fx-border-color: black ;");
	}

	@Override
	public void addEvents() {
		
	}

	@Override
	public void resize(int width, int height, int k) {
		for (int i = 0; i < slots.length; i++) {
			slots[i].setMinSize(width, height/cal.getIntervals());
			slots[i].setMaxSize(slots[i].getMinWidth(), slots[i].getMinHeight());
		}
	}
	
	public void update(int start_idx, int end_idx) {
		
		for (int i = start_idx; i <= end_idx; i++) 
			if (cal.isBusy(i, i))
				slots[i].setStyle("-fx-background-color: #111111; -fx-border-width: 0.5; -fx-border-color: black;");
			else
				slots[i].setStyle("-fx-background-color: #d1ff66; -fx-border-width: 0.5; -fx-border-color: black;");
	}
	
	public Label[] getSlots() {
		return slots;
	}
}
