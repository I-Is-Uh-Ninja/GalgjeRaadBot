package irc;

import org.jibble.pircbot.PircBot;

import start.StartBot;

public class RaadBotChatter extends PircBot {
	
	private static String channel = "#rsvier";
	private static boolean myTurn;
	private static String lastKnownWord;
	
	public RaadBotChatter(){
		this.setName("RaadBot");
		myTurn = false;
	}
	
	public RaadBotChatter(String channelName){
		this.setName("RaadBot");
		channel = channelName;
		myTurn = false;
	}
	
	public void onMessage(String channel, String sender,
            String login, String hostname, String message) {
		if (message.startsWith("!start ") && message.endsWith("raadbot")) {
			 String teRadenWoord = message.substring(6, message.indexOf("raadbot")).trim();
			 StartBot.handleRaden(teRadenWoord);
		}
		if (message.startsWith("!raadbot")){
			String teRadenWoord = message.substring(8).trim();
			StartBot.handleRaden(teRadenWoord);
		}
		
		if(message.contains("*")){
			lastKnownWord = message.replaceAll(" ", "").replaceAll("\\*", "-").trim();
		}
		if (message.contains("beurt") && sender.contains("GalgjeBot")){
			if(message.contains(this.getName())){
				myTurn = true;
				System.out.println("Hoera, het is mijn beurt!");
				String huidigWoord = lastKnownWord;
				System.out.println("Probeer iets te vinden wat lijkt op: " + huidigWoord);
				StartBot.raadEens(huidigWoord);
			}
			else{
				myTurn = false;
				System.out.println("Idling");
			}
		}
		if (message.contains("gewonnen") || message.contains("verloren")){
			StartBot.resetBot();
			if (message.contains(this.getName())){
				if(message.contains("gewonnen")){
					sendMessage("Hoera!");
				}
				else{
					sendMessage("Aww :(");
				}
			}
		}
	}
	
	public void sendMessage(String message){
		sendMessage(channel, message);
	}
}
