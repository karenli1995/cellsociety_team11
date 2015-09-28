package state;

import javafx.scene.paint.Color;

public class GOLState extends AbstractState {
	
	private static final Color[] COLORS = {Color.WHITE,Color.BLACK}; //0=dead,1=live
	
	public GOLState(int s) {
		super(s);
	}

	@Override
	public Color getColor() {
		return COLORS[myStateInt];
	}
}