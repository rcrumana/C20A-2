// Author: Ryan Rumana

package clueGame;

import java.util.HashSet;
import java.util.Set;

public class Room {

	////////////////////////////////////////////////////////// Private instance variables and initialization methods /////////////////////////////////////////////////////

	private String name = "";												// The name of the room
	private Character secretPassage = 0;									// If the room has a secret passage, where is it connected to?
	private BoardCell centerCell;											// cell representing the center of the room
	private BoardCell labelCell;											// cell representing the label of a room
	private Set<BoardCell> doors = new HashSet<BoardCell> ();				// The doors of a given room

	public Room(String roomName) {											// constructor
		this.name = roomName;												// setting the room name to the given name
	}

	////////////////////////////////////////////////////////// Getters and setters /////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("rawtypes")
	public Set getDoors(){ 													// getter for the door set
		return doors; 
	}

	public void addDoor(BoardCell door) {									// method that adds the cell passed into the method into the door set
		doors.add(door);													// adding the cell to the door set
	}

	public String getName() { 												// Getter for the room's name
		return this.name; 
	}

	public void setName(String name) { 										// Setter for the room's name
		this.name = name; 
	}

	public Character getSecretPassage() { 									// Getter for the room's secret passage
		return this.secretPassage; 
	}

	public Boolean isSecretPassage() { 										// Getter for if the room contains a secret passage
		if(this.secretPassage != 0) {
			return true;
		} else {
			return false;
		}

	}

	public void setSecretPassage(Character secretPassage) { 							// Setter for the room's secret passage
		this.secretPassage = secretPassage; 
	}

	public BoardCell getCenterCell() { 										// Getter for center cell
		return centerCell; 
	}

	public void setCenterCell(BoardCell cell) { 							// Setter for center cell
		this.centerCell = cell; 
	} 	

	public BoardCell getLabelCell() { 										// Getter for label cell
		return labelCell; 
	} 	

	public void setLabelCell(BoardCell cell) { 								// Setter for label cell
		this.labelCell = cell; 
	} 

}
