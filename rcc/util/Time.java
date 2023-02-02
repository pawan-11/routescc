package util;

import java.util.regex.Matcher;

public class Time {
	
	private int hour, minute;
	/*
	 * 24-hour clock time
	 */
	public Time(int hour, int minute) {
		changeTime(hour, minute);
	}
	
	/*
	 * 12 hour standard clock
	 */
	public Time(int hour, int minute, String delimeter) {
		hour += (delimeter.equals("pm") && hour <= 11)?12:0;
		changeTime(hour, minute);
	}
	
	public Time(int minutes) {
		this(minutes/60, minutes%60);
	}
	
	public Time() {
		this(0, 0);
	}
	
	public void changeTime(int hour, int minute) {
		this.hour = hour%24;
		this.minute = minute%60;
	}
	
	public boolean changeTime(String time) {
		try {
			Matcher time_m = Util.time.matcher(time);
			time_m.matches();	
			int hour = Integer.parseInt(time_m.group(1));
			hour += (time_m.group(3).equals("pm") && hour <= 11)?12:0;
			changeTime(hour, Integer.parseInt(time_m.group(2)));
		}
		catch (IllegalStateException e) {
			changeTime(0, 0);
			return false;
		}
		return true;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public boolean before(Time t) {
		return this.hour < t.getHour() || (this.hour == t.getHour() && this.minute < t.getMinute());
	}
	
	/**
	 * @param t
	 * @return true if this time comes strictly after t
	 */
	public boolean after(Time t) { 
		return this.hour > t.getHour() || (this.hour == t.getHour() && this.minute > t.getMinute());
	}
	
	public boolean equals(Time t) {
		return this.hour == t.getHour() && this.minute == t.getMinute();
	}
	
	public boolean afterOrAt(Time t) {
		return this.after(t) || this.equals(t);
	}
	
	public boolean beforeOrAt(Time t) {
		return this.before(t) || this.equals(t);
	}
	
	public int toMinutes() {
		return 60*hour+minute;
	}
	
	public int subtract(Time t) {
		int hours_diff = this.hour-t.getHour();
		int mins_diff = this.minute-t.getMinute();
		return hours_diff*60+mins_diff;
	}
	
	public String toString() {
		String meridiem = "am"; 
		int hour = this.hour;
		if (this.hour >= 12) {
			meridiem = "pm";
			if (this.hour >= 13)
				hour %= 12;
		}
		String str = String.format("%d", hour)+":"+String.format("%02d", minute)+" "+meridiem;;
		return str;
	}
	
}
