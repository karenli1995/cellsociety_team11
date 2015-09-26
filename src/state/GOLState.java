package state;

import javafx.scene.paint.Color;

public class GOLState extends State {
	
	public GOLState(int s) {
		super(s);
		Color[] colors = {Color.WHITE,Color.BLACK}; //0=dead,1=live
		setAvailableColors(colors);
		setColor(s);
	}
}