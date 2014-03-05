package studymaster.socket;

import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.channels.NotYetConnectedException;

public class AudioCl extends WebSocketClient {

	public AudioCl() throws URISyntaxException {
		this(new URI("ws://localhost:8089"));
	}

	private AudioCl(URI serverURI) {
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
}