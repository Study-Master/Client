package studymaster.examinee.ViewController;

import studymaster.all.ViewController.TestViewController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import studymaster.media.Webcamera;

public class TestView2 extends TestViewController {

    @FXML ImageView imageView;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        Webcamera webcam = Webcamera.getInstance();
        webcam.video(imageView);
    }
    
    @Override public void onMessage(String message) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
    }
        
    @FXML @Override public void nextAction() {
        super.nextAction();
        Webcamera webcam = Webcamera.getInstance();
        webcam.stop();
        director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
    }
     
    @FXML @Override public void backAction() {
        super.backAction();
        Webcamera webcam = Webcamera.getInstance();
        webcam.stop();
        director.pushStageWithFXML(getClass().getResource("/fxml/testView1.fxml"));
    }
}
