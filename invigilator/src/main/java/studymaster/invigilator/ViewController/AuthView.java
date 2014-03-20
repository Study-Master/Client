package studymaster.invigilator.ViewController;

<<<<<<< HEAD
import studymaster.invigilator.App;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javafx.scene.control.Button;
=======
import java.nio.ByteBuffer;
import java.util.ArrayList;
>>>>>>> Audio-Test
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.json.JSONObject;
<<<<<<< HEAD
import javafx.scene.image.Image;
=======
>>>>>>> Audio-Test
import studymaster.socket.ImgUtil;
import studymaster.all.ViewController.ViewController;
import studymaster.socket.VideoEventHandler;
import studymaster.socket.VideoSS;
<<<<<<< HEAD
import studymaster.invigilator.Slot;

public class AuthView extends ViewController implements VideoEventHandler {
	@FXML ImageView imgView0;
	@FXML Button authButton0;
	@FXML Button terminateButton0;
	@FXML ImageView imgView1;
	@FXML Button authButton1;
	@FXML Button terminateButton1;
	@FXML ImageView imgView2;
	@FXML Button authButton2;
	@FXML Button terminateButton2;
	private ArrayList<Slot> slots;
	private Map<WebSocket, Slot> clientSlots;
=======

public class AuthView extends ViewController implements VideoEventHandler {

	@FXML protected ImageView imgView1;
	@FXML protected ImageView imgView2;
	@FXML protected ImageView imgView3;
	private Map<WebSocket, ImageView> clients = null;
	private ArrayList<ImageView> imgViews;
>>>>>>> Audio-Test

	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		VideoSS.setDelegate(this);
		VideoSS videoSS = VideoSS.getInstance();
		videoSS.start();
<<<<<<< HEAD

		Slot s0 = new Slot(imgView0, authButton0, terminateButton0);
		Slot s1 = new Slot(imgView1, authButton1, terminateButton1);
		Slot s2 = new Slot(imgView1, authButton2, terminateButton2);

		slots = new ArrayList(3);
		clientSlots = new HashMap();

		slots.add(s0);
		slots.add(s1);
		slots.add(s2);
=======
		clients = new HashMap();
		imgViews = new ArrayList();
		imgViews.add(imgView1);
		imgViews.add(imgView2);
		imgViews.add(imgView3);
>>>>>>> Audio-Test
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

<<<<<<< HEAD
        if(event.equals("register") && getAvailableSlot()!= null ) {
        	clientSlots.put(conn, getAvailableSlot());
=======
        if(event.equals("register") && clients.size() <= 3) {
        	clients.put(conn, imgViews.get(clients.size()));
>>>>>>> Audio-Test
        	reContent.put("status", "success");
        }
        conn.send(reMsg.toString());
	}

	@Override
<<<<<<< HEAD
	public void onMessage(WebSocket conn, ByteBuffer message) {
		clientSlots.get(conn).imageView.setImage(ImgUtil.byteBufferToImage(message));
=======
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		clients.get(conn).setImage(ImgUtil.byteBufferToImage(message));
>>>>>>> Audio-Test
	}

	@Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    	System.out.println("New connection.");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
<<<<<<< HEAD
		System.out.println("[info] (AuthView onClose) One connection closed.");
		ImageView closedClientImageView = clientSlots.remove(conn).imageView;
		Image image = new Image("/image/user.png");
		closedClientImageView.setImage(image);
=======

>>>>>>> Audio-Test
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }
<<<<<<< HEAD

	private Slot getAvailableSlot() {
		for(int i=0; i<3; i++) {
			if(!clientSlots.containsValue(slots.get(i)))
				return slots.get(i);
		}
		return null;
	}
}
=======
}
>>>>>>> Audio-Test
