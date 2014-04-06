package studymaster.socket;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.util.Set;
import java.util.HashSet;
import studymaster.media.Sendable;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import studymaster.media.SoundUtil;

public class AudioCl extends WebSocketClient implements Sendable {
    private static AudioCl instance = null;
    private static String localServer = null;
    private static String localSender = "Default Sender";
    private static String localEndpoint = "Default AudioCl";
    private Set<AudioEventHandler> handlers;

    private AudioCl(URI serverURI) {
        super(serverURI);
        handlers = new HashSet();
        localSender = Connector.getSender();
        localEndpoint = Connector.getEndpoint();
    }


    public static AudioCl getInstance() {
        if(localServer == null){
            throw new NullPointerException();
        }
        else {
            AudioCl instance = null;
            try {
                instance = new AudioCl(new URI(localServer));
            } catch(Exception e) {
                e.printStackTrace();
            }
            return instance;
        }
    }

    public static void setServer(String server) {
        localServer = server;
    }

    public void addDelegate(AudioEventHandler handler) {
        handlers.add(handler);
    }

    @Override public void onOpen(ServerHandshake handshakedata) {}

    @Override public void onClose(int code, String reason, boolean remote) {}

    @Override public void onError(Exception ex){}

    @Override public void onMessage(String message) {}

    @Override public void onMessage(ByteBuffer message) {
        try {
            byte[] info = message.array();
            byte[] sender = new byte[50];
            byte[] media = new byte[info.length - 50];

            System.arraycopy(info, 0, sender, 0 ,50);
            System.arraycopy(info, 50, media, 0, info.length - 50);

            String name = new String(sender);
            name = name.trim();
            for(AudioEventHandler handler : handlers) {
                handler.onAudioMessage(name, media);
            }
        } catch(Exception e) {
            //ignore
        }
    }

    @Override public void sendMedia(byte[] media) {
        byte[] header = new byte[50];

        byte[] senderByte = localSender.getBytes(Charset.forName("UTF-8"));
        System.arraycopy(senderByte, 0, header, 0, senderByte.length);

        byte[] info = new byte[media.length + header.length];
        System.arraycopy(header, 0, info, 0, header.length);
        System.arraycopy(media, 0, info, header.length, media.length);
        send(info);
    }

    @Override public boolean isConnected() {
        return this.isOpen();
    }

}
