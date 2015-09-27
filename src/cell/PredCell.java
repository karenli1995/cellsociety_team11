package cell;

import java.util.List;
import java.util.Map;

import location.Location;
import state.PredState;
import state.State;
import gui.CellSocietyGUI;
import gui.SquareCellGUI;
import javafx.scene.input.MouseEvent;

public class PredCell extends AbstractCell {
	private static int EMPTY_STATE = 0;
	private static int SHARK_STATE = 1;
	private static int FISH_STATE = 2;
	private static final int NUM_STATES = 3;
	
	private int myFishEnergy = 2;
	private int myMaxSharkEnergy = 5;
	private int myLivesReproduce = 5;

	private int myNumChronon = 0;
	private int mySharkEnergy = 0;
	private boolean visited;

	public PredCell(State s, Location l, CellSocietyGUI CSGUI) {
		super(s, NUM_STATES, l, CSGUI);
		myCellGUI = new SquareCellGUI(CSGUI, l);
		myCellGUI.updateState(s);
		addClickListener();
		if (isInState(SHARK_STATE)) {
			setSharkEnergy(myMaxSharkEnergy);
		}
	}
	
	public void setParameters(int fishEnergy, int sharkEnergy, int livesReproduce){
		myFishEnergy = fishEnergy;
		myMaxSharkEnergy = sharkEnergy;
		myLivesReproduce = livesReproduce;
		if (isInState(SHARK_STATE)) {
			setSharkEnergy(myMaxSharkEnergy);
		}
	}

	public void setNumChronon(int chronon) {
		myNumChronon = chronon;
	}

	public void setSharkEnergy(int x) {
		mySharkEnergy = x;
	}

	public boolean getVisited() { // determines if a cell has been visited as a neighbor
		return visited;
	}

	public void setVisited(boolean b) {
		visited = b;
	}

	private PredCell getRandomNeighborInState(int state) {
		State stateVar = new PredState(state);
		List<Cell> neighbors = getNeighborsInState(stateVar);
		if (neighbors.size() == 0)
			return null;
		int randNeighbor = (int) (Math.random() * neighbors.size());
		PredCell potentialCell = (PredCell) neighbors.get(randNeighbor);

		for (int i = 0; i < neighbors.size(); i++) {
			if (!potentialCell.getVisited()) {
				return potentialCell;
			}
			randNeighbor = (randNeighbor == neighbors.size() - 1) ? 0 : randNeighbor + 1;
			potentialCell = (PredCell) neighbors.get(randNeighbor);
		}
		return null;
	}

	private boolean moveMyStateToCell(PredCell chosenCell) {
		if (chosenCell == null)
			return false;
		chosenCell.getState().setNextState(myState.getStateInt()); // This can
																	// be
																	// improved
		chosenCell.setNumChronon(myNumChronon);
		chosenCell.setVisited(true);
		if(chosenCell.isInState(FISH_STATE))
			chosenCell.setSharkEnergy(mySharkEnergy + myFishEnergy);
		if (myNumChronon < myLivesReproduce) {
			myState.setNextState(EMPTY_STATE);
			setSharkEnergy(0);
		} else {
			myState.setNextState(myState.getStateInt());
			if (myState.getStateInt() == SHARK_STATE)
				setSharkEnergy(myMaxSharkEnergy);
			else
				setSharkEnergy(0);
		}
		setNumChronon(0);// reproductive state reset
		return true;
	}

	/**
         * Determine the next state for the cell to go to
         */
	@Override
	public void determineNextState() {
		if (visited || isInState(EMPTY_STATE)) {
			return;
		}
		++myNumChronon;
		PredCell chosenCell;

		if (myState.getStateInt() == SHARK_STATE) {
			--mySharkEnergy;
			if (mySharkEnergy <= 0) {
				myState.setNextState(EMPTY_STATE);
				setNumChronon(0);
				setSharkEnergy(0);
				return;
			}
			chosenCell = getRandomNeighborInState(FISH_STATE);
			if (moveMyStateToCell(chosenCell)) {
				return;
			}
		}

		chosenCell = getRandomNeighborInState(EMPTY_STATE);
		moveMyStateToCell(chosenCell);
	}

	@Override
	public void goToNextState() {
		super.goToNextState();
		visited = false;
	}
	
	@Override
	public void incrementState() {
		super.incrementState();
		if(myState.getStateInt() == EMPTY_STATE) {
			setSharkEnergy(0);
		} else if(myState.getStateInt() == SHARK_STATE) {
			setSharkEnergy(myMaxSharkEnergy);
		} else if(myState.getStateInt() == FISH_STATE) {
			setSharkEnergy(0);
		}
		setNumChronon(0);// reproductive state reset
	}
}