package studymaster.invigilator.ViewController;

import studymaster.socket.VideoSS;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import studymaster.all.ViewController.ViewController;

public class AuthView extends ViewController {

	@FXML protected ImageView imgView;

	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		VideoSS videoSS = VideoSS.getInstance();
		videoSS.start();
	}

	@Override
	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
	}
}