package studymaster.socket;

import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import studymaster.media.ImgUtil;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.channels.NotYetConnectedException;

public class VideoCl extends WebSocketClient {
    private static String localServer = null;
    private static String localSender = "Default Sender";
    private static String localEndpoint = "Default VideoCl";
    private static ImageView imgView = null;

    private VideoCl(URI serverURI) {
        super(serverURI);
        localSender = Connector.getSender();
        localEndpoint = Connector.getEndpoint();
    }

    public static VideoCl getInstance() {
        if(localServer == null && imgView == null)
            return null;
        else {
            VideoCl instance = null;
            try { 
                instance = new VideoCl(new URI(localServer));
            } catch(Exception e) {
                instance = null;
            }
            return instance;
        }
    }

    public static void setServer(String server) {
        localServer = server;
    }

    public static void setImageView(ImageView imageView) {
        imgView = imageView;
    }

    @Override public void onOpen(ServerHandshake handshakedata) {}

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
}
