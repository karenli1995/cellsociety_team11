package model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import location.Location;
import location.ToroidalLocation;
import state.FireState;
import cell.FireCell;
import grid.CardinalGrid;
import grid.SquareGrid;
import grid.TriangleGrid;
import gui.CellSocietyGUI;

public class FireModel extends AbstractModel {
	
	private static final int EMPTY_STATE = 0;
	private static final int TREE_STATE = 1;
	private static final int FIRE_STATE = 2;
	
	private double myProbCatchFire = 0.5; //default value
	private static String STATE_NAMES[] = {"Empty", "Tree", "Burning"};

	public FireModel(CellSocietyGUI csGui) {
		super(csGui);
	}
	
	@Override
	public void setParameters(Map<String,String> map){
		// TODO Auto-generated method stub
		super.setParameters(map);
		if(map.containsKey("probCatchFire"))
			myProbCatchFire = Double.parseDouble(map.get("probCatchFire"));
	}

	@Override
	protected void setBasicConfig(Map<String, String> parameters){
		super.setBasicConfig(parameters);
		if(parameters.containsKey("probCatchFire")){
			double tmp = Double.parseDouble(parameters.get("probCatchFire"));
			myProbCatchFire = (tmp<=1 && tmp>=0)?tmp:myProbCatchFire;
		}
			
		
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
		myGrid = new CardinalGrid(getWidth(), getHeight(), myCells);
		myGrid.setNeighbors();
		setupGraph(STATE_NAMES);
	}

	@Override
	public void initialize(Map<String, String> parameters) throws Exception{
		setBasicConfig(parameters);
		int mat[][] = new int[getWidth()][getHeight()];
		int total = getWidth()*getHeight();
		int numBurning = Integer.parseInt(parameters.get("numBurning"));
		int numEmpty = Integer.parseInt(parameters.get("numEmpty"));
		for(int i=0;i<mat.length;i++){
			Arrays.fill(mat[i], TREE_STATE);
		}
		for(int i = 0;i<getWidth();i++){
			mat[i][0] = EMPTY_STATE;
			mat[i][getHeight()-1] = EMPTY_STATE;
		}
		for(int i = 0;i<getHeight();i++){
			mat[0][i] = EMPTY_STATE;
			mat[getWidth()-1][i] = EMPTY_STATE;
		}
		int i = 0;
		while(i < numBurning){
			int t = myRandom.nextInt(total);
			int x = t % getWidth(), y = t / getWidth();
			if(mat[x][y]==TREE_STATE){
				mat[x][y] = FIRE_STATE;
				i++;
			}
		}
		i = 0;
		while(i < numEmpty){
			int t = myRandom.nextInt(total);
			int x = t % getWidth(), y = t / getWidth();
			if(mat[x][y]==TREE_STATE){
				mat[x][y] = EMPTY_STATE;
				i++;
			}
		}
		for (int x = 0; x < mat.length; x++)
			for (int y = 0; y < mat[x].length; y++)
				addCell(x,y,mat[x][y]);	
		myGrid = new CardinalGrid(getWidth(), getHeight(), myCells);
		myGrid.setNeighbors();
		setupGraph(STATE_NAMES);
	}
	
	private void addCell(int x,int y,int state){
		FireCell cell = new FireCell(new FireState(state), new Location(x,y, getWidth(), getHeight()), myCSGUI);
		cell.setProbCatchFire(myProbCatchFire);
		myCells.add(cell);
	}

	
}
