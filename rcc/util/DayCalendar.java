package util;


public class DayCalendar {
	
	public static DayCalendar EMPTY = new DayCalendar(12);
	private int intervals;
	private boolean busy[];

	public DayCalendar(int intervals) {
		busy = new boolean[intervals];
		this.intervals = intervals;
		setSchedule(false);
	}
	
	public DayCalendar(int intervals, boolean all_busy) {
		this(intervals); 
		setSchedule(all_busy);
	}
	
	public DayCalendar(String bitmap) {
		this(bitmap.length());
		setSchedule(bitmap);
	}
	
	public void setSchedule(String bitmap) {
		assert(bitmap.length() == busy.length);
		if (bitmap.length() == busy.length)
		for (int i = 0; i < busy.length; i++) 
			busy[i] = bitmap.charAt(i) == '1';
		else
			Util.print(bitmap.length(), busy.length);
	}
	
	public void setSchedule(boolean all_busy) {
		for (int i = 0; i < busy.length; i++) 
			busy[i] = all_busy;
	}
	
	public void set(int start_idx, int end_idx, boolean is_busy) {
		for (int i = start_idx; i <= end_idx; i++) {
			busy[i] = is_busy;
		}
	}
	
	public void set(Time start, Time end, boolean is_busy) {
		set(getIndex(start), getIndex(end), is_busy);
	}
	
	public boolean isBusy(int start_idx, int end_idx) { //return true if free for this entire interval
		for (int i = start_idx; i <= end_idx; i++) {
			if (!busy[i])
				return false;
		}
		return true;
	}
	
	public boolean isBusy(Time start, Time end) { //return true if free for this entire interval
		return isBusy(getIndex(start), getIndex(end));
	}

	protected int getIndex(Time t) {
		return (int)Math.round((double)t.toMinutes()/(24*60/busy.length));
	}
	
	protected Time getStartTime(int index) { //get start time of the interval at this index
		return new Time(index*(24*60/busy.length));
	}
	
	public String getBitmap() {
		String bitmap = "";
		for (boolean b: busy) {
			bitmap += b?1:0; //1 is for busy, 0 for not busy
		}
		return bitmap;
	}
	
	public int getIntervals() {
		return intervals;
	}
	
}
