// Author: Ryan Rumana

package clueGame;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Board {

	////////////////////////////////////////////////////// Private instance variables and initialization methods /////////////////////////////////////////////////////

	private int numRows;																	// Number of rows in the Board
	private int numColumns;																	// Number of columns in the board
	private BoardCell[][] grid;																// The 2d array of BoardCells that represents the board itself
	private String layoutConfigFile;														// The name of the layout file, declared upon initialization
	private String setupConfigFile;															// The name of the setup file, declared upon initialization
	private Map<Character, Room> roomMap = new HashMap<Character, Room>();					// A map that relates each room to the character that represents it 
	private Set<BoardCell> targets = Collections.<BoardCell>emptySet();						// A set of all the current targets on the board
	private Set<BoardCell> visited = Collections.<BoardCell>emptySet();						// A set of all the rooms visited on a pieces trajectory to it's targets

	private static Board theInstance = new Board();											// A single static instance of the board

	private Board() {																		// A private board constructor to ensure only one is created
		super();
	}

	public static Board getInstance() {														// this method returns the only Board
		return theInstance;
	}

	public void initialize() throws IOException {											// initialize the board (since we are using singleton pattern)
		try{																				// Try-Catch to spot/handle potential BadConfigFormatExceptions
			loadSetupConfig();																// Methods required to populate the board
			loadLayoutConfig();																// Methods required to populate the board
		} catch(BadConfigFormatException e) { 												// Custom exception for formatting errors		
			System.out.println(e);															// Print the message generated by the exception
		}
	}

	////////////////////////////////////////////////////// Load Setup and Layout Methods /////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("resource")
	public void loadSetupConfig() throws IOException, BadConfigFormatException { 	// Method to read in rooms from legend and store them in a map
		FileReader setupInput = new FileReader(setupConfigFile);							// File reader to parse the setup file
		Scanner input = new Scanner(setupInput);											// Scanner to use the data from the File Reader

		while(input.hasNextLine()) {
			String currentLine = input.nextLine();											// Grabs the next line from the setup file
			String list[] = currentLine.split(", "); 										// Need to get ahold of the final letter and .put it with the preceding string
			if(list.length > 1) {
				if(list[0].equalsIgnoreCase("Room") || list[0].equalsIgnoreCase("Space")) { // Catches all of the strings that begin with a Monacre for a room
					char tempKey = list[2].charAt(0);										// creating a temporary roomMap key
					Room tempRoom = new Room(list[1]);										// creating a temporary roomMap room
					roomMap.put(tempKey, tempRoom);											// adding those temporary values to the room map
				} else {
					throw new BadConfigFormatException("incorrectly labeled space in setup file.");  // Custom exception for BadConfigFormatException
				}
			}
		}
		//setupInput.close();
	}

	@SuppressWarnings("resource")
	public void loadLayoutConfig() throws IOException, BadConfigFormatException {			// Method to load they layout csv and populate the board with the corresponding data
		FileReader layoutInput = new FileReader(layoutConfigFile);							// File reader to parse the layout file
		Scanner input = new Scanner(layoutInput);											// Scanner to use the data from the File Reader

		ArrayList<String[]> inputGrid = new ArrayList<String[]>();							// Arraylist of an Array of Strings This is necessary because we want a dynamic size
		while(input.hasNextLine()) {
			String thisLine = input.nextLine();												// Gather the next line from the layout file
			String grid[] = thisLine.split(","); 											// Separating the items separated by commas and turning them into an array of strings
			inputGrid.add(grid); 															// ads csv input to the grid
		}
		setNumColumns(inputGrid.get(0).length);												// Setting the number of columns to be the length of the first row
		setNumRows(inputGrid.size());														// Setting the number of rows to the number of elements in the input grid

		this.grid = new BoardCell[numRows][numColumns];										// Declaring the unpopulated grid and it's size

		for(int r = 0; r < numRows; r++) { 													// iterating through each row
			String temp[] = inputGrid.get(r);												// grabbing a row from the input grid
			if(temp.length != numColumns) {													// test to see if any of the rows are of incorrect length
				throw new BadConfigFormatException("uneven rows.");  						// Custom exception for BadConfigFormatException
			} else {
				if(temp[0].contains("﻿")) {	// get rid of								// test to see if the file conversion from CSV standard to UTF-8 has added artifacts
					String replace = temp[0].replace("﻿","");								// creates a temporary string that does not contain the artifact
					temp[0] = replace;														// sets the original string to be the temporary string, removing the artifact from the array
				}
			}
			for(int c = 0; c < numColumns; c++) {											// iterating through each element in the particular row
				this.grid[r][c] = new BoardCell(r,c);										// creates a BoardCell for each location on the board
				String auto = temp[c];														// creates a temporary string for each location on the board
				if(!(roomMap.containsKey(auto.charAt(0)))) {								// Checks and makes sure each cell is a cell type identified in the setup document
					throw new BadConfigFormatException("unspecified room detected.");  		// Custom exception for BadConfigFormatException
				} else {
					if(auto.length() > 1) { 												// Identifying Special cell (more than one initial per cell)
						String directions = "^><v";
						if(directions.indexOf(auto.charAt(1)) != -1) { 						// Handling doorways
							this.grid[r][c].setDoorDirection(auto.charAt(1));				// Setting the door direction of the cell to be wherever the arrow points
							this.grid[r][c].isDoorway();									// Setting the cell to identify as a door
							this.grid[r][c].setInitial(auto.charAt(0));						// Setting the initial of the cell 
						}
						else if(auto.charAt(1) == '#') { 									// Handling a room label
							this.grid[r][c].setLabel(true);									// Setting the cell to identify as a room label
							this.grid[r][c].setInitial(auto.charAt(0));						// Setting the initial of the cell 
						}
						else if(auto.charAt(1) == '*') { 									// Handling a room center
							this.grid[r][c].setCenter(true);								// Setting the cell to identify as a room center
							this.grid[r][c].setInitial(auto.charAt(0));						// Setting the initial of the cell 
						}
						else { 																// Handling a secret passageway
							this.grid[r][c].setSecretPassage(auto.charAt(1));				// Setting the cell to identify as a secret passage
							this.grid[r][c].setInitial(auto.charAt(0));						// Setting the initial of the cell 
						}
					}
					else {																	// Handling normal cells (only one initial per cell)
						this.grid[r][c].setInitial(auto.charAt(0));							// Setting the initial of the cell 
					}
				}
			}
		}
		// Iterating back through each cell after they have all been assigned
		// This is done so that when making the door map there are no cells that are still null
		for(int i = 0; i < this.numRows ; i++){												// Iterating through the rows
			for(int j = 0; j < this.numColumns ; j++){										// Iterating through each cell per row
				if(this.getCell(i,j).isDoorway()) {											// Getting each of the doorways
					switch (this.getCell(i,j).getDoorDirection()) {
					case UP:																// Getting the doors that point up
						roomMap.get(this.getCell(i-1,j).getInitial()).addDoor(this.getCell(i,j)); // Assigning each door to the room it points to
						break;
					case LEFT:																// Getting the doors that point left
						roomMap.get(this.getCell(i,j-1).getInitial()).addDoor(this.getCell(i,j)); // Assigning each door to the room it points to
						break;
					case DOWN:																// Getting the doors that point down
						roomMap.get(this.getCell(i+1,j).getInitial()).addDoor(this.getCell(i,j)); // Assigning each door to the room it points to
						break;
					case RIGHT:																// Getting the doors that point right
						roomMap.get(this.getCell(i,j+1).getInitial()).addDoor(this.getCell(i,j)); // Assigning each door to the room it points to
						break;
					default:
						break;
					}	
				}
				else if(this.getCell(i,j).isLabel()) {										// getting the label cells
					roomMap.get(this.getCell(i,j).getInitial()).setLabelCell(this.getCell(i,j)); // adding the label cells to each room
				}
				else if(this.getCell(i,j).isRoomCenter()) {									// getting the center cells
					roomMap.get(this.getCell(i,j).getInitial()).setCenterCell(this.getCell(i,j)); // adding the center cells to each room
				}
				else if(this.getCell(i,j).getSecretPassage() != 0) {						// getting the secret passage cells
					roomMap.get(this.getCell(i,j).getInitial()).setSecretPassage(this.getCell(i,j).getSecretPassage()); // adding the secret passage char to each room
				}
			}
		}
		for(int i = 0; i < this.numRows ; i++){												// Iterating through the rows
			for(int j = 0; j < this.numColumns ; j++){										// Iterating through each cell per row
				generateAdjList(i, j);
			}
		}
		layoutInput.close();																// Closing the layout document
	}

	/////////////////////////////////////////////////////////// Getters and Setters //////////////////////////////////////////////////////////////////////////////////

	public int getNumRows() { 																// Getter for the number of rows
		return numRows; 
	}

	private void setNumRows(int rows) {														// Setter for the number of rows
		numRows = rows; 
	}

	public int getNumColumns() { 															// Getter for the number of columns
		return numColumns; 
	}

	private void setNumColumns(int cols) { 													// Setter for the number of columns
		numColumns = cols; 
	}

	public BoardCell getCell(int row, int col) { 											// Getter for cell using row and column location
		return this.grid[row][col]; 
	}

	public void setConfigFiles(String csv, String txt) {									// Describes the files to be used for configuration
		layoutConfigFile = csv;																// setting the layout file (.csv)
		setupConfigFile = txt;																// setting the setup file(.txt)
	}

	public Room getRoom(char roomChar) { 													// Returns the room of a Board object using the room character
		return roomMap.get(roomChar); 
	}

	public Room getRoom(BoardCell currCell) {												// Returns the room of a Board object using a cell
		return roomMap.get(currCell.getInitial());											// returning the room related to the cell
	}

	@SuppressWarnings("rawtypes")
	public Set getAdjList(int row, int col) { 
		if(this.getCell(row, col).getAdjList() != null) {
			return this.getCell(row, col).getAdjList();
		}
		return Collections.emptySet();
	}

	public void generateAdjList(int row, int col) { 											// Method creating the adjacency list of a cell
		BoardCell originCell = this.getCell(row, col);										// Creates a temporary cell called originCell for convenience
		BoardCell testCell;																	// another temporary cell called testCell
		if(originCell.getInitial() != 'W' && !originCell.isRoomCenter()) {					// Checks for cells that are in rooms and are not centers
			return;
		}
		if(originCell.getInitial() != 'W' && originCell.isRoomCenter()) {					// Checks for cells that are in rooms and are centers
			@SuppressWarnings("unchecked")
			Set<BoardCell> doors = roomMap.get(originCell.getInitial()).getDoors();			// grabs all the doors for a certain room
			System.out.println(doors.size());
			for (Iterator<BoardCell> it = doors.iterator(); it.hasNext();) {				// iterates through the set of doors
				testCell = it.next();														// sets test cell to be the next door in the set of doors
				originCell.addAdj(this.getCell(testCell.getRow(),testCell.getCol()));		// all doors are adjacent to the room center
			}
			if(roomMap.get(originCell.getInitial()).isSecretPassage()) {					// determines if that room has a secret passage
				Character passage = roomMap.get(originCell.getInitial()).getSecretPassage();// creates a temporary char that is equal to the initial of the originCell's secret passage room
				BoardCell passageCell = roomMap.get(passage).getCenterCell();				// creates a temporary cell that is equal to the center of the room 
				originCell.addAdj(this.getCell(passageCell.getRow(), passageCell.getCol()));// adds the corresponding cell from the board to the adjacency set	
			}
			return;
		}
		else if(originCell.getInitial() == 'W' && !originCell.isDoorway()) {				// checks if the originCell is a walkway but not a doorway
			if(row > 0) {																	// Makes sure the cell has a cell above it to avoid null pointer exceptions
				testCell = this.getCell(row-1, col);										// initializes the test cell to the cell in that location
				if(testCell.getInitial() == 'W'){											// Since non-door walkways can only be adjacent to walkways, if the test cell is a walkway...
					originCell.addAdj(testCell);											// add it to the adjacency set
				}
			}
			if(col > 0) {																	// Makes sure the cell has a cell to the left to avoid null pointer exceptions
				testCell = this.getCell(row, col-1);										// initializes the test cell to the cell in that location
				if(testCell.getInitial() == 'W'){											// Since non-door walkways can only be adjacent to walkways, if the test cell is a walkway...
					originCell.addAdj(testCell);											// add it to the adjacency set
				}
			}
			if(row < this.getNumRows()-1) {													// Makes sure the cell has a cell below it to avoid null pointer exceptions
				testCell = this.getCell(row+1, col);										// initializes the test cell to the cell in that location
				if(testCell.getInitial() == 'W'){											// Since non-door walkways can only be adjacent to walkways, if the test cell is a walkway...
					originCell.addAdj(testCell);											// add it to the adjacency set
				}
			}
			if(col < this.getNumColumns()-1) {												// Makes sure the cell has a cell to the right to avoid null pointer exceptions
				testCell = this.getCell(row, col+1);										// initializes the test cell to the cell in that location
				if(testCell.getInitial() == 'W'){											// Since non-door walkways can only be adjacent to walkways, if the test cell is a walkway...
					originCell.addAdj(testCell);											// add it to the adjacency set
				}
			}
			return;	
		}
		else if(originCell.getInitial() == 'W' && originCell.isDoorway()) {					// checks to see if the cell is a doorway
			if(row > 0) {																	// Makes sure the cell has a cell above it to avoid null pointer exceptions
				testCell = this.getCell(row-1, col);										// initializes the test cell to the cell in that location
				if(originCell.getDoorDirection() == DoorDirection.UP)						// checks to see if the door direction is up
					originCell.addAdj(roomMap.get(testCell.getInitial()).getCenterCell());	// adds the center of the room corresponding to the character of the test cell to the adjacency list
				else if(testCell.getInitial() == 'W')										// otherwise, if the test cell above is a walkway...
					originCell.addAdj(testCell);											// add it to the adjacency set
			}
			if(col > 0) {																	// Makes sure the cell has a cell top the left to avoid null pointer exceptions
				testCell = this.getCell(row, col-1);										// initializes the test cell to the cell in that location
				if(originCell.getDoorDirection() == DoorDirection.LEFT)						// checks to see if the door direction is left
					originCell.addAdj(roomMap.get(testCell.getInitial()).getCenterCell());	// adds the center of the room corresponding to the character of the test cell to the adjacency list
				else if(testCell.getInitial() == 'W')										// otherwise, if the test cell above is a walkway...
					originCell.addAdj(testCell);											// add it to the adjacency set
			}
			if(row < this.getNumRows()-1) {													// Makes sure the cell has a cell below it to avoid null pointer exceptions
				testCell = this.getCell(row+1, col);										// initializes the test cell to the cell in that location
				if(originCell.getDoorDirection() == DoorDirection.DOWN)						// checks to see if the door direction is down
					originCell.addAdj(roomMap.get(testCell.getInitial()).getCenterCell());	// adds the center of the room corresponding to the character of the test cell to the adjacency list
				else if(testCell.getInitial() == 'W')										// otherwise, if the test cell above is a walkway...
					originCell.addAdj(testCell);											// add it to the adjacency set
			}
			if(col < this.getNumColumns()-1) {												// Makes sure the cell has a cell to the right to avoid null pointer exceptions
				testCell = this.getCell(row, col+1);										// initializes the test cell to the cell in that location
				if(originCell.getDoorDirection() == DoorDirection.RIGHT)					// checks to see if the door direction is right
					originCell.addAdj(roomMap.get(testCell.getInitial()).getCenterCell());	// adds the center of the room corresponding to the character of the test cell to the adjacency list
				else if(testCell.getInitial() == 'W')										// otherwise, if the test cell above is a walkway...
					originCell.addAdj(testCell);											// add it to the adjacency set
			}
			return;	
		}
		return;
	}

	public void calcTargets(BoardCell startCell, int i) {									// method that returns the current targets
		targets = new HashSet<BoardCell> ();												// Initializes the set of target cells
		visited = new HashSet<BoardCell> ();												// Initializes the set of visited cells

		@SuppressWarnings("unchecked")
		Set<BoardCell> adjList = getAdjList(startCell.getRow(), startCell.getCol());		// gets the adjacency list for the start cell
		visited.add(startCell);																// adds the start cell to the adjacency set
		for(BoardCell k : adjList) {														// iterates through each cell on the adjacency set
			recurseTargets(k, i-1);															// calls the method recurseTargets for each cell on the adjacency set, with one less move
		}
	}

	public void recurseTargets(BoardCell cell, int i) {										// method that does mostly the same stuff as calcTargets, but without the initialization and with all of the rules implemented
		@SuppressWarnings("unchecked")
		Set<BoardCell> adjList = getAdjList(cell.getRow(), cell.getCol());					// gets the adjacency set for the start cell
		visited.add(cell);																	// adds the start cell to the adjacency set
		if(cell.isRoomCenter()) {															// gets the cells that are room centers
			targets.add(cell);																// adds the cell to the target set
			visited.remove(cell);															// removes the cell from the visited set
			return;																			// exits the method (once rooms are entered, they cannot be left)
		}
		if(cell.getOccupied()) {															// gets cells that are occupied
			visited.remove(cell);															// removes the cell from the visited set
			return;																			// exits the method (occupied cells cannot be visited)
		}
		if(i == 0) {																		// catches if this current iteration represents the last step in the move
			targets.add(cell);																// adds the cell to the target set
			visited.remove(cell);															// removes the cell from the visited set
			return;																			// exits the method (the player can move no further, and therefore further iterations are not necessary)
		}
		for(BoardCell temp : adjList) {														// iterates through each cell in the adjacency set
			if(!(visited.contains(temp))) {													// gets the cells that have not yet been visited
				recurseTargets(temp,i-1);													// recursively calls itself with each cell and one less move
			}
		}
		visited.remove(cell);																// removes the cell from the visited set
		return;																				// exits the method (there are no more cases to check)
	}

	public Set<BoardCell> getTargets() { 													// method that returns the set of targets
		return targets; 
	} 
}
