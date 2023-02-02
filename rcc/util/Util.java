package util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

public class Util {
	
	public static SimpleDateFormat date_format =  new SimpleDateFormat("yyyy-MM-dd");
	public static final Pattern date = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$"); //ride date
	public static final Pattern time = Pattern.compile("^(\\d{1,2}):(\\d{2}) ([ap]m)$"); //time
	
	public static void print(String[] arr) {
		for (String s: arr) {
			System.out.print(s+", ");
		}
		System.out.println();
	}
	
	public static void print(String s) {
		System.out.println(s);
	}
	
	public static void print(Object... o) {
		System.out.println(Arrays.toString(o));
	}
	
	public static int getK(int width, int height) {
		return height < width? height/22+1:width/22+1;
	}
	
	
	public static String trim_last_dir(String path) {
		String new_path = path;
	
		int idx = path.length()-2;
		while (idx > 0 && !(path.charAt(idx)+"").equals(System.getProperty("file.separator"))) {
			idx -= 1;
		}
		
		new_path = new_path.substring(0, idx);
		return new_path;
	}
	
	
	public static String getParent(String path) {
		return new File(path).getParent();
	}
	
	public static Date trim_time(Date date) { //remove the time from date
		try {
			return date_format.parse(date_format.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * @param d1
	 * @param t1
	 * @param d2
	 * @param t2
	 * @return true if (d1, t1) is strictly before to (d2, t2)
	 */
	public static boolean before(Date d1, Time t1, Date d2, Time t2) { //used to compare data and time together
		boolean beforeDate = d1.before(d2);
		boolean beforeTime = t1.before(t2);
		return beforeDate || (d1.equals(d2) && beforeTime);
	}
	
	public static String capitalize(String str) {
		String[] words = str.split(" ");
		String cap_word = "";
		for (int i = 0; i < words.length-1; i++)
			cap_word += Character.toUpperCase(words[i].charAt(0))+words[i].substring(1)+" ";
		cap_word += Character.toUpperCase(words[words.length-1].charAt(0))+words[words.length-1].substring(1);
		return cap_word;
	}
	
}
