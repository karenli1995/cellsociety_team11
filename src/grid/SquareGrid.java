package grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import location.Location;
import cell.Cell;

public class SquareGrid extends Grid {
//	public static final int EMPTY_STATE = 0;
	
	public SquareGrid(int width, int height, Set<Cell> cells) {
		super(width, height, cells);
	}
	
	@Override
	public List<Location> getAdjacentLoc(Location loc) {
		int[] x = {-1,-1,-1, 0, 0, 1, 1, 1};
		int[] y = {-1, 0, 1,-1, 1,-1, 0, 1};
		List<Location> list = new ArrayList<>(x.length);
		for(int i=0;i<x.length;i++){
			Location neighbor = loc.getLocation(loc.getX()+x[i], loc.getY()+y[i]);
			if(neighbor!=null){
				list.add(neighbor);
			}
		}
		return list;
	}
}
