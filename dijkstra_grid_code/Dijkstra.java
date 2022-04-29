package dijkstra_grid_code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dijkstra_grid_code.Coordinate;
import dijkstra_grid_code.Grid;
import dijkstra_grid_code.Grid.GRID_CELL_IDENTITY;

public class Dijkstra implements Comparable<Dijkstra>{//to be sortable in DikjstraSolver
	
	/***
	 * Create a set of coordinate
	 * Note: hash set is a set that doesn't allow repeated element; Set is the interface of hashSet, and we use hashSet to implement Set
	 */
	Set<Coordinate> steps = new HashSet<>();//path?
	
	Coordinate lastStep = null;
	
	//Constructor: when we get the current path, we will call this function to see to progress in which direction
	//we want to see whether there exists another path
	public Dijkstra(Dijkstra otherPath, Coordinate step) {
		if(otherPath != null) {
			//add every element in other.steps to steps(path)
			for(Coordinate otherStep : otherPath.steps) {
				steps.add(otherStep);
			}
		}
		steps.add(step);
		lastStep = step;
	}
	
	//add the next available step(not visited and empty/end) after checking the adjacent list of the last step
	public List<Coordinate> getNextAvailableSteps(Grid grid) {
		List<Coordinate> adjacentCoordinates = lastStep.getAdjacentCoordinates();
		List<Coordinate> nextAvailableSteps = new ArrayList<Coordinate>();
		for(Coordinate candidateStep : adjacentCoordinates) {
			if(!steps.contains(candidateStep)) {//because the visited coordinates are in steps
				if(grid.getCellValue(candidateStep) == GRID_CELL_IDENTITY.EMPTY || grid.getCellValue(candidateStep) == GRID_CELL_IDENTITY.END) {
					nextAvailableSteps.add(candidateStep);
				}
			}
		}
		return nextAvailableSteps;//all the next path we may visit
	}
	
	public Set<Coordinate> getSteps() {
		return steps;
	}
	
	@Override
	public int compareTo(Dijkstra o) {
		return steps.size() - o.steps.size();
	}
}
