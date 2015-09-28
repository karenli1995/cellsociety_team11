package grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import location.Location;
import cell.Cell;

public class CardinalGrid extends Grid{
	private int[] myX = {-1, 0, 0, 1};
	private int[] myY = {0, -1, 1, 0};

	public CardinalGrid(int width, int height, Set<Cell> cells) {
		super(width, height, cells);
	}
	
	public void setNeighbors() {
		myCells.forEach((loc,cell)->{
			cell.setNeighborCells(getAdjacentCells(cell));
		});
	}
	
	@Override
	public List<Location> getAdjacentLoc(Location loc) {
		List<Location> list = new ArrayList<>(myX.length);
		for(int i=0;i<myX.length;i++){
			Location neighbor = loc.getLocation(loc.getX()+myX[i], loc.getY()+myY[i]);
			if(neighbor!=null){
				list.add(neighbor);
			}
		}
		return list;
	}

	@Override
	public List<Cell> getAdjacentCells(Cell cell) {
		List<Cell> neighbors = new ArrayList<>(8);
		getAdjacentLoc(cell.getLocation()).forEach(loc->{
			neighbors.add(getCell(loc));
		});
		return neighbors;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int r=0;r<getHeight();r++){
			for(int c=0;c<getWidth();c++){
				//Cell cell = getCell(new Location(c,r,))
			}
		}
		return "";
	}

	@Override
	public List<Location> getAdjacentLoc(Location loc, int radius) {
		// TODO Auto-generated method stub
		return null;
	}

}
