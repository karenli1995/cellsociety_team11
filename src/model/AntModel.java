package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import location.ToroidalLocation;
import state.AntState;
import cell.AntCell;
import grid.SquareGrid;
import gui.CellSocietyGUI;

public class AntModel extends AbstractModel{
	
	private double myEvaporationRate = 0.25; //default value
	private double myDiffusionRate = 0.25; //default value

	private static final int EMPTY_STATE = 0;
	private static final int NEST_STATE = 1;
	private static final int FOOD_SOURCE_STATE = 2;
	private static final int HOME_PHEROMONE_STATE = 3;
	private static final int FOOD_PHEROMONE_STATE = 4;
	
	private static final int ANT_STATE = 5;

	AntModel(CellSocietyGUI CSGUI) {
		super(CSGUI);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setParameters(Map<String,String> parameters){
		super.setParameters(parameters);
//		if(parameters.containsKey("evapRate"))
//			myEvaporationRate = Double.parseDouble(parameters.get("evapRate"));
//		if(parameters.containsKey("diffusionRate"))
//			myDiffusionRate = Double.parseDouble(parameters.get("diffusionRate"));
	}
	
	@Override
	protected void setBasicConfig(java.util.Map<String,String> parameters) {
		super.setBasicConfig(parameters);
		if(parameters.containsKey("evapRate")){
			double tmp = Double.parseDouble(parameters.get("evapRate"));
			myEvaporationRate = (tmp<=1 && tmp>=0)?tmp:myEvaporationRate;
		}
		if(parameters.containsKey("diffusionRate")){
			double tmp = Double.parseDouble(parameters.get("diffusionRate"));
			myDiffusionRate = (tmp<=1 && tmp>=0)?tmp:myDiffusionRate;
		}
		
		Map<Integer,String> map = new HashMap<>();
		map.put(EMPTY_STATE, "Empty");
		map.put(NEST_STATE, "Nest");
		map.put(FOOD_SOURCE_STATE, "Food");
		map.put(HOME_PHEROMONE_STATE, "Home Pheromone");
		map.put(FOOD_PHEROMONE_STATE, "Food Pheromone");
		map.put(ANT_STATE, "Ant");
		setupGraph(map);
	};
	
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
		int mat[][] = new int[getWidth()][getHeight()];
		int total = getWidth()*getHeight();
		
		double pA = 0.15;
		if(myParameters.containsKey("percentAnts")){
			double tmp = Double.parseDouble(myParameters.get("percentAnts"));
			pA = (tmp>=0 && tmp<=1)?tmp:pA;	
		}
		int numAnts = (int)(total*pA);
		
		double percentNestWidth = 0.3;
		int nestWidth = (int) (getWidth()*percentNestWidth);
		
		
		for(int i=0;i<mat.length;i++){
			Arrays.fill(mat[i], EMPTY_STATE);
		}
		
	
		int t = myRandom.nextInt(total);
		int x = t % getWidth(), y = t / getWidth();
		if(mat[x][y]==EMPTY_STATE){
			mat[x][y] = NEST_STATE;
		}
		
		int i=0;
		while(i < numAnts){
			for(int p=0; p<=nestWidth; p++){
				if ((x+p) < getWidth()){
					if(mat[x+p][y]==EMPTY_STATE){
						mat[x+p][y] = NEST_STATE;
					}
				} else {
					break;
				}
			}
			x = t % getWidth();
			if (y < getHeight()) y++;
			else break;
		}
		


		for (int x1 = 0; x1 < mat.length; x1++)
			for (int y1 = 0; y1 < mat[x1].length; y1++)
				addCell(x1,y1,mat[x1][y1]);	
		
		myGrid = new SquareGrid(getWidth(), getHeight(), myCells);
		myGrid.setNeighbors();
	}
	
	private void addCell(int x,int y,int state){
		AntCell cell = new AntCell(new AntState(state), new ToroidalLocation(x,y, myWidth, getHeight()), myCSGUI);
		cell.setParameters(myEvaporationRate, myDiffusionRate);
		myCells.add(cell);
	}

	@Override
	protected Map<Integer, Double> getDataPoints() {
		int[] states = new int[6];
		myCells.forEach(cell->{states[cell.getState().getStateInt()]++;});
		Map<Integer, Double> stateMap = new HashMap<>();
		stateMap.put(EMPTY_STATE, (double)states[EMPTY_STATE]);
		stateMap.put(NEST_STATE, (double)states[NEST_STATE]);
		stateMap.put(FOOD_SOURCE_STATE, (double)states[FOOD_SOURCE_STATE]);
		stateMap.put(HOME_PHEROMONE_STATE, (double)states[HOME_PHEROMONE_STATE]);
		stateMap.put(FOOD_PHEROMONE_STATE, (double)states[FOOD_PHEROMONE_STATE]);
		stateMap.put(ANT_STATE, (double)states[ANT_STATE]);

		return stateMap;
	}

}
