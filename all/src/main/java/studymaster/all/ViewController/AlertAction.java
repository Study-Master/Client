package studymaster.all.ViewController;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

public abstract class AlertAction {

    public abstract void ok(Stage stage);

    public void cancel(Stage stage){}

}
