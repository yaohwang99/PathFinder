package dijkstra_grid_code;

import java.util.ArrayList;
import java.util.List;

public class Coordinate {
	//Declare position: x and y
	private int x;
	private int y;
	
	//Constructor: set the position x and y
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//Getter: get the position x
	public int getX() {
		return x;
	}
	
	//Getter: get the position y
	public int getY() {
		return y;
	}
	
	//Create and return the adjacent coordinate(up, down, right, left) of a specified coordinate
	public List<Coordinate> getAdjacentCoordinates() {
		List<Coordinate> adjacentCoordinates = new ArrayList<>();
		adjacentCoordinates.add(new Coordinate(x, y+1));
		adjacentCoordinates.add(new Coordinate(x, y-1));
		adjacentCoordinates.add(new Coordinate(x+1, y));
		adjacentCoordinates.add(new Coordinate(x-1, y));
		return adjacentCoordinates;
	}
	
	
	//unknown method
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	//unknown method
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}

}
