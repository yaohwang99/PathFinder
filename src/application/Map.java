package application;


import java.util.ArrayList;



import java.util.PriorityQueue;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Map {
	private MapNode[][] m;//m is an array of MapNode, with initialization later on(set g as max)
	private int maxRow;
	private int maxCol;
	private PriorityQueue<MapNode> pq;
	public Point2D start, goal, checkPoint;
	public ArrayList<MapNode> result;
	public ArrayList<MapNode> traverseList;
//	public ArrayList<MapNode> passerList;//using to record checked node
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
		traverseList = new ArrayList<MapNode>();
		
	}
	/***
	 * Reset attribute of MapNode to run shortest path algo at each round successfully
	 */
	public void clear() {
		for (int row = 0; row < maxRow; row++) {
			for(int col = 0; col < maxCol; col++) {
				m[row][col].h = 0;
				m[row][col].f = m[row][col].g = Double.MAX_VALUE;
				m[row][col].visited = false;
				m[row][col].prev = null;
			}
		}
	}

	private double heuristicScore(MapNode a, MapNode b) {
		return Math.abs(a.r - b.r) + Math.abs(a.c - b.c);
	}
	public void setWall(double x, double y){
		int r = (int)y/20;
		int c = (int)x/20;
		m[r][c].isWall = true;
		
	}
	public boolean isWall(double x, double y){
		int r = (int)y/20;
		int c = (int)x/20;
		return (m[r][c].isWall);
		
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
	public Point2D setCP(double x, double y){
		int r = (int)y/20;
		int c = (int)x/20;
		m[r][c].checkPoint = true;
		checkPoint = new Point2D(x, y);
		return checkPoint;
	}
	public boolean isCheckPoint(double x, double y){
		int r = (int)y/20;
		int c = (int)x/20;
		return (m[r][c].checkPoint);
		
	}
	public void Breadth_first(GraphicsContext gc) {
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
			// add to animation
			traverseList.add(currNode);
			if (currNode == endNode) {
				System.out.println("done");
				createResult(endNode);
				return;
			}
			int[][] offset = {{0,1}, {1,0}, {-1,0}, {0,-1}};//right down up left 
			for(int i = 0; i < 4; i++) {
				System.out.println("find neighbor");
				int r = currNode.r + offset[i][0];
				int c = currNode.c + offset[i][1];
				if(r >= maxRow || r < 0 || c >= maxCol || c < 0)
					continue;
				MapNode neighNode = m[r][c];
				if(neighNode.isWall)
					continue;
				double tmpG = currNode.g + 1;
				if(neighNode.g > tmpG) {
					neighNode.prev = currNode;
					neighNode.g = tmpG;
					neighNode.f = neighNode.g;
					if(pq.contains(neighNode)) {
						pq.remove(neighNode);
					}
					pq.add(neighNode);
				}
					
			}		
		}
		System.out.println("failed");
	}
	public void AStar(GraphicsContext gc) {
		System.out.println("start path find");
		//pointer points to start and end
		MapNode startNode = node(start);
		MapNode endNode = node(goal);
		
		startNode.g = 0;
		startNode.f = 0;
		startNode.h = 0;
		startNode.visited = true;
		
		//priority queue of MapNode
		pq = new PriorityQueue<MapNode>(1, new AStarComparator());//parameter is initial capacity and a comparator?
		pq.add(startNode);
		while(pq.size() > 0) {
			System.out.println("remove from pq");
			MapNode currNode = pq.remove();
			traverseList.add(currNode);
			if (currNode == endNode) {
				System.out.println("done");
				createResult(endNode);
				return;
			}
			int[][] offset = {{0,1}, {-1,0}, {0,-1}, {1,0}};
			for(int i = 0; i < 4; i++) {
				System.out.println("find neighbor");
				int r = currNode.r + offset[i][0];
				int c = currNode.c + offset[i][1];
				if(r >= maxRow || r < 0 || c >= maxCol || c < 0)//check boundary
					continue;
				MapNode neighNode = m[r][c];
				if(neighNode.isWall)
					continue;
				double tmpG = currNode.g + 1;
				if(neighNode.g > tmpG) {
					neighNode.prev = currNode;
					neighNode.g = tmpG;
					neighNode.h = heuristicScore(endNode, neighNode);
					neighNode.f = neighNode.g + neighNode.h;
					if(pq.contains(neighNode)) {
						pq.remove(neighNode);
					}
					pq.add(neighNode);
				}
					
			}		
		}
		System.out.println("failed");
	}
	
	//empty traverse list and result
	public void dijkstra(GraphicsContext gc) {
		System.out.println("start Dijkstra path find");
		
		//pointer points to start and end
		MapNode startNode = node(start);
		MapNode endNode = node(goal);
		
		//only g matters in this algo and visited or not
		startNode.g = 0;
		startNode.f = 0;
		startNode.h = 0;
		startNode.visited = true;//now we visited start node
		
		//priority queue of MapNode to store those unvisited nodes
		pq = new PriorityQueue<MapNode>(maxRow * maxCol, new AStarComparator());//parameter is initial capacity=1 and a comparator(according to g)
		pq.add(startNode);
		System.out.println("start" + startNode.g);
		
		//do(n - 1) rounds(including start initialization), with node number = n
		while(pq.size() > 0){
			System.out.println("remove from pq");
			//take the node from the back
			MapNode currNode;
			//pq.comparator();//order pq
			currNode = pq.remove();
			System.out.println( "current: " + currNode.g);
			
			currNode.visited = true;
			traverseList.add(currNode);//if currNode is not in traverseList//////////////////
			
			// because  currNode == endNode, we end, and add to animation, end successfully
			if (currNode == endNode) {
				System.out.println("done");
				createResult(endNode);
				return;
			}
			//if we haven't reached the end, we find the four neighbors and calculate the distance between currNode and its four neighbor
			//implements relax()
			int i;
			int[][] offset = {{0,1}, {1,0}, {-1,0}, {0,-1}};
			for(i = 0; i < 4; i++) {
				System.out.println("find neighbor");
				int r = currNode.r + offset[i][0];
				int c = currNode.c + offset[i][1];
				if(r >= maxRow || r < 0 || c >= maxCol || c < 0)//check boundary
					continue;
				
				MapNode neighNode = m[r][c];//call the neighbor with specific position, and use neighNode to reference to it
				if(neighNode.isWall) {
					System.out.println("is wall");
					continue;
				}
				if(neighNode.visited == true) {
					System.out.println("visited");
					continue;
				}
				double tmpG = currNode.g + 1;//start to currNode + 1, using this to update shortest path
				if((!(neighNode.visited)) && (neighNode.g > tmpG)) {
					System.out.println("update weight");
					neighNode.prev = currNode;
					neighNode.g = tmpG;
					neighNode.h = 0;
					neighNode.f = neighNode.g + neighNode.h;
				}
				//insert back
				if(pq.contains(neighNode)) {
					pq.remove(neighNode);
				}
				pq.add(neighNode);
			}		
		}
		System.out.println("failed");
	}
	public int dfs_helper(MapNode start, MapNode end) {//dlr, return 0 means that we have reached endNode
		//visited this node
		start.visited = true;
		traverseList.add(start);
		
		if (start == end) {
			System.out.println("done");
			createResult(end);
			return 0;
		}
		//find all the neighbor and recursion
		int[][] offset = {{0,1}, {1,0}, {-1,0}, {0,-1}};
		int i;
		for(i = 0; i < 4; i++) {//find all the neighbors of nodestart, discover, color = gray
			//System.out.println("find neighbor");
			int r = start.r + offset[i][0];
			int c = start.c + offset[i][1];
			if(r >= maxRow || r < 0 || c >= maxCol || c < 0)//check boundary
				continue;
			
			MapNode neighNode = m[r][c];//call the neighbor with specific position, and use neighNode to reference to it
			if(neighNode.isWall)
				continue;
			
			//if the neighbor is not visited, call the recursive method to visit it
			if(!(neighNode.visited)) {
				//System.out.println("row: " + neighNode.r + " column: " + neighNode.c);
				neighNode.prev = start;
				if(dfs_helper(neighNode, end) == 0) {
					return 0;
				}
			}
		}
		System.out.println("failed");
		return 0;
	}
	public void depth_first(GraphicsContext gc) {
		System.out.println("start DFS path find");
		//pointer points to start and end
		MapNode startNode = node(start);
		MapNode endNode = node(goal);
		dfs_helper(startNode, endNode);
		return;
	}
	
	
	private void createResult(MapNode endNode) {
		if(endNode == null)
			return;
		result.add(0, endNode);
		createResult(endNode.prev);
		
	}

	private MapNode node(Point2D p) {
		int r = (int)p.getY()/20;
		int c = (int)p.getX()/20;
		return m[r][c];
	}
	
	public void startAnimation(GraphicsContext gc) {
		System.out.println("Start Animation");
        AnimationTimer loop;
        loop = new AnimationTimer() {//define the implementation of abstract class Animation Timer, here we create a timer
        	double initSize = 0;
        	double size = initSize;
    		double speed = 2;
    		double maxSize = 18;
    		double hue = 185;
    		ArrayList<MapNode> currList = traverseList;
    		MapNode curr = currList.remove(0);
    		
            @Override
            public void handle(long now) {//since handle is an abstract method, we must override to implement it, with parameter of long(timeStamp now)
            	double x = curr.coord.getX();
            	double y = curr.coord.getY();
            	double midX = x + 10;
        		double midY = y + 10;
        		
        		if(!(isCheckPoint(x, y))) {
        			gc.setFill(Color.hsb(hue, 1.0 * size / maxSize, 1.0 * size / maxSize));
            		gc.fillOval(midX - size / 2, midY - size / 2, size, size);
        		}
        		
        		
                if(size >= maxSize) {
                	assert (size == maxSize);
                	//if x, y is not check point
                	if(!(isCheckPoint(x, y))) {
                		gc.fillRect(midX - maxSize / 2, midY - maxSize / 2, maxSize, maxSize);
                    	System.out.println("size: " + size);
                	}
                	
                	if(currList.isEmpty()) {
                		if(currList == result) {
                			this.stop();//animation stops
                		}
                		else {
                			currList = result;
                			hue = 60;
                			curr = currList.remove(0);
                        	size = initSize;
                		}
                	}
                	else {
                		curr = currList.remove(0);
                    	size = initSize;
                	}
                }
                size+=speed;
            }

        };
        loop.start();
        
    }
}
