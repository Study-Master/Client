package studymaster.invigilator.ViewController;

import studymaster.invigilator.App;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javafx.scene.control.Button;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.json.JSONObject;
import javafx.scene.image.Image;
import studymaster.socket.ImgUtil;
import studymaster.all.ViewController.ViewController;
import studymaster.socket.VideoEventHandler;
import studymaster.socket.VideoSS;

public class AuthView extends ViewController implements VideoEventHandler {
	private class Slot {
		protected ImageView imageView;
		protected Button authButton;
		protected Button terminateButton;
		public Slot(ImageView imageView, Button authButton, Button terminateButton) {
			this.imageView = imageView;
			this.authButton = authButton;
			this.terminateButton = terminateButton;
		}
	}
	@FXML ImageView imgView0;
	@FXML Button authButton0;
	@FXML ImageView imgView1;
	@FXML Button authButton1;
	@FXML ImageView imgView2;
	@FXML Button authButton2;
	private ArrayList<Slot> slots;
	private Map<WebSocket, Slot> clientSlots;

	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		VideoSS.setDelegate(this);
		VideoSS videoSS = VideoSS.getInstance();
		videoSS.start();

		Slot s1 = new Slot(imgView0, authButton0, null);
		Slot s2 = new Slot(imgView1, authButton1, null);
		Slot s3 = new Slot(imgView1, authButton2, null);

		slots = new ArrayList();
		clientSlots = new HashMap();

		slots.add(s1);
		slots.add(s2);
		slots.add(s3);
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

        if(event.equals("register") && getAvailableSlot()!= null ) {
        	clientSlots.put(conn, getAvailableSlot());
        	reContent.put("status", "success");
        }
        conn.send(reMsg.toString());
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		clientSlots.get(conn).imageView.setImage(ImgUtil.byteBufferToImage(message));
	}

	@Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    	System.out.println("New connection.");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("[info] (AuthView onClose) One connection closed.");
		ImageView closedClientImageView = clientSlots.remove(conn).imageView;
		Image image = new Image("/image/user.png");
		closedClientImageView.setImage(image);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

	private Slot getAvailableSlot() {
		for(int i=0; i<3; i++) {
			if(!clientSlots.containsValue(slots.get(i)))
				return slots.get(i);
		}
		return null;
	}
}
