package studymaster.socket;

import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.channels.NotYetConnectedException;

public class VideoCl extends WebSocketClient {
	private static VideoCl instance = null;
	private static String localServer = null;
	private static String localSender = "Default Sender";
	private static String localEndpoint = "Default VideoCl";
	private static ImageView imgView = null;
	private static Webcam webcam;
	
	private VideoCl(URI serverURI) {
        	super(serverURI);
	}

	public static VideoCl getInstance() {
		if(localServer == null && imgView == null)
			return null;
		else {
			if(instance == null) {
				try {
					instance = new VideoCl(new URI(localServer));
				} catch(Exception e) {
					instance = null;
				}
				return instance;
			}
			return instance;
		}
	}

	public static void setServer(String server) {
		localServer = server;
	}

	public static void setSender(String sender) {
		localSender = sender;
	}

	public static void setEndpoint(String endpoint) {
		localEndpoint = endpoint;
	}

	public static void setImageView(ImageView imageView) {
		imgView = imageView;
	}

	public static void updateImageView(Image image) {
		imgView.setImage(image);
	}

	@Override
    public void onOpen(ServerHandshake handshakedata) {
    	request();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {}

    @Override
    public void onMessage(String message) {
    	System.out.println("[info] (VideoCl onMessage) Receive message: " + message);
		JSONObject msg = new JSONObject(message);

		String event = msg.getString("event");
		String endpoint = msg.getString("endpoint");
		JSONObject content = msg.getJSONObject("content");

		if(event.equals("register")) {
			String status = content.getString("status");
			if(status.equals("success"))
				videoStart();
		}
	}

	@Override
    public void onError(Exception ex) {}

    private void request() {
    	JSONObject msg = new JSONObject();
    	JSONObject content = new JSONObject();
    	msg.put("event", "register");
    	msg.put("endpoint", localEndpoint);
    	msg.put("content", content);
		super.send(msg.toString());
	}

	private BufferedImage processImage() {
		if(webcam==null)
			webcam = Webcam.getDefault();
		if(!webcam.isOpen())
			webcam.open();
		return webcam.getImage();
	}

	public void videoStart() {
		Thread videoStream = new Thread() {
			@Override
			public void run(){
				while(true) {
					VideoCl videoClient = VideoCl.getInstance();
					try {
						BufferedImage bufferedImage = videoClient.processImage();
						videoClient.updateImageView(ImgUtil.createImage(bufferedImage));

						byte[] byteImage = ImgUtil.toByte(bufferedImage);
						videoClient.send(byteImage);
						Thread.sleep(200);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		videoStream.run();
	}
}
