package state;

public abstract class AbstractState implements State{
	protected int myStateInt = 0;
	protected int myNextStateInt = 0;

	AbstractState(int state){
		myStateInt = state;
		myNextStateInt = state;
	}
	
	public double getOpacity(){
		return 1;
	}
	
	public int getStateInt(){
		return myStateInt;
	}
	
	public void setNextState(int nextStateInt) {
		myNextStateInt = nextStateInt;
	}
	
	public void setNextState(State s) {
		myNextStateInt = s.getStateInt();
	}
	
	public void goToNextState() {
		myStateInt = myNextStateInt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + myStateInt;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return myStateInt == ((AbstractState)obj).myStateInt;
	}
	
	@Override
	public String toString(){
		return Integer.toString(myStateInt); 
	}
}
