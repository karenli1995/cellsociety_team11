package state;

import javafx.scene.paint.Color;

public class AntState extends State{
	private static final int EMPTY_STATE = 0;
	private static final int NEST_STATE = 1;
	private static final int FOOD_SOURCE_STATE = 2;
	private static final int HOME_PHEROMONE_STATE = 3;
	private static final int FOOD_PHEROMONE_STATE = 4;
	
	private static final int ANT_STATE = 5;
	
	private boolean containsAnt;
	

	public AntState(int state) {  
		super(state);
	
		Color[] colors = {Color.WHITE, Color.PURPLE, Color.YELLOW, Color.RED, Color.GREEN, Color.BLACK}; //0=empty; 1=nest; 2=food source; 3=home pheromone; 4=food pheromone; 5=ant
		setAvailableColors(colors);
		this.setColor(state);
		
		
		if(state == NEST_STATE){
			setContainsAnt(true);
		}else{
			setContainsAnt(false);
		}
	}

	
	public void setContainsAnt(boolean b){
		containsAnt = b;
	}
	
	public boolean getContainsAnt(){
		return containsAnt;
	}
}
