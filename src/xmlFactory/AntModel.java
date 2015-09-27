package xmlFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AntModel extends Model {
	
	private static final int EMPTY_STATE = 0;
	private static final int NEST_STATE = 1;
	private static final int FOOD_SOURCE_STATE = 2;
	private static final int HOME_PHEROMONE_STATE = 3;
	private static final int FOOD_PHEROMONE_STATE = 4;
	
	private static final int ANT_STATE = 5;
	
	private int myNumAnts;

	AntModel(int width, int height, double percentAnts) {
		super("AntModel", width, height);
		int total = width * height;
		percentAnts = (percentAnts < 0) ? 0 : percentAnts;
		int numAnts = (int) (percentAnts * total);
		myNumAnts = numAnts <= total ? numAnts : total;
		setModelParameters(0.25,0.25);
		
	}
	
	public void setModelParameters(double evapRate, double diffusionRate){
		addModelParameter("evapRate", evapRate);
		addModelParameter("diffusionRate", diffusionRate);
	}

	//figure out fillCells and GetCells
	private void fillCells(int[][] mat, int limit, int state){
		Random rand = new Random(System.currentTimeMillis());
		int total = mat.length*mat[0].length;
//		int i = 0;
//		while(i < limit){
//			int t = rand.nextInt(total);
//			int x = t % mat[0].length, y = t / mat[0].length;
//			if(mat[x][y]==EMPTY_STATE){
//				mat[x][y] = state;
//				i++;
//			}
//		}
		
		int t = rand.nextInt(total);
		int x = t % getWidth(), y = t / getWidth();
		if(mat[x][y]==EMPTY_STATE){
			mat[x][y] = NEST_STATE;
		}

		
		int i=0;
		while(i < limit - 1){
			if(i%8 == 0){
				x++;
			}else if (i%8 == 1){
				y++;
			}else if (i%8 == 2){
				x--;
			}else if (i%8 == 3){
				x--;
			}else if (i%8 == 4){
				y--;
			}else if (i%8 == 5){
				y--;
			}else if (i%8 == 6){
				x++;
			}else if (i%8 == 7){
				x++;
			}
				
			
			if(mat[x][y]==EMPTY_STATE){
				mat[x][y] = NEST_STATE;
				i++;
			}
		}
	}
	
	@Override
	public List<Map<String, String>> getCells() {
		int mat[][] = new int[getWidth()][getHeight()];		
		fillCells(mat,myNumAnts,NEST_STATE);

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
		return getClass().getSimpleName()+"_"+getWidth()+"_"+getHeight();
	}

}
