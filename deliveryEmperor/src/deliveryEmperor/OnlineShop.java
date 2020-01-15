package deliveryEmperor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import repast.simphony.engine.schedule.ScheduledMethod;

public class OnlineShop {

	private MessageCenter msgCenter;
	private DeliveryCenter deliveryCenter;
	private int id = 0;
	private List<Messenger> messengers;
	private List<Customer> customers;
	private List<FIPA_Message> messages = new ArrayList<>();
	private Map<String, Trust> companyTrust;
	private boolean negotiating;

	public OnlineShop(MessageCenter msgCenter, DeliveryCenter deliveryCenter, List<Messenger> messengers, List<Customer> customers){
		this.msgCenter = msgCenter;
		this.deliveryCenter = deliveryCenter;
		this.negotiating = false;
		this.messengers = messengers;
		this.customers = customers;
		companyTrust = new HashMap<String, Trust>();
		messengers.forEach(messenger ->
			companyTrust.put(messenger.getCompany(), new Trust(messenger.getHomeWarehouse()))
		);
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void pickDelivery() {
		if (negotiating) return;
		
		Optional<Delivery> optDelivery = deliveryCenter.getFirstUndelivered();
		
		if (!optDelivery.isPresent()) {
			return;
		} else {
			System.out.println("Start negotiation");
			negotiating = true;
			Delivery delivery = optDelivery.get();
			FIPA_Message_Content content = new FIPA_Message_Content(delivery);
			
			messengers.forEach(messenger ->
				msgCenter.send(id, messenger.getId(), FIPA_Performative.CALL_FOR_PROPOSAL, content)
			);
			
		}
		
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void checkInvoice() {
		if (msgCenter.messagesAvailable(id)) {
			FIPA_Message msg = msgCenter.getMessage(id);
			FIPA_Message_Content content = msg.getContent();
			int messengerId = msg.getSender();			
			
			Delivery delivery = content.getDelivery();
			
			switch(msg.getPerformative()) {
				case REFUSE:
				case PROPOSE:
					messages.add(msg);
					checkMessages(delivery);	
					break;
				case INFORM:
					printTrust();
					deliveryCenter.complete(delivery);
					companyTrust.get(getMessengerById(messengerId).getCompany()).addSuccess();
					printTrust();
					break;
				case FAILURE:
					printTrust();
					deliveryCenter.complete(delivery);
					companyTrust.get(getMessengerById(messengerId).getCompany()).addFailure();
					printTrust();
					break;
				default:
					break;
			}
		}
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void orderDelivery() {
		if (!deliveryCenter.deliveriesInQueue()) {
			customers.forEach(customer -> deliveryCenter.addDelivery(customer));		
		}
	}

	public void checkMessages(Delivery d) {
		List<FIPA_Message> msgs = messages
				.stream()
				.filter(m -> m.getContent().getDelivery() == d)
				.collect(Collectors.toList());
		
		// If not all messengers answered yet, return
		if (msgs.size() < messengers.size()) return;
		
		int refuseCount = 0;
		double minPriceTrustRatio = Double.MAX_VALUE;
		int bestMessengerId = -1;
		
		for (FIPA_Message msg : msgs) {
			double trust = getTrustByMessengerId(msg.getSender());		
			double price = msg.getContent().getDistance();
			double priceTrustRatio = price - (price * trust * 0.8);
			
			if (msg.getPerformative() == FIPA_Performative.PROPOSE &&
					priceTrustRatio < minPriceTrustRatio) {
				
				minPriceTrustRatio = priceTrustRatio;
				bestMessengerId = msg.getSender();
				
			} else if (msg.getPerformative() == FIPA_Performative.REFUSE) {
				refuseCount++;
			}			
		}
		
		if (refuseCount < messengers.size()) {
			final int messengerId = bestMessengerId;
			
			msgCenter.send(id, messengerId, FIPA_Performative.ACCEPT_PROPOSAL, d);
			msgs
				.stream()
				.filter(m -> m.getSender() != messengerId && m.getPerformative() == FIPA_Performative.PROPOSE)
				.collect(Collectors.toList())
				.forEach(m -> msgCenter.send(id, m.getSender(), FIPA_Performative.REJECT_PROPOSAL, d));
			
			System.out.println("End negotiation: Send " + messengerId + " " + getMessengerById(messengerId).getCompany());
			
		} else {
			deliveryCenter.rejectDelivery(d);
			System.out.println("End negotiation: Reject");
		}
		
		// Remove all messages related to the requested Delivery
		messages = messages
				.stream()
				.filter(m -> m.getContent().getDelivery() != d)
				.collect(Collectors.toList());
		
		
		negotiating = false;
	}
	
	private Messenger getMessengerById(int id) {
		return this.messengers.stream().filter(messenger -> messenger.getId() == id).findAny().orElse(null);
	}
	
	private double getTrustByMessengerId(int messengerId) {
		return companyTrust.get(getMessengerById(messengerId).getCompany()).getTrustValue();
	}
	
	private void printTrust() {
		companyTrust.values().forEach(trust -> 
			System.out.println("Trust for: " + trust.getCompany().getCompanyName() + ": " + trust.getTrustValue())
		);
		
	}
	
}
