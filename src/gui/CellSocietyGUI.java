package gui;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import cellsociety_team11.CSManager;
import gui.dialogue.Dialog;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CellSocietyGUI {
	private static final int XSIZE = 800;
	private static final int YSIZE = 600;
	private static final String[] BUTTON_NAMES = { "LoadXML", "Start", "Pause", "Reset", "Step", " fps" };
	private static final int FRAMES_PER_SECOND = 10;
	private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	private static final String TITLE = "Cell Society";
	private static final int BUTTON_AREA_WIDTH = 400;
	private static final int GRAPH_HEIGHT = 300;
	private static final double BUTTON_HEIGHT = 40;

	private static final double GRID_MARGIN = 50;
	private static final int GRAPH_X_RANGE = 100;
	private static final boolean GRAPH_AUTO_RANGE = false;

	private static final double MIN_FPS = 1;
	private static final double MAX_FPS = 60;
	private static final double INCREMENT = 0.5;

	private Stage myStage;
	private Scene myScene;
	private Group myRoot;
	private Timeline myAnimation;
	private CSManager myManager;
	private ResourceBundle myResources;

	private HashMap<Integer, XYChart.Series<Number, Number>> mySeriesMap;
	private LineChart<Number, Number> myLineChart;
	private Map<String, Button> myButtons;
	private Label mySliderLabel;

	private int myWindowWidth, myWindowHeight;

	public CellSocietyGUI(Stage stage) {

		myManager = new CSManager(this);
		myStage = stage;
		Scene scene = init((int)stage.getWidth(), (int)stage.getHeight());
		stage.setScene(scene);
		stage.setTitle(TITLE);
		
		addButtons();
		addGraph();
		createGridArea();
		MenuPanel menu = new MenuPanel(this);
		menu.prefWidthProperty().bind(stage.widthProperty());
		myRoot.getChildren().add(menu);
		createAnimation();
		stage.show();
		
	}
	
	protected Map<String, Button> getReadOnlyButtons(){
		return Collections.unmodifiableMap(myButtons);
	}

	private Scene init(int width, int height) {
		myWindowWidth = width;
		myWindowHeight = height;
		myRoot = new Group();
		myScene = new Scene(myRoot,width,height,Color.AZURE);
		return myScene;
	}

	public String getTitle() {
		return TITLE;
	}

	public void addToScreen(Node e) {
		myRoot.getChildren().add(e);
	}

	public void removeFromScreen(Node e) {
		myRoot.getChildren().remove(e);
	}

	private void addButtons() {
		myButtons = new HashMap<>(BUTTON_NAMES.length);
		EventHandler<ActionEvent>[] events = new EventHandler[5];
		events[0] = (e) -> openXML();
		events[1] = (e) -> start();
		events[2] = (e) -> pause();
		events[3] = (e) -> reset();
		events[4] = (e) -> step();
		int index;
		for (index = 0; index < events.length; index++) {
			Button button = createAndPlaceButton(BUTTON_NAMES[index], index, events[index]);
			myButtons.put(button.getText(), button);
			button.setDisable(true);
		}
		Slider slider = addSlider(index, FRAMES_PER_SECOND);
		slider.valueProperty().addListener((observable, oldValue, newValue) -> {
			changeTime(newValue.doubleValue());
		});
		showSliderLabel(FRAMES_PER_SECOND + BUTTON_NAMES[index], ++index);
		myButtons.get("LoadXML").setDisable(false);
	}

	private void createAnimation() {
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> myManager.step());
		myAnimation = new Timeline();
		myAnimation.setCycleCount(Timeline.INDEFINITE);
		myAnimation.getKeyFrames().add(frame);
	}

	protected void openXML() {
		if(myAnimation.getStatus() == Status.RUNNING)
			pause();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog(myStage);

		try {
			if (file != null && myManager.loadXML(file)) {
				myButtons.get("Start").setDisable(false);
				myButtons.get("Reset").setDisable(false);
				myButtons.get("Step").setDisable(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	protected void openModelConfig(String name){
		if(myAnimation.getStatus() == Status.RUNNING)
			pause();
		Dialog dialog = Dialog.getDialog(name);
		dialog.showAndWait().ifPresent(map -> {
			try {
				if (myManager.loadModelConfig(map)) {
					myButtons.get("Start").setDisable(false);
					myButtons.get("Reset").setDisable(false);
					myButtons.get("Step").setDisable(false);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public void start() {
		if(myAnimation!=null){
			myButtons.get("Start").setDisable(true);
			myButtons.get("Pause").setDisable(false);
			myAnimation.play();
		}
	}

	public void pause() {
		if(myAnimation!=null){
			myButtons.get("Pause").setDisable(true);
			myButtons.get("Start").setDisable(false);
			myAnimation.pause();
		}
	}

	public void reset() {
		pause();
		myManager.reset();
	}
	
	public void step(){
		if (myAnimation == null) return;
		if (myAnimation.getStatus() == Status.RUNNING){
			pause();
			myManager.step();
		}else{
			myManager.step();
		}
	}

	public void changeTime(final double fps) {
		int timerInterval = (int) (1000 / fps);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(timerInterval), e -> myManager.step());

		Status prevStat = myAnimation.getStatus();
		myAnimation.stop();
		myAnimation.getKeyFrames().setAll(keyFrame);
		updateSliderLabel((int) fps + BUTTON_NAMES[5]);
		if (prevStat == Status.RUNNING)
			start();
	}

	public Button createAndPlaceButton(String property, double yIndex, EventHandler<ActionEvent> handler) {
		int buttonArea = myWindowWidth - BUTTON_AREA_WIDTH;
		String label = property;// myResources.getString(property);
		Button button = new Button(label);
		addToScreen(button);
		button.applyCss();
		double width = button.prefWidth(-1);
		// double height = button.prefHeight(-1);
		button.setLayoutX(buttonArea + (BUTTON_AREA_WIDTH - GRID_MARGIN - width) / 2);
		button.setLayoutY(GRID_MARGIN + BUTTON_HEIGHT * yIndex);
		button.setOnAction(handler);
		return button;
	}

	public Slider addSlider(double yIndex, double fps) {
		Slider slider = new Slider(MIN_FPS, MAX_FPS, fps);
		slider.setMajorTickUnit(1);
		slider.setBlockIncrement(INCREMENT);
		slider.setLayoutX(myWindowWidth - BUTTON_AREA_WIDTH);
		slider.setLayoutY(GRID_MARGIN + BUTTON_HEIGHT * yIndex);
		slider.setPrefWidth(BUTTON_AREA_WIDTH - GRID_MARGIN);
		addToScreen(slider);
		return slider;
	}

	public void showSliderLabel(String text, double yIndex) {
		mySliderLabel = new Label();
		myRoot.getChildren().add(mySliderLabel);
		mySliderLabel.applyCss();
		mySliderLabel.setLayoutY(GRID_MARGIN + BUTTON_HEIGHT * yIndex - mySliderLabel.prefHeight(-1));
		updateSliderLabel(text);
	}

	public void updateSliderLabel(String text) {
		mySliderLabel.setText(text);
		mySliderLabel.applyCss();
		double xPos = myWindowWidth - BUTTON_AREA_WIDTH
				+ (BUTTON_AREA_WIDTH - GRID_MARGIN - mySliderLabel.prefWidth(-1)) / 2;
		mySliderLabel.setLayoutX(xPos);
	}

	public void addGraph() {
		// defining the axes
		final NumberAxis xAxis = new NumberAxis("Frame Number", 0, GRAPH_X_RANGE, 50);
		xAxis.setAutoRanging(GRAPH_AUTO_RANGE);
		final NumberAxis yAxis = new NumberAxis();
		// creating the chart
		myLineChart = new LineChart<Number, Number>(xAxis, yAxis);
		mySeriesMap = new HashMap<Integer, XYChart.Series<Number, Number>>();

		myLineChart.setTitle("Number of Cells in Each State");
		myLineChart.setLayoutX(myWindowWidth - BUTTON_AREA_WIDTH);
		myLineChart.setLayoutY(myWindowHeight - GRAPH_HEIGHT - GRID_MARGIN);
		myLineChart.setPrefWidth(BUTTON_AREA_WIDTH - GRID_MARGIN);
		myLineChart.setPrefHeight(GRAPH_HEIGHT);
		myLineChart.setCreateSymbols(false);
		addToScreen(myLineChart);
	}

	public void addSeries(int index, String name) {
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName(name);

		myLineChart.getData().add(series);
		mySeriesMap.put(index, series);
	}

	public void resetGraph() {
		myLineChart.getData().clear();
		if(!GRAPH_AUTO_RANGE){
			NumberAxis xAxis = (NumberAxis)myLineChart.getXAxis();
			xAxis.setLowerBound(0);
			xAxis.setUpperBound(GRAPH_X_RANGE);
		}
	}

	public void addDataPoint(Number xVal, Number yVal, int seriesNum) {
		ObservableList<Data<Number, Number>> list = mySeriesMap.get(seriesNum).getData();
		list.add(new XYChart.Data<Number, Number>(xVal, yVal));
		if(!GRAPH_AUTO_RANGE && xVal.intValue()>GRAPH_X_RANGE){
			NumberAxis xAxis = (NumberAxis)myLineChart.getXAxis();
			xAxis.setLowerBound(xVal.intValue()-GRAPH_X_RANGE);
			xAxis.setUpperBound(xVal.intValue());
		}
	}

	public void createGridArea() {
		Rectangle rect = new Rectangle();
		rect.setX(GRID_MARGIN);
		rect.setY(GRID_MARGIN);
		rect.setWidth(getGridWidth());
		rect.setHeight(getGridHeight());
		rect.setFill(Color.WHITE);
		rect.setStroke(Color.BLACK);
		rect.setStrokeWidth(1);
		addToScreen(rect);
	}

	public double getGridWidth() {
		return myWindowWidth - GRID_MARGIN * 2 - BUTTON_AREA_WIDTH;
	}

	public double getGridHeight() {
		return myWindowHeight - GRID_MARGIN * 2;
	}

	public double getGridX() {
		return GRID_MARGIN;
	}

	public double getGridY() {
		return GRID_MARGIN;
	}
}