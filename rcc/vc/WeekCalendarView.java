package vc;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import util.WeekCalendar;
import util.DayCalendar;
import util.Time;
import util.Util;

public class WeekCalendarView extends Pane implements PaneInterface {

	private WeekCalendar cal;
	private GridPane grid;
	private RowConstraints r;
	private ColumnConstraints c;
	
	private DayCalendarView[] cal_views; //calendar slots for mon, tue,.. etc
	private Label[] week_lbls;  //mon, tue.. etc
	private Label[] time_lbls;	//8:00, 10:00 .. etc
	
	private int labels, days, intervals;
	
	public WeekCalendarView(int days, int intervals, int labels) {
		this.days = days;
		this.labels = labels;
		this.intervals = intervals;
		
		addContent();
		addLayout();
		addEvents();
	}

	public void changeCalendar(WeekCalendar cal) {
		
		if (this.days == cal.getNumDays() && cal.getIntervals() == intervals) {
			this.cal = cal;	
			for (int day = 0; day < days; day++) {
				cal_views[day].changeCalendar(cal.getDayCals()[day]);
			}
			
			String[] days = cal.getDays();
			for (int i = 0; i < days.length; i++)
				week_lbls[i].setText(days[i].substring(0, 3));
			
			Time[] times = cal.getLabels(labels); //times[i].toString() //cal.getIntervals()/2+1
			for (int i = 0; i < labels; i++)
				time_lbls[i].setText(times[i].toString());
		}
	}
	
	@Override
	public void addContent() {
		grid = new GridPane();
		week_lbls = new Label[days];
		time_lbls = new Label[labels];
		
		Label week_lbl; 
		for (int i = 0; i < days; i++) {
			week_lbl = new Label();
			week_lbl.setFont(Font.font(Menu.font));
			week_lbl.setTextAlignment(TextAlignment.CENTER);
			
			grid.add(week_lbl, i+1, 0);
			GridPane.setFillWidth(week_lbl, true);
			GridPane.setHalignment(week_lbl, HPos.CENTER);
			week_lbls[i] = week_lbl;
		}
		
		Label time_lbl;
		for (int i = 0; i < labels; i++) {
			time_lbl = new Label();
			time_lbl.setFont(Font.font(Menu.font, 10));
			time_lbl.setAlignment(Pos.BASELINE_CENTER);
			time_lbl.setTextAlignment(TextAlignment.CENTER);
			time_lbl.setTranslateY(-5);
			time_lbl.setMaxWidth(Double.MAX_VALUE);
			
			grid.add(time_lbl, 0, i+1);
			GridPane.setFillHeight(time_lbl, true);
			GridPane.setValignment(time_lbl, VPos.BASELINE);
			time_lbls[i] = time_lbl;
		}
		
		cal_views = new DayCalendarView[days];
		for (int day = 0; day < cal_views.length; day++) {
			cal_views[day] = new DayCalendarView(intervals);
			Label[] slots = cal_views[day].getSlots();
			for (int i = 0; i < intervals; i++)
				grid.add(slots[i], day+1, i+1);
		}
		this.getChildren().addAll(grid);
	}

	@Override
	public void addLayout() {		
		grid.getColumnConstraints().clear();
		grid.getRowConstraints().clear();
		
		r = new RowConstraints();
		c = new ColumnConstraints();
		c.setHgrow(Priority.ALWAYS);
		
		grid.getRowConstraints().add(r);
		//grid.getColumnConstraints().add(c);
		
		//for (int i = 0; i < WeekCalendar.days.length+1; i++)	
		//	grid.getColumnConstraints().add(c);
		//for (int i = 0; i < DayCalendar.intervals+2; i++)
		//	grid.getRowConstraints().add(r);
		
		grid.setHgap(0);
		grid.setVgap(0);
		
		this.setStyle("-fx-background-color: #dddeee; -fx-border-width: 0.5; -fx-border-color: black ;");
	}

	@Override
	public void addEvents() {
		/*this.setOnMouseClicked(m->{
			int col = (int)((m.getX()-c.getPrefWidth())/(c.getPrefWidth()));
			int row = (int)((m.getY()-r.getPrefHeight())/(r.getPrefHeight()));
			Util.print(m.getX(), m.getY(), "->", col, row);
			
			if (false && col > 0 && col < cal.getDayCalendars().length) {
				DayCalendar day_cal = cal.getDayCalendars()[col-1];
				//int row = (int)((m.getY()-r.getPrefHeight())/(r.getPrefHeight()));
				Util.print(row, col);
				day_cal.set(row-1, row-1, !day_cal.isBusy(row-1, row-1));
				//Util.print(col, day_cal.isBusy(row-1, row-1));
				cal_views[col-1].update(row-1, row-1);
			}
		});
		*/
		
	}
	
	public void resize(int width, int height, int k) {
		
		this.setMaxSize(width, height);
		this.setMinSize(width, height);
		grid.setMaxSize(width, height);
		grid.setMinSize(width, height);
		
		for (DayCalendarView cal_view: cal_views)
			cal_view.resize(width/(cal.getNumDays()+1), height-k*2, k);
		
		r.setPrefHeight(k);
		c.setPrefWidth(width/(cal.getNumDays()+1));
	}
	
}
