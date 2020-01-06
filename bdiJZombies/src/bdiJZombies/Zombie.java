/**
 * 
 */
package bdiJZombies;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
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
import repast.simphony.context.Context;

/**
 * @author benedikt
 *
 */
public class Zombie {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private boolean moved;

	public Zombie(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step () {
		// get the grid location of this Zombie
		GridPoint pt = grid.getLocation(this);
		
		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood .
		GridCellNgh<Human> nghCreator = new GridCellNgh<Human>(grid, pt, Human.class, 1, 1);
		
		List<GridCell<Human>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		GridPoint pointWithMostHumans = null ;
		int maxCount = -1;
		for ( GridCell<Human> cell : gridCells ) {
			if ( cell.size() > maxCount ) {
				pointWithMostHumans = cell.getPoint ();
				maxCount = cell.size(); 
			}
		}
		moveTowards(pointWithMostHumans);
		infect();
	}
	
	public void moveTowards(GridPoint pt) {
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint current = space.getLocation(this);
			NdPoint destination = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, current, destination);
			space.moveByVector(this,  1,  angle, 0);
			current = space.getLocation(this);
			grid.moveTo(this, (int) current.getX(), (int) current.getY());
			
			moved = true;
		}	
	}
	
	public void infect() {
		List<Object> humans = new ArrayList<Object>();
		for(Object obj : grid.getObjectsAt(myLocation().getX(), myLocation().getY())) {
			if(obj instanceof Human) {
				humans.add(obj);
			}
		}
		
		if(humans.size() > 0) {
			int index = RandomHelper.nextIntFromTo(0, humans.size()-1);
			Object obj = humans.get(index);
			NdPoint spacePt = space.getLocation(obj);
			Context<Object> context = ContextUtils.getContext(obj);
			context.remove(obj);
			Zombie zombie = new Zombie(space, grid);
			context.add(zombie);
			space.moveTo(zombie, spacePt.getX(), spacePt.getY());
			grid.moveTo(zombie, myLocation().getX(), myLocation().getY());
			
			Network<Object> net = (Network<Object>)context.getProjection("infection network");
			net.addEdge(this, zombie);
		}
	}
	
	private GridPoint myLocation() {
		return grid.getLocation(this);
	}
	

}
