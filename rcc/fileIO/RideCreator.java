package fileIO;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import person.Client;
import person.Driver;
import data.Data;
import java.nio.file.Paths;
import ride.Ride;
import java.util.Date;
import java.util.List;
import util.Location;
import util.Time;
import util.Util;

public class RideCreator {


	private RideCreator() {

	}

	public static void addRides(Data data) {
		String[] cols = null;
		int line = 0;

		Matcher date_m, time_m;
		Date ride_date;
		Time start_time, end_time;

		try {
			CSVReader cr = null;
			try {
				cr = new CSVReader(new FileReader(Paths.get(DataCreator.save_dir_path,"rides.csv").toString()));
			}
			catch (Exception e) {
				//cr = new CSVReader(new FileReader(DataCreator.class_path+"/rides.csv"));
				cr = new CSVReader(new InputStreamReader(DataCreator.class.getResourceAsStream("rides.csv")));
			}

			cr.readNext();
			cr.readNext();
			cr.readNext();

			while ((cols = cr.readNext()) != null && cols.length == 7) {

				date_m = Util.date.matcher(cols[1]);
				date_m.matches();
				ride_date = new SimpleDateFormat("yyyy-MM-dd").parse(date_m.group(0));

				time_m = Util.time.matcher(cols[2]);
				time_m.matches();				
				start_time = new Time(Integer.parseInt(time_m.group(1)), Integer.parseInt(time_m.group(2)), time_m.group(3));

				time_m = Util.time.matcher(cols[3]);
				time_m.matches();	
				end_time = new Time(Integer.parseInt(time_m.group(1)), Integer.parseInt(time_m.group(2)), time_m.group(3));

				Location pickup_loc = new Location(0, 0,cols[5]);
				Location dest_loc = new Location(0, 0,cols[6]);

				Client c = new Client(cols[0], "C First name"+line, "Last name"+line, "c phone #"); //only have client id in data so far

				data.addRide(new Ride(c, ride_date, start_time, end_time, pickup_loc, dest_loc));
			}

			cr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("rides.csv file read error at line "+line+":");
		}
		//data.check();
	}

	public static void addSavedRides(Data data) {
		String[] cols = null;
		int line = 0;

		Matcher date_m, time_m;
		Date ride_date;
		Time start_time, end_time;

		try {
			CSVReader cr = new CSVReader(new FileReader(Paths.get(DataCreator.save_dir_path, "upcoming_rides.csv").toString())); 
			Util.print("reading rides from "+Paths.get(DataCreator.save_dir_path, "upcoming_rides.csv").toString());
			cr.readNext();
			cr.readNext();
			cr.readNext();

			while ((cols = cr.readNext()) != null) {
				assert(cols.length == 16);

				date_m = Util.date.matcher(cols[4]);
				date_m.matches();
				ride_date = Util.date_format.parse(date_m.group(0));

				time_m = Util.time.matcher(cols[5]);
				time_m.matches();				
				start_time = new Time(Integer.parseInt(time_m.group(1)), Integer.parseInt(time_m.group(2)), time_m.group(3));

				time_m = Util.time.matcher(cols[6]);
				time_m.matches();	
				end_time = new Time(Integer.parseInt(time_m.group(1)), Integer.parseInt(time_m.group(2)), time_m.group(3));

				Location pickup_loc = new Location(Double.parseDouble(cols[8]), Double.parseDouble(cols[9]),cols[7]);
				Location dest_loc = new Location(Double.parseDouble(cols[11]), Double.parseDouble(cols[12]),cols[10]);

				Client c = new Client(cols[0], cols[1], cols[2], cols[3]); //only have client id in data so far
				Driver d = data.getDriver(cols[15]);

				Ride r = new Ride(c, ride_date, start_time, end_time, pickup_loc, dest_loc, cols[14]);
				if (d != null)
					r.assignDriver(d);
				data.addRide(r);
			}
			cr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("upcoming ride file read error at line "+line+":");
			//Util.print(cols);
		}
		//data.check();
	}

	public static void saveRides(Data data) {
		String save_path = "";
		try {

			save_path = Paths.get(DataCreator.save_dir_path, "past_rides.csv").toString();
			CSVWriter writer;

			File f = new File(save_path);

			if (f.exists()) {
				writer = new CSVWriter(new FileWriter(save_path, true));
			}
			else {
				writer = new CSVWriter(new FileWriter(save_path, false));	

				writer.writeNext("Upcoming Rides List");
				writer.writeNext("Client", "", "", "Driver name", "Driver Phone", "Ride", "", "", 
						"Pickup Location", "", "", "Destination Location", "", "", "");
				writer.writeNext(
						"Client ID", "First Name", "Last name", "Phone",
						"Date", "Start Time", "End Time", 
						"Address", "Latitude", "Longitude", //source loc
						"Address", "Latitude", "Longitude", //dest loc
						"Wait minutes", "Description", "Driver");
			}
			Util.print("saving to "+save_path);

			List<Ride> rides = data.getRides();

			int idx = 0;

			Date d = Util.date_format.parse(Util.date_format.format(new Date()));
			Ride r;
			while (idx < rides.size() && (r = rides.get(idx)).getRideDate().before(d)) {//if the ride is in the past day, save it to old rides file
				//Util.print(rides.get(idx).getRideDate()+" is before "+d.toString());
				writer.writeNext(getRideRow(r));
				idx += 1;
			}
			Util.print("Wrote to past files "+idx);
			writer.close();

			writer = new CSVWriter(new FileWriter(Paths.get(DataCreator.save_dir_path, "upcoming_rides.csv").toString(), false), ',');
			writer.writeNext("Past Rides List");
			writer.writeNext("Client", "", "", "Driver name", "Driver Phone", "Ride", "", "", 
					"Pickup Location", "", "", "Destination Location", "", "", "");
			writer.writeNext(
					"Client ID", "First Name", "Last name", "Phone",
					"Date", "Start Time", "End Time", 
					"Address", "Latitude", "Longitude", //source loc
					"Address", "Latitude", "Longitude", //dest loc
					"Wait minutes", "Description", "Driver");

			while (idx < rides.size()) {
				writer.writeNext(getRideRow(rides.get(idx)));
				idx += 1;
			}
			writer.close();
		}
		catch (IOException io) {
			System.out.println("Save Rides files Error "+save_path);
			io.printStackTrace();
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static String[] getRideRow(Ride ride) {
		String rideDate = new SimpleDateFormat("yyyy-MM-dd").format(ride.getRideDate());
		Client client = ride.getClient();

		return new String[] {
				client.getClientId(), client.getFirstName(), client.getLastName(), client.getPhone(), //client info
				rideDate, ride.getStartTime().toString(), ride.getEndTime().toString(),  //ride times
				ride.getPickupLoc().toString(), ride.getPickupLoc().getX()+"", ride.getPickupLoc().getY()+"", //ride pickup location
				ride.getDestLoc().toString(), ride.getDestLoc().getX()+"", ride.getDestLoc().getY()+"", //ride destination location
				ride.getWait()+"", ride.getDescription(), 
				ride.getDriver().getFirstName()+","+ride.getDriver().getPhone()};  //wait minutes for ride and description
	}

	public static void openCsv(boolean upcoming) { //open the drivers csv 
		try {
			if (upcoming) {
				String path = Paths.get(DataCreator.save_dir_path,"upcoming_rides.csv").toString();
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(new File(path));
				}
				else
					Runtime.getRuntime().exec("open "+path);

			}
			else {
				String path = Paths.get(DataCreator.save_dir_path,"past_rides.csv").toString();
				if (Desktop.isDesktopSupported())
					Desktop.getDesktop().open(new File(path));
				else
					Runtime.getRuntime().exec("open "+path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
