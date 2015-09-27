package gui;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CellSocietyGUI {
	private static final int XSIZE = 800;
	private static final int YSIZE = 600;
	private static final String[] BUTTON_NAMES = { "Start", "Pause", "Reset", "Step", " fps" };
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

	protected Stage myStage;
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
	
	/**
	 * Get a list of buttons
	 * @return
	 */
	protected Map<String, Button> getReadOnlyButtons(){
		return Collections.unmodifiableMap(myButtons);
	}

	/**
	 * Initialize the window
	 * @param width The width of the window
	 * @param height The height of the window
	 * @return the Scene that was initialized
	 */
	private Scene init(int width, int height) {
		myWindowWidth = width;
		myWindowHeight = height;
		myRoot = new Group();
		myScene = new Scene(myRoot,width,height,Color.AZURE);
		return myScene;
	}

	/**
	 * Get the title of the application
	 * @return The title
	 */
	public String getTitle() {
		return TITLE;
	}

	/**
	 * Add a node to the window.
	 * @param n The node
	 */
	public void addToScreen(Node n) {
		myRoot.getChildren().add(n);
	}

	/**
	 * Remove a node from the screen
	 * @param n The node
	 */
	public void removeFromScreen(Node n) {
		myRoot.getChildren().remove(n);
	}

	/**
	 * Add the control buttons to the screen
	 */
	private void addButtons() {
		myButtons = new HashMap<>(BUTTON_NAMES.length);
		EventHandler<ActionEvent>[] events = new EventHandler[4];
		events[0] = (e) -> start();
		events[1] = (e) -> pause();
		events[2] = (e) -> reset();
		events[3] = (e) -> step();
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
		//myButtons.get("LoadXML").setDisable(false);
	}

	/**
	 * Create the animation and timeline.
	 */
	private void createAnimation() {
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> myManager.step());
		myAnimation = new Timeline();
		myAnimation.setCycleCount(Timeline.INDEFINITE);
		myAnimation.getKeyFrames().add(frame);
	}
	
	protected void saveXML(){
		try{
			String fileName = myManager.save(getDataDirectory());
			showInfo("File saved successfully","File name: "+fileName);
		}catch(Exception e){
			showError("Save Exception","Failed to save current model as an XML",e);
		}
	}
	
	protected void showInfo(String header,String content){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	protected void showError(String header,String content){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	protected void showError(String header,String content,Exception e){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(content);
		
		// Create expandable Exception.
		Label label = new Label("The exception stacktrace was:");
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exceptionText = sw.toString();
		
		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();
	}
	
	private File getDataDirectory(){
		File file = new File(System.getProperty("user.dir")+"/data");
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}

	/**
	 * Parse a new XML file
	 */
	protected void openXML() {
		if(myAnimation.getStatus() == Status.RUNNING)
			pause();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File userDirectory = getDataDirectory();
		if(userDirectory.canRead()) {
			fileChooser.setInitialDirectory(userDirectory);
		}
		FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extentionFilter);
		File file = fileChooser.showOpenDialog(myStage);

		try {
			if (file != null && myManager.loadXML(file)) {
				myButtons.get("Start").setDisable(false);
				myButtons.get("Reset").setDisable(false);
				myButtons.get("Step").setDisable(false);
			}
		} catch (Exception e) {
			showError("Error!","Failed to load "+file.getName(),e);
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
				showError("Error!","Failed to load given configuration.",e);
			}
		});
	}

	/**
	 * Start animating
	 */
	public void start() {
		if(myAnimation!=null){
			myButtons.get("Start").setDisable(true);
			myButtons.get("Pause").setDisable(false);
			myAnimation.play();
		}
	}

	/**
	 * Pause the animation
	 */
	public void pause() {
		if(myAnimation!=null){
			myButtons.get("Pause").setDisable(true);
			myButtons.get("Start").setDisable(false);
			myAnimation.pause();
		}
	}

	/**
	 * Reset the game to the original position with the current file
	 */
	public void reset() {
		pause();
		myManager.reset();
	}
	
	/**
	 * Step to the next frame in the animation
	 */
	public void step(){
		if (myAnimation == null) return;
		if (myAnimation.getStatus() == Status.RUNNING){
			pause();
			myManager.step();
		}else{
			myManager.step();
		}
	}

	/**
	 * Change the amount of time between frames
	 * @param fps New FPS of the animation
	 */
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

	/**
	 * Create the control buttons and place them on the screen
	 * @param property The button text
	 * @param yIndex The index of the button
	 * @param handler The EventHandler associated with the button
	 * @return
	 */
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

	/**
	 * Add a slider to the screen
	 * @param yIndex The index of the node from the top of the screen
	 * @param fps The default value of the slider
	 * @return The Slider
	 */
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

	/**
	 * Show the label associated with the slider
	 * @param text The text to set the label to
	 * @param yIndex The index from the top of the page of the text
	 */
	public void showSliderLabel(String text, double yIndex) {
		mySliderLabel = new Label();
		myRoot.getChildren().add(mySliderLabel);
		mySliderLabel.applyCss();
		mySliderLabel.setLayoutY(GRID_MARGIN + BUTTON_HEIGHT * yIndex - mySliderLabel.prefHeight(-1));
		updateSliderLabel(text);
	}

	/**
	 * Updates the text of the slider label
	 * @param text The text to set the label to
	 */
	public void updateSliderLabel(String text) {
		mySliderLabel.setText(text);
		mySliderLabel.applyCss();
		double xPos = myWindowWidth - BUTTON_AREA_WIDTH
				+ (BUTTON_AREA_WIDTH - GRID_MARGIN - mySliderLabel.prefWidth(-1)) / 2;
		mySliderLabel.setLayoutX(xPos);
	}

	/**
	 * Add a graph to the button area of the screen
	 */
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

	/**
	 * Add a series to the graph
	 * @param index The series index
	 * @param name The name of the series
	 */
	public void addSeries(int index, String name) {
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName(name);

		myLineChart.getData().add(series);
		mySeriesMap.put(index, series);
	}

	/**
	 * Reset the graph and remove all lines and series
	 */
	public void resetGraph() {
		myLineChart.getData().clear();
		if(!GRAPH_AUTO_RANGE){
			NumberAxis xAxis = (NumberAxis)myLineChart.getXAxis();
			xAxis.setLowerBound(0);
			xAxis.setUpperBound(GRAPH_X_RANGE);
		}
	}

	/**
	 * Add a data point to the graph
	 * @param xVal The x value of the point
	 * @param yVal The y value of the point
	 * @param seriesNum The series number to set the point to
	 */
	public void addDataPoint(Number xVal, Number yVal, int seriesNum) {
		ObservableList<Data<Number, Number>> list = mySeriesMap.get(seriesNum).getData();
		list.add(new XYChart.Data<Number, Number>(xVal, yVal));
		if(!GRAPH_AUTO_RANGE && xVal.intValue()>GRAPH_X_RANGE){
			NumberAxis xAxis = (NumberAxis)myLineChart.getXAxis();
			xAxis.setLowerBound(xVal.intValue()-GRAPH_X_RANGE);
			xAxis.setUpperBound(xVal.intValue());
		}
	}

	/**
	 * Create the grid area and add it to the screen
	 */
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

	/**
	 * Get the width of the screen
	 * @return The width
	 */
	public double getGridWidth() {
		return myWindowWidth - GRID_MARGIN * 2 - BUTTON_AREA_WIDTH;
	}

	/**
	 * Get the height of the grid
	 * @return The height
	 */
	public double getGridHeight() {
		return myWindowHeight - GRID_MARGIN * 2;
	}

	/**
	 * Get the x coordinate of the grid
	 * @return
	 */
	public double getGridX() {
		return GRID_MARGIN;
	}

	/**
	 * Get the y coordinate of the grid
	 * @return
	 */
	public double getGridY() {
		return GRID_MARGIN;
	}
}