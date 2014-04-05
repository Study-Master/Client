package studymaster.examinee.ViewController;

import studymaster.all.ViewController.ViewController;
import studymaster.examinee.Configure;
import studymaster.socket.VideoCl;
import studymaster.socket.VideoEventHandler;
import studymaster.media.Webcamera;
import studymaster.media.ScreenCapture;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import studymaster.examinee.Configure;
import org.json.JSONObject;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class AuthView extends ViewController implements VideoEventHandler {

    @FXML private ImageView imgView;
    @FXML private Button startButton;
    @FXML private Label label1;
    @FXML private Label label2;
    private VideoCl videoCl;
    private VideoCl screenCl;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        startButton.setVisible(false);
        label1.setText("Please keep facing the webcam.");
        label2.setText("Authentication takes about one minute.");
        videoCl = VideoCl.getInstance(Configure.VIDEOSERVER, this);
        screenCl = VideoCl.getInstance(Configure.SERVER, this);
        videoCl.connect();
        screenCl.connect();
        Webcamera camera = Webcamera.getInstance();
        camera.startStreaming(imgView, videoCl);
        ScreenCapture sc = ScreenCapture.getInstance();
        sc.captureStreaming(null, screenCl);
        startButton.setDisable(true);
    }
    
    @Override public void onMessage(String message) {
        JSONObject msg = new JSONObject(message);
        String event = msg.getString("event");
        if (event.equals("auth_successful")) {
            label1.setText("");
            label2.setText("You may click the \"Start\" button to start exam now.");
            startButton.setVisible(true);    
            startButton.setDisable(false);
            startButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    director.pushStageWithFXML(getClass().getResource("/fxml/examView.fxml"));
                }
            });
        }
    }

    @Override public void onVideoClientOpen() {
        System.out.println("[info] (" + getClass().getSimpleName() + " onVideoClientOpen)");
    }
}
