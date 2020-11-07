// Authors: Ryan Rumana

package experiment;

import java.util.*;

public class TestBoardCell {
	private int row;
	private int column;
	private boolean isRoom = false;
	private boolean isOccupied = false;
	Set<TestBoardCell> adjList;


	// Constructor with row and column parameters passed into it
	public TestBoardCell(int rowNum, int colNum) {
		this.row = rowNum;
		this.column = colNum;
	}

	// Method that return adjacency list
	public Set<TestBoardCell> getAdjList(TestBoard board) {
		adjList = new HashSet<TestBoardCell>();
		if(row > 0)
			adjList.add(board.getCell(row-1, column));
		if(column > 0)
			adjList.add(board.getCell(row, column-1));
		if(row < board.ROWS-1)
			adjList.add(board.getCell(row+1, column));
		if(column < board.COLS-1)
			adjList.add(board.getCell(row, column+1));
		return adjList;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	// Setter indicating if space is part of a room
	public void setRoom(boolean roomBool) {
		isRoom = roomBool;
	}

	// Getter returning if a space is part of a room
	public boolean isRoom() {
		return isRoom;
	}

	// Setter to see if space is occupied by another player
	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
	}

	// Getter returning if space is occupied by another player
	public boolean isOccupied() {
		return isOccupied;
	}
}
