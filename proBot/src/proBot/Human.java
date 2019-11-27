/**
 * 
 */
package proBot;

import java.util.List;

import proBot.Robot;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

/**
 * 
 * @author benedikt
 *
 */
public class Human {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private int currentGoal = 0;
	private GridPoint[] goals;
	private boolean moved;
	
	/**
	 *  
	 * @param space
	 * @param grid
	 * @param goals Array of Gridpoints to visit
	 */
	public Human(ContinuousSpace<Object> space, Grid<Object> grid, GridPoint[] goals) {
		super();
		this.space = space;
		this.grid = grid;
		this.goals = goals;
	}
	

	@ScheduledMethod(start = 1, interval = 1)
	public void run() {		
		if (grid.getLocation(this).equals(goals[currentGoal])) {
			if (currentGoal < goals.length - 1) {
				currentGoal++;
				moveTowards(goals[currentGoal]);
				System.out.println("Next goal: "+ currentGoal);
			}
		} else {
			moveTowards(goals[currentGoal]);
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
			moved = true;
			//System.out.println((int) current.getX() +": " + (int) current.getY() + ": " + angle);
		}	
	}
	
}
