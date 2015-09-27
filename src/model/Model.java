package model;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cell.Cell;
import gui.CellSocietyGUI;

public interface Model {

	@SuppressWarnings("rawtypes")
	public static Model getModel(String name, CellSocietyGUI csGui){
		String tmp = "GOLModel PredModel SegModel FireModel AntModel SugarModel";
		List<String> models = Arrays.asList(tmp.split("\\s+"));
		if (models.contains(name)) {
			try {
				name = Model.class.getPackage().getName() + "." + name;
				Class[] types = { CellSocietyGUI.class };
				Constructor constructor;
				constructor = Class.forName(name).getDeclaredConstructor(types);
				return (Model) constructor.newInstance(csGui);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public abstract void initialize(Map<String, String> parameters, List<Map<String, String>> cells) throws Exception;

	public abstract void initialize(Map<String, String> parameters) throws Exception;

	public abstract void setParameters(Map<String, String> parameters);

	public abstract Map<String, String> getParameters();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract void step();

	public abstract void clear();
	
	public abstract Set<Cell> getCells();
}
