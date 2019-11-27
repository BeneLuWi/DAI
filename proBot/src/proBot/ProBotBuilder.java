/**
 * 
 */
package proBot;

import proBot.Human;
import proBot.Robot;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;
import repast.simphony.space.grid.WrapAroundBorders;

/**
 * @author benedikt
 *
 */
public class ProBotBuilder implements ContextBuilder<Object>{

	@Override
	public Context build(Context<Object> context) {
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("infection network", context, true);
		netBuilder.buildNetwork();
		
		context.setId("proBot");
		ContinuousSpaceFactory spaceFactory = 
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace(
						"space", 
						context, 
						new SimpleCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.StrictBorders(), 
						49, 
						49);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid(
				"grid", 
				context, 
				new GridBuilderParameters<Object>(
						new StrictBorders(), 
						new SimpleGridAdder<Object>(), 
						true, 
						49, 49));
	
		Robot robot = new Robot(space, grid);
		context.add(robot);
		space.moveTo(robot, 4.0, 4.0);
		
		// Grid Point (10,35) creates an infinite loop (agent bouncing between (9,35) - (10,34).
		// Therefore we changed that point
		GridPoint[] goals = {new GridPoint(10, 15), new GridPoint(12, 37), new GridPoint(30, 45), new GridPoint(5, 5)};
		
		Human human = new Human(space, grid, goals);
		context.add(human);
		space.moveTo(human, 4.9, 5.0);
		
		for (Object obj: context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}
		
		return context;
	}
}
