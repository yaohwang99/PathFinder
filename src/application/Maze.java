package application;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Maze{
	ArrayList<MapNode> wallList;
	Map map;
	MapNode[][] m;
	GraphicsContext gc;
	AnimationTimer loop;
	private Stack<int[]> stk;
	Maze(Map map, GraphicsContext gc){
		this.map = map;
		this.gc = gc;
		wallList = new ArrayList<MapNode>();
	}
	private boolean drawVertical(int width, int height, int r1, int c1, int r2, int c2) {
		Random rdm = new Random();
		int pos = rdm.nextInt(width - 2) + c1 + 1;
		int empty = rdm.nextInt(height) + r1;
		int cnt = 0;
		while (!m[r1 - 1][pos].isWall || !m[r2][pos].isWall) {
			pos = rdm.nextInt(width - 2) + c1 + 1;
			if(cnt++>100) {
				System.out.println("Maze is wrong");
				return false;
			}
				
		}
		for (int row = r1; row < r2; row++) {
			if(row == empty)
				continue;
			wallList.add(m[row][pos]);
			m[row][pos].isWall = true;
		}
		stk.push(new int[]{r1, c1, r2, pos});
		stk.push(new int[]{r1, pos + 1, r2, c2});
		return true;
	}
	private boolean drawHorizontol(int width, int height, int r1, int c1, int r2, int c2) {
		Random rdm = new Random();
		int empty = rdm.nextInt(width) + c1;
		int pos = rdm.nextInt(height - 2) + r1 + 1;
		int cnt = 0;
		while (!m[pos][c1 - 1].isWall || !m[pos][c2].isWall) {
			pos = rdm.nextInt(height - 2) + r1 + 1;
			if(cnt++>100) {
				System.out.println("Maze is wrong");
				return false;
			}
		}
		for (int col = c1; col < c2; col++) {
			if(col == empty)
				continue;
			wallList.add(m[pos][col]);
			m[pos][col].isWall = true;
		}
		stk.push(new int[]{r1, c1, pos, c2});
		stk.push(new int[]{pos + 1, c1, r2, c2});
		return true;
	}
	public void generate() {
		m = map.m;
		int maxCol = map.maxCol;
		int maxRow = map.maxRow;
		for (int col = 0; col < maxCol; col++) {
			wallList.add(m[0][col]);
			m[0][col].isWall = true;
		}
		for (int col = 0; col < maxCol; col++) {
			wallList.add(m[maxRow - 1][col]);
			m[maxRow - 1][col].isWall = true;
		}
		for (int row = 0; row < maxRow; row++) {
			wallList.add(m[row][maxCol - 1]);
			m[row][maxCol - 1].isWall = true;
		}
		for (int row = 0; row < maxRow; row++) {
			wallList.add(m[row][0]);
			m[row][0].isWall = true;
		}
		stk = new Stack<int[]>();
		stk.push(new int[]{1,1,maxRow - 1, maxCol - 1});
		while(!stk.isEmpty()) {
			int[] tmp = stk.pop();
			int r1 = tmp[0], c1 = tmp[1], r2 = tmp[2], c2 = tmp[3];
			int width = c2 - c1, height = r2 - r1;
			Random rdm = new Random();
			if(width < 3 || height < 3)
				continue;
			int dir = 2;
			if (width == height) {
				dir = rdm.nextInt(1);
			}
			if (width > height || dir == 0) { //draw vertical wall
				if(!drawVertical(width, height, r1, c1, r2, c2)) {
					if(height > 2)
						drawHorizontol(width, height, r1, c1, r2, c2);
				}
			} else if (height > width || dir == 1) {
				if(!drawHorizontol(width, height, r1, c1, r2, c2)) {
					if(width > 2)
						drawVertical(width, height, r1, c1, r2, c2);
				}
			}
			
		}
		createLoop();
		loop.start();
	}
	private void createLoop() {
		loop = new AnimationTimer() {//define the implementation of abstract class Animation Timer, here we create a timer
        	double initSize = 0;
        	double size = initSize;
    		double speed = 2.0;
    		double maxSize = 20;
    		ArrayList<MapNode> currList = wallList;
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
        			gc.setFill(Color.BLACK);
            		gc.fillOval(midX - (size - speed * i) / 2, midY - (size - speed * i) / 2, (size - speed * i), (size - speed * i));
        		}
        		
                if(size >= maxSize) {
                	assert (size == maxSize);
                	gc.setFill(Color.BLACK);
                	x = currList.get(idx).coord.getX();
                	y = currList.get(idx).coord.getY();
                	midX = x + 10;
            		midY = y + 10;
                	gc.fillRect(midX - maxSize / 2, midY - maxSize / 2, maxSize, maxSize);
                	if(idx >= currList.size() - 1) {
                			this.stop();//animation stops
                	} else {
                		++idx;
                    	size -= speed;
                	}
                } else {
                	size+=speed;
                }
                
            }

        };
	}
}
