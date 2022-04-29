package dijkstra_grid_code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dijkstra_grid_code.Coordinate;
import dijkstra_grid_code.Grid;

public class DijkstraSolver {
	
	//Declaration
	private Grid grid;
	private Coordinate start;
	private Coordinate end;
	
	//Constructor
	public DijkstraSolver(Grid grid) {
		this.grid = grid;
		this.start = grid.getStart();
		this.end = grid.getEnd();
	}
	
	public Dijkstra solve() {
		
		List<Dijkstra> candidates = new ArrayList<>();
		candidates.add(new Dijkstra(null, start));//add the start node to candidates
		while(!candidates.isEmpty()) {
			
			//Priority Queue: sort the element and choose the coordinate with the smallest weight
			Collections.sort(candidates);
			Dijkstra candidate = candidates.remove(0);//candidate is the next coordinate
			
			for(Coordinate step : candidate.getNextAvailableSteps(grid)) {//step stands for every next available node(path) of the current node(candidate)
				
				Dijkstra newPath = new Dijkstra(candidate, step);
				if(step.equals(end)) {//if we have reached and end
					return newPath;
				} else {
					candidates.add(newPath);
				}
				
			}
			
			
		}
		return null;
		
	}

}
