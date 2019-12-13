package deliveryKing;

import java.util.Optional;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Coordinator {
	private MessageCenter msgCenter;
	private DeliveryCenter deliveryCenter;
	private int id = 0;
	private int[] messengerIds = {1,2,3,4};
	
	
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
	
}
