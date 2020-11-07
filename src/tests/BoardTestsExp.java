// Authors: Ryan Rumana

package tests;

import java.util.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.TestBoard;
import experiment.TestBoardCell;

public class BoardTestsExp {
	TestBoard board;

	@BeforeEach
	public void setUp() { // Run before each test to refresh the
		board = new TestBoard();
	}

	// Tests a 4x4 board, NOT the game board
	@Test
	public void TopLeftTest() { // Testing [0][0]
		TestBoardCell cell = board.getCell(0,0);
		Set<TestBoardCell> testList = cell.getAdjList(board);
		Assert.assertTrue(testList.contains(board.getCell(1,0)));
		Assert.assertTrue(testList.contains(board.getCell(0,1)));
		Assert.assertEquals(2, testList.size());
	}

	@Test
	public void BottomRightTest() { // Testing [3][3]
		TestBoardCell cell = board.getCell(3,3);
		Set<TestBoardCell> testList = cell.getAdjList(board);
		Assert.assertTrue(testList.contains(board.getCell(2,3)));
		Assert.assertTrue(testList.contains(board.getCell(3,2)));
		Assert.assertEquals(2, testList.size());
	}

	@Test
	public void RightEdgeTest() { // Testing [1][3]
		TestBoardCell cell = board.getCell(1,3);
		Set<TestBoardCell> testList = cell.getAdjList(board);
		Assert.assertTrue(testList.contains(board.getCell(0,3)));
		Assert.assertTrue(testList.contains(board.getCell(2,3)));
		Assert.assertTrue(testList.contains(board.getCell(1,2)));
		Assert.assertEquals(3, testList.size());
	}

	@Test
	public void LeftEdgeTest() { // Testing [2][0]
		TestBoardCell cell = board.getCell(2,0);
		Set<TestBoardCell> testList = cell.getAdjList(board);
		Assert.assertTrue(testList.contains(board.getCell(3,0)));
		Assert.assertTrue(testList.contains(board.getCell(1,0)));
		Assert.assertTrue(testList.contains(board.getCell(2,1)));
		Assert.assertEquals(3, testList.size());
	}

	// Testing CalcTarget() code in the following tests
	@Test
	public void isOccupied() { // testing at 0,0
		board.getCell(0,0).setOccupied(true);
		Assert.assertTrue(board.getCell(0,0).isOccupied());
	}

	@Test
	public void isRoom() {  // testing at 0,0
		board.getCell(0,0).setRoom(true);
		Assert.assertTrue(board.getCell(0,0).isRoom());
	}

	@Test
	public void CornerMovement() { // testing at 0,0 moving 4
		TestBoardCell cell = board.getCell(0,0);
		board.calcTargets(cell, 4);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(3,1)));
		Assert.assertTrue(targets.contains(board.getCell(2,2)));
		Assert.assertTrue(targets.contains(board.getCell(1,3)));
		Assert.assertTrue(targets.contains(board.getCell(0,2)));
		Assert.assertTrue(targets.contains(board.getCell(1,1)));
		Assert.assertTrue(targets.contains(board.getCell(2,0)));
		Assert.assertEquals(6, targets.size());
	}

	@Test
	public void CenterMovement() { // testing at 2,2 moving 1
		TestBoardCell cell = board.getCell(2,2);
		board.calcTargets(cell, 1);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(2,1)));
		Assert.assertTrue(targets.contains(board.getCell(1,2)));
		Assert.assertTrue(targets.contains(board.getCell(2,3)));
		Assert.assertTrue(targets.contains(board.getCell(3,2)));
		Assert.assertEquals(4, targets.size());
	}

	@Test
	public void EdgeMovement() { // testing at 2,0 moving 6
		TestBoardCell cell = board.getCell(2,0);
		board.calcTargets(cell, 6);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(0,0)));
		Assert.assertTrue(targets.contains(board.getCell(1,1)));
		Assert.assertTrue(targets.contains(board.getCell(2,2)));
		Assert.assertTrue(targets.contains(board.getCell(3,3)));
		Assert.assertTrue(targets.contains(board.getCell(1,3)));
		Assert.assertTrue(targets.contains(board.getCell(3,1)));
		Assert.assertTrue(targets.contains(board.getCell(0,2)));
		Assert.assertEquals(7, targets.size());
	}
}
