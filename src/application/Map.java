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
	Point2D start, goal;
	public ArrayList<MapNode> result;
	public ArrayList<MapNode> traverseList;
	public ArrayList<MapNode> passerList;//using to record checked node
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

	private double heuristicScore(MapNode a, MapNode b) {
		return Math.abs(a.r - b.r) + Math.abs(a.c - b.c);
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
		pq = new PriorityQueue<MapNode>(maxRow * maxCol, new DijkComparator());//parameter is initial capacity=1 and a comparator(according to g)
		pq.add(endNode);
		MapNode unvisitedNode; int i, j;
		for(i = 0; i < maxRow; i++) {
			for(j = 0; j < maxCol; j++) {
				unvisitedNode = m[i][j];
				if((unvisitedNode != startNode) && (unvisitedNode != endNode)) {
					pq.add(unvisitedNode);
				}
			}
		}
		pq.add(startNode);
		
		MapNode preNode = startNode;
		
		//do(n - 1) rounds(including start initialization), with node number = n
		//for(int round = 0; round < (maxRow * maxCol - 2); round++) 
		while(pq.size() > 0){
			System.out.println("remove from pq");
			//take the node from the back
			MapNode currNode;
			//pq.comparator();//order pq
			currNode = pq.remove();
			System.out.println( "current: " + currNode.g);
			if(currNode != startNode) {//points to the previous node
				currNode.prev = preNode; preNode = currNode;
			}
			else {
				currNode.prev = null;
			}
			currNode.visited = true;
			traverseList.add(currNode);
			
			// because  currNode == endNode, we end, and add to animation, end successfully
			if (currNode == endNode) {
				System.out.println("done");
				createResult(endNode);
				return;
			}
			//if we haven't reached the end, we find the four neighbors and calculate the distance between currNode and its four neighbor
			//implements relax()
			int[][] offset = {{0,1}, {1,0}, {-1,0}, {0,-1}};
			for(i = 0; i < 4; i++) {
				System.out.println("find neighbor");
				int r = currNode.r + offset[i][0];
				int c = currNode.c + offset[i][1];
				if(r >= maxRow || r < 0 || c >= maxCol || c < 0)//check boundary
					continue;
				
				MapNode neighNode = m[r][c];//call the neighbor with specific position, and use neighNode to reference to it
				if(neighNode.isWall)
					continue;
				if(!(pq.contains(neighNode))) {//means neighNode is visited
					continue;
				}
				
				double tmpG = currNode.g + 1;//start to currNode + 1, using this to update shortest path
				if((!(neighNode.visited)) && (neighNode.g > tmpG)) {
					//neighNode.prev = currNode;
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
        loop = new AnimationTimer() {
        	double initSize = 0;
        	double size = initSize;
    		double speed = 1.0;
    		double maxSize = 18;
    		double hue = 185;
    		ArrayList<MapNode> currList = traverseList;
    		int idx = 0;
            @Override
            public void handle(long now) {
            	double x = currList.get(idx).coord.getX();
            	double y = currList.get(idx).coord.getY();
            	double midX = x + 10;
        		double midY = y + 10;
        		
        		for (int i = 0; size - speed * i > 0 ; i++) {
        			if (idx + i >= currList.size() || size - speed * i < 0)
        				break;
        			x = currList.get(idx + i).coord.getX();
                	y = currList.get(idx + i).coord.getY();
                	midX = x + 10;
            		midY = y + 10;
            		double sat = Math.min((size - speed * i) / maxSize, 1.0);
        			gc.setFill(Color.hsb(hue, sat, sat));
            		gc.fillOval(midX - (size - speed * i) / 2, midY - (size - speed * i) / 2, (size - speed * i), (size - speed * i));
        		}
        		
                if(size >= maxSize) {
                	assert (size == maxSize);
                	gc.setFill(Color.hsb(hue, 1.0, 1.0));
                	x = currList.get(idx).coord.getX();
                	y = currList.get(idx).coord.getY();
                	midX = x + 10;
            		midY = y + 10;
                	gc.fillRect(midX - maxSize / 2, midY - maxSize / 2, maxSize, maxSize);
                	if(idx >= currList.size() - 1) {
                		if(currList == result) {
                			this.stop();
                		}
                		else { // switch to result list
                			if (result.isEmpty()) {
                				this.stop();
                			}
                			currList = result;
                			idx = 0;
                			hue = 60;
                        	size = 0;
                		}
                	} else {
                		++idx;
                    	size -= speed;
                	}
                } else {
                	size+=speed;
                }
                
            }

        };
        
        loop.start();
    }
}
