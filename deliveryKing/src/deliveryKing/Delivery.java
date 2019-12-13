package deliveryKing;

public class Delivery {

	private Customer customer;

	private boolean delivered;
	private boolean busy;
	
	public Delivery(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	
	
	
	
		
}
