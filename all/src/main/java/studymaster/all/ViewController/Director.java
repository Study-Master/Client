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
	private static Director director;
	private Stage stage;
	private Scene scene;
	
	/**
	 * Private constructor to prevent creating instance with new keyword
	 */
	private Director() {
		scene = null;
		stage = null;
	}

	/**
	 * Get an instance of Director class
	 * @return instance of Director class
	 */
	public static Director getInstance() {
		if(director==null) {
			director = new Director();
		}
		return director;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		stage.setResizable(false);
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public void showStage() {
		stage.show();
	}

	/**
	 * Change current stage with a new scene
	 * @param  newScene new scene
	 */
	public void pushStageWithScene(Scene newScene) {
		setScene(newScene);
		stage.setScene(scene);
		showStage();
	}

	public void pushStageWithFXML(URL fxmlFile) throws java.io.IOException{
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(fxmlFile);
       	setScene(new Scene(rootNode));
       	stage.setScene(scene);
       	showStage();
	}
}