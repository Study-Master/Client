package studymaster.all.ViewController;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

public abstract class AlertAction {

    public void sceneStyle(Scene scene){}

    public void alertStyle(Stage stage){}

    public void gridPaneStyle(GridPane gridPane){}

    public abstract void onAction(Stage stage);

    public void cancel(Stage stage){}

}
