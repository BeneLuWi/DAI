package bdiJZombies;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Trap {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	public Trap(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
	
}
