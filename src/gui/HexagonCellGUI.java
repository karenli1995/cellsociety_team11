package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import location.Location;

public class HexagonCellGUI extends CellGUI {

	public HexagonCellGUI(CellSocietyGUI CSGUI, Location loc) {
		super(CSGUI);
		myShape = createHexagon(loc);
		addShapeToScreen();
	}

	private Polygon createHexagon(Location loc) {

		double width = myCSGUI.getGridWidth() / (1.5 * loc.getWidth() + 0.5);
		double height = (myCSGUI.getGridHeight() - 3) / (2.0 * loc.getHeight() + 1.0);

		double centerX = (1.5 * loc.getX() + 1) * width + myCSGUI.getGridX();
		double centerY = (loc.getY() * 2 + 1 + loc.getX() % 2) * height + myCSGUI.getGridY() + 1.5;

		Polygon hex = new Polygon();
		hex.getPoints().addAll(new Double[] { 
				centerX - width / 2, centerY - height, 
				centerX + width / 2, centerY - height,
				centerX + width, centerY, 
				centerX + width / 2, centerY + height, 
				centerX - width / 2, centerY + height, 
				centerX - width, centerY, });
		return hex;
	}

	@Override
	public void drawCircle(Color red) {
		// TODO Auto-generated method stub

	}

}
