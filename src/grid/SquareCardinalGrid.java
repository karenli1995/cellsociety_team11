package grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import location.Location;
import cell.Cell;

public class SquareCardinalGrid extends Grid{

	public SquareCardinalGrid(int width, int height, Set<Cell> cells) {
		super(width, height, cells);
	}
	
	@Override
	public List<Location> getAdjacentLoc(Location loc) {
		int[] x = {-1, 0, 0, 1};
		int[] y = {0, -1, 1, 0};
		List<Location> list = new ArrayList<>(x.length);
		for(int i=0;i<x.length;i++){
			addLocationToList(loc.getX()+x[i], loc.getY()+y[i], loc, list);
		}
		return list;
	}
	

	@Override
	public List<Location> getAdjacentLoc(Location loc, int radius) {
		List<Location> list = new ArrayList<>(radius * 4);
		for (int i = -radius; i <= radius; i++) {
			if (i == 0)
				continue;
			addLocationToList(loc.getX()-i, loc.getY(), loc, list);
			addLocationToList(loc.getX()+i, loc.getY(), loc, list);
			addLocationToList(loc.getX(), loc.getY()-i, loc, list);
			addLocationToList(loc.getX(), loc.getY()+i, loc, list);
		}
		return list;
	}
	
	private void addLocationToList(int x, int y, Location loc, List<Location> list){
		Location neighbor = loc.getLocation(x, y);
		if (neighbor != null) {
			list.add(neighbor);
		}
	}

}
