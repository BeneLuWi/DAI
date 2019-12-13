package deliveryKing;

public class FIPA_Message_Content {
	private double distance;
	private Delivery delivery;
	
	
	
	public FIPA_Message_Content(Delivery delivery, double distance) {
		this.distance = distance;
		this.delivery = delivery;
	}

	public FIPA_Message_Content(double distance) {
		this.distance = distance;
	}
	
	public FIPA_Message_Content(Delivery delivery) {
		this.delivery = delivery;
	}
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public Delivery getDelivery() {
		return delivery;
	}
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
	
	
}
