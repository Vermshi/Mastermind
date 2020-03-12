package backend;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;



@Path("/communicator")
public class Communicator {
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String mainCommunicator(@QueryParam("Symbol1") String s1, @QueryParam("Symbol2") String s2, @QueryParam("Symbol3") String s3, @QueryParam("Symbol4") String s4, @QueryParam("gameState") String gameState, @QueryParam("gameNumber") int gameNumber){
		String errorString = "<p style=\"color:red;\">The symbols doesnt match or one of the fields are empty</p>";
		if(gameNumber == 0) {
			//The GameState starts with our random combination (not so smart since it might be found client side by tech savy users)
			Combination randComb = new Combination();
			randComb.genRandomCombination();
			gameState= randComb.toString();
			errorString = "";
			
		}
		
		String guessedStateString = s1 + s2 + s3 + s4;
		guessedStateString = guessedStateString.toUpperCase();
		// decided to return the html from a java class to save time, but its not a good solution
		if(isLegalChars(guessedStateString) == false) {
			
			
			ArrayList<Combination> gameStateCombinations = stringArrayToCombinations(gameState);
			String stateOutput = "";
			Combination hiddenCombination = new Combination();
			int k = 0;
			for(Combination c: gameStateCombinations) {
				if(k==0) {
					hiddenCombination = c;
				}else {
					stateOutput += "<br>" + c.toStringWithMatchingFlags(hiddenCombination);
				}
				k++;
			}
		String resource =  "<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<body>\r\n" + 
				"\r\n" + 
				errorString + 
				"<h1>Master Mind</h1>\r\n" + 
				"<h4>A random combination of four letters have been set. You are going to guess a combination and will get a \"X\" if the combination cointains one or more\r\n" + 
				"  correct letter, or an \"O\" contains one or more right letter and at least one is in the same position as the chosen combination. The letters available are A, B, C, D, E and F. </h4>\r\n" + 
				"\r\n" + 
				"<form action=\"http://localhost/MasterMind/rest/communicator\" method=\"GET\">\r\n" + 
				"<input type=\"hidden\" id=\"gameState\" name=\"gameState\" value=\""+gameState+"\">" +
				"<input type=\"hidden\" id=\"gameNumber\" name=\"gameNumber\" value=\""+gameNumber+"\">" +
				"  <label for=\"Symbol1\">First Letter:</label><br>\r\n" + 
				"  <input type=\"Symbol1\" id=\"Symbol1\" name=\"Symbol1\" value=\"\"><br><br>\r\n" + 
				"  <label for=\"Symbol2\">Second Letter:</label><br>\r\n" + 
				"  <input type=\"Symbol2\" id=\"Symbol2\" name=\"Symbol2\" value=\"\"><br><br>\r\n" + 
				"  <label for=\"Symbol3\">Third Letter:</label><br>\r\n" + 
				"  <input type=\"Symbol3\" id=\"Symbol3\" name=\"Symbol3\" value=\"\"><br><br>\r\n" + 
				"  <label for=\"Symbol4\">Fourth Letter:</label><br>\r\n" + 
				"  <input type=\"Symbol4\" id=\"Symbol4\" name=\"Symbol4\" value=\"\"><br><br>\r\n" + 
				"  <input type=\"submit\" value=\"Guess\">\r\n" + 
				"</form>\r\n" + 
				"\r\n" + 
				"<h1> Guesses: </h1>\r\n" + 
				"\r\n" + gameNumber +
				stateOutput + 
				"</body>\r\n" + 
				"</html>";
		return resource;
		}
		else {
			//If you are here the guess should be legal
			Combination guessedCombination = stringToCombination(guessedStateString);
			
			gameState= gameState + guessedCombination.toString();
			ArrayList<Combination> gameStateCombinations = stringArrayToCombinations(gameState);
			String stateOutput = "";
			Combination hiddenCombination = new Combination();
			int k = 0;
			for(Combination c: gameStateCombinations) {
				if(k==0) {
					hiddenCombination = c;
				}else {
					stateOutput += "<br>" + c.toStringWithMatchingFlags(hiddenCombination);
				}
				k++;
			}
			gameNumber+= 1;
			String resource =  "<!DOCTYPE html>\r\n" + 
					"<html>\r\n" + 
					"<body>\r\n" + 
					"\r\n" + 
					"<h1>Master Mind</h1>\r\n" + 
					"<h4>A random combination of four letters have been set. You are going to guess a combination and will get a \"X\" if the combination cointains one or more\r\n" + 
					"  correct letter, or an \"O\" contains one or more right letter and at least one is in the same position as the chosen combination. The letters available are A, B, C, D, E and F. </h4>\r\n" + 
					"\r\n" + 
					"<form action=\"http://localhost/MasterMind/rest/communicator\" method=\"GET\">\r\n" + 
					"<input type=\"hidden\" id=\"gameState\" name=\"gameState\" value=\""+ gameState +"\">" +
					"<input type=\"hidden\" id=\"gameNumber\" name=\"gameNumber\" value=\""+ gameNumber +"\">" +
					"  <label for=\"Symbol1\">First Letter:</label><br>\r\n" + 
					"  <input type=\"Symbol1\" id=\"Symbol1\" name=\"Symbol1\" value=\"\"><br><br>\r\n" + 
					"  <label for=\"Symbol2\">Second Letter:</label><br>\r\n" + 
					"  <input type=\"Symbol2\" id=\"Symbol2\" name=\"Symbol2\" value=\"\"><br><br>\r\n" + 
					"  <label for=\"Symbol3\">Third Letter:</label><br>\r\n" + 
					"  <input type=\"Symbol3\" id=\"Symbol3\" name=\"Symbol3\" value=\"\"><br><br>\r\n" + 
					"  <label for=\"Symbol4\">Fourth Letter:</label><br>\r\n" + 
					"  <input type=\"Symbol4\" id=\"Symbol4\" name=\"Symbol4\" value=\"\"><br><br>\r\n" + 
					"  <input type=\"submit\" value=\"Guess\">\r\n" + 
					"</form>\r\n" + 
					"\r\n" + 
					"<h1> Guesses: </h1>\r\n" + 
					"\r\n" + gameNumber +
					stateOutput + 
					"</body>\r\n" + 
					"</html>";
			return resource;
			
		}

		
	}
	// Takes in a string withouth spaces of length four with legal chars, returns the corresponding combination object
	private Combination stringToCombination(String stringState) {

		Combination c = new Combination();
		c.setCombination(String.valueOf(stringState.charAt(0)), String.valueOf(stringState.charAt(1)), String.valueOf(stringState.charAt(2)), String.valueOf(stringState.charAt(3)));
		return c;
	}
	// Should have used chars instead of strings maybe
	private boolean isLegalChars(String stateString) {
		String legalLetters ="ABCDEF";
		if(stateString.length() < 4) {
			return false;
		}
		char[] stateChar = stateString.toCharArray();
		for(int i=0; i < stateChar.length; i++) {
			if(legalLetters.indexOf(stateChar[i])< 0) {
				return false;
			}
		}
		return true;
		
	}
	//Takes in a raw string saved in each HTML form UI and makes Combination object with the state
	private ArrayList<Combination> stringArrayToCombinations(String stateString){
		ArrayList<Combination> stateCombinations = new ArrayList<Combination>();

		for(int i = 7; i<=stateString.length(); i+=8) {
			String stringState = stateString.substring(i-7, i+1).replaceAll("\\s", "");
			Combination c = stringToCombination(stringState);
			stateCombinations.add(c);
		}
		return stateCombinations;
	}
	
	
//	public static void main(String[] args) {
	//	Communicator c = new Communicator();
		//System.out.println(c.isLegalChars("ABDE"));
		//System.out.println(c.stringArrayToCombinations("A B C D E F G H").get(1).toString());

	//}
}
