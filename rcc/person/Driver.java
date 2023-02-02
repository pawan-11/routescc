package person;

import util.WeekCalendar;

import java.util.Calendar;
import java.util.Date;

import au.com.bytecode.opencsv.CSVWriter;
import util.Location;
import util.Util;

public class Driver extends Person {

	private String cell_phone, comments; 
	private Location address;
	private WeekCalendar calendar;
	private Date birth_date, license_expiry, insurance_expiry;
	
	public static Driver nullDriver;
	static {
		nullDriver = new Driver(new WeekCalendar(), "No Driver", "", "", "", new Location(""),
				new Date(), new Date(), new Date(), "");
	}
	
	/**
	 * @param calendar - Availability calendar
	 * @param first_name
	 * @param last_name
	 * @param phone
	 * @param city_loc - The city at which this driver resides in
	 * @param expiry_date - Driver license expiry date
	 * @param comments
	 */
	public Driver(WeekCalendar calendar, String first_name, String last_name, String phone, String cell_phone,
			Location address, Date birth_date, Date license_expiry, Date insurance_expiry, String comments) {
		super(first_name, last_name, phone);
		this.address = address;
		this.calendar = calendar;
		this.birth_date = birth_date;
		this.insurance_expiry = insurance_expiry;
		this.license_expiry = license_expiry;
		this.cell_phone = cell_phone;
		this.comments = comments;
	}
	
	public void setAddress(Location address) {
		this.address = address;
	}
	
	public Location getAddress() {
		return address;
	}
	
	public String getCellPhone() {
		return cell_phone;
	}
	
	public String getComments() {
		return comments;
	}
	
	public Date getLicenseExpiry() {
		return license_expiry;
	}
	
	public Date getInsuranceExpiry() {
		return insurance_expiry;
	}
	
	public Date getBirthDate() {
		return birth_date;
	}
	
	public WeekCalendar getWeekCalendar() {
		return calendar;
	}
	
	public boolean isLicenseExpired() {
		return Util.trim_time(new Date()).after(getLicenseExpiry());
	}
	
	public boolean isInsuranceExpired() {
		return Util.trim_time(new Date()).after(getInsuranceExpiry());
	}
	
	public int getAge() {	
		Calendar birth = Calendar.getInstance();
		birth.setTime(birth_date);
		
		Calendar now = Calendar.getInstance();
		
		return now.get(Calendar.YEAR) - birth.get(Calendar.YEAR); //TODO
	}
	
	@Override
	public String getType() {
		return "Driver";
	}
	
	public String toString() {
		return "Name: "+first_name+" "+last_name+"\n"+
				"Phone: "+phone+"\n"+
				"Location: "+address;
	}
	
}
