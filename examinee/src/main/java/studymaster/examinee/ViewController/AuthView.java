package studymaster.examinee.ViewController;

import studymaster.all.ViewController.ViewController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.fxml.FXML;
import studymaster.examinee.App;
import studymaster.socket.Connector;
import studymaster.socket.VideoCl;

public class AuthView extends ViewController {

	@FXML protected ImageView imgView;

	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		connector.auth();
	}
	
	@Override
	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
		VideoCl.setServer("ws://localhost:8089");
		VideoCl.setImageView(imgView);
		VideoCl videoClient = VideoCl.getInstance();
		videoClient.connect();
	}
}