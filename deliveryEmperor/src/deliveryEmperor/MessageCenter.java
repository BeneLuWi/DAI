package deliveryEmperor;

import java.util.ArrayList;

public class MessageCenter {

	ArrayList<FIPA_Message> messageList;
	
	//Das MessageCenter dient als Plattform zum Nachrichtenaustausch.
	
	public MessageCenter(){
		this.messageList = new ArrayList<FIPA_Message>();
	}
	
	public void addMessage(FIPA_Message message){
		this.messageList.add(message);
	}
	
	//Pr�ft, ob noch eine Nachricht verf�gbar ist.
	public boolean messagesAvailable(int receiver){
		for(FIPA_Message message: this.messageList){		
			if( message.getReceiver() == receiver ){
				return true;
			}
		}
		return false;
	}
	
	//Fragt genau eine Nachricht ab.
	public FIPA_Message getMessage(int receiver){
		for(FIPA_Message message: this.messageList){
			if( message.getReceiver() == receiver ){
				this.messageList.remove(message);
				return message;
			}
		}
		return null;
	}
	
	public void send(int sender, int receiver, FIPA_Performative performative, FIPA_Message_Content content){
		this.addMessage( new FIPA_Message(sender, receiver, performative, content) );
	}
	
	public void send(int sender, int receiver, FIPA_Performative performative){
		this.addMessage( new FIPA_Message(sender, receiver, performative, null) );
	}
	
	public void send(int sender, int receiver, FIPA_Performative performative, Delivery delivery, Double distance){
		FIPA_Message_Content content = new FIPA_Message_Content(delivery, distance);
		this.addMessage( new FIPA_Message(sender, receiver, performative, content) );
	}
	
	public void send(int sender, int receiver, FIPA_Performative performative, Double distance){
		FIPA_Message_Content content = new FIPA_Message_Content(distance);
		this.addMessage( new FIPA_Message(sender, receiver, performative, content) );
	}
	public void send(int sender, int receiver, FIPA_Performative performative, Delivery delivery){
		FIPA_Message_Content content = new FIPA_Message_Content(delivery);
		this.addMessage( new FIPA_Message(sender, receiver, performative, content) );
	}
	
}