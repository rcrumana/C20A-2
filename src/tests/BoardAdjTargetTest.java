package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;

	@BeforeAll
	public static void setUp() throws IOException {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// we want to test a couple of different rooms.
		// First, the study that only has a single door but a secret room
		Set<BoardCell> testList = board.getAdjList(15, 3);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(11, 2)));
		assertTrue(testList.contains(board.getCell(18, 3)));
		assertTrue(testList.contains(board.getCell(12, 20)));
		
		// now test the ballroom (note not marked since multiple test here)
		testList = board.getAdjList(21, 11);
		assertEquals(5, testList.size());
		assertTrue(testList.contains(board.getCell(22, 7)));

		// one more room, the kitchen
		testList = board.getAdjList(22, 20);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(20, 17)));
		assertTrue(testList.contains(board.getCell(18, 21)));

		// one more cell, inside the ballroom, but not a center
		testList = board.getAdjList(21, 10);
		assertEquals(0, testList.size());
	}


	// Ensure door locations include their rooms and also additional walkways
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		Set<BoardCell> testList = board.getAdjList(11, 3);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(11, 4)));
		assertTrue(testList.contains(board.getCell(11, 2)));
		assertTrue(testList.contains(board.getCell(8, 3)));

		testList = board.getAdjList(20, 5);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(19, 5)));
		assertTrue(testList.contains(board.getCell(20, 6)));
		assertTrue(testList.contains(board.getCell(23, 3)));

		testList = board.getAdjList(16, 9);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(16, 8)));
		assertTrue(testList.contains(board.getCell(16, 10)));
		assertTrue(testList.contains(board.getCell(15, 9)));
		assertTrue(testList.contains(board.getCell(21, 11)));
	}

	// Test a variety of walkway scenarios
	// These tests are Dark Orange on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on bottom edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(25, 9);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(24, 9)));

		// Test on top edge of board, just one walkway piece
		testList = board.getAdjList(0, 16);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(1, 16)));

		// Test on right edge of board, just one walkway piece
		testList = board.getAdjList(7, 23);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(7, 22)));

		// Test on left edge of board, just one walkway piece
		testList = board.getAdjList(19, 0);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(19, 1)));

		// Test near a door but not adjacent
		testList = board.getAdjList(19, 6);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(19, 7)));
		assertTrue(testList.contains(board.getCell(18, 6)));
		assertTrue(testList.contains(board.getCell(19, 5)));
		assertTrue(testList.contains(board.getCell(20, 6)));

		// Test adjacent to walkways
		testList = board.getAdjList(15, 7);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(15, 6)));
		assertTrue(testList.contains(board.getCell(15, 8)));
		assertTrue(testList.contains(board.getCell(14, 7)));
		assertTrue(testList.contains(board.getCell(16, 7)));

		// Test next to ballroom
		testList = board.getAdjList(16,11);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(16, 10)));
		assertTrue(testList.contains(board.getCell(16, 12)));
		assertTrue(testList.contains(board.getCell(15, 11)));

	}


	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInDiningRoom() {
		// test a roll of 1
		board.calcTargets(board.getCell(2, 3), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(4, 6)));

		// test a roll of 3
		board.calcTargets(board.getCell(2, 3), 3);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(4, 4)));
		assertTrue(targets.contains(board.getCell(5, 5)));	
		assertTrue(targets.contains(board.getCell(6, 6)));
		assertTrue(targets.contains(board.getCell(5, 7)));	
		assertTrue(targets.contains(board.getCell(4, 8)));
		assertTrue(targets.contains(board.getCell(3, 7)));

		// test a roll of 4
		board.calcTargets(board.getCell(2, 3), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(4, 3)));
		assertTrue(targets.contains(board.getCell(5, 4)));	
		assertTrue(targets.contains(board.getCell(5, 6)));
		assertTrue(targets.contains(board.getCell(6, 7)));	
		assertTrue(targets.contains(board.getCell(5, 8)));	
		assertTrue(targets.contains(board.getCell(4, 7)));	
		assertTrue(targets.contains(board.getCell(3, 8)));	
		assertTrue(targets.contains(board.getCell(2, 7)));	
	}

	@Test
	public void testTargetsInKitchen() {
		// test a roll of 1
		board.calcTargets(board.getCell(22, 20), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(20, 17)));
		assertTrue(targets.contains(board.getCell(18, 21)));	

		// test a roll of 3
		board.calcTargets(board.getCell(22, 20), 3);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(22, 17)));
		assertTrue(targets.contains(board.getCell(21, 16)));	
		assertTrue(targets.contains(board.getCell(19, 16)));
		assertTrue(targets.contains(board.getCell(18, 17)));	

		// test a roll of 4
		board.calcTargets(board.getCell(22, 20), 4);
		targets= board.getTargets();
		assertEquals(15, targets.size());
		assertTrue(targets.contains(board.getCell(22, 16)));
		assertTrue(targets.contains(board.getCell(23, 17)));	
		assertTrue(targets.contains(board.getCell(20, 16)));
		assertTrue(targets.contains(board.getCell(21, 17)));
	}

	// Tests out of room center, 1, 2 and 3
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(11, 3), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(8, 3)));
		assertTrue(targets.contains(board.getCell(11, 2)));	
		assertTrue(targets.contains(board.getCell(11, 4)));	

		// test a roll of 2 
		board.calcTargets(board.getCell(11, 3), 2);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(11, 1)));
		assertTrue(targets.contains(board.getCell(11, 5)));
		assertTrue(targets.contains(board.getCell(15, 3)));	
		assertTrue(targets.contains(board.getCell(8, 3)));

		// test a roll of 3 (with secret passage)
		board.calcTargets(board.getCell(11, 3), 3);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(11, 6)));
		assertTrue(targets.contains(board.getCell(15, 3)));
		assertTrue(targets.contains(board.getCell(8, 3)));	
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(7, 11), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(7, 10)));
		assertTrue(targets.contains(board.getCell(7, 12)));	
		assertTrue(targets.contains(board.getCell(6, 11)));
	

		// test a roll of 2
		board.calcTargets(board.getCell(7, 11), 2);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(7, 9)));
		assertTrue(targets.contains(board.getCell(6, 10)));
		assertTrue(targets.contains(board.getCell(7, 13)));	
		assertTrue(targets.contains(board.getCell(6, 12)));	

		// test a roll of 3
		board.calcTargets(board.getCell(7, 11), 3);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(7, 10)));
		assertTrue(targets.contains(board.getCell(7, 12)));	
		assertTrue(targets.contains(board.getCell(6, 11)));
		assertTrue(targets.contains(board.getCell(7, 8)));
		assertTrue(targets.contains(board.getCell(7, 14)));	
		assertTrue(targets.contains(board.getCell(6, 13)));
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		board.getCell(13, 7).setOccupied(true);
		board.calcTargets(board.getCell(15, 7), 4);
		board.getCell(15, 7).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(13, targets.size());
		assertTrue(targets.contains(board.getCell(15, 9)));
		assertTrue(targets.contains(board.getCell(12, 6)));
		assertFalse( targets.contains( board.getCell(11, 7))) ;
		assertFalse( targets.contains( board.getCell(15, 7))) ;
		

		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(12, 20).setOccupied(true);
		board.getCell(8, 18).setOccupied(true);
		board.calcTargets(board.getCell(8, 17), 1);
		board.getCell(12, 20).setOccupied(false);
		board.getCell(8, 18).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(7, 17)));	
		assertTrue(targets.contains(board.getCell(8, 16)));	
		assertTrue(targets.contains(board.getCell(12, 20)));	

		// check leaving a room with a blocked doorway
		board.getCell(15, 18).setOccupied(true);
		board.calcTargets(board.getCell(12, 20), 3);
		board.getCell(12, 15).setOccupied(false);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(6, 17)));
		assertTrue(targets.contains(board.getCell(8, 19)));	
		assertTrue(targets.contains(board.getCell(8, 15)));

	}
}
