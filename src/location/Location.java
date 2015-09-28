package location;

import gui.CellSocietyGUI;

public class Location {
	
	private int myX, myY;
	protected int myWidth;
	protected int myHeight;
	
	public static Location makeLocation(int x, int y, int width, int height, CellSocietyGUI csGui){
		switch (csGui.getWrapType()) {
		case "false":
			return new Location(x,y,width,height);
		case "true":
			return new ToroidalLocation(x,y,width,height);
		default:
			return new Location(x,y,width,height);
		}
	}
	
	public Location(int x, int y, int width, int height){
		myX = x;
		myY = y;
		myWidth = width;
		myHeight = height;
	}
	
	public Location getLocation(int x, int y){
		Location loc = new Location(x,y,myWidth,myHeight);
		return loc.isValid()?loc:null;
	}
	
	public int getX(){
		return myX;
	}
	
	public int getY(){
		return myY;
	}
	
	private boolean isValid(){														
		return (getX() >= 0 && getX() < myWidth && getY() >= 0 && getY() < myHeight);
	}
	
	public int getWidth(){
		return myWidth;
	}
	
	public int getHeight(){
		return myHeight;
	}

	@Override
	public int hashCode() {
		return 1003 * myWidth + myHeight;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (myX != other.myX || myY != other.myY)
			return false;
		return true;
	}
}
