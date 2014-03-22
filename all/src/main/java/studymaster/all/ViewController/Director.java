package studymaster.all.ViewController;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Director {
	private static Director instance = null;
	private static Stage localStage = null;
	private static Scene localScene = null;
	
	private Director() {
		localScene = null;
		localStage = null;
	}

	public static Director getInstance() {
		if (instance==null) {
			instance = new Director();
		}
		else {}
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

	public static void pushStageWithScene(Scene newScene) {
		setScene(newScene);
		localStage.setScene(localScene);
		showStage();
	}

	public static void pushStageWithFXML(final URL fxmlFile) {
		javafx.application.Platform.runLater(new Runnable() {
			@Override public void run() {
				try {
					FXMLLoader loader = new FXMLLoader();
					Parent rootNode = (Parent) loader.load(fxmlFile);
					setScene(new Scene(rootNode));
					localStage.setScene(localScene);
  					showStage();
  				} catch (Exception e) {
  					System.err.println("[err] (" + getClass().getSimpleName() + ") Error when switching scene");
  					e.printStackTrace();
  				}
  			}
  		});
	}

	public Stage initStageWithFXML(final URL fxmlFile) {
		Stage stage = null;
  		try {
  			stage = new Stage();
  			stage.setResizable(false);
  			FXMLLoader loader = new FXMLLoader();
  			Parent rootNode = (Parent) loader.load(fxmlFile);
  			stage.setScene(new Scene(rootNode));
  		} catch (Exception e) {
  			System.err.println("[err] (" + getClass().getSimpleName() + ") Error when switching scene");
  			e.printStackTrace();
  		}
  		return stage;
  	}

  	public Stage toggleStage(Stage stage) {
  		if(stage.isShowing()) {
  			stage.hide();
  		}
  		else {
  			stage.show();
  		}
  		return stage;
  	}
}