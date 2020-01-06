package bdiJZombies;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Material {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	public Material(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
	
	
}
