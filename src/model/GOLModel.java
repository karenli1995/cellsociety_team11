
package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import location.ToroidalLocation;
import state.GOLState;
import cell.Cell;
import cell.GOLCell;
import grid.TriangleGrid;
import gui.CellSocietyGUI;

public class GOLModel extends AbstractModel {
	
	private static final double DEFAULT_PERCENT_LIVE_CELLS = 0.5;
	private static final int DEAD_STATE = 0;
	private static final int LIVE_STATE = 1;
	
	public GOLModel(CellSocietyGUI csGui) {
		super(csGui);
	}

	@Override
	public void setParameters(Map<String,String> parameters){
		initialize(parameters);
	}
	
	@Override
	protected void setBasicConfig(java.util.Map<String,String> parameters) {
		super.setBasicConfig(parameters);
		Map<Integer,String> map = new HashMap<>();
		map.put(DEAD_STATE, "Dead");
		map.put(LIVE_STATE, "Live");
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
		myGrid = new TriangleGrid(getWidth(), getHeight(), myCells);
		myGrid.setNeighbors();
	}

	@Override
	public void initialize(Map<String, String> parameters) {
		setBasicConfig(parameters);
		double percentLive = DEFAULT_PERCENT_LIVE_CELLS;
		if(myParameters.containsKey("percentLive")){
			double tmp = Double.parseDouble(myParameters.get("percentLive"));
			if(tmp>=0 && tmp<=1)
				percentLive = tmp;
		}
		int total = myWidth*myHeight;
		int numLiveCells = (int)(total*percentLive);
		int mat[][] = new int[getWidth()][getHeight()];
		
		int i = 0;
		while(i < numLiveCells){
			int t = myRandom.nextInt(total);
			int x = t % getWidth(), y = t / getWidth();
			if(mat[x][y]==DEAD_STATE){
				mat[x][y] = LIVE_STATE;
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
		Cell cell = new GOLCell(new GOLState(state), new ToroidalLocation(x,y, myWidth, getHeight()), myCSGUI);
		myCells.add(cell);
	}

	@Override
	protected Map<Integer, Double> getDataPoints() {
		int[] states = new int[2];
		myCells.forEach(cell->{states[cell.getState().getStateInt()]++;});
		Map<Integer, Double> stateMap = new HashMap<>();
		stateMap.put(DEAD_STATE, (double)states[DEAD_STATE]);
		stateMap.put(LIVE_STATE, (double)states[LIVE_STATE]);
		return stateMap;
	}
}

