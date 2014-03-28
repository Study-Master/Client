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
import javafx.scene.layout.ColumnConstraints;

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
                gridPane.getColumnConstraints().addAll(new ColumnConstraints(30), new ColumnConstraints(300), new ColumnConstraints(30));
                gridPane.setAlignment(javafx.geometry.Pos.CENTER);
                gridPane.setHgap(20);

                GridPane.setConstraints(up, 1, 0);
                GridPane.setConstraints(message, 1, 1);
                GridPane.setHalignment(message, javafx.geometry.HPos.CENTER);
                GridPane.setConstraints(in, 1, 2);
                GridPane.setConstraints(button, 1, 3);
                GridPane.setHalignment(button, javafx.geometry.HPos.CENTER);
                GridPane.setConstraints(down, 1, 4);


                gridPane.getChildren().addAll(up, button, in, message, down);
                
                Scene alertScene = new Scene(gridPane);
                alertScene.getStylesheets().add("stylesheet/alert.css");

                alert.setScene(alertScene);
                alert.show();
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent event) {
                        action.ok(alert);
                    }
                });
            }
        });
    }

    public static void invokeInfoAlert(final String title, final String content, final AlertAction action) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override public void run() {
                final Stage alert = new Stage();
                alert.setFullScreen(false);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setResizable(false);

                Pane up = new Pane();
                up.setPrefSize(30, 30);
                
                Pane in = new Pane();
                in.setPrefSize(20, 20);
                Label titleLabel = new Label(title);
                titleLabel.setWrapText(true);
                titleLabel.setId("title");
                Label contentLabel = new Label(content);
                contentLabel.setWrapText(true);
                Pane down = new Pane();
                down.setPrefSize(30, 30);
               
                GridPane buttonPane = new GridPane();
                buttonPane.getColumnConstraints().addAll(new ColumnConstraints(15), new ColumnConstraints(100), new ColumnConstraints(100), new ColumnConstraints(15));
                buttonPane.setAlignment(javafx.geometry.Pos.CENTER);
                buttonPane.setHgap(20);
                Button buttonOK = new Button("OK");
                Button buttonCancel = new Button("Cancel");
                GridPane.setConstraints(buttonCancel, 1, 0);
                GridPane.setConstraints(buttonOK, 2, 0);
                buttonPane.getChildren().addAll(buttonCancel, buttonOK);

                GridPane gridPane = new GridPane();
                gridPane.getColumnConstraints().addAll(new ColumnConstraints(30), new ColumnConstraints(300), new ColumnConstraints(30));
                gridPane.setAlignment(javafx.geometry.Pos.CENTER);
                gridPane.setVgap(20);

                GridPane.setConstraints(up, 1, 0);
                GridPane.setConstraints(titleLabel, 1, 1);
                GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);
                GridPane.setConstraints(contentLabel, 1, 2);
                GridPane.setHalignment(contentLabel, javafx.geometry.HPos.CENTER);
                GridPane.setConstraints(buttonPane, 1, 3);
                GridPane.setHalignment(buttonPane, javafx.geometry.HPos.CENTER);
                GridPane.setConstraints(down, 1, 4);
                gridPane.getChildren().addAll(up, titleLabel, contentLabel, buttonPane, down);

                Scene alertScene = new Scene(gridPane);
                alertScene.getStylesheets().add("stylesheet/alert.css");

                alert.setScene(alertScene);
                alert.show();

                buttonOK.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent event) {
                        action.ok(alert);
                    }
                });
                buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent event) {
                        action.cancel(alert);
                    }
                });
            }
        });
    }
}
