package studymaster.socket;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.ByteBuffer;

public class AudioCl extends WebSocketClient {
    private static String localServer = null;
    private static String localSender = "Default Sender";
    private static String localEndpoint = "Default AudioCl";
    private AudioEventHandler handler;

    private AudioCl(URI serverURI, AudioEventHandler handler) {
        super(serverURI);
        localSender = Connector.getSender();
        localEndpoint = Connector.getEndpoint();
        this.handler = handler;
    }


    public static AudioCl getInstance(AudioEventHandler handler) {
        if(localServer == null || SoundUtil.getByteArray() == null || handler == null){
            throw new NullPointerException();
        }
        else {
            AudioCl instance = null;
            try {
                instance = new AudioCl(new URI(localServer), handler);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return instance;
        }
    }

    public static void setServer(String server) {
        localServer = server;
    }

    @Override public void onOpen(ServerHandshake handshakedata) {
        handler.onAudioClientOpen();
    }

    @Override public void onClose(int code, String reason, boolean remote) {}

    @Override public void onError(Exception ex){}

    @Override public void onMessage(String message) {}

    @Override public void onMessage(ByteBuffer message) {}

}
