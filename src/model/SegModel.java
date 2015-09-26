package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import location.Location;
import location.ToroidalLocation;
import state.SegState;
import cell.Cell;
import cell.SegCell;
import grid.SquareGrid;
import grid.TriangleGrid;
import gui.CellSocietyGUI;

public class SegModel extends AbstractModel {

	private static final int EMPTY_STATE = 0;
	private static final int STATE_A = 1;
	private static final int STATE_B = 2;

	private double mySimilarity = 0.7; // default value
	private double myDissatisfactionRate;

	public SegModel(CellSocietyGUI csGui) {
		super(csGui);
	}

	@Override
	public void setParameters(Map<String, String> map) {
		// TODO Auto-generated method stub
		super.setParameters(map);
		if (map.containsKey("similarity"))
			mySimilarity = Double.parseDouble(map.get("similarity"));
	}

	@Override
	public void step() {
		Stack<Cell> exchangeList = getDissatisfiedCells();
		if (myCells != null && !myCells.isEmpty())
			myDissatisfactionRate = 1 - 100.0 * exchangeList.size() / myCells.size();
		exchangeList.addAll(getVacentCells());
		Collections.shuffle(exchangeList);

		while (exchangeList.size() >= 2) {
			SegCell cell1 = (SegCell) exchangeList.pop();
			SegCell cell2 = (SegCell) exchangeList.pop();
			cell2.determineNextState(cell1.getState());
			cell1.determineNextState(cell2.getState());
		}
		myCells.forEach(cell -> {
			cell.goToNextState();
		});
		myStepNum++;
		updateGraph();
	}

	private Stack<Cell> getDissatisfiedCells() {
		Stack<Cell> list = new Stack<>();
		myCells.forEach(cell -> {
			if (!((SegCell) cell).isEmpty() && !((SegCell) cell).isSatisfied())
				list.add(cell);
		});
		return list;
	}

	private Stack<Cell> getVacentCells() {
		Stack<Cell> list = new Stack<>();
		myCells.forEach(cell -> {
			if (((SegCell) cell).isEmpty())
				list.add(cell);
		});
		return list;
	}

	@Override
	protected void setBasicConfig(java.util.Map<String, String> parameters) {
		super.setBasicConfig(parameters);
		if (parameters.containsKey("similarity")) {
			double tmp = Double.parseDouble(myParameters.get("similarity"));
			mySimilarity = (tmp <= 1 && tmp >= 0) ? tmp : mySimilarity;
		}
		Map<Integer, String> map = new HashMap<>();
		map.put(0, "Satisfaction Rate (%)");
		setupGraph(map);
	};

	@Override
	public void initialize(Map<String, String> parameters, List<Map<String, String>> cells) {
		setBasicConfig(parameters);
		cells.forEach(map -> {
			int x = Integer.parseInt(map.get("x"));
			int y = Integer.parseInt(map.get("y"));
			int state = Integer.parseInt(map.get("state"));
			addCell(x, y, state);
		});
		if (myCells.size() < getWidth() * getHeight())
			System.err.println("Missing Cell Info!");
		myGrid = new SquareGrid(getWidth(), getHeight(), myCells);
		myGrid.setNeighbors();
	}

	@Override
	public void initialize(Map<String, String> parameters) {
		setBasicConfig(parameters);
		double pA = 0.5, pB = 0.5;
		if (myParameters.containsKey("percentA")) {
			double tmp = Double.parseDouble(myParameters.get("percentA"));
			pA = (tmp >= 0 && tmp <= 1) ? tmp : pA;
			pB = 1 - pA;
		}
		if (myParameters.containsKey("percentB")) {
			double tmp = Double.parseDouble(myParameters.get("percentB"));
			pB = (tmp >= 0 && tmp <= pB) ? tmp : pB;
		}

		int total = myWidth * myHeight;
		int numTypeA = (int) (total * pA), numTypeB = (int) (total * pB);
		int mat[][] = new int[getWidth()][getHeight()];

		int i = 0;
		while (i < numTypeA) {
			int t = myRandom.nextInt(total);
			int x = t % getWidth(), y = t / getWidth();
			if (mat[x][y] == EMPTY_STATE) {
				mat[x][y] = STATE_A;
				i++;
			}
		}
		i = 0;
		while (i < numTypeB) {
			int t = myRandom.nextInt(total);
			int x = t % getWidth(), y = t / getWidth();
			if (mat[x][y] == EMPTY_STATE) {
				mat[x][y] = STATE_B;
				i++;
			}
		}
		for (int x = 0; x < mat.length; x++)
			for (int y = 0; y < mat[x].length; y++)
				addCell(x, y, mat[x][y]);
		myGrid = new TriangleGrid(getWidth(), getHeight(), myCells);
		myGrid.setNeighbors();
	}

	private void addCell(int x, int y, int state) {
		SegCell cell = new SegCell(new SegState(state), new Location(x, y, myWidth, getHeight()), myCSGUI);
		cell.setSimilarity(mySimilarity);
		myCells.add(cell);
	}

	@Override
	protected Map<Integer, Double> getDataPoints() {
		Map<Integer, Double> map = new HashMap<>();
		map.put(0, (double) myDissatisfactionRate);
		return map;
	}

}
