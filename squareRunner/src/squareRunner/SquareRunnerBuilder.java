package squareRunner;


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

public class SquareRunnerBuilder implements ContextBuilder<Object>{

	@Override
	public Context build(Context<Object> context) {
		context.setId("squareRunner");
		ContinuousSpaceFactory spaceFactory = 
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace(
						"space",
						context, 
						new SimpleCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.StrictBorders(), 
						10, 
						5);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid(
				"grid", 
				context, 
				new GridBuilderParameters<Object>(
						new StrictBorders(), 
						new SimpleGridAdder<Object>(), 
						true, 
						10, 
						5));
	
		
		
		// Place Context-Objects to corresponding grid location
		for (Object obj: context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) Math.round(pt.getX()), (int) Math.round(pt.getY()));
		}
		
		
		
		/**********************
		 * 
		 * INIT FIELDS
		 * 
		 **********************/
		Field[][] fieldsArray = new Field[10][5];
		for (int x = 0; x < 10; x++) {
			for (int y  = 0; y < 5; y++) {
				fieldsArray[x][y] = new Field(space, grid, x, y);
			}
		}
		
		fieldsArray[0][3].setType(FieldType.SLIPPERY);
		fieldsArray[3][3].setType(FieldType.SLIPPERY);
		fieldsArray[3][4].setType(FieldType.SLIPPERY);
		fieldsArray[5][2].setType(FieldType.SLIPPERY);
		fieldsArray[6][1].setType(FieldType.SLIPPERY);
		fieldsArray[7][0].setType(FieldType.SLIPPERY);
		
		fieldsArray[1][0].setType(FieldType.TRAP);
		fieldsArray[2][2].setType(FieldType.TRAP);
		fieldsArray[8][3].setType(FieldType.TRAP);
		fieldsArray[9][1].setType(FieldType.TRAP);
		
		fieldsArray[3][1].setType(FieldType.IMPASSABLE);
		fieldsArray[3][2].setType(FieldType.IMPASSABLE);
		fieldsArray[6][4].setType(FieldType.IMPASSABLE);
		fieldsArray[7][3].setType(FieldType.IMPASSABLE);
		fieldsArray[7][4].setType(FieldType.IMPASSABLE);
		
		fieldsArray[8][4].setType(FieldType.EXIT);
		
		for (int x = 0; x < 10; x++) {
			for (int y  = 0; y < 5; y++) {
				context.add(fieldsArray[x][y]);
				space.moveTo(fieldsArray[x][y], x, y);
			}
		}
		
		/**********************
		 * 
		 * INIT QLEARNING
		 * 
		 **********************/
		
		QLearning learner = new QLearning(0.1, 0.9, fieldsArray);
		
		/**********************
		 * 
		 * INIT RUNNERS
		 * 
		 **********************/

		Runner runnerOne = new Runner(space, grid, learner);
		Runner runnerTwo = new Runner(space, grid, learner);
		
		context.add(runnerOne);
		context.add(runnerTwo);
		
		space.moveTo(runnerOne, 0, 0);
		space.moveTo(runnerTwo, 0, 4);
		
		RunnerOverseer overseer = new RunnerOverseer(runnerOne, runnerTwo);
		
		context.add(overseer);
		space.moveTo(overseer, 0,0);
		
		/**********************
		 * 
		 * PLACE ALL AGENTS
		 * 
		 **********************/
		
		for (Object obj: context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) Math.round(pt.getX()), (int) Math.round(pt.getY()));
		}
		
		return context;
	}
}
