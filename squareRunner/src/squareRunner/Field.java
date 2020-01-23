package squareRunner;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Field {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	private int effort;

	public Field(ContinuousSpace<Object> space, Grid<Object> grid, int effort) {
		super();
		this.space = space;
		this.grid = grid;
		this.effort = effort;
	}

	public int getEffort() {
		return effort;
	}

	public void setEffort(int effort) {
		this.effort = effort;
	}
	
	
	
}
