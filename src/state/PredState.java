package state;

import javafx.scene.paint.Color;

public class PredState extends AbstractState{

	private static final Color[] COLORS = {Color.WHITE,Color.RED,Color.AQUAMARINE}; //0=empty,1=shark,2=fish
	
	public PredState(int s) {
		super(s);
	}
	@Override
	public Color getColor() {
		return COLORS[myStateInt];
	}

}
