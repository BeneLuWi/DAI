package deliveryKing;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Warehouse {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	public Warehouse(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
	
	
}
