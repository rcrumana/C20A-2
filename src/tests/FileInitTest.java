// Authors: Ryan Rumana

package tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

public class FileInitTest {

	// Constants to reference in testing
	public static final int LEGEND_SIZE = 13;
	public static final int NUM_ROWS = 26;
	public static final int NUM_COLUMNS = 24;

	private static Board board;

	@BeforeAll
	public static void setUp() throws IOException {
		board = Board.getInstance();
		// Set the file names to input into Config functions
		board.setConfigFiles("data\\ClueLayout.csv", "data\\ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
		}

	@Test
	public void testDimensions() {
	// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	@Test
	public void testRooms() {
		// Testing cell [0][0] which is in the study
		BoardCell cell = board.getCell(0, 0);
		Room room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Study" ) ;
		assertFalse( cell.isLabel() );
		assertFalse( cell.isRoomCenter() ) ;
		assertFalse( cell.isDoorway()) ;

		// Testing an unused cell [11][11]
		cell = board.getCell(11, 11);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Unused" ) ;

		// Testing a walkway [7][14]
		cell = board.getCell(7, 14);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Walkway" ) ;

		// Testing the bottom right corner [25][23]
		cell = board.getCell(25, 23);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Unused" ) ;

		// Test a secret passageway [12][0]
		cell = board.getCell(12, 0);
		System.out.println(cell.getInitial());
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		System.out.println(room.getName());
		assertEquals( room.getName(), "Billiard Room" ) ;
		assertTrue( cell.getSecretPassage() == 'D' );

		// Testing a label cell [20][11] in the Ballroom
		cell = board.getCell(20, 11);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Ballroom" ) ;
		assertFalse( cell.isRoomCenter() );
		assertTrue( cell.isLabel() );

		// Testing a center cell [12][20] in the Dining Room
		cell = board.getCell(12, 20);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Dining Room" ) ;
		assertTrue( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
	}

	@Test
	public void testRoomLabels() {
		assertEquals("Study", board.getRoom('S').getName() );
		assertEquals("Ballroom", board.getRoom('B').getName() );
		assertEquals("Billiard Room", board.getRoom('R').getName() );
		assertEquals("Hall", board.getRoom('H').getName() );
		assertEquals("Unused", board.getRoom('X').getName() );
		assertEquals("Walkway", board.getRoom('W').getName() );
	}

	@Test
	public void testNumDoors() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(18, numDoors);
	}

	@Test
	public void testDoorDirect() {
		BoardCell cell = board.getCell(15, 18);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(8, 17);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		cell = board.getCell(22, 7);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(16, 9);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		cell = board.getCell(7, 7);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(17, 17);
		assertFalse(cell.isDoorway());
	}

}
