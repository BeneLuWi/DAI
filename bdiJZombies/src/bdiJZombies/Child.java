package bdiJZombies;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Child {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	public Child(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
}
