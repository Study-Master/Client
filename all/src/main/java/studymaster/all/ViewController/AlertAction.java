package studymaster.all.ViewController;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

public interface AlertAction {

    public abstract void ok(Stage stage);

    public abstract void cancel(Stage stage);

}
