package studymaster.all.ViewController;

import javafx.fxml.Initializable;
import studymaster.socket.Connector;
import studymaster.socket.Callback;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
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
        systemErrorAlert("Connection lost.");
    }

    @Override public final void onError(Exception ex) {
        System.err.println("[err] ("+ getClass().getSimpleName() +" onError) An socket error has been caught");
        ex.printStackTrace();
    }
    
    protected final void systemErrorAlert(final String content) {
        final ViewController controller = this;
        AlertAction action = new AlertAction() {
            @Override public void ok(Stage stage) {
                if(!(controller instanceof LoginViewController)) {
                    director.pushStageWithFXML(ViewController.class.getResource("/fxml/loginView.fxml"));
                }
                else{}
                connector.renew();
                stage.close();
            }
        };
        director.invokeErrorAlert(content, action);
    }
}
