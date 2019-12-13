package deliveryKing;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;

public class DeliveryKingBuilder  implements ContextBuilder<Object>{

	@Override
	public Context build(Context<Object> context) {
		context.setId("deliveryKing");
		ContinuousSpaceFactory spaceFactory = 
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace(
						"space", 
						context, 
						new SimpleCartesianAdder<Object>(), 
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
	
				
		// Initialize Customers and place them in Space and Grid
		double[] x = {15, 5, 40, 35, 40, 10};
		double[] y = {10, 35, 10, 45, 35, 35};
		for (int i = 0; i < 6; i++) {
			
			Customer customer = new Customer(space, grid);
			context.add(customer);
			space.moveTo(customer, x[i], y[i]);
		}
		
		// Initialize Warehouses and Messengers and place them in Space and Grid
		double[] wx = {2, 45, 5, 45};
		double[] wy = {5, 5, 25, 45};
		for (int i = 0; i < 4; i++) {
			
			Warehouse warehouse = new Warehouse(space, grid);
			context.add(warehouse);
			space.moveTo(warehouse, wx[i], wy[i]);
			
			Messenger messenger = new Messenger(space, grid, warehouse);
			context.add(messenger);
			space.moveTo(messenger, wx[i], wy[i]);
			
		}
		
		// Place Context-Objects to corresponding grid location
		for (Object obj: context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) Math.round(pt.getX()), (int) Math.round(pt.getY()));
		}
		
		
		return context;
	}
	
}
