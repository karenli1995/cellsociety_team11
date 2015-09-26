package state;

import javafx.scene.paint.Color;

public class FireState extends State {
	
	public FireState(int s) {
		super(s);
		Color[] colors = {Color.YELLOW,Color.GREEN,Color.BROWN}; //Empty, Live, Burning tree
		this.setAvailableColors(colors);
		this.setColor(s);
	}

}
