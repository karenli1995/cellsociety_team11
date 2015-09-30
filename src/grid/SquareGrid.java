// This entire file is part of my masterpiece.
// Cameron Givler

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
	
	/**
	 * Returns a List of adjacent locations to the specified location
	 */
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

	@Override
	public List<Location> getAdjacentLoc(Location loc, int radius) {
		List<Location> list = new ArrayList<>(radius*radius);
		for(int x = -radius;x<=radius;x++){
			for(int y = -radius;y<=radius;y++){
				if(x==0 && y==0) continue;
				if(x*x+y*y>radius*radius) continue;
				Location neighbor = loc.getLocation(loc.getX()+x, loc.getY()+y);
				if(neighbor!=null){
					list.add(neighbor);
				}
			}
		}
		return list;
	}
}
