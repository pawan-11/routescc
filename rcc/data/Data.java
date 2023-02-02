package data;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import fileIO.DriverCreator;
import fileIO.RideCreator;
import ride.Ride;
import person.Client;
import person.Person;
import person.Driver;
import java.util.Date;
import util.Location;
import util.Time;
import util.Util;


public class Data {
	
	private ArrayList<Ride> rides;
	
	private HashMap<String, Driver> drivers; //firstname,phone: driver
	public HashMap<String, Client> clients; //clientid: client, derived from Rides
	//private ArrayList<Person> persons; //union of clients, operators and drivers
	
	/**
	 * Wrapper for all the data. Data filled by a DataCreator object
	 */
	public Data() {
		this.drivers = new HashMap<String, Driver>();
		this.rides = new ArrayList<Ride>();	
		this.clients = new HashMap<String, Client>();
	}
	 
	/**
	 * Add new rides to this.rides while maintaining increasing ride date order
	 * Assume this.rides is in increasing date order
	 * @param new_rides
	 */
	public void addRide(Ride... new_rides) {
		int idx = 0;
		if (this.rides.size() == 0 && new_rides.length != 0) { //add the first ride manually
			this.rides.add(new_rides[0]);
			
			if (this.clients.get(new_rides[0].getClient().getClientId()) == null) { 
				this.clients.put(new_rides[0].getClient().getClientId(), new_rides[0].getClient());
			}	
			
			idx = 1;
		}
		
		Ride ride;
		while (idx < new_rides.length) {
			ride = new_rides[idx];
			
			int i = this.rides.size()-1;
			while (i >= 0 && Util.before(ride.getRideDate(), ride.getStartTime(), 
					this.rides.get(i).getRideDate(), this.rides.get(i).getStartTime())) { //loop until ride is greater 
				//(should be constant time if the rides are added from oldest to newest
				i--;
			}
			
			this.rides.add(i+1, ride);	
			idx++;
			
			if (this.clients.get(ride.getClient().getClientId()) == null) { //if client not in the hashmap already
				this.clients.put(ride.getClient().getClientId(), ride.getClient());
			}	
			
		}
			
	}
	
	public void addDriver(Driver... new_drivers) {
		for (Driver d: new_drivers) {
			if (!this.drivers.containsKey(d.getFirstName()+" "+d.getPhone()))
				this.drivers.put(d.getFirstName()+","+d.getPhone(), d);
				
			//this.persons.add(d);
		}
	}
	
	public void check() {
		//check the rides list is sorted by date and time
		for (int i = 1; i < this.rides.size(); i++) {
			assert rides.get(i).getRideDate().after(rides.get(i-1).getRideDate()); 
			//|| rides.get(i).getRideDate().equals(rides.get(i-1).getRideDate()
			Util.print(rides.get(i-1).getRideDate(), rides.get(i-1).getStartTime());
		}
		Util.print(rides.get(this.rides.size()-1).getRideDate(), rides.get(rides.size()-1).getStartTime());
	}
	
	public ArrayList<Person> searchPerson(String keyword) {
		ArrayList<Person> persons = new ArrayList<Person>(); //fill this list with names matching <name>
		
		persons.addAll(searchDriver(keyword));
		persons.addAll(searchClient(keyword));
		
		return persons; //should be just persons after complete
	}
	
	public ArrayList<Driver> searchDriver(String keyword) {
		keyword = keyword.toLowerCase();
		ArrayList<Driver> drivers = new ArrayList<Driver>();
		
		for (Driver d: this.drivers.values()) {
			if (d.getFirstName().toLowerCase().contains(keyword) || d.getLastName().toLowerCase().contains(keyword)
					|| d.getAddress().getTown().toLowerCase().contains(keyword) || (d.getType().toLowerCase()+"s").contains(keyword))
				drivers.add(d);
		}
		return drivers;
	}
	
	
	public ArrayList<Client> searchClient(String keyword) {
		keyword = keyword.toLowerCase();
		ArrayList<Client> clients = new ArrayList<Client>();
		
		for (Client c: this.clients.values()) {
			if (c.getFirstName().toLowerCase().contains(keyword) || c.getLastName().toLowerCase().contains(keyword)
					|| (c.getType().toLowerCase()+"s").contains(keyword))
				clients.add(c);
		}
		return clients;
	}
	
	public ArrayList<Driver> searchDriver(Location loc, double radius) {
		ArrayList<Driver> drivers = new ArrayList<Driver>();
		
		for (Driver d: drivers) {
			if (d.getAddress().distance(loc) <= radius)
				drivers.add(d);
		}
		return drivers;
	}
	
	
	/**
	 * @param city
	 * @return the Rides in this town/city
	 */
	public ArrayList<Ride> searchRide(String town) { 
		ArrayList<Ride> rides = new ArrayList<Ride>(); 
		
		for (Ride r: this.rides) {
			if (r.getPickupLoc().getTown().equals(town))
				rides.add(r);
		}
		
		return rides; 
	}
	
	/**
	 * @param city
	 * @return the Rides in this town/city
	 */
	public ArrayList<Ride> searchRide(Date date) { 
		date = Util.trim_time(date);
		ArrayList<Ride> rides = new ArrayList<Ride>(); 
		
		for (Ride r: this.rides) {
			if (date.equals(Util.trim_time(r.getRideDate())))
				rides.add(r);
			if (r.getRideDate().after(Util.trim_time(date))) //using precondition that rides are sorted by time
				break;
		}
		
		return rides; 
	}
	
	public ArrayList<Ride> findRide(Driver d) { 
		ArrayList<Ride> rides = new ArrayList<Ride>(); //find list of Rides that suit this driver
		
		
		for (Ride r: rides) {
			if (r.getPickupLoc().getTown().equals(d.getAddress().getTown()) &&
					!d.getWeekCalendar().isBusy(r.getRideDate(), r.getStartTime(), r.getEndTime()))
				rides.add(r);
		}
		
		return rides;
	}
	
	/**
	 * @param Ride
	 * @return drivers suitable for this ride
	 */
	public ArrayList<Driver> findDrivers(Ride r, int max) {
		
		ArrayList<Driver> drivers = new ArrayList<Driver>(); //list of drivers that suit this Ride
		int found = 0;
		for (Driver d: this.drivers.values()) {
			if (r.getPickupLoc().getTown().equals(d.getAddress().getTown()) ||
					!d.getWeekCalendar().isBusy(r.getRideDate(), r.getStartTime(), r.getEndTime())) {
				drivers.add(d);
				found++;
				if (found == max)
					break;
			}
		}	
		return drivers;
	}
	
	public ArrayList<Driver> findDrivers(Ride r) {
		return findDrivers(r, this.drivers.size());
	}
	
	public List<Ride> getEmptyRides(Date date) {
		List<Ride> empty_rides = new ArrayList<Ride>(); 
		List<Ride> day_rides = this.searchRide(date);
		
		for (Ride r: day_rides) {
			if (r.getDriver().getFullName().equals(Driver.nullDriver.getFullName()) &&
					r.getRideDate().equals(date))
				empty_rides.add(r);
		}
		
		return empty_rides;
	}
	
	public List<Ride> getFilledRides(Date date) {
		List<Ride> filled_rides = new ArrayList<Ride>(); 
		List<Ride> day_rides = this.searchRide(date);
		
		for (Ride r: day_rides) {
			if (!r.getDriver().getFullName().equals(Driver.nullDriver.getFullName()) &&
					r.getRideDate().equals(date))
				filled_rides.add(r);
		}
		
		return filled_rides;
	}
	
	/**
	 * 	Create a convenient matching between volunteers and unfulfilled Rides
	 * 	prefernces, availability etc
	 *	Ride details should match volunteer
	 *	Rides without a driver 
	 * @return Mapping between Rides and drivers
	 */
	public HashMap<Ride, Driver> getMatching() {
		HashMap<Ride, Driver> matching = new HashMap<Ride, Driver>();
		
		//TODO: Match as many pairing as possible between list of Rides and drivers
		
		
		return matching;
	}
	
	public Client getClient(String client_id) {
		return clients.get(client_id);
	}
	
	public Driver getDriver(String driver_id) { //driver id is phone number
		Driver d = drivers.get(driver_id);
		return (d == null)?Driver.nullDriver: d;
	}
	
	
	public ArrayList<Ride> getRides() {
		return rides;
	}
	
	public Driver[] getDrivers() {
		return drivers.values().toArray(new Driver[0]);
	}
	
	public Client[] getClients() {
		return clients.values().toArray(new Client[0]);
	}
	
	
}
