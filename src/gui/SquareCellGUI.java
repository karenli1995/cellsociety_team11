package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import location.Location;

public class SquareCellGUI extends CellGUI {

	private double myCellWidth;
	private double myCellHeight;
	private double myXPos;
	private double myYPos;

	public SquareCellGUI(CellSocietyGUI CSGUI, Location loc) {
		super(CSGUI);
		myCellWidth = CSGUI.getGridWidth() / loc.getNumCols();
		myCellHeight = CSGUI.getGridHeight() / loc.getWidth();
		myShape = createRectangle(loc);
		addShapeToScreen();
	}
	
	private Rectangle createRectangle(Location loc) {
		Rectangle rect = new Rectangle();
		rect.setWidth(myCellWidth);
		rect.setHeight(myCellHeight);
		myXPos = myGridXPos + loc.getX() * myCellWidth;
		myYPos = myGridYPos + loc.getY() * myCellHeight;
		rect.setX(myXPos);
		rect.setY(myYPos);
		return rect;
	}

	@Override
	public void drawCircle(Color color) {
		myCircle = new Circle();
		myCircle.setCenterX(myXPos + myCellWidth / 2);
		myCircle.setCenterY(myYPos + myCellHeight / 2);
		double radius = (myCellWidth > myCellHeight) ? (myCellHeight / 2) : (myCellWidth / 2);
		myCircle.setRadius(radius);
		myCircle.setFill(color);
		myCSGUI.addToScreen(myCircle);
	}
}
