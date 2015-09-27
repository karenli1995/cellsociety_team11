package gui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;

public class MenuPanel extends MenuBar {
	
	private CellSocietyGUI myGui;

	public MenuPanel(CellSocietyGUI csGui){
        myGui = csGui;
        getMenus().addAll(fileMenu(), modelMenu(), controlMenu(),settingMenu());
	}
	
	private Menu fileMenu() {
		Menu menu = new Menu("File");
		
		MenuItem open = new MenuItem("Open XML");
		open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
		open.setOnAction(e->{myGui.openXML();});
		
		MenuItem save = new MenuItem("Save XML");
		save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		save.disableProperty().bind(myGui.getReadOnlyButtons().get("Step").disabledProperty());
		save.setOnAction(e->{myGui.saveXML();});
		
		MenuItem exit = new MenuItem("Exit");
		exit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		exit.setOnAction(e->{System.exit(0);});
		
		menu.getItems().addAll(open,save, new SeparatorMenuItem(), exit);
		
		return menu;
	}
	
	private Menu modelMenu() {
		Menu menu = new Menu("New Model");
		MenuItem gol = new MenuItem("Game of Live Model");
		gol.setOnAction(e -> {
			myGui.openModelConfig("GOLModel");
		});
		MenuItem fire = new MenuItem("Fire Spreading Model");
		fire.setOnAction(e -> {
			myGui.openModelConfig("FireModel");
		});
		MenuItem pred = new MenuItem("Predator-prey Model");
		pred.setOnAction(e -> {
			myGui.openModelConfig("PredModel");
		});
		MenuItem seg = new MenuItem("Segregation Model");
		seg.setOnAction(e -> {
			myGui.openModelConfig("SegModel");
		});
		MenuItem ant = new MenuItem("Foraging Ants Model");
		ant.setDisable(true);
		ant.setOnAction(e -> {
			myGui.openModelConfig("AntModel");
		});
		MenuItem sugar = new MenuItem("Sugarscape Model");
		sugar.setDisable(true);
		sugar.setOnAction(e -> {
			myGui.openModelConfig("SugarModel");
		});
		menu.getItems().addAll(gol,fire,pred,seg,ant,sugar);
		return menu;
	}
	
	private Menu controlMenu(){
		Menu menu = new Menu("Control");
		
		MenuItem start = new MenuItem("Start");
		start.disableProperty().bind(myGui.getReadOnlyButtons().get("Start").disabledProperty());
		start.setAccelerator(KeyCombination.keyCombination("Ctrl+7"));
		start.setOnAction(e->{myGui.start();});
		
		MenuItem pause = new MenuItem("Pause");
		pause.disableProperty().bind(myGui.getReadOnlyButtons().get("Pause").disabledProperty());
		pause.setAccelerator(KeyCombination.keyCombination("Ctrl+8"));
		pause.setOnAction(e->{myGui.pause();});
		
		MenuItem reset = new MenuItem("Reset");
		reset.disableProperty().bind(myGui.getReadOnlyButtons().get("Reset").disabledProperty());
		reset.setAccelerator(KeyCombination.keyCombination("Ctrl+9"));
		reset.setOnAction(e->{myGui.reset();});
		
		MenuItem step = new MenuItem("Step");
		step.disableProperty().bind(myGui.getReadOnlyButtons().get("Step").disabledProperty());
		step.setAccelerator(KeyCombination.keyCombination("Ctrl+0"));
		step.setOnAction(e->{myGui.step();});
		
		menu.getItems().addAll(start,pause,reset,step);
		return menu;
	}
	
	private Menu settingMenu(){
		Menu menu = new Menu("Settings");
		return menu;
	}

}

