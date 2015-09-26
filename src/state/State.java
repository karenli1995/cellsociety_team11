package state;

import javafx.scene.paint.Color;

public abstract class State {
	private Color myColor;
	private Color[] myColors;
	private int myStateInt = 0;
	private int myNextStateInt = 0;

	State(int state){
		myStateInt = state;
		myNextStateInt = state;
	}
	
	public Color getColor() {
		return myColor;
	}
	
	public void setAvailableColors(Color[] colors){
		myColors = colors;
	}
	
	public void setColor(int s){
		myColor = myColors[s];
	}
	
	public int getStateInt(){
		return myStateInt;
	}
	
	public void setNextState(int nextStateInt) {
		myNextStateInt = nextStateInt;
	}
	
	public void goToNextState() {
		myStateInt = myNextStateInt;
		setColor(myStateInt);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + myStateInt;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return myStateInt == ((State)obj).myStateInt;
	}
	
	@Override
	public String toString(){
		return Integer.toString(myStateInt); 
	}
}
