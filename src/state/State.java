package state;

import javafx.scene.paint.Color;

public interface State {
	
	/**
	 * Get the color representing current state
	 * @return color
	 */
	public Color getColor();
	
	/**
	 * Get opacity of current state
	 * @return default is 1
	 */
	public double getOpacity();
	
	/**
	 * Get the int representing the current state
	 * @return an int value
	 */
	public int getStateInt();
	
	/**
	 * Set the integer value of the next state
	 * @param nextStateInt
	 */
	public void setNextState(int nextStateInt);
	
	/**
	 * Set the next state
	 * @param a state
	 */
	public void setNextState(State s);
	
	/**
	 * Let the current state equals next state
	 */
	public void goToNextState();

}
