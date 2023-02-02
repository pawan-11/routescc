package ride;

import person.Client;
import person.Driver;
import java.text.SimpleDateFormat;
import java.util.Date;
import au.com.bytecode.opencsv.CSVWriter;
import util.Location;
import util.Time;

public class Ride {

	private String description;
	private Location pickup_loc, dest_loc;
	private Client client;
	private Date ride_date;
	private Time start_time, end_time;
	private Driver driver;
	
	public Ride(Client client, Date ride_date, Time start_time, 
			Time end_time, Location pickup_loc, Location dest_loc, String description) {
		
		this.client = client;
		this.description = description;
		this.pickup_loc = pickup_loc;
		this.dest_loc = dest_loc;
		this.start_time = start_time;
		this.end_time = end_time;
		this.ride_date = ride_date;
		assignDriver(Driver.nullDriver);
	}
	
	public Ride(Client client, Date Ride_date, Time start_time, 
			Time end_time, Location pickup_loc, Location dest_loc) {
		this(client, Ride_date, start_time, end_time, pickup_loc, dest_loc, "");
	}
	
	public void assignDriver(Driver d) {
		this.driver = d;
		this.driver.getWeekCalendar().setBusy(ride_date, start_time, end_time);
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public void setStartTime(Time start_time) {
		this.start_time = start_time;
	}
	
	public void setEndTime(Time end_time) {
		this.end_time = end_time;
	}
	
	public void setRideDate(Date d) {
		this.ride_date = d;
	}
	
	public void setPickupLoc(Location pickup_loc) {
		this.pickup_loc = pickup_loc;
	}
	
	public void setDestLoc(Location dest_loc) {
		this.dest_loc = dest_loc;
	}
	
	public Client getClient() {
		return client;
	}
	
	public Driver getDriver() {
		return driver;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Location getPickupLoc() {
		return pickup_loc;
	}
	
	public Location getDestLoc() {
		return dest_loc;
	}
	
	public int getWait() {
		return end_time.subtract(start_time);
	}

	public Time getStartTime() {
		return start_time;
	}
	
	public Time getEndTime() {
		return end_time;
	}
	
	public Date getRideDate() {
		return ride_date;
	}
	
}
