package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import location.Location;

public class HexagonCellGUI extends CellGUI{
	private double myCellWidth;
	private double myCellHeight;
	private double myCenterX;
	private double myCenterY;
	private double myRadius;

	public HexagonCellGUI(CellSocietyGUI CSGUI, Location loc) {
		super(CSGUI);
		myCellWidth = CSGUI.getGridWidth() / (loc.getNumCols() + 1) * 2;
		myCellHeight = CSGUI.getGridHeight() / loc.getWidth();
		myShape = createHexagon(loc);
		addShapeToScreen();
	}
	
	private Polygon createHexagon(Location loc) {
		Polygon hex = new Polygon();
		double point1X = myGridXPos + ((loc.getX() + loc.getY() % 2) / 2) * myCellWidth + ((loc.getY() + 1) %2) * myCellWidth / 2;
		double point1Y = myGridYPos + loc.getY() * myCellHeight;
		double point2X = point1X + myCellWidth / 2;
		double point2Y = point1Y + myCellHeight;
		
		boolean pointingUp = (loc.getX() + loc.getY()) % 2 == 0;
		
		double sideLength = Math.sqrt(Math.pow(myCellHeight, 2) + Math.pow(myCellWidth/2, 2));
		
		myCenterX = pointingUp ? point1X : point2X;
		myRadius = myCellHeight * myCellWidth / (2 * sideLength + myCellWidth);		// #GeometryForTheWin
		myCenterY = pointingUp ? (point1Y + myCellHeight - myRadius) : (point2Y - myCellHeight + myRadius);

		double point3X = pointingUp ? (point2X - myCellWidth): (point1X + myCellWidth);
		double point3Y = pointingUp ? point2Y: point1Y;
		
		tri.getPoints().addAll(new Double[]{
			    point1X, point1Y,
			    point2X, point2Y,
			    point3X, point3Y });
		
		
		return tri;
		
		
//		Polygon tri = new Polygon();
//		double point1X = myGridXPos + ((loc.getX() + loc.getY() % 2) / 2) * myCellWidth + ((loc.getY() + 1) %2) * myCellWidth / 2;
//		double point1Y = myGridYPos + loc.getY() * myCellHeight;
//		double point2X = point1X + myCellWidth / 2;
//		double point2Y = point1Y + myCellHeight;
//		
//		boolean pointingUp = (loc.getX() + loc.getY()) % 2 == 0;
//		
//		double sideLength = Math.sqrt(Math.pow(myCellHeight, 2) + Math.pow(myCellWidth/2, 2));
//		
//		myCenterX = pointingUp ? point1X : point2X;
//		myRadius = myCellHeight * myCellWidth / (2 * sideLength + myCellWidth);		// #GeometryForTheWin
//		myCenterY = pointingUp ? (point1Y + myCellHeight - myRadius) : (point2Y - myCellHeight + myRadius);
//
//		double point3X = pointingUp ? (point2X - myCellWidth): (point1X + myCellWidth);
//		double point3Y = pointingUp ? point2Y: point1Y;
//		
//		tri.getPoints().addAll(new Double[]{
//			    point1X, point1Y,
//			    point2X, point2Y,
//			    point3X, point3Y });
//		
//		
//		return tri;
	}

	@Override
	public void drawCircle(Color red) {
		// TODO Auto-generated method stub
		
	}

}
