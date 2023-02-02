package person;

import util.Location;

abstract public class Person {
	
	protected String first_name, last_name, phone;
	
	public Person(String first_name, String last_name, String phone)  {
		this.first_name = first_name;
		this.last_name = last_name;
		this.phone = phone;
	}
	
	public String getFirstName() {
		return first_name;
	}
	
	public String getLastName() {
		return last_name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}
	
	public void setLastName(String last_name) {
		this.last_name = last_name;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getFullName() {
		return first_name+" "+last_name;
	}
	
	public String getType() {
		return "Person";
	}
	
}
