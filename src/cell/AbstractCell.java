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
	
	protected void addClickListener() {
		myCellGUI.addClickListener(e->incrementState());
	}

	public State getState() {
		return myState;
	}

	public boolean isInState(State s) {
		return getState().equals(s);
	}

	protected boolean isInState(int s) {
		return getState().getStateInt() == s;
	}

	public void setState(State s) {
		myState = s;
	}

	public Location getLocation() {
		return myLoc;
	}

	
	public void setNeighborCells(List<Cell> neighbors) {
		myNeighborCells = neighbors;
	}
	
	public List<Cell> getNeighborCells() {
		return myNeighborCells;
	}

	protected List<Cell> getNeighborsInState(State s) {
		List<Cell> neighbors = new ArrayList<>();
		myNeighborCells.forEach(cell -> {
			if (cell.isInState(s))
				neighbors.add(cell);
		});
		return neighbors;
	}
	
	protected List<Cell> getNeighborsInStateInt(int stateInt) {
		List<Cell> neighbors = new ArrayList<>();
		myNeighborCells.forEach(cell->{
			if(cell.getState().getStateInt() == stateInt)
				neighbors.add(cell);
		});
		return neighbors;
	}
	
	public abstract void determineNextState();

	public void goToNextState() {
		myState.goToNextState();
		myCellGUI.updateState(myState);
	}

	public final void remove(){
		myCellGUI.remove();
	}

	public void incrementState() {
		int nextState = (myState.getStateInt() + 1) % myNumStates;
		myState.setNextState(nextState);
		myState.goToNextState();
		myCellGUI.updateState(myState);
	}

	public void removeOutlines() {
		myCellGUI.removeOutlines();
	}

	public void addOutlines() {
		myCellGUI.addOutlines();
	}
	@Override
	public final Map<String, String> getAttributes() {
		Map<String,String> map = new HashMap<>();
		map.put("x", Integer.toString(myLoc.getX()));
		map.put("y", Integer.toString(myLoc.getY()));
		map.put("state", myState.toString());
		return map;
	}
}
