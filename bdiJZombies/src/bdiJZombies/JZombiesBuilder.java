/**
 * 
 */
package bdiJZombies;

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
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;

/**
 * @author benedikt
 *
 */
public class JZombiesBuilder implements ContextBuilder<Object>{

	@Override
	public Context build(Context<Object> context) {
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("infection network", context, true);
		netBuilder.buildNetwork();
		
		context.setId("bdiJZombies");
		ContinuousSpaceFactory spaceFactory = 
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace(
						"space", 
						context, 
						new RandomCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.StrictBorders(), 
						50, 
						50);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid(
				"grid", 
				context, 
				new GridBuilderParameters<Object>(
						new StrictBorders(), 
						new SimpleGridAdder<Object>(), 
						true, 
						50, 50));
		
		int zombieCount = 10;
		int humanCount = 5;
		int materialCount = 150;
		int childCount = 40;
		
		context.add(new Overseer());
		
		for (int i = 0; i < zombieCount; i++) {
			context.add(new Zombie(space, grid));
		}
		
		for (int i = 0; i < childCount; i++) {
			context.add(new Child(space, grid));
		}
		
		for (int i = 0; i < materialCount; i++) {
			context.add(new Material(space, grid));
		}
		
		for (int i = 0; i < humanCount; i++) {
			int energy = RandomHelper.nextIntFromTo(4,  10);			
			context.add(new Human(space, grid, energy));
		}

		for (Object obj: context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}
		
		return context;
	}

}
