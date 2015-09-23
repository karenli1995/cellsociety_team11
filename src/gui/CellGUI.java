package gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import state.State;

public abstract class CellGUI {

	CellSocietyGUI myCSGUI;
	protected double myGridXPos;
	protected double myGridYPos;
	protected Shape myShape;
	protected Circle myCircle;
	protected EventHandler<? super MouseEvent> myClickCallback;

	public CellGUI(CellSocietyGUI CSGUI) {
		myCSGUI = CSGUI;
		myGridXPos = CSGUI.getGridX();
		myGridYPos = CSGUI.getGridY();
	}
	
	private void setColor(Color color) {
		myShape.setFill(color);
	}
	
	public void updateState(State state) {
		setColor(state.getColor());
	}
	
	public void remove() {
		removeCircle();
		myCSGUI.removeFromScreen(myShape);
	}
	
	protected void addShapeToScreen() {
		myShape.setStrokeWidth(0.2);
		myShape.setFill(Color.WHITE);
		myShape.setStroke(Color.GREY);
		myCSGUI.addToScreen(myShape);
	}
	
	public abstract void drawCircle(Color red);

	public void removeCircle() {
		myCSGUI.removeFromScreen(myCircle);
	}
	
	public void addClickListener(EventHandler<? super MouseEvent> callback) {
        myShape.setOnMouseClicked(callback);
	}
	
	public void addCircleClickListener(EventHandler<? super MouseEvent> callback) {
        myCircle.setOnMouseClicked(callback);
	}
}
