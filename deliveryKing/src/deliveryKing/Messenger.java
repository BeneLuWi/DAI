package deliveryKing;

import repast.simphony.engine.schedule.ScheduledMethod;
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
	private int id;
	private MessageCenter msgCenter;
	private GridPoint goal;
	
	
	public Messenger(ContinuousSpace<Object> space, Grid<Object> grid, Warehouse homeWarehouse, MessageCenter msgCenter, int id) {
		this.space = space;
		this.grid = grid;
		this.homeWarehouse = homeWarehouse;
		this.inWarehouse = true;
		this.id = id;
		this.msgCenter = msgCenter;
	}
	
	
	@ScheduledMethod(start = 1, interval = 1)
	public void checkInvoice() {
		if (msgCenter.messagesAvailable(id)) {
			FIPA_Message msg = msgCenter.getMessage(id);
			
			switch(msg.getPerformative()) {
				case CALL_FOR_PROPOSAL:
					if(inWarehouse) {
						Customer c = msg.getContent().getDelivery().getCustomer();						
						msgCenter.send(id, 0, FIPA_Performative.PROPOSE, distanceToLocation(grid.getLocation(c)));
					} else {
						msgCenter.send(id, 0, FIPA_Performative.REFUSE, msg.getContent().getDelivery());
					}					
					break;
					
				case ACCEPT_PROPOSAL:
					Customer c = msg.getContent().getDelivery().getCustomer();
					inWarehouse = false;
					goal = grid.getLocation(c);
					break;
					
				case REJECT_PROPOSAL:
					// Nothing happens
					break;
					
				default:
					break;
			
			}
			
			
			
		}
		
		
	}
	
	
	@ScheduledMethod(start = 1, interval = 1)
	public void walk() {
		if (grid.getLocation(this).equals(grid.getLocation(homeWarehouse))) {
			inWarehouse = true;
		}
		
		if (grid.getLocation(this).equals(goal) && !inWarehouse) {
			// Package Delivered
			msgCenter.send(id, 0, FIPA_Performative.INFORM);
			goal = grid.getLocation(homeWarehouse);
			moveTowards(goal);
		}
			
	}
	
	public double distanceToLocation(GridPoint dest) {
		NdPoint current = space.getLocation(this);
		NdPoint destination = new NdPoint(dest.getX(), dest.getY());
		return space.getDistance(current, destination);
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
