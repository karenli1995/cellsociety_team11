package state;

import javafx.scene.paint.Color;

public class SegState extends AbstractState {

	private static final Color[] COLORS = {Color.WHITE,Color.BLUE,Color.YELLOW}; //0=empty,1=group1,2=group2
			
	public SegState(int s) {
		super(s);
	}

	@Override
	public Color getColor() {
		return COLORS[myStateInt];
	}
	
	

}
