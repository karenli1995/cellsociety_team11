// This entire file is part of my masterpiece.
// Mike Ma (ym67)

package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import location.Location;
import state.FireState;
import cell.FireCell;
import gui.CellSocietyGUI;

public class FireModel extends Model {

	private static final int EMPTY_STATE = 0;
	private static final int TREE_STATE = 1;
	private static final int FIRE_STATE = 2;

	private double myProbCatchFire = 0.5; // default value

	public FireModel(CellSocietyGUI csGui) {
		super(csGui);
	}

	@Override
	protected void setBasicConfig(Map<String, String> parameters) throws ParseFormatException {
		super.setBasicConfig(parameters);
		if (parameters.containsKey("probCatchFire")) {
			double tmp = Double.parseDouble(parameters.get("probCatchFire"));
			myProbCatchFire = (tmp <= 1 && tmp >= 0) ? tmp : myProbCatchFire;
		}
		Map<Integer, String> map = new HashMap<>();
		map.put(EMPTY_STATE, "Empty");
		map.put(TREE_STATE, "Tree");
		map.put(FIRE_STATE, "Burning");
		setupGraph(map);
	}

	@Override
	public void initialize(Map<String, String> parameters, List<Map<String, String>> cells)
			throws ParseFormatException {
		super.initialize(parameters, cells);
		myGrid.setNeighbors();
	}

	@Override
	public void initialize(Map<String, String> parameters) throws ParseFormatException {
		super.initialize(parameters);
		myGrid.setNeighbors();
	}

	@Override
	protected Map<Integer, Double> getDataPoints() {
		int[] states = new int[3];
		myCells.forEach(cell -> {
			states[cell.getState().getStateInt()]++;
		});
		Map<Integer, Double> stateMap = new HashMap<>();
		stateMap.put(TREE_STATE, (double) states[TREE_STATE]);
		stateMap.put(FIRE_STATE, (double) states[FIRE_STATE]);
		stateMap.put(EMPTY_STATE, (double) states[EMPTY_STATE]);
		return stateMap;
	}

	@Override
	protected void fillMatrix(int[][] mat, Map<String, String> parameters) {
		int numBurning = Integer.parseInt(parameters.get("numBurning"));
		int numEmpty = Integer.parseInt(parameters.get("numEmpty"));
		for (int i = 0; i < mat.length; i++) {
			Arrays.fill(mat[i], TREE_STATE);
		}
		randomFill(mat, TREE_STATE, FIRE_STATE, numBurning);
		randomFill(mat, TREE_STATE, EMPTY_STATE, numEmpty);
	}

	@Override
	protected void addCell(Map<String, String> map) {
		int x = Integer.parseInt(map.get("x"));
		int y = Integer.parseInt(map.get("y"));
		int state = Integer.parseInt(map.get("state"));
		Location loc = Location.makeLocation(x, y, getWidth(), getHeight(), myCSGUI);
		FireCell cell = new FireCell(new FireState(state), loc, myCSGUI);
		cell.setProbCatchFire(myProbCatchFire);
		myCells.add(cell);

	}

}
