package state;

import javafx.scene.paint.Color;

public class PredState extends State{

	public PredState(int s) {
		super(s);
		Color[] colors = {Color.WHITE,Color.RED,Color.AQUAMARINE}; //0=empty,1=shark,2=fish
		setAvailableColors(colors);
		this.setColor(s);
	}

}
