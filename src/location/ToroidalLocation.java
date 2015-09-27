package location;

public class ToroidalLocation extends Location {

	public ToroidalLocation(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	/**
	 * Get the location of the cell.  If the location is out of the bounds
	 * of the grid, return a valid in corresponding "wrapped" location
	 */
	@Override
	public Location getLocation(int x, int y){
		ToroidalLocation loc = new ToroidalLocation(Math.floorMod(x, myWidth), 
				Math.floorMod(y, myHeight), myWidth, myHeight);
		return loc;
	}

}
