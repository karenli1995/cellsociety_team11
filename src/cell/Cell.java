package cell;

import java.util.List;
import java.util.Map;

import location.Location;
import state.State;

public interface Cell {

	/**
	 * returns the State of a Cell
	 */
	public State getState();

	/**
	 * returns true if in State s
	 */
	public boolean isInState(State s);
	
	/**
	 * sets State of Cell
	 */
	public void setState(State s);

	/**
	 * returns the Location of the Cell
	 */
	public Location getLocation();

	/**
	 * set the Neighboring Cells
	 * 
	 */
	public void setNeighborCells(List<Cell> neighbors);
	
	public List<Cell> getNeighborCells();
	
	/**
	 * determines what the next State of a Cell should be based on simulation rules
	 * 
	 */
	public abstract void determineNextState();

	/**
	 * sets State's next state
	 */
	public void goToNextState();

	/**
	 * 
	 */
	public void remove();

	public void incrementState();

	public void removeOutlines();

	public void addOutlines();
	
	public Map<String,String> getAttributes();
}
