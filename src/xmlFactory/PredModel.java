package xmlFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PredModel extends Model {
	
	private static final int EMPTY_STATE = 0;
	private static final int SHARK_STATE = 1;
	private static final int FISH_STATE = 2;

	private int myNumShark, myNumFish;

	PredModel(int width, int height, double percentShark, double percentFish) {
		super("PredModel", width, height);
		int total = width * height;
		percentShark = (percentShark < 0) ? 0 : percentShark;
		percentFish = (percentFish < 0) ? 0 : percentFish;
		int numSharks = (int) (percentShark * total);
		int numFish = (int) (percentFish * total);
		myNumShark = numSharks <= total ? numSharks : total;
		myNumFish = (numFish <= total - myNumShark) ? numFish : total - myNumShark;
		setModelParameters(5,5,5);
		
	}
	
	public void setModelParameters(int fishEnergy, int sharkEnergy, int livesReproduce){
		addModelParameter("FISH_ENERGY", fishEnergy);
		addModelParameter("SHARK_ENERGY", sharkEnergy);
		addModelParameter("LIVES_REPRODUCE", livesReproduce);
	}
	
	private void fillCells(int[][] mat, int limit, int state){
		Random rand = new Random(System.currentTimeMillis());
		int total = mat.length*mat[0].length;
		int i = 0;
		while(i < limit){
			int t = rand.nextInt(total);
			int x = t % mat[0].length, y = t / mat[0].length;
			if(mat[x][y]==EMPTY_STATE){
				mat[x][y] = state;
				i++;
			}
		}
	}
	
	@Override
	public List<Map<String, String>> getCells() {
		int mat[][] = new int[getWidth()][getHeight()];		
		fillCells(mat,myNumShark,SHARK_STATE);
		fillCells(mat,myNumFish,FISH_STATE);

		List<Map<String, String>> list = new ArrayList<>();
		
		for (int x = 0; x < mat.length; x++) {
			for (int y = 0; y < mat[x].length; y++) {
				list.add(createCell(x, y, mat[x][y]));
			}
		}
		return list;
	}

	private Map<String, String> createCell(int x, int y, int state) {
		Map<String, String> map = new HashMap<>();
		map.put("x", Integer.toString(x));
		map.put("y", Integer.toString(y));
		map.put("state", Integer.toString(state));
		return map;
	}
	
	@Override
	public String toString(){
		return getClass().getSimpleName()+"_"+getWidth()+"_"+
				getHeight()+"_"+myNumShark+"_"+myNumFish;
	}

}
