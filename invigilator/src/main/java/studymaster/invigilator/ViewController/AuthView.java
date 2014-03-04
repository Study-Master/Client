package studymaster.invigilator.ViewController;

import studymaster.socket.VideoSS;
import studymaster.socket.VideoEventHandler;
import org.java_websocket.WebSocket;
import java.nio.ByteBuffer;
import javafx.scene.image.ImageView;
import studymaster.all.ImgUtil;
import javafx.fxml.FXML;
import studymaster.all.ViewController.ViewController;

public class AuthView extends ViewController implements VideoEventHandler {

	@FXML protected ImageView imgView;

	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		VideoSS.setDelegate(this);
		VideoSS videoSS = VideoSS.getInstance();
		videoSS.start();
	}

	@Override
	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
	}

	public void onMessage( WebSocket conn, ByteBuffer message ) {
		imgView.setImage(ImgUtil.byteBufferToImage(message));
	}
}