package grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import location.Location;
import cell.Cell;

public abstract class Grid {
	
	protected int myWidth;		// This should be in squareGrid.  Grid doesn't necessarily have rows and columns
	protected int myHeight;
	protected Map<Location, Cell> myCells;
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @param cells
	 */
	public Grid(int width, int height, Set<Cell> cells){
		myWidth = width;
		myHeight = height;
		myCells = new HashMap<>();
		cells.forEach(cell->{myCells.put(cell.getLocation(), cell);});
	}
	
	public int getHeight() {
		return myHeight;
	}

	public int getWidth() {
		return myWidth;
	}
	
	public Cell getCell(Location loc){
		return myCells.get(loc);
	}

	public void setNeighbors() {
		myCells.forEach((loc,cell)->{
			cell.setNeighborCells(getAdjacentCells(cell));
		});
	}

	public abstract List<Location> getAdjacentLoc(Location loc);

	public List<Cell> getAdjacentCells(Cell cell) {
		List<Cell> neighbors = new ArrayList<>();
		getAdjacentLoc(cell.getLocation()).forEach(loc->{
			neighbors.add(getCell(loc));
		});
		return neighbors;
	}
	
	public void step() {
		determineNextStates();
		goToNextStates();
	}
	
	private void determineNextStates() {
		for (Cell c : myCells.values()) {
			c.determineNextState();
		}
	}
	
	private void goToNextStates() {
		for (Cell c : myCells.values()) {
			c.goToNextState();
		}
	}
	
	public void clear() {
		for (Cell c : myCells.values()) {
			c.remove();
		}
	}
	
	public void removeOutlines() {
		for (Cell c : myCells.values()) {
			c.removeOutlines();
		}
	}
	
	public void addOutlines() {
		for (Cell c : myCells.values()) {
			c.addOutlines();
		}
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int x=0;x<getWidth();x++){
			for(int y=0;y<getHeight();y++){
				Cell cell = getCell(new Location(x,y,getWidth(),getHeight()));
				sb.append(cell.getState());
			}
			sb.append('\n');
		}
		return sb.toString();
	}
}
