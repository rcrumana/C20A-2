// Author: Ryan Rumana

package clueGame;

import java.util.*;

public class BoardCell {

	////////////////////////////////////////////////////// Private instance variables and initialization methods /////////////////////////////////////////////////////

	private int row;															// the row the cell is in
	private int column;															// the column the cell is in
	private char initial;														// the initial of the cell
	private DoorDirection doorDirection = DoorDirection.NONE;					// the door direction of the cell (assuming it is a door)
	private boolean roomLabel = false;											// if the cell is a room label (assumed false)
	private boolean roomCenter = false;											// if the cell is a room center (assumed false)
	private boolean isOccupied;													// if the cell is occupied
	private char secretPassage;													// the character of the room that is connected via the secret passage
	private Set<BoardCell> adjList = new HashSet<BoardCell> ();					// the adjacency set of the cell

	public BoardCell(int rowNum, int colNum) {									// Constructor with row and column parameters passed into it
		this.row = rowNum;														// setting the row of the cell to the row parameter
		this.column = colNum;													// setting the column of the cell to the column parameter
	}

	/////////////////////////////////////////////////////////// Getters and Setters //////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("rawtypes")
	public Set getAdjList(){ 													// getter for the adjacency list
		return adjList; 
	}

	public void addAdj(BoardCell adj) {											// method that adds the cell passed into the method into the adjacency set
		adjList.add(adj);														// adding the cell to the adjacency set
	}

	public int getRow() { 														// getter for the row
		return row; 
	}

	public void setRow(int row) { 												// setter for the row
		this.row = row; 
	}

	public int getCol() { 														// getter for the column
		return column; 
	}

	public void setCol(int column) { 											// setter for the column
		this.column = column; 
	}

	public boolean isDoorway() {												// getter for if it is a doorway
		if(doorDirection != DoorDirection.NONE) {								// catches if there is a door direction, indicating that the cell is a door
			return true;														// return true, indicating that the cell is a door
		}
		else {																	// if there is no door direction
			return false;														// return false, indicating that the cell is not a door
		}
	}

	public DoorDirection getDoorDirection() { 									// getter for the door direction
		return this.doorDirection; 
	}

	public void setDoorDirection(char charAt) {									// setter for door direction based on read in character
		if(charAt == '^') {
			doorDirection = DoorDirection.UP;									// set the door direction that corresponds with the direction of the given arrow character
		} else if(charAt == '>') {
			doorDirection = DoorDirection.RIGHT;								// set the door direction that corresponds with the direction of the given arrow character
		} else if(charAt == '<') {
			doorDirection = DoorDirection.LEFT;									// set the door direction that corresponds with the direction of the given arrow character
		} else {
			doorDirection = DoorDirection.DOWN;									// set the door direction that corresponds with the direction of the given arrow character
		}
	}

	public boolean isLabel() { 													// getter for if the door is a label
		return roomLabel; 
	}

	public void setLabel(boolean isLabel) { 									// Setter for room label
		this.roomLabel = isLabel; 
	}

	public boolean isRoomCenter() {												// getter for if the room is a room center
		return roomCenter; 
	}	

	public void setCenter(boolean isCenter) { 									// Setter for room center
		this.roomCenter = isCenter; 
	}

	public char getSecretPassage() { 											// getter for secretPassage character
		return secretPassage; 
	} 

	public void setSecretPassage(char destination) { 							// Setter for secret passage
		this.secretPassage = destination; 
	}

	public char getInitial() { 													// Getter for room initial
		return this.initial; 
	} 

	public void setInitial(char roomInitial) { 									// Setter for room initial
		this.initial = roomInitial; 
	}

	public boolean getOccupied() { 												// Getter for if the room is occupied
		return isOccupied; 
	}

	public void setOccupied(boolean b) { 										// setter for if the room is occupied
		isOccupied = b; 
	}
}
