// Authors: Ryan Rumana

package experiment;

import java.util.*;

//
public class TestBoard {
	final static int COLS = 4;
	final static int ROWS = 4;
	private TestBoardCell[][] grid = new TestBoardCell[ROWS][COLS];
	private Set<TestBoardCell> targets = new HashSet<TestBoardCell>();
	private Set<TestBoardCell> visited = new HashSet<TestBoardCell>();


	// Constructor that sets board up
	public TestBoard() {
		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLS; y++) {
				grid[x][y] = new TestBoardCell(x,y);
			}
		}
	}

	// Calculates distance from startCell of pathLength to valid move positions
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		Set <TestBoardCell> adjList = startCell.getAdjList(this);
		if(startCell.isOccupied() == false && startCell.isRoom() == false) {
			visited.add(startCell);
		}else
			return;

		if(pathlength==0) {
			targets.add(startCell);
			visited.remove(startCell);
			return;
		}

		for(TestBoardCell i : adjList) {
			if(visited.contains(i) == false)
				calcTargets(i, pathlength-1);
		}

		visited.remove(startCell);
		return;
	}

	// Fills board with possible valid positions, receives most recent calcTargets results
	public Set<TestBoardCell> getTargets() {
		return targets;
	}

	// Returns the cell from the board at row, col
	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}

}
