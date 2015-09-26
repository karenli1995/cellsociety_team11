package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import location.Location;
import location.ToroidalLocation;
import state.PredState;
import cell.PredCell;
import grid.SquareGrid;
import grid.TriangleGrid;
import gui.CellSocietyGUI;

public class PredModel extends AbstractModel {
	
	private static final int EMPTY_STATE = 0;
	private static final int SHARK_STATE = 1;
	private static final int FISH_STATE = 2;
	
	private int myFishEnergy = 5;
	private int myMaxSharkEnergy = 5;
	private int myLivesReproduce = 5;
	
	public PredModel(CellSocietyGUI CSGUI) {
		super(CSGUI);
	}
	
	@Override
	public void setParameters(Map<String,String> map){
		// TODO Auto-generated method stub
		super.setParameters(map);
	}
	
	@Override
	protected void setBasicConfig(Map<String, String> parameters){
		super.setBasicConfig(parameters);
		if(parameters.containsKey("FISH_ENERGY")){
			int tmp = Integer.parseInt(parameters.get("FISH_ENERGY"));
			myFishEnergy = tmp>0?tmp:myFishEnergy;
		}
		if(parameters.containsKey("SHARK_ENERGY")){
			int tmp = Integer.parseInt(parameters.get("SHARK_ENERGY"));
			myMaxSharkEnergy = tmp>0?tmp:myMaxSharkEnergy;
		}	
		if(parameters.containsKey("LIVES_REPRODUCE")){
			int tmp = Integer.parseInt(parameters.get("LIVES_REPRODUCE"));
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
		myGrid = new SquareGrid(getWidth(), getHeight(), myCells);
		myGrid.setNeighbors();
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
		
		int i = 0;
		while(i < numFish){
			int t = myRandom.nextInt(total);
			int x = t % getWidth(), y = t / getWidth();
			if(mat[x][y]==EMPTY_STATE){
				mat[x][y] = FISH_STATE;
				i++;
			}
		}
		i = 0;
		while(i < numShark){
			int t = myRandom.nextInt(total);
			int x = t % getWidth(), y = t / getWidth();
			if(mat[x][y]==EMPTY_STATE){
				mat[x][y] = SHARK_STATE;
				i++;
			}
		}
		for (int x = 0; x < mat.length; x++)
			for (int y = 0; y < mat[x].length; y++)
				addCell(x,y,mat[x][y]);	
		myGrid = new TriangleGrid(getWidth(), getHeight(), myCells);
		myGrid.setNeighbors();
	}
	
	private void addCell(int x,int y,int state){
		PredCell cell = new PredCell(new PredState(state), new Location(x,y, getWidth(), getHeight()), myCSGUI);
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

