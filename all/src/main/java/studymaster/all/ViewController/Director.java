package studymaster.all.ViewController;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public final class Director {
    private static Director instance = null;
    private static Stage localStage = null;
    private static Scene localScene = null;
    
    private Director() {
        System.out.println("[info] (" + Director.class.getSimpleName() + " Director) Create director instance");
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
        System.out.println("[info] (" + Director.class.getSimpleName() + " pushStageWithScene) Switch scene to a new scene");
        setScene(newScene);
        localStage.setScene(localScene);
        showStage();
    }

    public static void pushStageWithFXML(final URL fxmlFile) {
        System.out.println("[info] (" + Director.class.getSimpleName() + " pushStageWithFXML) Switch scene to a new scene from " + fxmlFile);
        javafx.application.Platform.runLater(new Runnable() {
            @Override public void run() {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    Parent rootNode = (Parent) loader.load(fxmlFile);
                    setScene(new Scene(rootNode));
                    localStage.setScene(localScene);
                    showStage();
                } catch (Exception e) {
                    System.err.println("[err] (" + Director.class.getSimpleName() + ") Error when switch scene");
                    e.printStackTrace();
                }
            }
        });
    }

    public static Stage initStageWithFXML(final URL fxmlFile) {
        System.out.println("[info] (" + Director.class.getSimpleName() + " initStageWithFXML) Create a new scene from " + fxmlFile);
        Stage stage = null;
        try {
            stage = new Stage();
            stage.setResizable(false);
            FXMLLoader loader = new FXMLLoader();
            Parent rootNode = (Parent) loader.load(fxmlFile);
            stage.setScene(new Scene(rootNode));
        } catch (Exception e) {
            System.err.println("[err] (" + Director.class.getSimpleName() + ") Error when switch scene");
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

    public static void invokeErrorAlert(final String error, final AlertAction action) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override public void run() {
                final Stage alert = new Stage();
                action.alertStyle(alert);
                alert.setFullScreen(false);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setResizable(false);

                Pane up = new Pane();
                up.setPrefSize(30, 30);
                Button button = new Button("OK");
                Pane in = new Pane();
                in.setPrefSize(20, 20);
                Label message = new Label(error);
                message.setWrapText(true);
                Pane down = new Pane();
                down.setPrefSize(30, 30);

                GridPane gridPane = new GridPane();
                action.gridPaneStyle(gridPane);

                GridPane.setConstraints(up, 0, 0);
                GridPane.setConstraints(message, 0, 1);
                GridPane.setHalignment(message, javafx.geometry.HPos.CENTER);
                GridPane.setConstraints(in, 0, 2);
                GridPane.setConstraints(button, 0, 3);
                GridPane.setHalignment(button, javafx.geometry.HPos.CENTER);
                GridPane.setConstraints(down, 0, 4);

                gridPane.getChildren().addAll(up, button, in, message, down);
                
                Scene alertScene = new Scene(gridPane);
                action.sceneStyle(alertScene);

                alert.setScene(alertScene);
                alert.show();
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent event) {
                        action.onAction(alert);
                    }
                });
            }
        });
    }
}
