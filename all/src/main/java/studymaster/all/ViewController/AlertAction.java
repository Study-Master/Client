package studymaster.all.ViewController;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

public abstract class AlertAction {

    public void ok(Stage stage){}

    public void ok(Stage stage, TextArea textarea){}

    public void cancel(Stage stage) {}

}
