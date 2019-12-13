package deliveryKing;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Customer {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private DeliveryCenter deliveryCenter;
	
	public Customer(ContinuousSpace<Object> space, Grid<Object> grid, DeliveryCenter deliveryCenter) {
		this.space = space;
		this.grid = grid;
		this.deliveryCenter = deliveryCenter;
	}
	
	@ScheduledMethod(start = 1, interval = 100)
	public void orderDelivery() {
		deliveryCenter.addDelivery(this);		
	}
	
}
