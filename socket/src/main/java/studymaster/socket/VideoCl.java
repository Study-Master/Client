package studymaster.socket;

import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.channels.NotYetConnectedException;

public class VideoCl extends WebSocketClient {

	public VideoCl() throws URISyntaxException {
		this(new URI("ws://localhost:8088"));
	}

	private VideoCl(URI serverURI) {
        super(serverURI);
    }

	@Override
    public void onOpen(ServerHandshake handshakedata) {}

    @Override
    public void onClose(int code, String reason, boolean remote) {}

    @Override
    public void onMessage(String message) {}

    @Override
    public void onError(Exception ex) {}

    public void register() {
    	JSONObject msg = new JSONObject();
    	JSONObject content = new JSONObject();

    	msg.put("event", "register");
    	msg.put("endpoint", "test");
    	msg.put("content", content);

    	super.send(msg.toString());
    }
}