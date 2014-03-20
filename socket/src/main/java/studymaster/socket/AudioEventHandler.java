package studymaster.socket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import java.nio.ByteBuffer;

public interface AudioEventHandler {

    public void onMessage(WebSocket conn, String message);

    public void onMessage(WebSocket conn, ByteBuffer message );

    public void onOpen(WebSocket conn, ClientHandshake handshake);

    public void onClose(WebSocket conn, int code, String reason, boolean remote);

    public void onError(WebSocket conn, Exception ex);
}