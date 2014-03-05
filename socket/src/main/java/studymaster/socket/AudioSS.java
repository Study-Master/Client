package studymaster.socket;

import java.util.Map;
import java.util.HashMap;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class AudioSS extends WebSocketServer {

	private static class defaultDelegate implements AudioEventHandler {
		public void onMessage( WebSocket conn, ByteBuffer message ) {
			System.out.println("[debug] (AudioSS.defaultDelegate onMessage): Receiving message, using default delegate.");
		}
	}
	private static AudioSS instance = null;
	private static AudioEventHandler localDelegate = null;
	private static Map<String, WebSocket> clients = null;

	public static AudioSS getInstance() {
		if(instance == null) {
			if(localDelegate == null)
				localDelegate = new defaultDelegate();
			String localhost = "0.0.0.0";
			int port = 8089;
			InetSocketAddress address = new InetSocketAddress(localhost, port);
			instance = new AudioSS(address);
		}
		return instance;
	}

	private AudioSS(InetSocketAddress address) {
        super(address);
        clients = new HashMap<String, WebSocket>();
    }

    private static void setDelegate(AudioEventHandler delegate) {
    	localDelegate = delegate;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("new connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {}

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
    	localDelegate.onMessage(conn, message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }
}