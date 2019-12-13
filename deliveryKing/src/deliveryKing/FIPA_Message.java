package deliveryKing;


public class FIPA_Message {
	
	private int sender;
	private int receiver;
	private FIPA_Performative performative;
	private FIPA_Message_Content content;
	
	
	
	//Eine Nachricht besteht aus den folgenden 4 Parametern: 
	//Sender, Empf�nger, einem FIPA-Performative und einem Nachrichteninhalt.
	
	public FIPA_Message( int sender, int receiver, FIPA_Performative performative, FIPA_Message_Content content ){
		this.setPerformative( performative );
		this.setSender( sender );
		this.setReceiver( receiver );
		this.setContent( content );
	}
	
	private void setPerformative(FIPA_Performative performative){
		this.performative = performative;
	}
	
	public String getPerformative(){
		return this.performative.toString();
	}
	
	public int getSender(){
		return this.sender;
	};
	
	private void setSender(int sender){
		this.sender = sender;
	};
	
	
	public int getReceiver(){
		return this.receiver;
	};
	
	private void setReceiver(int receiver){
		this.receiver = receiver;
	}

	public FIPA_Message_Content getContent() {
		return content;
	}

	public void setContent(FIPA_Message_Content content) {
		this.content = content;
	};
	
	
	
}