package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import cell.Cell;
import grid.Grid;
import gui.CellSocietyGUI;

public abstract class AbstractModel implements Model {
	
	protected CellSocietyGUI myCSGUI;
	protected int myWidth;
	protected int myHeight;
	protected Grid myGrid;
	protected Set<Cell> myCells;
	protected Map<String,String> myParameters;
	protected Random myRandom;
	protected String myAuthor = "CPS308_Team11";
	protected int myStepNum = 0;
	
	AbstractModel(CellSocietyGUI CSGUI){
		myCSGUI = CSGUI;
		myRandom = new Random();
		myCells = new HashSet<>();
	}
	
	public void setParameters(Map<String,String> parameters){
		myParameters = parameters;
	}
	
	public Map<String,String> getParameters(){
		return Collections.unmodifiableMap(myParameters);
	}
	
	protected void setDimensions(int width, int height){
		myWidth = width;
		myHeight = height;
	}

	public int getHeight() {
		return myHeight;
	}

	public int getWidth() {
		return myWidth;
	}
	
	public void step(){
		++myStepNum;
		myGrid.step();
		updateGraph();
	}
	
	public void clear() {
		myStepNum = 0;
		if(myGrid!=null)
			myGrid.clear();
		if(myCells!=null)
			myCells.clear();
		myCSGUI.resetGraph();
	}
	
	protected void randomFillMatrix(int[][] mat,int initialState,int finalState, int totalToFill){
		int i = 0;
		while(i < totalToFill){
			int t = myRandom.nextInt(myWidth*myHeight);
			int x = t % myWidth, y = t / myWidth;
			if(mat[x][y]==initialState){
				mat[x][y] = finalState;
				i++;
			}
		}
	}
	
	protected void setupGraph(Map<Integer,String> names) {
		myCSGUI.resetGraph();
		names.forEach((k,v)->{myCSGUI.addSeries(k, v);});
	}
	
	protected void updateGraph() {
		getDataPoints().forEach((series,value)->{myCSGUI.addDataPoint(myStepNum,value,series);});
	}
	/**
	 * 
	 * @return Map from Series to Y value
	 */
	protected abstract Map<Integer, Double> getDataPoints();
	
	protected void setBasicConfig(Map<String, String> parameters){
		clear();
		myParameters = parameters;
		int width = Integer.parseInt(myParameters.get("width"));
		int height = Integer.parseInt(myParameters.get("height"));
		setDimensions(width, height);
		myAuthor = myParameters.containsKey("author")?myParameters.get("author"):myAuthor;
	}
	
	public Set<Cell> getCells(){
		return Collections.unmodifiableSet(myCells);
	}

	@Override
	public final String toString() {
		String name = "model";
		if(myParameters!=null && myParameters.containsKey("name")){
			name = myParameters.get("name");
		}
		return name+"_"+myWidth+"_"+myHeight+"_"+myAuthor;
	}
}
