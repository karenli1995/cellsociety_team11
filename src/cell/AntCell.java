package cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gui.CellSocietyGUI;
import location.Location;
import state.AntState;
import state.State;

public class AntCell extends AbstractCell{
	private static final int MAX_HOME_PHEROMONE = 10;
	private static final int MAX_FOOD_PHEROMONE = 10;
	
	private static final int EMPTY_STATE = 0;
	private static final int NEST_STATE = 1;
	private static final int FOOD_SOURCE_STATE = 2;
	private static final int HOME_PHEROMONE_STATE = 3;
	private static final int FOOD_PHEROMONE_STATE = 4;
	
	private static final int NUM_STATES = 3;
	
	AntState myAntState;
	
	private int myEvaporationRate;
	private int myDiffusionRate;
	
	private int myFoodPheromone;
	private int myHomePheromone;
	private boolean myHasFoodItem;

	AntCell(State s, Location l, CellSocietyGUI CSGUI) {
		super(s, NUM_STATES, l, CSGUI);
		myAntState = (AntState) s;
		
		myFoodPheromone = 0;
		myHomePheromone = 0;
		
		myHasFoodItem = false;
	}
	
	public void setParameters(int evaporationRate, int diffusionRate){
		myEvaporationRate = evaporationRate;
		myDiffusionRate = diffusionRate;
	}
	
	private void setMyHasFoodItem(boolean b){
		myHasFoodItem = b;
	}

	private boolean getMyHasFoodItem(){
		return myHasFoodItem;
	}
	
	private int getMyFoodPheromone(){
		return myFoodPheromone;
	}
	
	private void setMyFoodPheromone(int fp){
		myFoodPheromone = fp;
	}

	private int getMyHomePheromone(){
		return myHomePheromone;
	}
	
	private void setMyHomePheromone(int hp){
		myHomePheromone = hp;
	}

	
	
	@Override
	public void determineNextState() {
		//evaporation
		int foodPheromoneAfterEvap = this.getMyFoodPheromone() - (this.getMyFoodPheromone()*myEvaporationRate);
		if(foodPheromoneAfterEvap > 0){
			this.setMyFoodPheromone(foodPheromoneAfterEvap);
		}else{
			this.setMyFoodPheromone(0);
		}
		
		int homePheromoneAfterEvap = this.getMyHomePheromone() - (this.getMyHomePheromone()*myEvaporationRate);
		if(homePheromoneAfterEvap > 0){
			this.setMyFoodPheromone(homePheromoneAfterEvap);
		}else{
			this.setMyHomePheromone(0);
		}
		
		
		if(this.getMyHasFoodItem() == false){
			antFindFoodSource();	
		}else{
			antReturnToNest();
		}
	}

	
	private void antFindFoodSource(){
		if(myAntState.getStateInt() == NEST_STATE && myAntState.getContainsAnt() == true){
			ArrayList<Cell> neighbors = (ArrayList<Cell>) this.getNeighborCells();

			int maxPheromone=0;
			AntCell chosenCellMaxPheromone = this;
			for(int i=0; i<neighbors.size(); i++) {
				AntCell chosenCell = (AntCell) neighbors.get(i);
				int otherFoodPheromone = chosenCell.getMyFoodPheromone();
				if (otherFoodPheromone > maxPheromone){
					maxPheromone = otherFoodPheromone;
					chosenCellMaxPheromone = chosenCell;
				}
			}
			if(maxPheromone > 0){
				//drop home pheromones on current state
				//dropHomePheromones(chosenCellMaxPheromone, neighbors);
				dropPheromones(chosenCellMaxPheromone, neighbors, NEST_STATE);
				this.getState().setNextState(HOME_PHEROMONE_STATE);  //check this
				
				
				AntState prevCellState = (AntState) this.getState();
				prevCellState.setContainsAnt(false);
				
				AntState chosenCellState = (AntState) chosenCellMaxPheromone.getState();
				chosenCellState.setContainsAnt(true);
				if(chosenCellState.getStateInt() == FOOD_SOURCE_STATE){
					chosenCellState.setNextState(EMPTY_STATE);
					chosenCellMaxPheromone.setMyHasFoodItem(true);
					//return to nest
				}
			}else{
				this.getState().setNextState(myAntState.getStateInt());
				AntCell chosenCell = (AntCell) neighbors.get(0);
				AntState chosenCellState = (AntState) chosenCell.getState();
				chosenCellState.setContainsAnt(true);
			}
		}
	}
	
	private void antReturnToNest(){
		if(myAntState.getStateInt() == FOOD_SOURCE_STATE && myAntState.getContainsAnt() == true){
			ArrayList<Cell> neighbors = (ArrayList<Cell>) this.getNeighborCells();

			int maxPheromone=0;
			AntCell chosenCellMaxPheromone = this;
			for(int i=0; i<neighbors.size(); i++) {
				AntCell chosenCell = (AntCell) neighbors.get(i);
				int otherFoodPheromone = chosenCell.getMyHomePheromone();
				if (otherFoodPheromone > maxPheromone){
					maxPheromone = otherFoodPheromone;
					chosenCellMaxPheromone = chosenCell;
				}
			}
			if(maxPheromone > 0){
				//drop food pheromones on current state
				//dropFoodPheromones(chosenCellMaxPheromone, neighbors);
				dropPheromones(chosenCellMaxPheromone, neighbors, FOOD_SOURCE_STATE);
				
				this.getState().setNextState(FOOD_PHEROMONE_STATE);  //check this

				AntState prevCellState = (AntState) this.getState();
				prevCellState.setContainsAnt(false);
				
				AntState chosenCellState = (AntState) chosenCellMaxPheromone.getState();
				chosenCellState.setContainsAnt(true);
				if(chosenCellState.getStateInt() == NEST_STATE){
					//drop food item
					chosenCellMaxPheromone.setMyHasFoodItem(false);
					//return back to find food
				}
			}else{
				this.getState().setNextState(myAntState.getStateInt());
				AntCell chosenCell = (AntCell) neighbors.get(0);
				AntState chosenCellState = (AntState) chosenCell.getState();
				chosenCellState.setContainsAnt(true);
			}
		}
	}
	
	
	
	public void dropPheromones(AntCell maxPheromoneCell, ArrayList<Cell> neighbors, int state){
		if(myAntState.getContainsAnt() == true && myAntState.getStateInt() == state){
			if(state == NEST_STATE) setMyHomePheromone(MAX_HOME_PHEROMONE);
			else setMyFoodPheromone(MAX_FOOD_PHEROMONE);
		}else{
			if(state == NEST_STATE){ 
				int maxHomePheromones = maxPheromoneCell.getMyHomePheromone();
				int des = maxHomePheromones-2;
				int d = des - this.getMyHomePheromone();
				if(d > 0){
					this.setMyHomePheromone(getMyHomePheromone() + d);
					
					//diffusion -- might want to check if this works correctly
					for(int i=0; i<neighbors.size(); i++) {
						AntCell chosenCell = (AntCell) neighbors.get(i);
						int chosenHomePheromone = chosenCell.getMyHomePheromone();
						
						chosenCell.setMyHomePheromone(chosenHomePheromone + (d * myDiffusionRate));
						chosenCell.getState().setNextState(HOME_PHEROMONE_STATE);
					}
				}
				
			}else{
				int maxFoodPheromones = maxPheromoneCell.getMyFoodPheromone();
				int des = maxFoodPheromones-2;
				int d = des - this.getMyFoodPheromone();
				if(d > 0){
					this.setMyFoodPheromone(getMyFoodPheromone() + d);
					
					//diffusion -- might want to check if this works correctly
					for(int i=0; i<neighbors.size(); i++) {
						AntCell chosenCell = (AntCell) neighbors.get(i);
						int chosenFoodPheromone = chosenCell.getMyFoodPheromone();
						
						chosenCell.setMyFoodPheromone(chosenFoodPheromone + (d * myDiffusionRate));
						chosenCell.getState().setNextState(FOOD_PHEROMONE_STATE);
					}
					
				}
			}
			
		}
	}
	
	
//	public void dropHomePheromones(AntCell maxPheromoneCell, ArrayList<Cell> neighbors){
//		if(myAntState.getContainsAnt() == true && myAntState.getStateInt() == NEST_STATE){
//			setMyHomePheromone(MAX_HOME_PHEROMONE);
//		}else{
//			int maxHomePheromones = maxPheromoneCell.getMyHomePheromone();
//			int des = maxHomePheromones-2;
//			int d = des - this.getMyHomePheromone();
//			if(d > 0){
//				this.setMyHomePheromone(d);
//				
//				//diffusion -- might want to check if this works correctly
//				for(int i=0; i<neighbors.size(); i++) {
//					AntCell chosenCell = (AntCell) neighbors.get(i);
//					int chosenHomePheromone = chosenCell.getMyHomePheromone();
//					
//					chosenCell.setMyHomePheromone(chosenHomePheromone + (d * myDiffusionRate));
//					chosenCell.getState().setNextState(HOME_PHEROMONE_STATE);
//				}
//			}
//		}
//	}
//	
//	public void dropFoodPheromones(AntCell maxPheromoneCell, ArrayList<Cell> neighbors){
//		if(myAntState.getContainsAnt() == true && myAntState.getStateInt() == FOOD_SOURCE_STATE){
//			setMyFoodPheromone(MAX_FOOD_PHEROMONE);
//		}else{
//			int maxFoodPheromones = maxPheromoneCell.getMyFoodPheromone();
//			int des = maxFoodPheromones-2;
//			int d = des - this.getMyFoodPheromone();
//			if(d > 0){
//				this.setMyFoodPheromone(d);
//				
//				//diffusion -- might want to check if this works correctly
//				for(int i=0; i<neighbors.size(); i++) {
//					AntCell chosenCell = (AntCell) neighbors.get(i);
//					int chosenFoodPheromone = chosenCell.getMyFoodPheromone();
//					
//					chosenCell.setMyFoodPheromone(chosenFoodPheromone + (d * myDiffusionRate));
//					chosenCell.getState().setNextState(FOOD_PHEROMONE_STATE);
//				}
//				
//			}
//		}
//	}

}
