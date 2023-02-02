package fileIO;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import data.Data;
import util.Util;
import vc.Menu;

public class DataCreator {

	public static String save_dir_path = "";

	static {
		new DataCreator();

		try {
			save_dir_path = Util.trim_last_dir(new File(DataCreator.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static Data getData() { //return a data object by adding Rides and drivers from csv files
		Menu.debug_msg.setText(Menu.debug_msg.getText()+"\nsave_dir_path:("+save_dir_path+")");
		
		Data data = new Data();

		DriverCreator.addSavedDrivers(data);
		if (data.getDrivers().length == 0) {
			System.out.println("reading old drivers ");
			DriverCreator.addDrivers(data);
			DriverCreator.saveDrivers(data);
		}
		RideCreator.addSavedRides(data);
		if (data.getRides().isEmpty()) {
			System.out.println("read old rides");
			RideCreator.addRides(data);
			RideCreator.saveRides(data);
		}

		return data;
	}

	public static void save(Data data) {
		RideCreator.saveRides(data);
		DriverCreator.saveDrivers(data);
	}

	
	public static void openFolder() {
		try {
			File file = new File(DataCreator.save_dir_path);
			if (Desktop.isDesktopSupported() && file.exists()) {
				Menu.debug_msg.setText(Menu.debug_msg.getText()+"\ndesktop supported, opening "+file.getPath());
				Desktop.getDesktop().open(file);	
			}
			else {
				Menu.debug_msg.setText(Menu.debug_msg.getText()+"\ndesktop not supported, opening "+file.getPath());
				Runtime.getRuntime().exec("open "+DataCreator.save_dir_path);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Menu.debug_msg.setText(Menu.debug_msg.getText()+"\n error:\n"+e.getLocalizedMessage());
		}
	}
	

}
