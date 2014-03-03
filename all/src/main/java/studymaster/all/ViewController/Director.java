package studymaster.all.ViewController;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class to control the stage and scene
 */
public final class Director {
	private static Director instance;
	private static Stage localStage;
	private static Scene localScene;
	
	/**
	 * Private constructor to prevent creating instance with new keyword
	 */
	private Director() {
		localScene = null;
		localStage = null;
	}

	/**
	 * Get an instance of Director class
	 * @return instance of Director class
	 */
	public static Director getInstance() {
		if(instance==null) {
			instance = new Director();
		}
		return instance;
	}

	public static void setStage(Stage stage) {
		localStage = stage;
		localStage.setResizable(false);
	}

	public static void setScene(Scene scene) {
		localScene = scene;
	}

	public static void showStage() {
		localStage.show();
	}

	public static Scene getScene() {
		return localScene;
	}

	/**
	 * Change current stage with a new scene
	 * @param  newScene new scene
	 */
	public static void pushStageWithScene(Scene newScene) {
		setScene(newScene);
		localStage.setScene(localScene);
		showStage();
	}

	/**
	 * Change current stage with a fxml file
	 * @param fxmlFile url to fxml file
	 */
	public static void pushStageWithFXML(final URL fxmlFile) {
       	javafx.application.Platform.runLater(new Runnable() {
  			@Override
  			public void run() {
  				try {
  					FXMLLoader loader = new FXMLLoader();
  					Parent rootNode = (Parent) loader.load(fxmlFile);
  					setScene(new Scene(rootNode));
  					localStage.setScene(localScene);
  					showStage();
  				} catch (Exception e) {
  					System.out.println(e);
  					System.err.println("[err] (Director) Error when switching scene");
  				}
  			}
  		});
	}
}