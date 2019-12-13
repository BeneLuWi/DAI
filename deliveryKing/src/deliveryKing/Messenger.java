package deliveryKing;

import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Messenger {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private boolean inWarehouse;
	private Warehouse homeWarehouse;

	
	
	public Messenger(ContinuousSpace<Object> space, Grid<Object> grid, Warehouse homeWarehouse) {
		super();
		this.space = space;
		this.grid = grid;
		this.homeWarehouse = homeWarehouse;
		this.inWarehouse = true;
	}
	
	
	
	
	public void moveTowards(GridPoint pt) {
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint current = space.getLocation(this);
			NdPoint destination = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, current, destination);
			space.moveByVector(this,  1,  angle, 0);
			current = space.getLocation(this);
			grid.moveTo(this, (int) Math.round(current.getX()), (int) Math.round(current.getY()));
		}	
	}
	
}
