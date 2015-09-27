package cell;

import java.util.Map;
import java.util.Random;

import location.Location;
import state.FireState;
import state.State;
import gui.CellSocietyGUI;
import gui.SquareCellGUI;

public class FireCell extends AbstractCell{
	
	private static final int EMPTY_STATE = 0;
	private static final int LIVE_STATE = 1;
	private static final int BURN_STATE = 2;
	private static final int NUM_STATES = 3;
	
	private double myProbCatchFire;

	public FireCell(State s, Location l, CellSocietyGUI CSGUI) {
		super(s, NUM_STATES, l, CSGUI);
		myCellGUI = new SquareCellGUI(CSGUI, l);
		myCellGUI.updateState(s);
		addClickListener();
	}

	
	public void setProbCatchFire(double probCatchFire) {
		myProbCatchFire = probCatchFire;
	}
	 
	/**
         * Determine the next state for the cell to go to
         */
	@Override
	public void determineNextState() {
		int numBurningNeighbors = getNeighborsInState(new FireState(BURN_STATE)).size();
		Random randomGenerator = new Random();
		if (isInState(LIVE_STATE) && numBurningNeighbors >= 1) {
			if (randomGenerator.nextDouble() < myProbCatchFire) {
				myState.setNextState(BURN_STATE);
			} else {
				myState.setNextState(LIVE_STATE);
			}
		} else if(isInState(BURN_STATE)) {
			myState.setNextState(EMPTY_STATE);
		}
	}
}
