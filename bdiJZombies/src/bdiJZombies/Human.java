/**
 * 
 */
package bdiJZombies;

import java.util.ArrayList;
import java.util.List;

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
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

/**
 * @author Benedikt
 *
 */
public class Human {
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private int energy, startingEnergy;
	public Human(ContinuousSpace<Object> space, Grid<Object> grid, int energy) {
		super();
		this.space = space;
		this.grid = grid;
		this.energy = startingEnergy = energy;
	}
	private int materialCount;
	private boolean running;
	private Child child;
	
	@Watch(
			watcheeClassName = "bdiJZombies.Zombie", 
			watcheeFieldNames = "moved",
			query = "within_moore 4",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void run() {
		GridPoint pt = grid.getLocation(this);
		
		GridCellNgh<Zombie> nghCreator = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 1, 1);
		List<GridCell<Zombie>> gridCells = nghCreator.getNeighborhood(true);
		
		GridPoint pointWithLeastZombies = null;
		int minCount = Integer.MAX_VALUE;
		for (GridCell<Zombie> cell: gridCells) {
			if (cell.size() < minCount) {
				pointWithLeastZombies = cell.getPoint();
				minCount = cell.size();
			}
		}
		
		GridCellNgh<Child> nghCreatorChildren = new GridCellNgh<Child>(grid, pt, Child.class, 4, 4);
		List<GridCell<Child>> gridCellsChildren = nghCreatorChildren.getNeighborhood(true);
		
		GridPoint pointWithChild = null ;
		int maxCount = 0;
		for ( GridCell<Child> cell : gridCellsChildren ) {
			if ( cell.size() > maxCount ) {
				pointWithChild = cell.getPoint ();
				maxCount = cell.size(); 
			}
		}
		
		if(energy > 0) {
			if(pointWithChild == null) {
				moveTowards(pointWithLeastZombies, 2);
			} else {
				moveTowards(pointWithChild, 1);
			}
			
		} else {
			energy = startingEnergy;
		}
	
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step () {

		GridPoint pt = grid.getLocation(this);
		
		// Check if we pick up the child
		if (isZombieClose() && child == null) {
			List<Object> children = new ArrayList<Object>();
			for(Object obj : grid.getObjectsAt(myLocation().getX(), myLocation().getY())) {
				if(obj instanceof Child) {
					children.add(obj);
				}
			}
			if (children.size() > 0) {
				child = (Child) children.get(0);				
			}
		} else if (!isZombieClose() && child != null) {
			child = null;
		}
		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood .
		GridCellNgh<Material>nghCreator = new GridCellNgh<Material>(grid, pt, Material.class, 4, 4);
		
		List<GridCell<Material>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		GridPoint pointWithMaterial = null ;
		int maxCount = -1;
		for ( GridCell<Material> cell : gridCells ) {
			if ( cell.size() > maxCount ) {
				pointWithMaterial= cell.getPoint ();
				maxCount = cell.size(); 
			}
		}
		if(energy > 0) {
			moveTowards(pointWithMaterial, 1);
		} else {
			energy = startingEnergy;
		}
		collect();
		if (materialCount >= 3) craft();
	}
	
	public void collect() {
		List<Object> materials = new ArrayList<Object>();
		for(Object obj : grid.getObjectsAt(myLocation().getX(), myLocation().getY())) {
			if(obj instanceof Material) {
				materials.add(obj);
			}
		}
		
		materials.forEach(material -> {
			NdPoint spacePt = space.getLocation(material);
			Context<Object> context = ContextUtils.getContext(material);
			context.remove(material);
			materialCount++;
		});
		
	}
	
	public void craft() {
		materialCount = 0;
		Trap trap= new Trap(space, grid);
		Context<Object> context = ContextUtils.getContext(this);
		context.add(trap);
		NdPoint spacePt = space.getLocation(this);
		space.moveTo(trap, spacePt.getX(), spacePt.getY());
		grid.moveTo(trap, myLocation().getX(), myLocation().getY());
		System.out.println("Setting Trap at (" + myLocation().getX() + ", " + myLocation().getY() + ")");
	}
	
	public void moveTowards(GridPoint pt, int distance) {
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint current = space.getLocation(this);
			int x = pt.getX() <= 0 ? 2 : pt.getX();
			x = x >= grid.getDimensions().getWidth() ? x - 2 : x;
			
			int y = pt.getY() <= 0 ? 2 : pt.getY();
			y = y >= grid.getDimensions().getHeight() ? y - 2 : y;
					
			NdPoint destination = new NdPoint(x,y);
			double angle = SpatialMath.calcAngleFor2DMovement(space, current, destination);
			space.moveByVector(this,  distance,  angle, 0);
			current = space.getLocation(this);
			grid.moveTo(this, (int) Math.round(current.getX()), (int) Math.round(current.getY()));
			energy--;
			
			if (child != null) {
				space.moveTo(child, current.getX(), current.getY());
				grid.moveTo(child, myLocation().getX(), myLocation().getY());
			}
			
			
		}	
	}
	
	private boolean isZombieClose() {
		GridPoint pt = grid.getLocation(this);
		
		GridCellNgh<Zombie> nghCreator = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 4, 4);
		List<GridCell<Zombie>> gridCells = nghCreator.getNeighborhood(true);
		
		for (GridCell<Zombie> cell: gridCells) {
			if (cell.size() > 0) {
				return true;
			}
		}
		return false;
	}
	
	private GridPoint myLocation() {
		return grid.getLocation(this);
	}
	
	
}