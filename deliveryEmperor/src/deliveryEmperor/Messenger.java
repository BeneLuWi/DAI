package deliveryEmperor;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
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
	
	private GridPoint goal = null;
	private Delivery currentDelivery;
	private List<Delivery> deliveryq = new ArrayList<>();
	
	
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
					Delivery d = msg.getContent().getDelivery();
					Customer customer = d.getCustomer();
					msgCenter.send(id, 0, FIPA_Performative.PROPOSE, d, distanceToLocation(grid.getLocation(customer)));				
					break;
				case ACCEPT_PROPOSAL:
					deliveryq.add(msg.getContent().getDelivery());
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
		// No current goal
		if (goal == null) {
			nextDelivery();
			return;
		}
		
		if (!grid.getLocation(this).equals(goal)) {
			moveTowards(goal);
		}
		
		else if (grid.getLocation(this).equals(goal) && !inWarehouse) {
			
			// Check if delivery was a success
			// Package Delivered successfully
			if(homeWarehouse.getSuccess() > RandomHelper.nextIntFromTo(0, 100)) {
				msgCenter.send(id, 0, FIPA_Performative.INFORM, currentDelivery);
				currentDelivery = null;
			} else {
				msgCenter.send(id, 0, FIPA_Performative.FAILURE, currentDelivery);
				currentDelivery = null;
			}
			
			goal = grid.getLocation(homeWarehouse);
			moveTowards(goal);
		}
		
		if (grid.getLocation(this).equals(grid.getLocation(homeWarehouse))) {
			inWarehouse = true;
			nextDelivery();
		}
			
	}
	
	public double distanceToLocation(GridPoint dest) {
		NdPoint current = space.getLocation(homeWarehouse);
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
	
	private void nextDelivery() {
		if (deliveryq.size() > 0) {
			currentDelivery = deliveryq.get(0);
			deliveryq.remove(0);
			goal = grid.getLocation(currentDelivery.getCustomer());
			inWarehouse = false;
		}
	}
	
	public String getCompany() {
		return this.homeWarehouse.getCompanyName();
	}
	
	public boolean isInWarehouse() {
		return inWarehouse;
	}


	public void setInWarehouse(boolean inWarehouse) {
		this.inWarehouse = inWarehouse;
	}


	public Warehouse getHomeWarehouse() {
		return homeWarehouse;
	}


	public void setHomeWarehouse(Warehouse homeWarehouse) {
		this.homeWarehouse = homeWarehouse;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public MessageCenter getMsgCenter() {
		return msgCenter;
	}


	public void setMsgCenter(MessageCenter msgCenter) {
		this.msgCenter = msgCenter;
	}


	public GridPoint getGoal() {
		return goal;
	}


	public void setGoal(GridPoint goal) {
		this.goal = goal;
	}


	public Delivery getDelivery() {
		return currentDelivery;
	}


	public void setDelivery(Delivery delivery) {
		this.currentDelivery = delivery;
	}
	
	
	
}
