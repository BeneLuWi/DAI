package squareRunner;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Runner {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private boolean reachedExit;
	QLearning learn;
	
	int prevSteps;
	int prevprevSteps;
	int steps;
	
	
	public Runner(ContinuousSpace<Object> space, Grid<Object> grid, QLearning learn) {
		this.space = space;
		this.grid = grid;
		this.learn = learn;
	}
	
	

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		
		if (reachedExit) return;
		Field fieldBeforeMove = getCurrentField();
		
		// Exit reached
		if (fieldBeforeMove.getType() == FieldType.EXIT) {
			this.reachedExit = true;
			return;
		}
		
		int x_beforeMove = myLocation().getX();
		int y_beforeMove = myLocation().getY();
		
		
		// Get the best possible action for the current field		
		Action bestAction = learn.getBestAction(x_beforeMove, y_beforeMove);
		move(bestAction); 		
		steps++;
		
		// Update the value of the previously visited field
		Field fieldAfterMove = getCurrentField();
		learn.update(bestAction, x_beforeMove, y_beforeMove, fieldAfterMove.getEffort(), 
				fieldAfterMove.getX(), fieldAfterMove.getY());
		
	}
	

	/**
	 * Move in one Direction
	 * 
	 * @param direction
	 * @return Return true, if moved 
	 */
	public void move(Action direction) {
		// Current Position
		NdPoint current = space.getLocation(this);
		int x = (int) Math.round(current.getX());
		int y = (int) Math.round(current.getY());
		
			
		switch(direction) {
			case NORTH:
				y++;
				break;
			case SOUTH:
				y--;
				break;
			case WEST:
				x--;
				break;
			case EAST:
				x++;
				break;
		}

		moveTo(x, y);
		
	
	}


	/**
	 * Move to place in grid and space
	 * 
	 * @param x
	 * @param y
	 */
	public void moveTo(int x, int y) {
		this.space.moveTo(this, x, y);
		this.grid.moveTo(this, x, y);
	}

	/**
	 * Gets the field object the runner is currently standing on
	 * 
	 * @return
	 */
	private Field getCurrentField() {	
		for(Object obj : grid.getObjectsAt(myLocation().getX(), myLocation().getY())) {
			if(obj instanceof Field) {
				return (Field) obj; 
			}
		}
		return null;
	}


	private boolean canMoveThere(int x, int y) {
		
		if ( x < 0 || x > 9 || y < 0 || y > 4) return false;
		if ( getFieldAt(x, y).getType() == FieldType.IMPASSABLE) return false;
	
		return true;		
		
	}
	
	/*
	 * Getters and Setters
	 */
	
	public ContinuousSpace<Object> getSpace() {
		return space;
	}


	/**
	 * Gets the field object the runner is currently standing on
	 * 
	 * @return
	 */
	private Field getFieldAt(int x, int y) {	
		for(Object obj : grid.getObjectsAt(x, y)) {
			if(obj instanceof Field) {
				return (Field) obj; 
			}
		}
		return null;
	}




	public void setSpace(ContinuousSpace<Object> space) {
		this.space = space;
	}




	private GridPoint myLocation() {
		return grid.getLocation(this);
	}



	public Grid<Object> getGrid() {
		return grid;
	}








	public void setGrid(Grid<Object> grid) {
		this.grid = grid;
	}








	public boolean hasReachedExit() {
		return reachedExit;
	}








	public void setReachedExit(boolean reachedExit) {
		this.reachedExit = reachedExit;
	}








	public QLearning getLearn() {
		return learn;
	}








	public void setLearn(QLearning learn) {
		this.learn = learn;
	}
	
	
	
	
	
	
}
