package cell;

import java.util.ArrayList;
import java.util.Random;

import gui.CellGUI;
import gui.CellSocietyGUI;
import gui.SquareCellGUI;
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
	
	private static final int ANT_STATE = 5;
	
	private static final int NUM_STATES = 6;
	
	AntState myAntState;
	
	private double myEvaporationRate;
	private double myDiffusionRate;
	
	private double myFoodPheromone;
	private double myHomePheromone;
	private boolean myHasFoodItem;

	public AntCell(State s, Location l, CellSocietyGUI CSGUI) {
		super(s, NUM_STATES, l, CSGUI);
		myAntState = (AntState) s;
		myCellGUI = CellGUI.makeCellGUI(CSGUI, l);
		myCellGUI.updateState(s);
		addClickListener();
		
		myFoodPheromone = 0;
		myHomePheromone = 0;
		
		myHasFoodItem = false;
		
		myCellGUI = new SquareCellGUI(CSGUI, l);
		myCellGUI.updateState(s);
		addClickListener();
	}
	
	public void setParameters(double evaporationRate, double diffusionRate){
		myEvaporationRate = evaporationRate;
		myDiffusionRate = diffusionRate;
	}
	
	private void setMyHasFoodItem(boolean b){
		myHasFoodItem = b;
	}

	private boolean getMyHasFoodItem(){
		return myHasFoodItem;
	}
	
	private double getMyFoodPheromone(){
		return myFoodPheromone;
	}
	
	private void setMyFoodPheromone(double fp){
		myFoodPheromone = fp;
	}

	private double getMyHomePheromone(){
		return myHomePheromone;
	}
	
	private void setMyHomePheromone(double hp){
		myHomePheromone = hp;
	}

	
	
	@Override
	public void determineNextState() {
		//evaporation
//		double foodPheromoneAfterEvap = this.getMyFoodPheromone() - (this.getMyFoodPheromone()*myEvaporationRate);
//		if(foodPheromoneAfterEvap > 0){
//			this.setMyFoodPheromone(foodPheromoneAfterEvap);
//			this.getState().setNextState(FOOD_PHEROMONE_STATE);
//		}else{
//			if(this.getState().getStateInt() != FOOD_SOURCE_STATE){
//				this.getState().setNextState(EMPTY_STATE);
//				this.setMyFoodPheromone(0);
//			}
//		}
//		
//		double homePheromoneAfterEvap = this.getMyHomePheromone() - (this.getMyHomePheromone()*myEvaporationRate);
//		if(homePheromoneAfterEvap > 0){
//			this.setMyFoodPheromone(homePheromoneAfterEvap);
//			this.getState().setNextState(HOME_PHEROMONE_STATE);
//		}else{
//			if(this.getState().getStateInt() != FOOD_SOURCE_STATE){
//				this.getState().setNextState(EMPTY_STATE);
//				this.setMyHomePheromone(0);
//			}
//		}
//		
		double foodPheromoneAfterEvap = this.getMyFoodPheromone() - (this.getMyFoodPheromone()*myEvaporationRate);
		if(foodPheromoneAfterEvap > 0){
			this.setMyFoodPheromone(foodPheromoneAfterEvap);
		}else{
			this.setMyFoodPheromone(0);
			this.getState().setNextState(EMPTY_STATE);
		}
		
		double homePheromoneAfterEvap = this.getMyHomePheromone() - (this.getMyHomePheromone()*myEvaporationRate);
		if(homePheromoneAfterEvap > 0){
			this.setMyFoodPheromone(homePheromoneAfterEvap);
		}else{
			this.setMyHomePheromone(0);
			this.getState().setNextState(EMPTY_STATE);
		}
		
		
		if(this.getMyHasFoodItem() == false){
			System.out.println("time");
			antFindFoodSource();	
		}else{
			System.out.println("te");

			antReturnToNest();
		}
	}

	
	private void antFindFoodSource(){
		ArrayList<Cell> neighbors = (ArrayList<Cell>) this.getNeighborCells();
		//ArrayList<Cell> emptyNeighbors = (ArrayList<Cell>) this.getNeighborsInStateInt(EMPTY_STATE);
		
//		if(myAntState.getStateInt() == NEST_STATE && myAntState.getContainsAnt() == true){
//			Random r = new Random();
//			AntCell chosenCell = (AntCell) emptyNeighbors.get(r.nextInt(neighbors.size()-1));
//			AntState chosenCellState = (AntState) chosenCell.getState();
//			chosenCellState.setNextState(ANT_STATE);
//			chosenCellState.setContainsAnt(true);
//		}else
		if(myAntState.getContainsAnt() == true){

			double maxPheromone=0;
			AntCell chosenCellMaxPheromone = this;
			for(int i=0; i<neighbors.size(); i++) {
				AntCell chosenCell = (AntCell) neighbors.get(i);
				double otherFoodPheromone = chosenCell.getMyFoodPheromone();
				if (otherFoodPheromone > maxPheromone){
					maxPheromone = otherFoodPheromone;
					chosenCellMaxPheromone = chosenCell;
				}
			}
			
			System.out.println("fdjkls " + maxPheromone);
			if(maxPheromone > 0){
				dropPheromones(this, neighbors, NEST_STATE);
				
				AntState prevCellState = (AntState) this.getState();
				prevCellState.setNextState(HOME_PHEROMONE_STATE);  //check this
				prevCellState.setContainsAnt(false);
				
				AntState chosenCellState = (AntState) chosenCellMaxPheromone.getState();
				chosenCellState.setNextState(ANT_STATE);
				chosenCellState.setContainsAnt(true);
				if(chosenCellState.getStateInt() == FOOD_SOURCE_STATE){
					chosenCellState.setNextState(EMPTY_STATE);
					chosenCellMaxPheromone.setMyHasFoodItem(true);
					//return to nest
				}
			}else{
				AntState myPrevState = (AntState) this.getState();
				if(myPrevState.getStateInt() != NEST_STATE){
					myPrevState.setNextState(EMPTY_STATE); 
					Random r = new Random();
					AntCell chosenCell = (AntCell) neighbors.get(r.nextInt(neighbors.size()-1));
					AntState chosenCellState = (AntState) chosenCell.getState();
					
					if(chosenCellState.getStateInt() == FOOD_SOURCE_STATE){
						chosenCellState.setNextState(EMPTY_STATE);
						
						dropPheromones(chosenCell, neighbors, FOOD_SOURCE_STATE);
						chosenCell.setMyHasFoodItem(true);
					}
					
					chosenCellState.setNextState(ANT_STATE);
					chosenCellState.setContainsAnt(true);
					
				}else{
					myPrevState.setNextState(NEST_STATE); 
				}
				myPrevState.setContainsAnt(false);
				
//				Random r = new Random();
//				AntCell chosenCell = (AntCell) neighbors.get(r.nextInt(neighbors.size()-1));
//				AntState chosenCellState = (AntState) chosenCell.getState();
//				
//				if(chosenCellState.getStateInt() == FOOD_SOURCE_STATE){
//					chosenCellState.setNextState(EMPTY_STATE);
//					
//					dropPheromones(chosenCell, neighbors, FOOD_SOURCE_STATE);
//					chosenCell.setMyHasFoodItem(true);
//				}
//				
//				chosenCellState.setNextState(ANT_STATE);
//				chosenCellState.setContainsAnt(true);
				

				this.getState().setNextState(myAntState.getStateInt());
				AntCell chosenCell = (AntCell) neighbors.get(0);
				AntState chosenCellState = (AntState) chosenCell.getState();
				chosenCellState.setNextState(ANT_STATE);
				chosenCellState.setContainsAnt(true);
			}
		}
	}
	
	private void antReturnToNest(){
		if(myAntState.getContainsAnt() == true){
			ArrayList<Cell> neighbors = (ArrayList<Cell>) this.getNeighborCells();

			double maxPheromone=0;
			AntCell chosenCellMaxPheromone = this;
			for(int i=0; i<neighbors.size(); i++) {
				AntCell chosenCell = (AntCell) neighbors.get(i);
				double otherFoodPheromone = chosenCell.getMyHomePheromone();
				if (otherFoodPheromone > maxPheromone){
					maxPheromone = otherFoodPheromone;
					chosenCellMaxPheromone = chosenCell;
				}
			}
			if(maxPheromone > 0){
				dropPheromones(chosenCellMaxPheromone, neighbors, FOOD_SOURCE_STATE);				

				AntState prevCellState = (AntState) this.getState();
				prevCellState.setNextState(FOOD_PHEROMONE_STATE);  //check this
				prevCellState.setContainsAnt(false);
				
				AntState chosenCellState = (AntState) chosenCellMaxPheromone.getState();
				chosenCellState.setNextState(ANT_STATE);
				chosenCellState.setContainsAnt(true);
				if(chosenCellState.getStateInt() == NEST_STATE){
					chosenCellMaxPheromone.setMyHasFoodItem(false);
				}
			}else{
				AntState myPrevState = (AntState) this.getState();
				if(this.getState().getStateInt() != NEST_STATE){
					myPrevState.setNextState(EMPTY_STATE); //or nest state
				}else{
					myPrevState.setNextState(NEST_STATE); //or nest state
				}
				myPrevState.setContainsAnt(false);
				
				Random r = new Random();
				AntCell chosenCell = (AntCell) neighbors.get(r.nextInt(neighbors.size()-1));
				AntState chosenCellState = (AntState) chosenCell.getState();
				
				if(chosenCellState.getStateInt() == NEST_STATE){
					dropPheromones(chosenCell, neighbors, NEST_STATE);
					this.setMyHasFoodItem(false);
				}
				
				chosenCellState.setContainsAnt(true);
				
			}
		}
	}
	
	
	
	public void dropPheromones(AntCell cell, ArrayList<Cell> neighbors, int state){
		AntState antState = (AntState) cell.getState();
		if(antState.getContainsAnt() == true && antState.getStateInt() == state){
			if(state == NEST_STATE) setMyHomePheromone(MAX_HOME_PHEROMONE);
			else setMyFoodPheromone(MAX_FOOD_PHEROMONE);
		}else{
			if(state == NEST_STATE){ 
				double maxHomePheromones = cell.getMyHomePheromone();
				double des = maxHomePheromones-2;
				double d = des - this.getMyHomePheromone();
				System.out.println("dell " + d);
				if(d > 0){
					this.setMyHomePheromone(getMyHomePheromone() + d);
					this.getState().setNextState(HOME_PHEROMONE_STATE);
					
					//diffusion -- might want to check if this works correctly
//					for(int i=0; i<neighbors.size(); i++) {
//						AntCell chosenCell = (AntCell) neighbors.get(i);
//						double chosenHomePheromone = chosenCell.getMyHomePheromone();
//						
//						chosenCell.setMyHomePheromone(chosenHomePheromone + (d * myDiffusionRate));
//						chosenCell.getState().setNextState(HOME_PHEROMONE_STATE);
//					}
				}
				
			}else{
				double maxFoodPheromones = cell.getMyFoodPheromone();
				double des = maxFoodPheromones-2;
				double d = des - this.getMyFoodPheromone();

				if(d > 0){
					this.setMyFoodPheromone(getMyFoodPheromone() + d);
					this.getState().setNextState(FOOD_PHEROMONE_STATE);
					
					//diffusion -- might want to check if this works correctly
//					for(int i=0; i<neighbors.size(); i++) {
//						AntCell chosenCell = (AntCell) neighbors.get(i);
//						double chosenFoodPheromone = chosenCell.getMyFoodPheromone();
//						
//						chosenCell.setMyFoodPheromone(chosenFoodPheromone + (d * myDiffusionRate));
//						chosenCell.getState().setNextState(FOOD_PHEROMONE_STATE);
//					}
					for(int i=0; i<neighbors.size(); i++) {
						AntCell chosenCell = (AntCell) neighbors.get(i);
						double chosenFoodPheromone = chosenCell.getMyFoodPheromone();
						
						chosenCell.setMyFoodPheromone(chosenFoodPheromone + (d * myDiffusionRate));
						chosenCell.getState().setNextState(FOOD_PHEROMONE_STATE);
					}
					
				}
			}
			
		}
	}
}

