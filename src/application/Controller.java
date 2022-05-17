package application;

import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


public class Controller {
	
	@FXML
	private Canvas myCanvas;
	@FXML
	private ImageView startArrow;
	@FXML
	private ImageView endTarget;
	private GraphicsContext gc;
	
	private Map map;
	private int maxCol, maxRow;
	
	public ArrayList<Point2D> checkPointQueue = new ArrayList<Point2D>();
	public Point2D checkPoint;
	public Point2D checkPointNext;
	
	private int roundto20(double a) {
		return (int)a/20 * 20;
	}
	private void fitGrid(ImageView iv) {
		iv.setLayoutX(roundto20(iv.getLayoutX()));
		iv.setLayoutY(roundto20(iv.getLayoutY()));
	}
	public void moveImageView(MouseEvent e) {
		ImageView iv = (ImageView)e.getTarget();
		Point2D p = iv.localToParent(e.getX(), e.getY());
		iv.setLayoutX(roundto20(p.getX()));
		iv.setLayoutY(roundto20(p.getY()));

	}
	/***
	 * Draw the 2D table and place imageView
	 * Also used in clear()
	 */
	public void drawInit() {
		if (map != null)
			map.stopLoop();
		gc = myCanvas.getGraphicsContext2D();

		gc.setFill(Color.BLACK);
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        double h = myCanvas.getHeight();
        double w = myCanvas.getWidth();
        
        gc.clearRect(0,0,w,h);//clear a portion of the canvas with transparent color
        for(int y = 0; y <= h; y += 20) {//stroke a line between start point(x1, y1) and end point(x2, y2)
        	gc.strokeLine(0, y, w, y);
        }
        for(int x = 0; x <= w; x += 20) {
        	gc.strokeLine(x, 0, x, h);
        }
        maxCol = (int)w/20;
        maxRow = (int)h/20;
        map = new Map(maxRow, maxCol);
        fitGrid(startArrow);
        fitGrid(endTarget);
        //fitGrid(checkPoint);
	}
	
	
	
	/***
	 * Draw the walls
	 * 1. Get the position of the clicked mouse
	 * 2. Draw the position (Rectangle and Black)
	 * @param e
	 */
	public void draw(MouseEvent e) {
		gc.setFill(Color.BLACK);
		double x = roundto20(e.getX());
		double y = roundto20(e.getY());
		gc.fillRect(x, y, 20, 20);
		map.setWall(x, y);
	}
	
	/***
	 * Set checkPoint
	 * 1. To solve the problem of being detected incorrectly as click(from release from drag), we have to first know whether the node is wall
	 * 2. If the node is not wall, we can place checkPoint
	 * 3. add CheckPoint to checkPoint Queue
	 * @param e
	 */
	public void setCheck(MouseEvent e) {
		double x = roundto20(e.getX());
		double y = roundto20(e.getY());
		if(map.isWall(x, y)) {
			System.out.println("is wall");
			gc.setFill(Color.BLACK);
			gc.fillRect(x, y, 20, 20);
			map.setWall(x, y);
		}
		else {
			System.out.println("not wall" + " x: " + roundto20(x)/20 + " y: " + roundto20(y)/20);
			
			try {
				//using absolute position rather than grid position
				Image check = new Image(getClass().getResource("checkpoint.png").toURI().toString());
				gc.setFill(new ImagePattern(check));
				gc.fillRect(x, y, 20, 20);
				
				Point2D temp = map.setCP(x, y);
				checkPointQueue.add(temp);
				System.out.println("x: " + temp.getX() + " y: " + temp.getY());
				//place this node(x, y) to queue
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			
		}
		
		
	}
	
	
	public void pathFind(ActionEvent e) {
		//add start at 0, and end target at the end
		checkPoint = new Point2D(startArrow.getLayoutX(), startArrow.getLayoutY());
		checkPointQueue.add(0, checkPoint);
		checkPoint = new Point2D(endTarget.getLayoutX(), endTarget.getLayoutY());
		checkPointQueue.add(checkPoint);
		
		checkPoint = checkPointQueue.remove(0);
		
		while(!(checkPointQueue.isEmpty())){//Point2D
			checkPointNext = checkPointQueue.remove(0);
			map.clear();
			
			System.out.println("***New coord" + checkPoint.getX() + " y: " + checkPoint.getY());
			System.out.println("***New coord Next" + checkPointNext.getX() + " y: " + checkPointNext.getY());
			System.out.println();
			
			map.setStart(checkPoint.getX(), checkPoint.getY());
			map.setGoal(checkPointNext.getX(), checkPointNext.getY());
			
//			map.depth_first(gc);
//			map.AStar(gc);
			map.dijkstra(gc);
			map.startAnimation(gc);
			
			checkPoint = checkPointNext;
		}
	}
}