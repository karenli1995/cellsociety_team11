package gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class CellSociety extends Application {
	
	@Override
	public void start(Stage stage) {
		new CellSocietyGUI(stage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}