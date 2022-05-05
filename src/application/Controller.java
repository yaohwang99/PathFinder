package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


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
	public void drawInit() {
		gc = myCanvas.getGraphicsContext2D();

		gc.setFill(Color.BLACK);
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        double h = myCanvas.getHeight();
        double w = myCanvas.getWidth();
        gc.clearRect(0,0,w,h);
        for(int y = 0; y <= h; y += 20) {
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
        
	}
	public void draw(MouseEvent e) {
		double x = roundto20(e.getX());
		double y = roundto20(e.getY());
		gc.fillRect(x, y, 20, 20);
		map.setWall(x, y);
	}
	public void pathFind(ActionEvent e) {
		map.setGoal(endTarget.getLayoutX(), endTarget.getLayoutY());
		map.setStart(startArrow.getLayoutX(), startArrow.getLayoutY());
		map.AStar(gc);
		map.startAnimation(gc);
		gc.setFill(Color.YELLOW);
//		for( MapNode node : map.result) {
//			double x = node.coord.getX();
//			double y = node.coord.getY();
//			gc.fillRect(x, y, 20, 20);
//		}
//		gc.setFill(Color.BLACK);
	}
}
