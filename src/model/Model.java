package model;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cell.Cell;
import gui.CellSocietyGUI;

public interface Model {

	/**
	 * Create a model with specified model name
	 * 
	 * @param name
	 * @param csGui
	 * @return an instance of a subclass implementing model
	 */
	@SuppressWarnings("rawtypes")
	public static Model getModel(String name, CellSocietyGUI csGui) throws Exception {
		String tmp = "GOLModel PredModel SegModel FireModel SugarModel AntModel";
		List<String> models = Arrays.asList(tmp.split("\\s+"));
		name = Model.class.getPackage().getName() + "." + name;
		Class[] types = { CellSocietyGUI.class };
		Constructor constructor;
		constructor = Class.forName(name).getDeclaredConstructor(types);
		return (Model) constructor.newInstance(csGui);
	}

	/**
	 * Initialize this model with a set of given parameters and a list of cell
	 * attributes
	 * 
	 * @param parameters
	 * @param cells
	 * @throws Exception
	 */
	public abstract void initialize(Map<String, String> parameters, List<Map<String, String>> cells) throws Exception;

	/**
	 * Initialize this model with a set of given parameters, generating cells
	 * randomly
	 * 
	 * @param parameters
	 * @throws Exception
	 */
	public abstract void initialize(Map<String, String> parameters) throws Exception;

	/**
	 * Set current model parameters
	 * 
	 * @param parameters
	 */
	public abstract void setParameters(Map<String, String> parameters);

	/**
	 * Get current model parameters, read only
	 * 
	 * @param parameters
	 */
	public abstract Map<String, String> getParameters();

	/**
	 * Get # of columns of this model
	 * 
	 * @return width
	 */
	public abstract int getWidth();

	/**
	 * Get # of rows of this model
	 * 
	 * @return height
	 */
	public abstract int getHeight();

	/**
	 * Go to next frame
	 */
	public abstract void step();

	/**
	 * clear the grid
	 */
	public abstract void clear();

	/**
	 * get an immutable set of cells
	 * 
	 * @return Set<Cell>
	 */
	public abstract Set<Cell> getCells();
}
