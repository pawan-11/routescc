package util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekCalendar { 

	private DayCalendar[] day_cals; 
	public static WeekCalendar EMPTY = new WeekCalendar();
	public final static String weekdays[] = {"Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
	
	private int intervals;
	private int start_day;
	private Time start_time, end_time;
	
	public WeekCalendar(int start_day, int cal_days, Time start_time, Time end_time, String bitmap) {
		
		this.start_day = start_day;
		this.start_time = start_time;
		this.end_time = end_time;
		this.intervals = bitmap.length()/cal_days;
		this.day_cals = new DayCalendar[cal_days];
		for (int i = 0; i < day_cals.length; i++) {
			day_cals[i] = new DayCalendar(intervals);
		}
		setSchedule(bitmap);
	}
	
	public WeekCalendar(int start_day, int cal_days, Time start_time, Time end_time, int intervals, boolean all_busy) {
		this(start_day, cal_days, start_time, end_time, getBitmap(all_busy, cal_days*intervals));
	}
	
	public WeekCalendar() {
		this(0, 7, new Time(8,0), new Time(20, 0), 12, false);
	}
	
	public void setSchedule(String bitmap) {

		if (bitmap.length() != day_cals.length*intervals) {
			//Util.print("invalid bitmap");
			setSchedule(false);
		}
		else {
			for (int i = 0; i < day_cals.length; i++) {
				String day_bitmap = bitmap.substring(i*(intervals), (i+1)*intervals);
				assert(day_bitmap.length() == intervals);
				day_cals[i].setSchedule(day_bitmap);
			}
		}
	}
	
	public void setSchedule(boolean all_busy) {
		for (int i = 0; i < day_cals.length; i++)
			day_cals[i].setSchedule(all_busy);
	}
	
	
	public boolean isBusy(Date date, Time start, Time end) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return day_cals[getIndex(cal.get(Calendar.DAY_OF_WEEK))].isBusy(start, end);
	}
	
	public void setBusy(Date date, Time start_time, Time end_time) {
		Calendar.getInstance().setTime(date);
		day_cals[getIndex(Calendar.DAY_OF_WEEK)].set(start_time, end_time, true);
	}
	
	public void setBusy(String day, Time start_time, Time end_time) {
		day_cals[getIndex(day)].set(start_time, end_time, true);
	}
	
	public int getIndex(int calendar_index) {
		//calendar index is 1 for sunday, 2 monday ...
		//our index is 6 for sunday, 0 monday, ...
		return (calendar_index+5)%7;
	}
	
	public Time[] getLabels(int labels) { //return <labels> many Times from start time and end time, parititoned equally
		Time[] time_labels = new Time[labels];	
		Time curr_time = start_time;
		
		int partition = (end_time.toMinutes()-start_time.toMinutes())/intervals;
		
		//Util.print(curr_time, end_time, curr_time.toMinutes(), end_time.toMinutes(), intervals, partition);
		for (int i = 0; i < labels; i++) {
			time_labels[i] = curr_time;
			curr_time = new Time(curr_time.toMinutes()+partition);	
		}
		return time_labels;
	}
	
	
	public String[] getDays() {
		int i = 0, idx = start_day;
		
		String days[] = new String[day_cals.length];
		
		while (i < day_cals.length) {
			assert(idx < weekdays.length);
			days[i] = weekdays[idx];
			i++;
			idx++;
			if (idx >= weekdays.length)
				idx = 0;
		}
		return days;
	}
	
	public DayCalendar[] getDayCals() {
		return day_cals;
	}
	
	public int getNumDays() {
		return day_cals.length;
	}
	
	public int getIntervals() { //intervals in every day of this calendar
		return intervals;
	}
	
	public String getBitmap() {
		String bitmap = "";
		for (DayCalendar cal: day_cals) {
			bitmap += cal.getBitmap();
		}
		return bitmap;
	}
	
	public String toString() {
		return start_day+","+getNumDays()+","+start_time.toMinutes()+","+end_time.toMinutes()+","+getBitmap();
	}
	
	public static int getIndex(String day) {	
		for (int i = 0; i < weekdays.length; i++)
			if (weekdays[i].toLowerCase().contains(day.toLowerCase()))
				return i;
		return 6;
	}
	
	public static String getBitmap(boolean all_busy, int length) {
		if (all_busy)
			return new String(new char[length]).replace('\0', '1');
		return new String(new char[length]).replace('\0', '0');
	}
	
}
