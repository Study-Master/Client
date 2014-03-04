package studymaster.socket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import java.nio.ByteBuffer;

public interface VideoEventHandler {

    public void onMessage( WebSocket conn, ByteBuffer message );

}