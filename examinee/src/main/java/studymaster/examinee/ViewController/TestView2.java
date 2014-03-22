package studymaster.examinee.ViewController;

import studymaster.all.ViewController.TestViewController;
import javafx.fxml.FXML;

public class TestView2 extends TestViewController {
    boolean test2 = false;
    
    @Override public void onMessage(String message) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
    }
        
    @FXML @Override public void nextAction() {
        super.nextAction();
        if (test2) {
        director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
        }
    }
     
    @FXML @Override public void backAction() {
        super.backAction();
        director.pushStageWithFXML(getClass().getResource("/fxml/testView1.fxml"));
    }
    
    @Override public void test() {
        test2 = true;
    }
        
    @Override public void aftertest() {
    }
}
