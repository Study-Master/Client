package studymaster.socket;

import java.util.Map;
import java.util.HashMap;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;

public class VideoSS extends WebSocketServer {

	@FXML protected ImageView imgView;

	private static VideoSS instance;
	private Map<String, WebSocket> clients;

	public static VideoSS getInstance() {
		if(instance == null) {
			String localhost = "0.0.0.0";
			int port = 8088;
			InetSocketAddress address = new InetSocketAddress(localhost, port);
			instance = new VideoSS(address);
		}
		return instance;
	}

	private VideoSS(InetSocketAddress address) {
        super(address);
        clients = new HashMap<String, WebSocket>();
        System.out.println("Start server on " + address.getAddress() + " port " + address.getPort());
    }

    public void setImgView(ImageView imgView) {
    	this.imgView = imgView;
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
        System.out.println("receive img");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }
}