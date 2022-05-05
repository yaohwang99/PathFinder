package application;


import java.util.ArrayList;
import java.util.PriorityQueue;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Map {
	private MapNode[][] m;
	private int maxRow;
	private int maxCol;
	private PriorityQueue<MapNode> pq;
	Point2D start, goal;
	public ArrayList<MapNode> result;
	public ArrayList<MapNode> traverseList;
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
	public void AStar(GraphicsContext gc) {
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
        	double initSize = 15;
        	double size = initSize;
    		double speed = 1;
    		double maxSize = 18;
    		ArrayList<MapNode> currList = traverseList;
    		MapNode curr = currList.remove(0);
            @Override
            public void handle(long now) {
            	double x = curr.coord.getX();
            	double y = curr.coord.getY();
            	double midX = x + 10;
        		double midY = y + 10;
        		if(currList == result) {
        			gc.setFill(Color.YELLOW);
        		}
        		else {
        			gc.setFill(Color.LIGHTBLUE);
        		}
        		gc.fillRect(midX - size / 2, midY - size / 2, size, size);
                
                size+=speed;
                if(size >= maxSize) {
                	if(currList.isEmpty()) {
                		if(currList == result) {
                			this.stop();
                		}
                		else {
                			currList = result;
                			curr = currList.remove(0);
                        	size = initSize;
                		}
                	} else {
                		curr = currList.remove(0);
                    	size = initSize;
                	}
            		
                }
            }

        };
        
        loop.start();
    }
}
