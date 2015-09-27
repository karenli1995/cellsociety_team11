package cell;

import java.util.List;
import java.util.Map;

import gui.CellSocietyGUI;
import location.Location;
import state.State;
import state.SugarState;

public class SugarCell extends AbstractCell{
//	private static final int PATCH_STATE_0 = 0;
//	private static final int PATCH_STATE_1 = 1;
//	private static final int PATCH_STATE_2 = 2;
//	private static final int PATCH_STATE_3 = 3;
//	private static final int PATCH_STATE_4 = 4;
//	private static final int NUM_STATES = 5;
	
	private static final int NO_AGENT_STATE = 0;
	private static final int AGENT_STATE = 1;
	
	private static final int SUGAR_GROWBACK_RATE = 1;
	//private static final int SUGAR_GROWBACK_INTERVAL = 1;
	
	private SugarState mySugarState;
	private int myAgentState;
	
	private int myMaxSugarCapacity;
	
	private int myPatchAmntSugar;
	private int myAgentAmntSugar = 5;  //default value
	private int myAgentSugarMetabolism = 1; //default value
	private int myAgentVision = 1 ; //default value
	
	

	public SugarCell(State s, Location l, CellSocietyGUI CSGUI) {
		super(s, l, CSGUI);
		//setPatchAmntSugar(s.getStateInt());
		
		mySugarState = (SugarState) s;
		myAgentState = mySugarState.getAgent();
	}
	
	public void setParameters(int agentVision, int agentSugar, int agentSugarMetabolism, int maxSugarCapacity){
		myAgentVision = agentVision;
		if (myAgentState == AGENT_STATE){
			setAgentAmntSugar(agentSugar);
		}
		myAgentSugarMetabolism = agentSugarMetabolism;
		myMaxSugarCapacity = maxSugarCapacity;
		setPatchAmntSugar(myMaxSugarCapacity);
	}
	
	private int getPatchAmntSugar(){
		return myPatchAmntSugar;
	}
	
	private void setPatchAmntSugar(int sugar){
		myPatchAmntSugar = sugar;
	}
	
	private int getAgentAmntSugar(){
		return myAgentAmntSugar;
	}
	
	private void setAgentAmntSugar(int sugar){
		myAgentAmntSugar = sugar;
	}
	
	@Override
	public void determineNextState() {
		if (myAgentState == AGENT_STATE) {
			for(int i=myMaxSugarCapacity; i>=0; i--){
				List<Cell> neighborsInState = getNeighborsInStateInt(i);
				if (!neighborsInState.isEmpty()){
					SugarCell chosenCell = (SugarCell) findClosestCell(neighborsInState);
					moveMyStateAndAgentToCell(chosenCell);
					break;
				}else{
					continue;
				}

			}	
		}
		
		if (myPatchAmntSugar != myMaxSugarCapacity){
			setPatchAmntSugar(getPatchAmntSugar() + SUGAR_GROWBACK_RATE);
		}
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		myCellGUI.remove();
	}
	
	private boolean moveMyStateAndAgentToCell(SugarCell chosenCell) {
		if (chosenCell == null)
			return false;
		int prevSugar = chosenCell.getPatchAmntSugar();
		SugarState prevState = (SugarState) chosenCell.getState();
		int nextAgentAmntSugar = getAgentAmntSugar() + prevSugar - myAgentSugarMetabolism;
		
		if (nextAgentAmntSugar <= 0){
			chosenCell.setAgentAmntSugar(0);
			prevState.setAgent(0);
		}else{
			chosenCell.setAgentAmntSugar(nextAgentAmntSugar);
			chosenCell.setPatchAmntSugar(0);  //agent takes sugar
			prevState.setNextState(0);        //state sugar=0
			prevState.setAgent(1);			  //there is an agent in this cell now
		}
		
		this.mySugarState.setNextState(mySugarState.getStateInt());
		this.mySugarState.setAgent(0);
		this.setAgentAmntSugar(0);
		
		return true;
	}

	
	
	private Cell findClosestCell(List<Cell> neighborCellList){
		int currX = this.getLocation().getX();
		int currY = this.getLocation().getY();
		
		double minDistance = Integer.MAX_VALUE;
		Cell minDistCell = this;
		
		for(int i=0; i<neighborCellList.size(); i++){
			int otherX = neighborCellList.get(i).getLocation().getX();
			int otherY = neighborCellList.get(i).getLocation().getY();
			
			double distance = Math.sqrt(Math.pow(currX-otherX, 2) + Math.pow(currY-otherY, 2));
			if (distance < minDistance){
				minDistance = distance;
				minDistCell = neighborCellList.get(i);
			}
		}
		
		return minDistCell;
	}

}
