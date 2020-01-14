package deliveryEmperor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeliveryCenter {

	private List<Delivery> deliveries = new ArrayList<>();
	
	public void addDelivery(Customer customer) {
		this.deliveries.add(new Delivery(customer));
	}
	
	public Optional<Delivery> getFirstUndelivered() {
		Optional<Delivery> delivery = deliveries
											.stream()
											.filter(d -> !d.isDelivered() && !d.isBusy() )
											.collect(Collectors.toList())
											.stream()
											.findAny();
		
		if (delivery.isPresent()) delivery.get().setBusy(true);
		return delivery;
	}
	
	// Increase number of rejections for delivery
	public void rejectDelivery(Delivery delivery) {
		for (Delivery d: deliveries) {
			if (d == delivery) {
				// Puts delivery back into queue if all messengers reject
				d.setBusy(false);
			}
		}
		
	}
	
	// Increase number of rejections for delivery
	public void complete(Delivery delivery) {
		for (Delivery d: deliveries) {
			if (d == delivery) {
				// Puts delivery back into queue if all messengers reject
				d.setDelivered(true);
				
				int count = (int) deliveries
						.stream()
						.filter(del -> !del.isDelivered() )
						.count();
				
				System.out.println("Package delivered " + count + " to go" );
				
			}
		}
		
	}
	
}
