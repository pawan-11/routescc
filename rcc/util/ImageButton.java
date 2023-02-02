package util;

import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import vc.Menu;

public class ImageButton extends StackPane { 
	
	public int width, height;
	private Rectangle border;
	private Text text;
	private ImageView iv;
	private boolean lock = false;
	
	public ImageButton() {		
		iv = new ImageView();
		
		text = new Text();
		text.setTextAlignment(TextAlignment.CENTER);
		border = new Rectangle();
		border.setFill(null);
		border.setStroke(Color.WHITE);
		border.setVisible(false);
		
		text.setCache(true);
		border.setCache(true);
		border.setCacheHint(CacheHint.SCALE);
		
		this.getChildren().addAll(iv, border, text);
	}
	
	public ImageButton(Image i) {
		this();
		setImage(i);
	}
	
	public ImageButton(String text) {
		this();
		setText(text);
	}
	
	public ImageButton(Image i, String text) {
		this();
		setImage(i);
		setText(text);
	}
	
	public void updateEffect() {	
		int x = width<40?1:2; 
		int y = height<40?1:2;
		
		setOnMouseEntered(m->{	
			
			highlight(true);
			iv.setFitWidth(width-x);
			iv.setFitHeight(height-y);
		});
		setOnMouseExited(m->{
			highlight(false || lock);
			iv.setFitWidth(width+x);
			iv.setFitHeight(height+y);
		});
	}
	
	
	public void resize(int width, int height) {	
		
		iv.setFitWidth(width);
		iv.setFitHeight(height);
		
		this.layout();
		
		this.width =  (int)iv.getLayoutBounds().getWidth();
		this.height = (int)iv.getLayoutBounds().getHeight();
		
		border.setWidth(this.width+0.05*this.width);
		border.setHeight(this.height+0.05*this.height);
		border.setStrokeWidth(Math.ceil(0.01*this.height));
		
		text.setFont(Font.font(Menu.font, this.height/2));
		text.setWrappingWidth(this.width*0.8);
		
		this.setMaxSize(this.width+0.05*this.width, this.height+0.05*this.height);
		
		updateEffect();
		this.layout();
	}
	
	public void highlight(boolean hl) {
		border.setVisible(hl);
	}
	
	public void lock(boolean lock) {
		this.lock = lock;
		highlight(lock);
	}
	
	public void setImage(Image i) {
		iv.setImage(i);
	}
	
	public ImageView getIv() {
		return iv;
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}
	
}
