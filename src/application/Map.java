package application;

import java.util.ArrayList;
import java.util.PriorityQueue;

import javafx.geometry.Point2D;

public class Map {
	private MapNode[][] m;
	private int maxRow;
	private int maxCol;
	private PriorityQueue<MapNode> pq;
	Point2D start, goal;
	public ArrayList<MapNode> result;
	Map(int r, int c){
		m = new MapNode[r][c];
		maxRow = r;
		maxCol = c;
		for (int row = 0; row < r; row++) {
			for(int col = 0; col < c; col++) {
				m[row][col] = new MapNode(0, Double.MAX_VALUE, Double.MAX_VALUE,
						false, new Point2D((double)col * 20, (double)row * 20));
			}
		}
		result = new ArrayList<MapNode>();
	}

	private double distance(MapNode a, MapNode b) {
		double x = b.coord.getX() - a.coord.getX();
		double y = b.coord.getY() - a.coord.getY();
		return Math.sqrt(x * x + y * y);
	}
	public void setWall(double x, double y){
		int r = (int)y/20;
		int c = (int)x/20;
		m[r][c].isWall = true;
		
	}
	public void setStart(double x, double y){
		start = new Point2D(x, y);
		
	}
	public void setGoal(double x, double y) {
		goal = new Point2D(x, y);
	}
	public void setWall(int r, int c){
		m[r][c].isWall = true;
	}
	public void AStar() {
		System.out.println("start path find");
		MapNode startNode = node(start);
		MapNode endNode = node(goal);
		startNode.g = 0;
		startNode.f = 0;
		startNode.h = 0;
		startNode.visited = true;
		pq = new PriorityQueue<MapNode>(1, new AStarComparator());
		pq.add(startNode);
		while(pq.size() > 0) {
			System.out.println("remove from pq");
			MapNode currNode = pq.remove();
			if (currNode == endNode) {
				System.out.println("done");
				print_path(endNode);
				return;
			}
			int[][] offset = {{0,1}, {1,0}, {-1,0}, {0,-1}};
			for(int i = 0; i < 4; i++) {
				System.out.println("find neighbor");
				int r = currNode.r + offset[i][0];
				int c = currNode.c + offset[i][1];
				if(r >= maxRow || r < 0 || c >= maxCol || c < 0)
					continue;
				MapNode neighNode = m[r][c];
				if(neighNode.isWall)
					continue;
				double tmpG = currNode.g + distance(currNode, neighNode);
				if(neighNode.g > tmpG) {
					neighNode.prev = currNode;
					neighNode.g = tmpG;
					neighNode.f = neighNode.g + distance(startNode, neighNode);
					if(pq.contains(neighNode)) {
						pq.remove(neighNode);
					}
					pq.add(neighNode);
				}
					
						
			}		
		}
		System.out.println("failed");
	}
	private void print_path(MapNode endNode) {
		if(endNode == null)
			return;
		System.out.println("(" + endNode.r +"," + endNode.c + ")");
		result.add(endNode);
		print_path(endNode.prev);
		
	}

	private MapNode node(Point2D p) {
		int r = (int)p.getY()/20;
		int c = (int)p.getX()/20;
		return m[r][c];
	}
}
