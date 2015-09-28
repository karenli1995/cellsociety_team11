package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import location.Location;
import location.ToroidalLocation;
import state.SugarState;
import cell.Cell;
import cell.SugarCell;
import grid.Grid;
import grid.SquareGrid;
import gui.CellSocietyGUI;

public class SugarModel extends AbstractModel{
	
	private int mySugarGrowBackRate = 1;
	private int mySugarGrowBackInterval = 1;
	private int myVision = 5;
	private int myMetabolism = 5;
	private int myAgentMaxSugar = 50;
	
	private Set<SugarCell> myAgents;
	

	SugarModel(CellSocietyGUI CSGUI) {
		super(CSGUI);
		myAgents = new HashSet<>();
	}
	
	
	@Override
	protected void setBasicConfig(Map<String, String> parameters){
		super.setBasicConfig(parameters);
		if(parameters.containsKey("sugarGrowBackRate")){
			int tmp = Integer.parseInt(parameters.get("sugarGrowBackRate"));
			mySugarGrowBackRate = tmp>0?tmp:mySugarGrowBackRate;
		}
		if(parameters.containsKey("sugarGrowBackInterval")){
			int tmp = Integer.parseInt(parameters.get("sugarGrowBackInterval"));
			mySugarGrowBackInterval = tmp>0?tmp:mySugarGrowBackInterval;
		}	
		if(parameters.containsKey("vision")){
			int tmp = Integer.parseInt(parameters.get("vision"));
			myVision = tmp>0?tmp:myVision;
		}
		if(parameters.containsKey("metabolism")){
			int tmp = Integer.parseInt(parameters.get("metabolism"));
			myMetabolism = tmp>0?tmp:myMetabolism;
		}
		Map<Integer, String> map = new HashMap<>();
		map.put(0, "Agents");
		map.put(1, "Avg Sugar per Agent");
		map.put(2, "Avg Sugar per Grid");
		setupGraph(map);	
	}
	
	@Override
	public void initialize(Map<String, String> parameters, List<Map<String, String>> cells) {
		myCells.clear();
		cells.forEach(map -> {
			int x = Integer.parseInt(map.get("x"));
			int y = Integer.parseInt(map.get("y"));
			int sugar = Integer.parseInt(map.get("state"));
			boolean isAgent = Integer.parseInt(map.get("agent"))!=0;
			int maxSugar = Integer.parseInt(map.get("max"));
			addCell(x,y,sugar,maxSugar,isAgent);
		});
		if(myCells.size()<getWidth()*getHeight())
			System.err.println("Missing Cell Info!");
		myGrid = Grid.makeGrid(getWidth(), getHeight(), myCells, myCSGUI);
		myGrid.setNeighbors(myVision);
	}

	@Override
	public void initialize(Map<String, String> parameters) {
		setBasicConfig(parameters);
		int numAgent = 0, maxSugar = 1,initialAgentSugar = 10;
		if(myParameters.containsKey("numAgent")){
			int tmp = Integer.parseInt(myParameters.get("numAgent"));
			if (tmp>=0 && tmp <= myHeight*myWidth) numAgent = tmp;
		}
		if(myParameters.containsKey("maxSugar")){
			int tmp = Integer.parseInt(myParameters.get("maxSugar"));
			if (tmp>0) maxSugar = tmp;
		}
		if(myParameters.containsKey("initialAgentSugar")){
			int tmp = Integer.parseInt(myParameters.get("initialAgentSugar"));
			if (tmp>0) initialAgentSugar = tmp;
		}
		int mat[][] = new int[getWidth()][getHeight()];
		randomFillMatrix(mat, 0, -1, numAgent);
		for(int x = 0;x<mat.length;x++){
			for(int y=0;y<mat[x].length;y++){
				if(mat[x][y]==-1)
					addCell(x,y,initialAgentSugar,myRandom.nextInt(maxSugar),true);
				else{
					int randomMaxSugar = myRandom.nextInt(maxSugar);
					addCell(x,y,randomMaxSugar,randomMaxSugar,false);
				}
			}
		}
		myGrid = Grid.makeGrid(getWidth(), getHeight(), myCells, myCSGUI);
		myGrid.setNeighbors(myVision);
	}
	
	private void addCell(int x,int y,int sugar, int max, boolean isAgent){
		Location loc = Location.makeLocation(x, y, getWidth(), getHeight(), myCSGUI);
		SugarCell cell = new SugarCell(new SugarState(sugar,isAgent),loc, myCSGUI);
		cell.setParameters(mySugarGrowBackRate, mySugarGrowBackInterval, myMetabolism, max);
		myCells.add(cell);
		if(isAgent)myAgents.add(cell);
	}
	
	@Override
	public void step() {
		Set<SugarCell> visited = new HashSet<>();
		myAgents.forEach(agent->{
			Map<Integer,List<Cell>> map = new HashMap<>();
			agent.getNeighborCells().forEach(cell->{
				if(!visited.contains(cell) && !((SugarState)cell.getState()).getAgent()){
					int sugar = cell.getState().getStateInt();
					if(!map.containsKey(sugar))
						map.put(sugar, new ArrayList<>());
					map.get(sugar).add(cell);
				}
			});
			if(!map.isEmpty()){
				List<Cell> list = map.get(Collections.max(map.keySet()));
				if(!list.isEmpty()){
					SugarCell cell = (SugarCell)list.get(myRandom.nextInt(list.size()));
					visited.add(cell);
					int state = cell.getState().getStateInt()+agent.getState().getStateInt()-myMetabolism;
					if(state>0){
						cell.getState().setNextState(new SugarState(state>myAgentMaxSugar?myAgentMaxSugar:state,true));
					}else{
						cell.getState().setNextState(0);
					}
					agent.getState().setNextState(new SugarState(0, false));
				}
			}
			
		});
		myAgents.clear();
		myCells.forEach(cell->{cell.determineNextState();});
		myCells.forEach(cell->{
			cell.goToNextState();
			if(((SugarState)cell.getState()).getAgent())
				myAgents.add((SugarCell)cell);
			});
		myStepNum++;
		updateGraph();
	}

	@Override
	protected Map<Integer, Double> getDataPoints() {
		int[] states = new int[3];
		myCells.forEach(cell->{
			if(((SugarState)cell.getState()).getAgent()){
				states[0]++;
				states[1]+=cell.getState().getStateInt();
			}
			states[2]+=cell.getState().getStateInt();
		});
		Map<Integer, Double> stateMap = new HashMap<>();
		stateMap.put(0, (double)states[0]);
		stateMap.put(1, 1.0*states[1]/states[0]);
		stateMap.put(2, 1.0*states[2]/myCells.size());
		return stateMap;
	}

}
