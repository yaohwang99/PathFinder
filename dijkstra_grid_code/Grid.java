package dijkstra_grid_code;

/***
 * get and set the coordinate and gird
 * @author apple
 *
 */

public class Grid {
	
	//a grid to know the identity of each coordinate
	public enum GRID_CELL_IDENTITY { INVALID, EMPTY, WALL, START, BOMB, END, ROUTE };
	private GRID_CELL_IDENTITY[][] grid;
	protected int gridSize;
	private Coordinate start;
	private Coordinate end;
	
	//Constructor: construct a 2d-grid with a given size*size
	public Grid(int gridSize) {
		this.gridSize = gridSize;
		grid = new GRID_CELL_IDENTITY[gridSize][gridSize];
		
		for(int x=0; x<gridSize; x++) {
			for(int y=0; y<gridSize; y++) {
				setCellValue(new Coordinate(x,y),GRID_CELL_IDENTITY.EMPTY);
			}
		}
	}
	//Getter: Return the grid
	public GRID_CELL_IDENTITY[][] getGrid() {//change to static method?
		return grid;
	}
	//Getter: return the gridSize
	public int getGridSize() {
		return gridSize;
	}
	
	//Getter: get the cell value of a given coordinate
	public GRID_CELL_IDENTITY getCellValue(Coordinate coord) {
		try {
			return grid[coord.getX()][coord.getY()];
		} catch(ArrayIndexOutOfBoundsException e) {
			return GRID_CELL_IDENTITY.INVALID;
		}
	}
	
	//Setter: set the value of a specified coordinate
	public void setCellValue(Coordinate coord, GRID_CELL_IDENTITY value) {
		grid[coord.getX()][coord.getY()] = value;
		if(value == GRID_CELL_IDENTITY.START) {
			start = coord;
		}
		if(value == GRID_CELL_IDENTITY.END) {
			end = coord;
		}
	}
	
	//Clear the grid's start, end, and route
	public void clear() {
		start = null;
		end= null;
		for(int x=0; x<gridSize; x++) {
			for(int y=0; y<gridSize; y++) {
				Coordinate coord = new Coordinate(x, y);
				if(getCellValue(coord) != GRID_CELL_IDENTITY.WALL) {
					setCellValue(coord,GRID_CELL_IDENTITY.EMPTY);
				}
			}
		}
	}
	//Getter: get the coordinate of the start cell
	public Coordinate getStart() {
		return start;
	}
	//Getter: get the coordinate of the end cell
	public Coordinate getEnd() {
		return end;
	}
	

}
