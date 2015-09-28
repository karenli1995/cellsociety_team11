package cell;

import java.util.Map;

import gui.CellGUI;
import gui.CellSocietyGUI;
import gui.SquareCellGUI;
import location.Location;
import state.State;
import state.SugarState;

public class SugarCell extends AbstractCell {

	private int mySugarGrowBackRate = 1;
	private int mySugarGrowBackInterval = 1;
	private int myMetabolism = 5;
	private int myMaxSugar = 5;
	private int myTick;

	public SugarCell(State s, Location l, CellSocietyGUI CSGUI) {
		super(s, 2, l, CSGUI);
		myCellGUI = CellGUI.makeCellGUI(CSGUI, l);
		myCellGUI.updateState(s);
	}

	public void setParameters(int sugarGrowBackRate, int sugarGrowBackInterval, int metabolism, int maxSugar) {
		mySugarGrowBackInterval = sugarGrowBackInterval;
		mySugarGrowBackRate = sugarGrowBackRate;
		myMetabolism = metabolism;
		myMaxSugar = maxSugar;
	}
	
//	private boolean moveMyStateAndAgentToCell(SugarCell chosenCell) {
//		if (chosenCell == null)
//			return false;
//		int prevSugar = chosenCell.getPatchAmntSugar();
//		SugarState prevState = (SugarState) chosenCell.getState();
//		int nextAgentAmntSugar = getAgentAmntSugar() + prevSugar - myAgentSugarMetabolism;
//		
//		if (nextAgentAmntSugar <= 0){
//			chosenCell.setAgentAmntSugar(0);
//			prevState.setAgent(0);
//		}else{
//			chosenCell.setAgentAmntSugar(nextAgentAmntSugar);
//			chosenCell.setPatchAmntSugar(0);  //agent takes sugar
//			prevState.setNextState(0);        //state sugar=0
//			prevState.setAgent(1);			  //there is an agent in this cell now

	@Override
	public void determineNextState() {
		SugarState state = (SugarState) myState;
		if (!state.getAgent()) {
			if (myTick % mySugarGrowBackInterval == 0)
				state.setNextState(state.getStateInt() + mySugarGrowBackRate);
			if (state.getStateInt() > myMaxSugar)
				state.setNextState(myMaxSugar);
			myTick++;
		} else {
			myTick = 0;
		}
	}

	@Override
	public Map<String, String> getAttributes() {
		Map<String, String> map = super.getAttributes();
		map.put("agent", ((SugarState)myState).getAgent()?"1":"0");
		map.put("max",Integer.toString(myMaxSugar));
		return map;
	}
}
