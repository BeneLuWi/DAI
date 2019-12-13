package deliveryKing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Coordinator {
	private MessageCenter msgCenter;
	private DeliveryCenter deliveryCenter;
	private int id = 0;
	private int[] messengerIds = {1,2,3,4};
	private List<FIPA_Message> messages = new ArrayList<>();
	
	
	public Coordinator(MessageCenter msgCenter, DeliveryCenter deliveryCenter) {
		this.msgCenter = msgCenter;
		this.deliveryCenter = deliveryCenter;
	}


	@ScheduledMethod(start = 1, interval = 1)
	public void pickDelivery() {
		Optional<Delivery> optDelivery = deliveryCenter.getFirstUndelivered();
		
		if (!optDelivery.isPresent()) {
			return;
		} else {
			Delivery delivery = optDelivery.get();
			FIPA_Message_Content content = new FIPA_Message_Content(delivery);
			for (int msgId : messengerIds) {
				msgCenter.send(id, msgId, FIPA_Performative.CALL_FOR_PROPOSAL, content);
			}
		}
		
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void checkInvoice() {
		if (msgCenter.messagesAvailable(id)) {
			FIPA_Message msg = msgCenter.getMessage(id);
			FIPA_Message_Content content = msg.getContent();
			
			Delivery delivery = content.getDelivery();
			
			switch(msg.getPerformative()) {
				case REFUSE:
				case PROPOSE:
					messages.add(msg);
					checkMessages(delivery);	
					break;
				case INFORM:
					deliveryCenter.complete(delivery);
					break;
				default:
					break;
			}
		}
	}
	
	
	public void checkMessages(Delivery d) {
		
		List<FIPA_Message> msgs = messages
				.stream()
				.filter(m -> m.getContent().getDelivery() == d)
				.collect(Collectors.toList());
		
		if (msgs.size() < messengerIds.length) return;
		
		int refuseCount = 0;
		double minDistance = Double.MAX_VALUE;
		int minDistanceId = -1;
		
		for (FIPA_Message msg : msgs) {
			if (msg.getPerformative() == FIPA_Performative.PROPOSE && 
					msg.getContent().getDistance() < minDistance) {
				
				minDistance = msg.getContent().getDistance();
				minDistanceId = msg.getSender();
				
			} else if (msg.getPerformative() == FIPA_Performative.REFUSE) {
				refuseCount++;
			}			
		}
		
		if (refuseCount < messengerIds.length) {
			final int messengerId = minDistanceId;
			
			msgCenter.send(id, messengerId, FIPA_Performative.ACCEPT_PROPOSAL, d);
			msgs
				.stream()
				.filter(m -> m.getSender() != messengerId && m.getPerformative() == FIPA_Performative.PROPOSE)
				.collect(Collectors.toList())
				.forEach(m -> msgCenter.send(id, messengerId, FIPA_Performative.REJECT_PROPOSAL, d));
		} else {
			deliveryCenter.rejectDelivery(d);			
		}
		
		
	}
	
}













