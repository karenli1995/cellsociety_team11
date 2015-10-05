// This entire file is part of my masterpiece.
// Mike Ma (ym67)

package model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import cell.Cell;
import grid.Grid;
import grid.GridFactory;
import gui.CellSocietyGUI;

public abstract class Model {

	protected CellSocietyGUI myCSGUI;
	protected int myWidth;
	protected int myHeight;
	protected Grid myGrid;
	protected Set<Cell> myCells;
	protected Map<String, String> myParameters;
	protected Random myRandom;
	protected String myAuthor = "CPS308Team11";
	protected int myStepNum = 0;

	/**
	 * Construct a model from the name
	 * 
	 * @param name
	 * @param csGui
	 * @return model
	 * @throws ParseFormatException
	 */
	public static Model getModel(String name, CellSocietyGUI csGui) throws ParseFormatException {

		try {
			name = Model.class.getPackage().getName() + "." + name;
			Class[] types = { CellSocietyGUI.class };
			Constructor constructor = Class.forName(name).getDeclaredConstructor(types);
			return (Model) constructor.newInstance(csGui);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ParseFormatException(e.getMessage());
		}
	}

	Model(CellSocietyGUI CSGUI) {
		myCSGUI = CSGUI;
		myRandom = new Random();
		myCells = new HashSet<>();
	}

	/**
	 * Go to next frame
	 */
	public void step() {
		++myStepNum;
		myGrid.step();
		updateGraph();
	}

	/**
	 * clear the grid
	 */
	public void clear() {
		myStepNum = 0;
		if (myGrid != null)
			myGrid.clear();
		if (myCells != null)
			myCells.clear();
		myCSGUI.resetGraph();
	}

	/**
	 * Initialize this model with a set of given parameters, generating cells
	 * randomly
	 * 
	 * @param parameters
	 * @throws ParseFormatException
	 */
	public void initialize(Map<String, String> parameters) throws ParseFormatException {
		setBasicConfig(parameters);
		int mat[][] = new int[getWidth()][getHeight()];
		fillMatrix(mat, parameters);
		for (int x = 0; x < mat.length; x++) {
			for (int y = 0; y < mat[x].length; y++) {
				Map<String, String> map = new HashMap<>();
				map.put("x", Integer.toString(x));
				map.put("y", Integer.toString(y));
				map.put("state", Integer.toString(mat[x][y]));
				addCell(map);
			}
		}
		myGrid = GridFactory.makeGrid(getWidth(), getHeight(), myCells, myCSGUI);
	}

	protected abstract void fillMatrix(int[][] mat, Map<String, String> parameters);

	/**
	 * Initialize this model with a set of given parameters and a list of cell
	 * attributes
	 * 
	 * @param parameters
	 *            map containing model parameters
	 * @param cells
	 * @throws ParseFormatException
	 */
	public void initialize(Map<String, String> parameters, List<Map<String, String>> cells)
			throws ParseFormatException {
		setBasicConfig(parameters);
		cells.forEach(map -> {
			addCell(map);
		});
		if (myCells.size() < getWidth() * getHeight())
			throw new ParseFormatException(
					"Missing " + (getWidth() * getHeight() - myCells.size()) + " cell information!");
		myGrid = GridFactory.makeGrid(getWidth(), getHeight(), myCells, myCSGUI);
	}

	protected abstract void addCell(Map<String, String> map);

	/**
	 * Randomly fill cells in a matrix whose state is initialState with
	 * finalState
	 * 
	 * @param mat
	 *            Matrix to fill
	 * @param initialState
	 * @param finalState
	 *            # of cells to fill
	 * @param totalToFill
	 */
	protected void randomFill(int[][] mat, int initialState, int finalState, int totalToFill) {
		Stack<Integer> stack = new Stack<>();
		for (int x = 0; x < mat.length; x++)
			for (int y = 0; y < mat[x].length; y++) {
				if (mat[x][y] == initialState)
					stack.push(1000 * x + y);
			}
		Collections.shuffle(stack, myRandom);
		int i = 0;
		while (!stack.isEmpty() && i < totalToFill) {
			int t = stack.pop();
			int x = t / 1000, y = t % 1000;
			mat[x][y] = finalState;
			i++;
		}
	}

	protected void setupGraph(Map<Integer, String> names) {
		myCSGUI.resetGraph();
		names.forEach((k, v) -> {
			myCSGUI.addSeries(k, v);
		});
	}

	protected void updateGraph() {
		getDataPoints().forEach((series, value) -> {
			myCSGUI.addDataPoint(myStepNum, value, series);
		});
	}

	/**
	 * 
	 * @return Map from Series to Y value
	 */
	protected abstract Map<Integer, Double> getDataPoints();

	protected void setBasicConfig(Map<String, String> parameters) throws ParseFormatException {
		clear();
		myParameters = parameters;
		try {
			int width = Integer.parseInt(myParameters.get("width"));
			int height = Integer.parseInt(myParameters.get("height"));
			setDimensions(width, height);
		} catch (NumberFormatException | NullPointerException e) {
			throw new ParseFormatException("No valid width or height info!");
		}
		myAuthor = myParameters.containsKey("author") ? myParameters.get("author") : myAuthor;
	}

	/**
	 * Set current model parameters
	 * 
	 * @param parameters
	 */
	public void setParameters(Map<String, String> parameters) {
		myParameters = parameters;
	}

	/**
	 * Get current model parameters, read only
	 * 
	 * @param parameters
	 */
	public Map<String, String> getParameters() {
		return Collections.unmodifiableMap(myParameters);
	}

	/**
	 * Set width & height of the model
	 * 
	 * @param width
	 * @param height
	 */
	protected void setDimensions(int width, int height) {
		myWidth = width;
		myHeight = height;
	}

	/**
	 * Get # of rows of this model
	 * 
	 * @return height
	 */
	public int getHeight() {
		return myHeight;
	}

	/**
	 * Get # of columns of this model
	 * 
	 * @return width
	 */
	public int getWidth() {
		return myWidth;
	}

	/**
	 * get an immutable set of cells
	 * 
	 * @return Set<Cell>
	 */
	public Set<Cell> getCells() {
		return Collections.unmodifiableSet(myCells);
	}

	@Override
	public final String toString() {
		String name = "model";
		if (myParameters != null && myParameters.containsKey("name")) {
			name = myParameters.get("name");
		}
		return name + "_" + myWidth + "_" + myHeight + "_" + myAuthor;
	}

}
