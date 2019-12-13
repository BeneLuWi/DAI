/**
 * 
 */
package proBot;

import java.util.ArrayList;
import java.util.List;

import proBot.Human;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

/**
 * @author benedikt
 *
 */
public class Robot {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	

	public Robot(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}

	@Watch(
			watcheeClassName = "proBot.Human", 
			watcheeFieldNames = "moved",
			query = "within_moore 5",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void step () {
		// get the grid location of this Robot
		GridPoint pt = grid.getLocation(this);
		
		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood .
		GridCellNgh<Human> nghCreator = new GridCellNgh<Human>(grid, pt, Human.class, 3, 3);
		
		List<GridCell<Human>> gridCells = nghCreator.getNeighborhood(true);
		for ( GridCell<Human> cell : gridCells ) {
			if ( cell.size() > 0 ) {
				moveTowards(cell.getPoint());
				break;
			}
		}

	}
	
	public void moveTowards(GridPoint pt) {
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint current = space.getLocation(this);
			NdPoint destination = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, current, destination);
			space.moveByVector(this,  1,  angle, 0);
			current = space.getLocation(this);
			grid.moveTo(this, (int) current.getX(), (int) current.getY());
			
		}	
	}

}
