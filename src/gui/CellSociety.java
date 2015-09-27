package gui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CellSociety extends Application {
	
	@Override
	public void start(Stage stage) {
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		stage.setWidth(bounds.getWidth());
		stage.setHeight(bounds.getHeight());
		stage.setResizable(false);
		new CellSocietyGUI(stage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}