package studymaster.examinee.ViewController;

import studymaster.all.ViewController.ViewController;
import studymaster.socket.VideoCl;
import studymaster.socket.VideoEventHandler;
import studymaster.media.Webcamera;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import studymaster.examinee.Configure;

public class AuthView extends ViewController implements VideoEventHandler {

    @FXML private ImageView imgView;
    @FXML private Button startButton;
    private VideoCl videoCl;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        VideoCl.setServer(Configure.VIDEOSERVER);
        videoCl = VideoCl.getInstance(this);
        startButton.setDisable(true);
        videoCl.connect();
    }
    
    @Override public void onMessage(String message) {}

    @Override public void onVideoClientOpen() {
        System.out.println("[info] (" + getClass().getSimpleName() + " onVideoClientOpen)");
        Webcamera camera = Webcamera.getInstance();
        camera.startStreaming(imgView, videoCl);
    }
}
