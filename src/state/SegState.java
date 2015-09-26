package state;

import javafx.scene.paint.Color;

public class SegState extends State {

	public SegState(int s) {
		super(s);
		Color[] colors = {Color.WHITE,Color.BLUE,Color.YELLOW}; //0=empty,1=group1,2=group2
		setAvailableColors(colors);
		this.setColor(s);
	}

}
