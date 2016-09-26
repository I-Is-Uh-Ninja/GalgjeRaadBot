package start;

import java.io.IOException;

import org.jibble.pircbot.IrcException;

import ai.RaadBot;
import irc.RaadBotChatter;

public class StartBot {
	private static int fouten;
	private static boolean finished;
	private static StringBuffer huidigWoord;
	private static RaadBotChatter raadBotChatter;
	private static RaadBot raadBot;

	public static void main(String[] args) {
		raadBotChatter = new RaadBotChatter("#rsvier");
		raadBot = new RaadBot();
		try {
			raadBotChatter.connect("openirc.snt.utwente.nl");
			raadBotChatter.joinChannel("#rsvier");
		} catch (IOException | IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void raadEens(String huidig){
		huidigWoord = new StringBuffer(huidig);
		raadBot.setHuidigWoord(huidigWoord);
		raadBotChatter.sendMessage("!raad " + raadBot.handleRaden());
	}
	
	public static void resetBot(){
		raadBot = new RaadBot();
	}
	
	public static boolean handleRaden(String teRadenWoord){
		if(teRadenWoord.length() > 2 && teRadenWoord.length() <= 8){
			finished = false;
			teRadenWoord = teRadenWoord.trim();
			huidigWoord = new StringBuffer();
			System.out.println("Het woord is " + teRadenWoord);
			raadBot = new RaadBot();
			initializeWoord(teRadenWoord.length());
			raadBot.setHuidigWoord(huidigWoord);
			while(!finished && fouten < 10){
				raadBotChatter.sendMessage("Het huidige bekende woord is: " + huidigWoord.toString());
				raadBotChatter.sendMessage("Het aantal fouten is " + fouten + "/10.");
				String gok = raadBot.handleRaden();
				if(gok.length() > 1){
					raadBotChatter.sendMessage("Is het woord " + gok + "?");
					if(isGeraden(gok, teRadenWoord)){
						finished = true;
						raadBotChatter.sendMessage("Het was goed! RaadBot wint! :D");
					}
					else{
						raadBotChatter.sendMessage("Het was niet het goede woord :(");
						fouten++;
					}
				}
				else if(gok.length() == 1){
					raadBotChatter.sendMessage("Zit de letter " + gok + " in het woord?");
					if(zitLetterInWoord(gok.charAt(0), teRadenWoord)){
						raadBotChatter.sendMessage("De letter " + gok + " zit in het woord! :D");
						plaatsLetterInWoord(gok.charAt(0), teRadenWoord);
						raadBot.setHuidigWoord(huidigWoord);
						if(isGeraden(huidigWoord.toString(), teRadenWoord)){
							raadBotChatter.sendMessage("RaadBot heeft het woord geraden! :D");
							finished = true;
						}
					}
					else {
						raadBotChatter.sendMessage("De letter zit niet in het woord :(");
						fouten++;
					}
				}
				else{
					raadBotChatter.sendMessage("De bot is kapot D:");
					finished = true;
				}
			}
			if(fouten >= 10){
				fouten = 0;
				raadBotChatter.sendMessage("RaadBot heeft het woord niet kunnen raden T-T");
				return false;
			}
			else{
				fouten = 0;
				return true;
			}
		}
		else {
			raadBotChatter.sendMessage("Woord moet tussen de 3 en 8 karakters lang zijn!");
			return false;
		}
	}
	
	//Zit de letter in het woord om te raden?
    private static boolean zitLetterInWoord(char randomLetter, String teRadenWoord){
        String letter = "" + randomLetter;
        return teRadenWoord.contains(letter);
    }
    
    //Zet de letter in het voorlopige woord
    private static void plaatsLetterInWoord(char letter, String teRadenWoord){
        char[] lettersInWoord = teRadenWoord.toCharArray();
        for(int i = 0; i < teRadenWoord.length(); i++){
            if(lettersInWoord[i] == letter){
                String letterToString = "" + letter;
                huidigWoord.replace(i, i+1, letterToString);
            }
        }
    }
    
    private static boolean isGeraden(String gegoktWoord, String teRadenWoord){
        return gegoktWoord.equalsIgnoreCase(teRadenWoord);
    }
    
    private static void initializeWoord(int lengte){
        for(int i = 0; i < lengte; i++){
            huidigWoord.append("-");
        }
    }

}
