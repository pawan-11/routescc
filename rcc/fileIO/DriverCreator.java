package fileIO;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Date;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import data.Data;
import person.Driver;
import util.WeekCalendar;
import util.Location;
import util.Time;
import util.Util;


public class DriverCreator {

	
	public static final String driverfilename = "drivers.csv";

	static {
		new DriverCreator(); //Singleton
	}

	private DriverCreator() { 

	}

	public static void addDrivers(Data data) {

		String[] cols = null;
		int line = 0;

		try {
			CSVReader cr = null;
			try {
				Util.print(Paths.get(DataCreator.save_dir_path, "drivers.csv").toString());
				cr = new CSVReader(new FileReader(Paths.get(DataCreator.save_dir_path, "drivers.csv").toString()));
			}
			catch(Exception e) {
			//	cr = new CSVReader(new FileReader(DataCreator.class_path+"/drivers.csv"));
			//	cr = new CSVReader(new FileReader(DataCreator.class.getResource("drivers.csv").getPath()));
				cr = new CSVReader(new InputStreamReader(DataCreator.class.getResourceAsStream("drivers.csv")));
			}
			cr.readNext();
			cr.readNext();
			
			while ((cols = cr.readNext()) != null) {
				Date birth_date = Util.date_format.parse("1965-10-20");
				Date license_expiry = Util.date_format.parse("2029-10-10");
				Date insurance_expiry = Util.date_format.parse("2025-10-10");
				
				data.addDriver(new Driver(new WeekCalendar(), cols[0], cols[1], cols[2], cols[3],
						new Location(0, 0, cols[4]), birth_date, license_expiry, insurance_expiry, cols[5]));
				
				line++;
			}
			cr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Driver file read error at line "+line+" : "+cols);
		}
	}

	public static void addSavedDrivers(Data data) { //add drivers from saved drivers file

		String[] cols = null;
		int line = 0;

		try {
			CSVReader saved_cr = null;
			saved_cr = new CSVReader(new FileReader(Paths.get(DataCreator.save_dir_path,"saved_drivers.csv").toString()));

			saved_cr.readNext();
			saved_cr.readNext(); //headers

			while ((cols = saved_cr.readNext()) != null) {
				assert(cols.length == 12);

				Date birth_date = Util.date_format.parse(cols[7]);
				Date license_expiry = Util.date_format.parse(cols[8]);
				Date insurance_expiry = Util.date_format.parse(cols[9]);

				String[] cal_details = cols[11].split(","); //start_time_minutes,week_days,bitmap
				int start_day = Integer.parseInt(cal_details[0]); //start day of schedule, e.g monday
				int week_days = Integer.parseInt(cal_details[1]); //number of days in schedule after start_day, e.g. 5
				Time start_time = new Time(Integer.parseInt(cal_details[2])); //start time of each day in weekly calendar
				Time end_time = new Time(Integer.parseInt(cal_details[3])); //start time of each day in weekly calendar
				String week_bitmap = cal_details[4]; //1s and 0s indicating when the driver is busy or free

				data.addDriver(new Driver(new WeekCalendar(start_day, week_days, start_time, end_time, week_bitmap),
						cols[0], cols[1], cols[2], cols[3], 
						new Location(Double.parseDouble(cols[5]), Double.parseDouble(cols[6]), cols[4]),
						birth_date, license_expiry, insurance_expiry, cols[10]));

				line++;
			}
			saved_cr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("save_driver file read error at line "+line);
		}
	}

	public static void saveDrivers(Data data) {
		String save_path = Paths.get(DataCreator.save_dir_path, "saved_drivers.csv").toString();		
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(save_path, false), ',');

			Driver[] drivers = data.getDrivers();

			writer.writeNext("Active Drivers");
			writer.writeNext("First Name","Last Name","Phone","Cell Phone",
					"Address", "Latitude", "Longitude",
					"Birth Date", "License Expiry", 
					"Insurance expiry", "Comments", "Calendar");

			for (Driver d: drivers) {
				String[] attrs = getDriverRow(d);	
				assert(attrs.length == 12); //12 column file
				writer.writeNext(attrs);
			}
			writer.close();	
		}
		catch (Exception io) {
			System.out.println("Save Driver Data Error "+save_path);
			//io.printStackTrace();
		}
	}

	private static String[] getDriverRow(Driver driver) {
		WeekCalendar cal = driver.getWeekCalendar();
		return new String[] {driver.getFirstName(), driver.getLastName(), driver.getPhone(), "",
				driver.getAddress().toString(), driver.getAddress().getX()+"", driver.getAddress().getY()+"",
				Util.date_format.format(driver.getBirthDate()), Util.date_format.format(driver.getLicenseExpiry()),
				Util.date_format.format(driver.getInsuranceExpiry()), driver.getComments(), 
				cal.toString()};
	}

	public static void openCsv() { //open the drivers csv 
		try {
			String path = Paths.get(DataCreator.save_dir_path, "saved_drivers.csv").toString();
			if (Desktop.isDesktopSupported())
				Desktop.getDesktop().open(new File(path));
			else
				Runtime.getRuntime().exec("open "+path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
