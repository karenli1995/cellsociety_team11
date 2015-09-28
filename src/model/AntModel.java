package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import location.Location;
import state.AntState;
import cell.AntCell;
import grid.Grid;
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
		myGrid = Grid.makeGrid(getWidth(), getHeight(), myCells, myCSGUI);
		myGrid.setNeighbors();
	}

	@Override
	public void initialize(Map<String, String> parameters) {
		setBasicConfig(parameters);
		int mat[][] = new int[getWidth()][getHeight()];
		int total = getWidth()*getHeight();
		
		double pA = 0.1;
		if(myParameters.containsKey("percentAnts")){
			double tmp = Double.parseDouble(myParameters.get("percentAnts"));
			pA = (tmp>=0 && tmp<=1)?tmp:pA;	
		}
		int numAnts = (int)(total*pA);
		
		double percentNestWidth = 0.3;
		int nestWidth = (int) (getWidth()*percentNestWidth);
	
		int t = myRandom.nextInt(total);
		int x = t % getWidth(), y = t / getWidth();
		if(mat[x][y]==EMPTY_STATE){
			mat[x][y] = NEST_STATE;
		}
		int i=0;
		while(i < numAnts){
			
			for(int p=0; p<=nestWidth; p++){
				if ((x+p) < getWidth() && y<getHeight()){
					if(mat[x+p][y]==EMPTY_STATE){
						mat[x+p][y] = NEST_STATE;
						i++;
					}
				} else {
					break;
				}
			}
			x = t % getWidth();
			if (y < getHeight()-1){y++;}
			else{ break;}
		}
		
		
		double percentFood = 0.1;
		int numFood = (int) (total*percentFood);
		
		int j = 0;
		while(j < numFood){
			int t1 = myRandom.nextInt(total);
			int x1 = t1 % getWidth(), y1 = t1 / getWidth();
			if(mat[x1][y1]==EMPTY_STATE){
				mat[x1][y1] = FOOD_SOURCE_STATE;
				j++;
			}
		}


		for (int x1 = 0; x1 < mat.length; x1++)
			for (int y1 = 0; y1 < mat[x1].length; y1++)
				addCell(x1,y1,mat[x1][y1]);	
		myGrid = Grid.makeGrid(getWidth(), getHeight(), myCells, myCSGUI);
		myGrid.setNeighbors();
	}
	
	private void addCell(int x,int y,int state){
		Location loc = Location.makeLocation(x, y, getWidth(), getHeight(), myCSGUI);
		AntCell cell = new AntCell(new AntState(state), loc, myCSGUI);
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