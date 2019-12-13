package deliveryKing;

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
	
}
