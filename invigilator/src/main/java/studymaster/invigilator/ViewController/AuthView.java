package studymaster.invigilator.ViewController;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.json.JSONObject;
import studymaster.all.ImgUtil;
import studymaster.all.ViewController.ViewController;
import studymaster.socket.VideoEventHandler;
import studymaster.socket.VideoSS;

public class AuthView extends ViewController implements VideoEventHandler {

	@FXML protected ImageView imgView1;
	@FXML protected ImageView imgView2;
	@FXML protected ImageView imgView3;
	private Map<WebSocket, ImageView> clients = null;
	private ArrayList<ImageView> imgViews;

	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		VideoSS.setDelegate(this);
		VideoSS videoSS = VideoSS.getInstance();
		videoSS.start();
		clients = new HashMap();
		imgViews = new ArrayList();
		imgViews.add(imgView1);
		imgViews.add(imgView2);
		imgViews.add(imgView3);
	}

	@Override
	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);

        JSONObject msg = new JSONObject(message);
        String event = msg.getString("event");
        String endpoint = msg.getString("endpoint");
        JSONObject content = msg.getJSONObject("content");

        if(event.equals("register") && clients.size() <= 3) {
        	clients.put(conn, imgViews.get(clients.size()));
        	System.out.println("registered");
        }
	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		clients.get(conn).setImage(ImgUtil.byteBufferToImage(message));
	}

	@Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }
}