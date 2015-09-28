package cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import location.Location;
import state.State;
import gui.CellGUI;
import gui.CellSocietyGUI;

public abstract class AbstractCell implements Cell{

	protected State myState;
	protected Location myLoc;
	protected List<Cell> myNeighborCells;
	protected CellSocietyGUI myCSGUI;
	protected CellGUI myCellGUI;
	protected int myNumStates;

	AbstractCell(State s, int numStates, Location l, CellSocietyGUI CSGUI) {
		myState = s;
		myLoc = l;
		myCSGUI = CSGUI;
		myNumStates = numStates;
	}
	
	AbstractCell(State s,Location l, CellSocietyGUI CSGUI) {
		myState = s;
		myLoc = l;
		myCSGUI = CSGUI;
	}
	
	/**
	 * Associates a click listener with the current cell
	 */
	protected void addClickListener() {
		myCellGUI.addClickListener(e->incrementState());
	}

	/**
	 * Return the current state
	 */
	public State getState() {
		return myState;
	}

	/**
	 * Return true if the cell is in the specified state
	 */
	public boolean isInState(State s) {
		return getState().equals(s);
	}

	/**
	 * Returns true if the cell has the specified state int
	 * @param s
	 * @return
	 */
	protected boolean isInState(int s) {
		return getState().getStateInt() == s;
	}

	/**
	 * Set the current state to the specified state
	 */
	public void setState(State s) {
		myState = s;
	}

	/**
	 * Return the cell's location
	 */
	public Location getLocation() {
		return myLoc;
	}

	/**
	 * Set the cell's neighbors
	 */
	public void setNeighborCells(List<Cell> neighbors) {
		myNeighborCells = neighbors;
	}
	
	/**
	 * Returns the cell's neighbors
	 */
	public List<Cell> getNeighborCells() {
		return myNeighborCells;
	}

	/**
	 * Determines which neighbors are in the specified state and returns them
	 * @param s The state
	 * @return List of the neighbors in that state
	 */
	protected List<Cell> getNeighborsInState(State s) {
		List<Cell> neighbors = new ArrayList<>();
		myNeighborCells.forEach(cell -> {
			if (cell.isInState(s))
				neighbors.add(cell);
		});
		return neighbors;
	}
	
	/**
	 * Get neighbors which have the specified state int
	 * @param stateInt The state int to check
	 * @return The neighbors in that state
	 */
	protected List<Cell> getNeighborsInStateInt(int stateInt) {
		List<Cell> neighbors = new ArrayList<>();
		myNeighborCells.forEach(cell->{
			if(cell.getState().getStateInt() == stateInt)
				neighbors.add(cell);
		});
		return neighbors;
	}
	
	/**
	 * Use the cell-specific logic to determine the next state
	 */
	public abstract void determineNextState();

	/**
	 * Go to the state that had previously been determined
	 */
	public void goToNextState() {
		myState.goToNextState();
		myCellGUI.updateState(myState);
	}

	/**
	 * Remove the cell
	 */
	public final void remove(){
		myCellGUI.remove();
	}

	/**
	 * Increment the state to the next state in order
	 */
	public void incrementState() {
		int nextState = (myState.getStateInt() + 1) % myNumStates;
		myState.setNextState(nextState);
		myState.goToNextState();
		myCellGUI.updateState(myState);
	}

	/**
	 * remove the cell's outlines
	 */
	public void removeOutlines() {
		myCellGUI.removeOutlines();
	}

	/**
	 * Add outlines to the cell
	 */
	public void addOutlines() {
		myCellGUI.addOutlines();
	}
	
	@Override
	public Map<String, String> getAttributes() {
		Map<String,String> map = new HashMap<>();
		map.put("x", Integer.toString(myLoc.getX()));
		map.put("y", Integer.toString(myLoc.getY()));
		map.put("state", myState.toString());
		return map;
	}
}
