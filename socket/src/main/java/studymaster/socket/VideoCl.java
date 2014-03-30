package studymaster.socket;

import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import studymaster.media.ImgUtil;
import studymaster.media.Sendable;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.channels.NotYetConnectedException;

public class VideoCl extends WebSocketClient implements Sendable {
    private static String localServer = null;
    private static String localSender = "Default Sender";
    private static String localEndpoint = "Default VideoCl";
    private ImageView imgView = null;
    private VideoEventHandler handler;

    private VideoCl(URI serverURI, VideoEventHandler handler) {
        super(serverURI);
        localSender = Connector.getSender();
        localEndpoint = Connector.getEndpoint();
        this.handler = handler;
    }

    public static VideoCl getInstance(VideoEventHandler handler) {
        if(localServer == null || handler == null) {
            throw new NullPointerException();
        }
        else {
            VideoCl instance = null;
            try { 
                instance = new VideoCl(new URI(localServer), handler);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return instance;
        }
    }

    public static void setServer(String server) {
        localServer = server;
    }

    public void setImageView(ImageView imageView) {
        imgView = imageView;
    }

    @Override public void onOpen(ServerHandshake handshakedata) {
        handler.onVideoClientOpen();
    }

    @Override public void onClose(int code, String reason, boolean remote) {}

    @Override public void onMessage(String message) {}

    @Override public void onError(Exception ex) {}

    @Override public void onMessage(ByteBuffer message) {
        if(imgView==null) {}
        else {
            Image image = ImgUtil.byteBufferToImage(message);
            if(image!=null) {
                imgView.setImage(image);
            }
            else {}
        }
    }

    @Override public boolean isConnected() {
        return this.isOpen();
    }

    @Override public void sendMedia(byte[] media) {
        send(media);
    }
}
