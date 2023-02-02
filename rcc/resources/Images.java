package resources;

import java.net.URL;
import javafx.scene.image.Image;


public class Images {

	public static Image menubg1, menubg2, 
	title,
	searchmodebtn, schedulebtn, plannerbtn,
	searchbtn, homebtn, morebtn, closebtn, assignbtn,
	textfieldbg,
	nextbtn, prevbtn
	;
	
	static {
		title = getImg("rcclogo.png");
		
		searchbtn = getImg("searchbtn.png");
		homebtn = getImg("homebtn.png");
		closebtn = getImg("closebtn.png");

		morebtn = getImg("morebtn.png");
		assignbtn = getImg("assignbtn.png");
		
		schedulebtn = getImg("schedulebtn.png");
		plannerbtn = getImg("plannerbtn.png");

		textfieldbg = getImg("textfieldbg.png");
		
		nextbtn = getImg("nextbtn.png");
		prevbtn = getImg("prevbtn.png");
	}
			
	private static URL url;
	private static Image getImg(String name) {
		url = Images.class.getResource("images/"+name);
		if (url == null) {
			System.out.println("Image "+name+" does not exist");
			return null;
		}
		return new Image(url.toString());
	}
	
}
