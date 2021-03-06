/*
 * Sarmiento, Chyna Ezra S.
 * 20 April - 20 May 2020
 * 1 - BSIT AI
 * Programming 2 x Data Structures and Algorithms
 * FINAL PROJECT
 * Enigma Machine: Special Machine
 */

package finals;

import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class SimpleEnigmaMachine
{
	// ------- FILE DIRECTORY ------- //
	public static String fileDirectory = "C:\\Users\\sneidelaire\\Desktop\\Enigma Machine\\";
	public static String saveEncrypted = "Saved Encryption.txt";
	
	//KEY
	public static Integer intKey;
	public static Integer MAX_vLETTER = 26; 
	public static enum EntryW {A (1),B (2),C (3),D (4),E (5),F (6),G (7),H (8),I (9),J(10),K(11),L(12),M(13),
		                   N(14),O(15),P(16),Q(17),R(18),S(19),T(20),U(21),V(22),W(23),X(24),Y(25),Z(26);
		
		public Integer keyValue;
		
		//SET key's 2nd value
		EntryW(Integer num) { this.keyValue = num; }
		
		//GET key's 2nd value
		public Integer getValue() { return keyValue; }
	}
	
	// ------- Map ------- //
	//-- rotors & global variables.
	public static String rotorSet = "Rotor Set.csv";
	public static Map<Integer, String> rotor1map = new HashMap<Integer, String>();
	public static Map<Integer, String> rotor2map = new HashMap<Integer, String>();
	public static Map<Integer, String> rotor3map = new HashMap<Integer, String>();
	
	//-- assigns values to the Map
	public static Map<Integer, String> smMap(Map<Integer, String> rotor, Integer key, String value)
	{
		rotor.put(key, value);
		return rotor;
	}
	
	//get the Int Key for the String
	public static Integer getIntKey(String letter)
	{
		//converts String letter to enum Key
		return EntryW.valueOf(letter).getValue();
	}
	
	//-- displays the Map
	public static void displayMap(Map<Integer, String> rotor1map2)
	{
		rotor1map2.entrySet().forEach(entry -> { System.out.println(entry.getKey() + " " + entry.getValue()); });
		System.out.println();
	}
	
	// ------- File Handling ------- //
	public static void write(String message)     { System.out.print(message);   }
	public static void writeLine(String message) { System.out.println(message); }
	
	//modified for .csv ONLY READ THE ROTOR SET & assigning map
	public static String[] readMap(String fileName)
	{
		String[] column = null;
		String   output = "";
		String  splitBy = ",";
		
		try
		{
			Reader reader = new FileReader(fileDirectory + fileName);
			BufferedReader csvReader = new BufferedReader(reader);
			
			while ((output = csvReader.readLine()) != null)
			{
				//splits by comma
				column = output.split(splitBy);
				
				//PROOFING: displays the .csv
				//String column key @ orig letters.
//				System.out.println(column[0] + " | " + column[1] + " " + column[2] + " " + column[3]);
				
				//converts the String column -> Enum's associated value of its Key
				intKey = getIntKey(column[0]);
				
				//assigns the map
				smMap(rotor1map, intKey, column[1]);
				smMap(rotor2map, intKey, column[2]);
				smMap(rotor3map, intKey, column[3]);
			}
			csvReader.close();
		}	
		catch(Exception ex) { System.out.println(ex.getMessage()); }
		return column;
	}
	
	public static void writeMessage(String message, boolean ToAppend)
	{
		if(ToAppend) { write(message);     }
		else         { writeLine(message); }
	}
	
	//method overriding...
	public static void writeMessage(String fileName, String message, boolean nextLine)
	{
		File file = new File(fileDirectory + fileName);
		
		try
		{
			if(!file.exists())
			{
				file.createNewFile();
				writeLine("File Created.");
			}
			
			Writer w = new FileWriter(file, true);
			w.write(message);
			
			if(nextLine)
			{
				w.write("\n");
			}
			w.close();
			System.out.println("Your encrypted message is saved.");
		}
		catch(IOException e) { e.printStackTrace(); }
	}
	
	// ------- CHECKERS ------- // --> CHANGE TO ONE WHOLE METHOD TO SWITCH CASE
	//-- checks if Message only contains letters
	//regardless of capitalisation.
	public static boolean ONLYletterChecker(String checkInput)
	{
		if(checkInput.matches("[a-zA-Z]+")) { return true;  }
		else                                { return false; }
	}
	
	//-- checks if userChoice contains only YES/NO.
	public static boolean userSaveRestriction(String checkInput)
	{
		if((checkInput.equalsIgnoreCase("YES")) || (checkInput.equalsIgnoreCase("NO")))
		     { return true;  }
		else { return false; }
	}
	
	//check if key is in the range of 1-26, inclusive
	public static boolean checkKey(Integer key)
	{
		int intKey = key.intValue();
		
		//need to check because there's an offset.
		if((1 <= intKey) && (intKey <= 26))
		     { return true;  }
		else { return false; }
	}
	
	//offsetting
	public static int needToMove(Map<Integer, String> rotor, int offset)
	{
		int rotorNum;
		int counter1,  counter2,  counter3;
		    counter1 = counter2 = counter3 = 0;
		
		//assigns the rotorNum & counts each move
		if(rotor.equals(rotor1map))      { rotorNum = 1;
		                                   counter1++; }
		//counts if previous rotor completed a loop (26), exclusive
		else if(rotor.equals(rotor2map)) { rotorNum = 2;
		     if(counter1 > MAX_vLETTER)  { counter2++; }
		}
		else if(rotor.equals(rotor3map)) { rotorNum = 3;
		     if(counter2 > counter3)     { counter3++; }
		}
		else
		{
			//because I only used 3 rotors.
			rotorNum = 0;
		}
		
		switch(rotorNum)
		{
			//rotor 1 always move
			case 1: offset++;
			break;
			
			//for rotors 2 and above
			case 2:
			case 3:
				if((counter2 > MAX_vLETTER) || (counter3 > MAX_vLETTER)) { offset++; }
			break;
			default: offset *= 1; //just returning the offset
			break;
		}
		
		return offset;
	}
	
	// ------- ERROR MESSAGES, ETC ------- // --> CHANGE TO ONE WHOLE METHOD
	//-- error message when user keeps on inputting non-letter String.
	public static void ERRORletter() { System.out.println("PLEASE INPUT ONLY LETTERS.\n"); }
	
	//-- error message when user keeps on inputting not Yes/No.
	public static void ERRORsave()	 { System.out.println("PLEASE ONLY INPUT (YES) or (NO).\n"); }
	
	// ------- INTERFACE THINGS ------- //
	//global variables v2
	public static int	 userChoice;
	public static String saveECMessage;
	public static String userOGMessage, encryptMessage = "";
	public static String endMessage = "\nThank you for using Chyna's Simple Enigma Machine";
	
	//just shows you the setting of the machine.
	//-- DISPLAYS the Settings
	public static void MSetting()
	{
		System.out.printf("%-8s  %-12s %-14s",        "", "Wheel Order", "Ring Settings\n");
		System.out.printf("%-11s %-12s %-12s", "DEFAULT",    "III II I", "01 01 01\n");
		System.out.println();
	}
	
	//MESSAGE ENCODE
	//-- passes input as a parameter
	public static void inputMessage(Scanner input)
	{
		//loops through asking for the Message
		//if the user keeps on inputting non-letter String.
		do
		{
			System.out.print("Enter your message: ");
			userOGMessage = input.nextLine();
			
			//converts userMessage to all capital letters (enums are captial)
			userOGMessage = userOGMessage.toUpperCase();
			
			if(ONLYletterChecker(userOGMessage) == false) { ERRORletter(); } //disgusting repetition
		}while(ONLYletterChecker(userOGMessage) == false);                   //disgusting repetition
	}
	
	//-- save your encrypted message to a .txt file
	//FH: SAVE ENCRYPTED MESSAGE
	public static void saveMessage(Scanner input)
	{
		//loops through asking whether to save the Encrypted Message
		//if the user keeps on inputting things not "yes/no".
		do
		{
			System.out.print("Save encrypted message to a file (Yes/No)? ");
			saveECMessage = input.nextLine();
			
			if(userSaveRestriction(saveECMessage) == false) { ERRORsave(); } //disgusting repetition
			else
			{
				if(saveECMessage.equalsIgnoreCase("YES"))
				{
					writeMessage(saveEncrypted, encryptMessage, true);
					System.out.print(endMessage);
				}
				else { System.out.print(endMessage); }
			}
		}while(userSaveRestriction(saveECMessage) == false); //disgusting repetition
	}
	
	// ------- ENCRYPTION ------- //
	public static String encryptAlgorithm(Map<Integer, String> rotor, String message)
	{
		String[] setWord = message.split("");
		Integer  key, keyLevel, tempKL;
		String   tempLevel,  layer;
		         tempLevel = layer = "";
		int loopDiv, loopNum;
		    loopDiv = loopNum = 0;
		int offset  = 0;
		
		//iterates through each letters
		for(String mLine : setWord)
		{
			key      = getIntKey(mLine);
			keyLevel = key + offset;
			
			//DEBUG: BEFORE STATE OF KEYLEVEL
//			System.out.println("BEFORE: " + keyLevel + " " + checkKey(keyLevel));
			
			//offsets the rotor when letter key
			//goes it over 26
			if(checkKey(keyLevel) == false)
			{
				loopNum = keyLevel / MAX_vLETTER;
				loopDiv = keyLevel % MAX_vLETTER;
				
				if(loopDiv != 0)
				     { tempKL  = keyLevel -  (loopNum *    MAX_vLETTER); }
				else { tempKL  = keyLevel - ((loopNum-1) * MAX_vLETTER); }
			}
			else { tempKL  = keyLevel; }
			
			//DEBUG: MATH
//			System.out.println("MATH: " + tempKL);
			
			//DEBUG: AFTER STATE OF KEYLEVEL
//			System.out.println("AFTER: " + tempKL + " " + checkKey(tempKL));
			
			tempLevel = rotor.get(tempKL);
			layer    += tempLevel;
			
			offset = needToMove(rotor, offset);
		} offset++;
		
		return layer;
	}
	
	public static String encryptMessage(String ogMessage)
	{
		String layer1,  layer2,  layer3;
		       layer1 = layer2 = layer3 = "";
		
		//THE CONNECTION: layers connects to each other.
		layer1 = encryptAlgorithm(rotor1map, ogMessage);
		layer2 = encryptAlgorithm(rotor2map, layer1);
		layer3 = encryptAlgorithm(rotor3map, layer2);
		
		encryptMessage = layer3;
		
		//OPTIONAL/PROOFING: displays the encrypted letters 
//		System.out.println(layer1);
//		System.out.println(layer2);
//		System.out.println(layer3);
		
		return encryptMessage;
	}
	
	// ------- MAIN CLASS ------- //
	public static void main(String[] args)
	{
		//reads the Rotor Set for the Special Enigma Machine (A1807S) ONLY 
		readMap(rotorSet);
		
		//OPTIONAL/PROOFING: displays the Rotor Maps 
//		displayMap(rotor1map);
//		displayMap(rotor2map);
//		displayMap(rotor3map);
		
		//Shows the type of Enigma Machine is used
		System.out.println("Enigma Machine: Enigma I Sondermaschine (A1807S).\n");
		
		//default setting.
		MSetting();
		
		// ------- INTERFACE ------- //
		Scanner input = new Scanner(System.in);
			//asks for the message for encryption
			inputMessage(input);
			
			//Prints the Encrypted Message
			System.out.println("\nYour Encrypted Message:");
			System.out.println(encryptMessage(userOGMessage));
			System.out.println();
			
			//File Handling: Ask to save encrypted message
			saveMessage(input);
		input.close();
	}
}
