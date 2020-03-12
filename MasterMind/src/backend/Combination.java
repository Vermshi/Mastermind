package backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Combination {
	ArrayList<Letter> combination = new ArrayList<Letter>();
	
	Combination(){
		
	}
	
	public void setCombination(String l1, String l2, String l3, String l4) {
		ArrayList<Letter> c = new ArrayList<Letter>();
		c.add(new Letter(l1));
		c.add(new Letter(l2));
		c.add(new Letter(l3));
		c.add(new Letter(l4));
		this.combination = c;
	}
	
	//Returns array with [Combination exist, Position exist, Won the game], 0=false, 1=true
	public int[] checkIfMatching(Combination guess) {
		
		ArrayList<Letter> guessList = guess.getCombination();
		int[] matchingResults = {0,0,0};
		//not the most cost effective way, but works
		int nCorrectCombinations = 0;
		int tn = 1;
		for(Letter t: getCombination()) {
			int gn = 1;
			for(Letter g: guessList) {

				if(t.getVal().equals(g.getVal())) {
					matchingResults[0]=1;
					if(tn == gn) {
						matchingResults[1]=1;
						nCorrectCombinations+=1;
					}
				}
				
				
				gn+=1;
			}
			tn+=1;
		}
		if(nCorrectCombinations == 4) {
			matchingResults[2] = 1;
		}
		
		return matchingResults;
	}
	// for generating the hidden combination
	public void genRandomCombination() {
		String[] legalLetters = { "A", "B", "C", "D", "E", "F"};
		Random rand = new Random(); 
		
		ArrayList<Letter> c = new ArrayList<Letter>();
		c.add(new Letter(legalLetters[rand.nextInt(6)]));
		c.add(new Letter(legalLetters[rand.nextInt(6)]));
		c.add(new Letter(legalLetters[rand.nextInt(6)]));
		c.add(new Letter(legalLetters[rand.nextInt(6)]));
		this.combination = c;

	}
	
	public ArrayList<Letter> getCombination(){
		return this.combination;
		
	}
	
	
	@Override
	public String toString() {
		String s= "";
		for(Letter l: this.combination) {
			s+=" " + l.getVal();
		}
		return s;
	}
	//Returns the string to be outputted based on the hidden combination as input and matches of this object
	public String toStringWithMatchingFlags(Combination hiddenCombination) {
		int[] matchingCriteria = this.checkIfMatching(hiddenCombination);
		String outputString = this.toString();
		if(matchingCriteria[2] == 1) {
			return "You won";
		}
		if(matchingCriteria[0] == 1) {
			outputString+= " X";
		}
		if(matchingCriteria[1] == 1) {
			outputString+= " O";
		}
		return outputString;
	}
	
	
	
public static void main(String[] args) {
	Combination c = new Combination();
	c.setCombination("A", "B", "C", "D");
	Combination k = new Combination();
	k.setCombination("A", "B", "C", "D");

	System.out.println(Arrays.toString(c.checkIfMatching(k)));

}

}

