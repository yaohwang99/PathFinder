package application;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;


public class Controller {
	@FXML
	private Button start;
	@FXML
	private Button clear;
	@FXML
	private MenuButton choose_alg;
	@FXML
	private Canvas myCanvas;
	@FXML
	private ImageView startArrow;
	@FXML
	private ImageView endTarget;
	private GraphicsContext gc;
	
	private Stage popStage;
	private Parent popRoot;
	private Scene popScene;
	
	
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
	public void init_menu_button() {
		start.setDisable(true);
        choose_alg.getItems().add(new MenuItem("Astar"));
        choose_alg.getItems().add(new MenuItem("Breath-First-Search"));
        choose_alg.getItems().add(new MenuItem("Dijkastra"));
        choose_alg.getItems().add(new MenuItem("Depth-First-Search"));
        ObservableList<MenuItem> items = choose_alg.getItems();
        for(int i =0; i< items.size(); i++) {
        	MenuItem item = items.get(i);
        	item.setOnAction(event -> {
        	    choose_alg.setText(item.getText());
        	    start.setDisable(false);
        	});
        }
        
	}
	/***
	 * Draw the 2D table and place imageView
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
        start.setDisable(false);
        
	}
	/***
	 * Show pop up window after clicking "help" button in PathFindingUI
	 * @param e
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public void popUp(MouseEvent e) throws IOException, URISyntaxException {
		popStage = new Stage();
		FXMLLoader popLoader = new FXMLLoader(getClass().getResource("popUpWindow.fxml"));
		popRoot = popLoader.load();
		popScene = new Scene(popRoot);
		popScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Image iconPic;
		iconPic = new Image(getClass().getResource("icon.png").toURI().toString());
		popStage.getIcons().add(iconPic);
			
		popStage.setTitle("Path Visualizer Manual");
		popStage.setScene(popScene);
		popStage.show();
	}
	/***
	 * switch to scene 2
	 * @throws IOException 
	 */
	public void switchScene2(MouseEvent e) throws IOException {
		popRoot = FXMLLoader.load(getClass().getResource("popUpWindow2.fxml"));
		popStage = (Stage)((Node)e.getSource()).getScene().getWindow();
		popScene = new Scene(popRoot);
		popScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		popStage.setScene(popScene);
		popStage.show();
	}
	public void switchScene3(MouseEvent e) throws IOException {
		popRoot = FXMLLoader.load(getClass().getResource("popUpWindow3.fxml"));
		popStage = (Stage)((Node)e.getSource()).getScene().getWindow();
		popScene = new Scene(popRoot);
		popScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		popStage.setScene(popScene);
		popStage.show();
	}
	public void switchScene4(MouseEvent e) throws IOException {
		popRoot = FXMLLoader.load(getClass().getResource("popUpWindow4.fxml"));
		popStage = (Stage)((Node)e.getSource()).getScene().getWindow();
		popScene = new Scene(popRoot);
		popStage.setScene(popScene);
		popStage.show();
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
		start.setDisable(true);
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
			
			map.setStart(checkPoint.getX(), checkPoint.getY());
			map.setGoal(checkPointNext.getX(), checkPointNext.getY());

			switch(choose_alg.getText()) {
			case "Astar":
				map.AStar(gc);
				break;
			case "Breath-First-Search":
				map.Breadth_first(gc);
				break;
			case "Dijkastra":
				map.dijkstra(gc);
				break;
			case "Depth-First-Search":
				map.depth_first(gc);
				break;
			default:
				return;
			}
			
			
			checkPoint = checkPointNext;
		}
		map.startAnimation(gc);
	}
	public void generateMaze(ActionEvent e) {
		//start.setDisable(true);
		//clear.setDisable(true);
		Maze maze = new Maze(map,gc);
		maze.generate();
		//start.setDisable(false);
		//clear.setDisable(false);
	}
}