package state;

import javafx.scene.paint.Color;

public class SugarState extends AbstractState{
	
	private boolean isAgent;
	private static int maxSugar = 0; //Well, I do want this to sync across all SugarState instances

	public SugarState(int s,boolean isAgent) {
		super(s);
		this.isAgent = isAgent;
		if(!this.isAgent && myStateInt>maxSugar)
			maxSugar = myStateInt;
	}
	
	@Override
	public void setNextState(State s) {
		super.setNextState(s);
		if(s instanceof SugarState){
			isAgent = ((SugarState)s).isAgent;
		}
	}
	
	public boolean getAgent(){
		return isAgent;
	}
	
	@Override
	public double getOpacity() {
		if(isAgent)
			return 1;
		else
			return myStateInt*1.0/maxSugar;
	};

	@Override
	public Color getColor() {
		if(isAgent)
			return Color.RED;
		else
			return Color.DARKORANGE;
	}

}
