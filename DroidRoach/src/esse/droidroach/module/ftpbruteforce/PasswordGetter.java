/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.ftpbruteforce;

import esse.droidroach.utility.WordGenerator;

public class PasswordGetter {
	private WordGenerator wordGenerator;
	private int pwdListPosition;
	private int pwdListLength;
	private int requestedWords =0;
	
	public PasswordGetter(){
		pwdListPosition=0;
		pwdListLength=List.getCommondPassword().length;

	}
	
	public PasswordGetter(int startLength, int maxWordLength, String alphabet){
		wordGenerator = new WordGenerator(startLength, maxWordLength, alphabet);
	}
	
	public String getNextPassword(){
		requestedWords++;
		
		if (wordGenerator!=null){
			return wordGenerator.generateNextWord();
		}else {
			
			if (pwdListPosition>=pwdListLength)
				return null;
			
			String r= List.getCommondPassword()[pwdListPosition];
			pwdListPosition++;
			return r;
		}
		
	}

	public int getRequestedWords() {
		return requestedWords;
	}

	

}
