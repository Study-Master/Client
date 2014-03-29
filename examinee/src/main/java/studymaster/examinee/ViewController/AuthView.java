package studymaster.examinee.ViewController;

import studymaster.all.ViewController.ViewController;
import studymaster.socket.VideoCl;
import studymaster.socket.VideoEventHandler;
import studymaster.media.Webcamera;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import studymaster.examinee.Configure;

public class AuthView extends ViewController implements VideoEventHandler {

    @FXML private ImageView imgView;
    private VideoCl videoCl;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        VideoCl.setServer(Configure.VIDEOSERVER);
        videoCl = VideoCl.getInstance(this);
        videoCl.connect();
    }
    
    @Override public void onMessage(String message) {}

    @Override public void onVideoClientOpen() {
        Webcamera camera = Webcamera.getInstance();
        camera.startStreaming(imgView, videoCl);
    }
}
