/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.utility;

/**
 * This class is useful to generate "random" words for a brute-force attack.
 * You have to provide the starting length of the word and the maximum length you want to try plus the alphabet .
 * Then calling every time you call the function generateNextWord() you will get a new word.
 * This class is perfect to be used with a multi-threaded brute-force attack in order to avoid password duplicates being generated.
 * 
 * @author Stefano Gabryel
 *
 */

/*
 * Usage Example:
 * 
 * WordGenerator w = new WordGenerator(3,3, "abc");
 * String word=w.generateNextWord();
 * while (word!=null;){
 *   System.out.println(word);
 *   word=w.generateNextWord();
 * }
 * 
 * Console output:
 * aaa
 * aab
 * aac
 * aba
 * abb
 * ...
 * ...
 * ccc
 */
public class WordGenerator {
	private long wordCount=0;
	private int wordLenght;
	private int maxWordLength;
	private char[] alphabet;
	
	
	
	/**
	 * @param startLength
	 * @param maxWordLength
	 * @param alphabet
	 */
	public WordGenerator(int startLength, int maxWordLength, String alphabet){
		this.wordLenght=startLength;
		this.maxWordLength=maxWordLength;
		this.alphabet=alphabet.toCharArray();
	}
	
	
	/**
	 * This function is to be called by one or more threads performing a brute-force attack against the same target in order to keep the randomic word generation "sincronized"
	 * @return The next word generated or null if all the combinations have been calculated
	 */
	public String generateNextWord(){
		

		final long MAX_WORDS = (long) Math.pow(alphabet.length, wordLenght);
		final int RADIX = alphabet.length;


			int[] indices = convertToRadix(RADIX, wordCount, wordLenght);
			String word = "";
			for (int k = 0; k < wordLenght; k++) {
				word += alphabet[indices[k]];
			}

			wordCount++;
			if (wordCount>MAX_WORDS){
				wordCount=1;
				wordLenght++;
				if (wordLenght>maxWordLength){
					return null;
				}
			}
		
		
		return word;
		
		
	}
	
	
	
	/**
	 * Not used
	 * @param wlength
	 */
	public  void generate(int wlength) {
		int wordlength = wlength;
		char[] alphabet  = this.alphabet;
		
		
		final long MAX_WORDS = (long) Math.pow(alphabet.length, wordlength);
		final int RADIX = alphabet.length;

		for (long i = 1; i < MAX_WORDS; i++) {
			int[] indices = convertToRadix(RADIX, i, wordlength);
			String word = "";
			for (int k = 0; k < wordlength; k++) {
				word += alphabet[indices[k]];
			}
			System.out.println(word+"  i: "+i);
		}
	}
	
	
	private static int[] convertToRadix(int radix, long number, int wordlength) {
		int[] indices = new int[wordlength];

		for (int i = wordlength - 1; i >= 0; i--) {
			if (number > 0) {
				int rest = (int) (number % radix);
				number /= radix;
				indices[i] = rest;
			} else {
				indices[i] = 0;
			}

		}
		return indices;
	}

}
