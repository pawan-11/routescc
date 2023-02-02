package util;

public class Location {
	
	private double x = 0, y = 0;
	private String address;
	
	public Location(double x, double y, String address) { 
		copy(x, y);
		if (x == 0 && y == 0) //invalid coordinates
			GMaps.fix(this);
		this.address = address;
	}
	
	public Location(double x, double y) {
		this(x, y, "No address name");
	}
	
	public Location(String name) {
		this(-1, -1, name);
	}
	
	public int deg_ang(Location p) {
		return Math.floorMod((int)Math.toDegrees(this.rad_ang(p)), 360);
	}
	
	public double rad_ang(Location p) {
		return Math.atan2(y-p.getY(), p.getX()-x);
	}
	
	public int distance(Location loc) {
		return (int)(Math.sqrt(Math.pow(loc.getY()-y, 2)+Math.pow(loc.getX()-x, 2)));
	}
	
	public void copy(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public String getStreet() {
		String[] parts = this.address.split(", ");
		return parts.length == 4?parts[0]:"";	
	}
	
	public String getTown() {
		String[] parts = this.address.split(", ");
		if (parts.length == 4)
			return parts[1];
		else if (parts.length == 1)
			return parts[0];
		return "";
	}
	
	public String getProvince() {
		String[] parts = this.address.split(", ");
		return parts.length == 4?parts[2]:"";	
	}
	
	public String getPostal() {
		String[] parts = this.address.split(", ");
		return parts.length == 4?parts[3]:"";	
	}
	
	public double getX() { //get latitude of the location
		return x;
	}
	
	public double getY() { //get longitude of the location
		return y;
	}
	
	public Location copy() {
		return new Location(x, y);
	}
	
	public String toString() {
		return address;
	}
	
	//StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	//StackTraceElement e = stacktrace[2];//maybe this number needs to be corrected
	//String methodName = e.getMethodName();
	//System.out.println(methodName);
}
