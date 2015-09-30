// This entire file is part of my masterpiece.
// Karen Li

package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import location.Location;
import location.ToroidalLocation;
import state.PredState;
import cell.PredCell;
import grid.Grid;
import grid.SquareGrid;
import gui.CellSocietyGUI;

public class PredModel extends AbstractModel {
	
	private static final int EMPTY_STATE = 0;
	private static final int SHARK_STATE = 1;
	private static final int FISH_STATE = 2;
	
	private int myFishEnergy = 5;
	private int myMaxSharkEnergy = 5;
	private int myLivesReproduce = 5;
	private int myVision = 1;
	
	public PredModel(CellSocietyGUI CSGUI) {
		super(CSGUI);
	}
	
	@Override
	protected void setBasicConfig(Map<String, String> parameters){
		super.setBasicConfig(parameters);
		if(parameters.containsKey("fishEnergy")){
			int tmp = Integer.parseInt(parameters.get("fishEnergy"));
			myFishEnergy = tmp>0?tmp:myFishEnergy;
		}
		if(parameters.containsKey("maxSharkEnergy")){
			int tmp = Integer.parseInt(parameters.get("maxSharkEnergy"));
			myMaxSharkEnergy = tmp>0?tmp:myMaxSharkEnergy;
		}	
		if(parameters.containsKey("livesReproduce")){
			int tmp = Integer.parseInt(parameters.get("livesReproduce"));
			myLivesReproduce = tmp>0?tmp:myLivesReproduce;
		}
		Map<Integer, String> map = new HashMap<>();
		map.put(FISH_STATE, "Fish");
		map.put(SHARK_STATE, "Shark");
		setupGraph(map);	
	}

	@Override
	public void initialize(Map<String, String> parameters, List<Map<String, String>> cells) {
		setBasicConfig(parameters);
		cells.forEach(map -> {
			int x = Integer.parseInt(map.get("x"));
			int y = Integer.parseInt(map.get("y"));
			int state = Integer.parseInt(map.get("state"));
			addCell(x,y,state);
			
		});
		if(myCells.size()<getWidth()*getHeight())
			System.err.println("Missing Cell Info!");
		myGrid = Grid.makeGrid(getWidth(), getHeight(), myCells, myCSGUI);
		myGrid.setNeighbors(myVision);
	}
	
	

	@Override
	public void initialize(Map<String, String> parameters) {
		setBasicConfig(parameters);
		double pF = 0.5,pS = 0.5;
		if(myParameters.containsKey("percentFish")){
			double tmp = Double.parseDouble(myParameters.get("percentFish"));
			pF = (tmp>=0 && tmp<=1)?tmp:pF;
			pS = 1 - pF;		
		}
		if(myParameters.containsKey("percentShark")){
			double tmp = Double.parseDouble(myParameters.get("percentShark"));
			pS = (tmp>=0 && tmp<=pS)?tmp:pS;
		}
		
		int total = myWidth*myHeight;
		int numFish = (int)(total*pF),numShark = (int)(total*pS);
		int mat[][] = new int[getWidth()][getHeight()];
		
		randomFillMatrix(mat, EMPTY_STATE, FISH_STATE, numFish);
		randomFillMatrix(mat, EMPTY_STATE, SHARK_STATE, numShark);

		for (int x = 0; x < mat.length; x++)
			for (int y = 0; y < mat[x].length; y++)
				addCell(x,y,mat[x][y]);	
		myGrid = Grid.makeGrid(getWidth(), getHeight(), myCells, myCSGUI);
		myGrid.setNeighbors(myVision);
	}
	
	private void addCell(int x,int y,int state){
		Location loc = Location.makeLocation(x, y, getWidth(), getHeight(), myCSGUI);
		PredCell cell = new PredCell(new PredState(state), loc, myCSGUI);
		cell.setParameters(myFishEnergy, myMaxSharkEnergy, myLivesReproduce);
		myCells.add(cell);
	}

	@Override
	protected Map<Integer, Double> getDataPoints() {
		int[] states = new int[3];
		myCells.forEach(cell->{states[cell.getState().getStateInt()]++;});
		Map<Integer, Double> stateMap = new HashMap<>();
		stateMap.put(FISH_STATE, (double)states[FISH_STATE]);
		stateMap.put(SHARK_STATE, (double)states[SHARK_STATE]);
		return stateMap;
	}

}

