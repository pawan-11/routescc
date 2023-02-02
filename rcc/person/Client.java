package person;

import java.util.ArrayList;
import java.util.Date;

import ride.Ride;
import util.Location;
import util.WeekCalendar;


public class Client extends Person {

	private ArrayList<Ride> rides;
	private String client_id;
	private WeekCalendar calendar;
	//private ClientPreferences preferences;
	
	public static Client nullClient;
	static {
		nullClient = new Client("XX-XXXXXX", "First Name", "Last Name", "000-000-0000");
	}
	
	public Client(String client_id, String first_name, String last_name, String phone) {
		super(first_name, last_name, phone);
		this.client_id = client_id;
		this.rides = new ArrayList<Ride>();
	}
	
	public void addRide(Ride ride) {
		rides.add(ride);
	}
	
	public ArrayList<Ride> getRides() {
		return rides;
	}

	public String getClientId() {
		return client_id;
	}
	
	@Override
	public String getType() {
		return "Client";
	}
	
}
