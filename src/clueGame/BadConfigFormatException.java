// Author: Ryan Rumana

package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

@SuppressWarnings("serial")
public class BadConfigFormatException extends Exception {

	public BadConfigFormatException() {								// Constructor with default message
		super("Error: Bad board configuration");					// super call to give the message exception properties
		PrintWriter out;											// output
		try {														// Try-Catch to spot/deal with potential FileNotFoundExceptions
			out = new PrintWriter("logfile.txt");					// Code that will print BadConfigFormatException to logfile.txt
			out.println("Error: Bad board configuration");
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public BadConfigFormatException(String message) {				// Constructor with case specific message
		super("Error: Bad board configuration: " + message);		// super call to give the message exception properties
		PrintWriter out;											// output
		try {														// Try-Catch to spot/deal with potential FileNotFoundExceptions
			out = new PrintWriter("logfile.txt");					// Code that will print BadConfigFormatException to logfile.txt
			out.println("Error: Bad board configuration: " + message);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
