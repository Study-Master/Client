package studymaster.all.ViewController;

import javafx.fxml.Initializable;
import studymaster.socket.Connector;
import studymaster.socket.Callback;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Label;

public abstract class ViewController implements Initializable, Callback {
    protected Director director = null;
    protected Connector connector = null;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" initialize) Load fxml file from " + location);

        javafx.scene.text.Font.loadFont(ViewController.class.getResource("/font/HelveticaLT25UltraLight.ttf").toExternalForm(), 10);
        javafx.scene.text.Font.loadFont(ViewController.class.getResource("/font/HelveticaLT45Light.ttf").toExternalForm(), 10);
        
        Connector.setDelegate(this);
        
        director = Director.getInstance();
        connector = Connector.getInstance();
    }

    @Override public final void onOpen(short httpStatus, String httpStatusMessage) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onOpen) Socket's connection established");
    }

    @Override public final void onClose(int code, String reason, boolean remote) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onClose) Socket's connection closed");
        systemErrorAlert("Connection has been closed");
    }

    @Override public final void onError(Exception ex) {
        System.err.println("[err] ("+ getClass().getSimpleName() +" onError) An socket error has been caught");
        ex.printStackTrace();
    }
    
    protected final void systemErrorAlert(final String content) {
        AlertAction action = new AlertAction() {
            @Override public void sceneStyle(Scene scene) {
                scene.getStylesheets().add("stylesheet/alert.css");
            }

            @Override public void gridPaneStyle(GridPane gridPane) {
                gridPane.getColumnConstraints().add(new ColumnConstraints(300));
                gridPane.setAlignment(javafx.geometry.Pos.CENTER);
                gridPane.setHgap(20);
            }

            @Override public void onAction(Stage stage) {
                connector.renew();
                stage.close();
            }
        };
        director.invokeErrorAlert(content, action);
    }
}
