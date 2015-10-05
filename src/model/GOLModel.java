// This entire file is part of my masterpiece.
// Mike Ma (ym67)
package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import location.Location;
import state.GOLState;
import cell.Cell;
import cell.GOLCell;
import gui.CellSocietyGUI;

public class GOLModel extends Model {

	private static final double DEFAULT_PERCENT_LIVE_CELLS = 0.5;
	private static final int DEAD_STATE = 0;
	private static final int LIVE_STATE = 1;

	public GOLModel(CellSocietyGUI csGui) {
		super(csGui);
	}

	@Override
	public void setParameters(Map<String, String> parameters) {
		initialize(parameters);
	}

	@Override
	protected void setBasicConfig(java.util.Map<String, String> parameters) throws ParseFormatException {
		super.setBasicConfig(parameters);
		Map<Integer, String> map = new HashMap<>();
		map.put(DEAD_STATE, "Dead");
		map.put(LIVE_STATE, "Live");
		setupGraph(map);
	};

	@Override
	public void initialize(Map<String, String> parameters, List<Map<String, String>> cells)
			throws ParseFormatException {
		initialize(parameters, cells);
		myGrid.setNeighbors();
	}

	@Override
	public void initialize(Map<String, String> parameters) {
		initialize(parameters);
		myGrid.setNeighbors();
	}

	@Override
	protected Map<Integer, Double> getDataPoints() {
		int[] states = new int[2];
		myCells.forEach(cell -> {
			states[cell.getState().getStateInt()]++;
		});
		Map<Integer, Double> stateMap = new HashMap<>();
		stateMap.put(DEAD_STATE, (double) states[DEAD_STATE]);
		stateMap.put(LIVE_STATE, (double) states[LIVE_STATE]);
		return stateMap;
	}

	@Override
	protected void fillMatrix(int[][] mat, Map<String, String> parameters) {
		double percentLive = DEFAULT_PERCENT_LIVE_CELLS;
		if (myParameters.containsKey("percentLive")) {
			double tmp = Double.parseDouble(myParameters.get("percentLive"));
			if (tmp >= 0 && tmp <= 1)
				percentLive = tmp;
		}
		int numLiveCells = (int) (myWidth * myHeight * percentLive);
		randomFill(mat, DEAD_STATE, LIVE_STATE, numLiveCells);

	}

	@Override
	protected void addCell(Map<String, String> map) {
		int x = Integer.parseInt(map.get("x"));
		int y = Integer.parseInt(map.get("y"));
		int state = Integer.parseInt(map.get("state"));
		Location loc = Location.makeLocation(x, y, getWidth(), getHeight(), myCSGUI);
		Cell cell = new GOLCell(new GOLState(state), loc, myCSGUI);
		myCells.add(cell);
	}
}
