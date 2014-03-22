package studymaster.examinee.ViewController;

import studymaster.all.ViewController.TestViewController;
import javafx.fxml.FXML;

public class TestView2 extends TestViewController {

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        //TODO: video test
    }
    
    @Override public void onMessage(String message) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
    }
        
    @FXML @Override public void nextAction() {
        super.nextAction();
        director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
    }
     
    @FXML @Override public void backAction() {
        super.backAction();
        director.pushStageWithFXML(getClass().getResource("/fxml/testView1.fxml"));
    }
}
