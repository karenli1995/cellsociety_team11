package location;

public class ToroidalLocation extends Location {

	/**
	 * If the location is out of the bounds of the grid, return a valid in
	 * corresponding "wrapped" location
	 */
	public ToroidalLocation(int x, int y, int width, int height) {
		super(Math.floorMod(x, width), Math.floorMod(y, height), width, height);
	}

	@Override
	public Location getLocation(int x, int y) {
		return new ToroidalLocation(x, y, myWidth, myHeight);
	};

}
