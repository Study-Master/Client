package studymaster.all.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public abstract class TestViewController extends ViewController {
    @FXML protected TextField accountField;
    @FXML protected Label nameField;
    
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources){
        super.initialize(location, resources);
        nameField.setText(connector.getSender());
    }

    @FXML public final void testAction() {
        System.out.println("[info] (" + getClass().getSimpleName() + " testAction): Trying to test...");
        test();
    }
        
    @FXML public final void aftertestAction() {
        System.out.println("[info] (" + getClass().getSimpleName() + " testAction): Finishing test...");
        aftertest();
    }

    @FXML public final void exitAction() {
        System.out.println("[info] (" + getClass().getSimpleName() + " exitAction): Exiting...");
        System.exit(0);
    }

    @FXML public void backAction() {
        System.out.println("[info] (" + getClass().getSimpleName() + " backAction): back to previous page");
    }
 
    @FXML public void nextAction() {
        System.out.println("[info] (" + getClass().getSimpleName() + " nextAction): go to next test page");
    }
     
    public abstract void test();
    public abstract void aftertest();
}
