package state;

import javafx.scene.paint.Color;

public class FireState extends AbstractState {
	
	private static final Color[] COLORS = {Color.YELLOW,Color.GREEN,Color.BROWN}; //Empty, Live, Burning tree
	
	public FireState(int s) {
		super(s);
	}
	
	@Override
	public Color getColor() {
		return COLORS[myStateInt];
	}
	

}
