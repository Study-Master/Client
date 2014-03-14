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
import studymaster.socket.ImgUtil;
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

        JSONObject reMsg = new JSONObject();
        JSONObject reContent = new JSONObject();
        reMsg.put("event", event);
        reMsg.put("endpoint", "Invigilator Video Server");
        reMsg.put("content", reContent);

        if(event.equals("register") && clients.size() <= 3) {
        	clients.put(conn, imgViews.get(clients.size()));
        	reContent.put("status", "success");
        }
        conn.send(reMsg.toString());
	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		clients.get(conn).setImage(ImgUtil.byteBufferToImage(message));
	}

	@Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    	System.out.println("New connection.");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }
}