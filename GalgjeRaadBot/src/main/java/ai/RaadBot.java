package ai;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RaadBot {
	private static File woordenlijst;
    private static final String ALFABET = "abcdefghijklmnopqrstuvwxyz";
    private StringBuffer woord;
    private String geradenLetters = "";
    private List<String> alGeradenWoorden;
    private double[] letterChances = {7.49, 1.58, 1.24, 5.93, 18.91, 0.81, 3.40, 2.38, 6.50, 1.46, 2.25, 3.57, 2.21, 10.03, 6.06, 1.57, 0.01, 6.41, 3.73, 6.79, 1.99, 2.85, 1.52, 0.04, 0.034, 1.39};

    public RaadBot() {
        woordenlijst = new File("woordenlijst.txt");
        woord = new StringBuffer();
        alGeradenWoorden = new ArrayList<>();
    }
    
    public void setHuidigWoord(StringBuffer huidigWoord){
    	this.woord = huidigWoord;
    }
     
    String raadWoord(String woord){
        try(Scanner inputWoorden = new Scanner(woordenlijst)){
        	String woordPattern = woord.toLowerCase().replaceAll("-", ".");
            String patternString = "\\b" + woordPattern + "\\b";
            Pattern pattern = Pattern.compile(patternString);
            //System.out.println("Pattern: " + pattern.pattern());
            String gevondenWoord = "";
            boolean finished = false;
            while(inputWoorden.hasNext() && !finished){
                if(inputWoorden.hasNext(pattern)){
                    String currentWoord = inputWoorden.next();
                    if(!alGeradenWoorden.contains(currentWoord)){
                        gevondenWoord = currentWoord;
                        alGeradenWoorden.add(currentWoord);
                        finished = true;
                    }
                }
                inputWoorden.nextLine();
            }
            return gevondenWoord;
        }
        catch (FileNotFoundException ex) {
            System.err.println("Kon bestand niet vinden: " + ex.getMessage());
        }
        return "";
    }
    
    public String handleRaden(){
        char[] lettersInWoord = woord.toString().toCharArray();
        int amountOfBlanks = 0;
        for(char c : lettersInWoord){
            if(c=='-'){
                amountOfBlanks++;
            }
        }
        if(amountOfBlanks==1 || (amountOfBlanks==2 && woord.length()>6)){
            return handleRaadWoord();
        }
        else {
            return handleRaadLetter();
        }
    }
    
    private String handleRaadWoord(){
    	//kijk of het woord tot nu toe overeenkomt met een bekend woord
        String geradenWoord = raadWoord(woord.toString());
        //zo niet, raad een letter
        if(geradenWoord.isEmpty()){
            return handleRaadLetter();
        }
        //anders return het woord om te raden
        else{
            return geradenWoord;
        }
    }
    
    private String handleRaadLetter(){
        char randomLetter = genereerLetter();
        String letter = "" + randomLetter;
        while(geradenLetters.contains(letter)){
            randomLetter = genereerLetter();
            letter = "" + randomLetter;
        }
        geradenLetters += letter;
        return letter;
    }
    
    public void addToGeradenLetters(char letter){
    	geradenLetters += letter;
    }
    
    public void addToGeradenWoorden(String woord){
    	alGeradenWoorden.add(woord);
    }
    
    private char genereerLetter(){
        double percentage = Math.random() * 100;
        //System.out.println("Percentile: " + percentage);
        double letterChance = 0.00;
        char letter = 0;
        for (int i = 0; i < letterChances.length; i++){
            double max = letterChance + letterChances[i];
            //System.out.println("Character '" + ALFABET.charAt(i) + "' is between " + letterChance + " and " + max);
            if(percentage >= letterChance && percentage <= max){
                letter = ALFABET.charAt(i);
                break;
            }
            letterChance += letterChances[i];
        }
        return letter;
    }
    //Deprecated
    private char raadLetter(){
        int charIndex = (int)(Math.random() * 26) - 1;
        char[] letters = ALFABET.toCharArray();
        return letters[charIndex];
    }
    
    
}
