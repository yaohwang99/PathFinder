package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PathFinderUI.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			Controller controller = loader.getController();
		    controller.drawInit();
		    controller.init_menu_button();
		    
			Image iconPic = new Image(getClass().getResource("icon.png").toURI().toString());
			primaryStage.getIcons().add(iconPic);		
			primaryStage.setTitle("Path Visualizer");
			
			
			//pop window
			Stage popStage = new Stage();
			FXMLLoader popLoader = new FXMLLoader(getClass().getResource("popUpWindow.fxml"));
			Parent popRoot = popLoader.load();
			Scene popScene = new Scene(popRoot);
			popScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			popStage.getIcons().add(iconPic);		
			popStage.setTitle("Path Visualizer Manual");
			popStage.setScene(popScene);
			
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			popStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
