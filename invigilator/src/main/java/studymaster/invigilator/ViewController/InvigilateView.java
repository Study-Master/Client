package studymaster.invigilator.ViewController;

import studymaster.all.ViewController.ViewController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import studymaster.socket.VideoSS;
import studymaster.socket.VideoEventHandler;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import java.nio.ByteBuffer;
import javafx.stage.Stage;

public class InvigilateView extends ViewController implements VideoEventHandler {

	@FXML private ImageView imgView0;
	@FXML private ImageView screenView0;
	@FXML private Button chatButton0;
	@FXML private Button terminateButton0;
	private Stage chatWindow0;

	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		VideoSS.setDelegate(this);
		VideoSS videoSS = VideoSS.getInstance();
		chatWindow0 = director.initStageWithFXML(getClass().getResource("/fxml/chatView.fxml"));
		chatWindow0.setResizable(false);
	}

	@Override
	public void onMessage(String message){}

	public void onMessage(WebSocket conn, String message){}

    public void onMessage(WebSocket conn, ByteBuffer message ){}

    public void onOpen(WebSocket conn, ClientHandshake handshake){}

    public void onClose(WebSocket conn, int code, String reason, boolean remote){}

    public void onError(WebSocket conn, Exception ex){}

	public void chatAction0() {
		System.out.println("[info] (" + getClass().getSimpleName() + " chatAction0)");
		director.toggleStage(chatWindow0);
	}
}