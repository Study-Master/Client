package studymaster.socket;

import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.nio.ByteBuffer;

public class AudioSS extends WebSocketServer {

    private static class defaultDelegate implements AudioEventHandler {
        public void onMessage( WebSocket conn, ByteBuffer message ) {
            System.out.println("[debug] (AudioSS.defaultDelegate onMessage): Receiving byte message, using default delegate.");
        }

        public void onMessage(WebSocket conn, String message) {
            System.out.println("[debug] (AudioSS.defaultDelegate onMessage): Receiving message, using default delegate.");
        }

        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            System.out.println("[debug] (AudioSS.defaultDelegate onOpen): Open new connection, using default delegate.");
        }

        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("[debug] (AudioSS.defaultDelegate onClose): Close connection, using default delegate.");
        }

        public void onError(WebSocket conn, Exception ex) {
            System.err.println("[err] (AudioSS.defaultDelegate onError): An error, using default delegate.");
        }
    }
    private static AudioSS instance = null;
    private static AudioEventHandler localDelegate = null;


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
    }

    public static void setDelegate(AudioEventHandler delegate) {
        localDelegate = delegate;
    }

    //Override methods
    @Override public void onOpen(WebSocket conn, ClientHandshake handshake) {
        localDelegate.onOpen(conn, handshake);
    }

    @Override public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        localDelegate.onClose(conn, code, reason, remote);
    }

    @Override public void onMessage(WebSocket conn, String message) {
        localDelegate.onMessage(conn, message);
    }

    @Override public void onMessage( WebSocket conn, ByteBuffer message ) {
        localDelegate.onMessage(conn, message);
    }

    @Override public void onError(WebSocket conn, Exception ex) {
        localDelegate.onError(conn, ex);
    }

    //Methods called by audioview from invigilators
    public static void setByteArray(ByteBuffer temp) {
        SoundUtil.setByteArray(temp);
    }

    public static void playAudio() {
        SoundUtil.playAudio();
    }
}
