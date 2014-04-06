package studymaster.socket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import studymaster.media.ImgUtil;
import studymaster.media.Sendable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.awt.image.BufferedImage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.channels.NotYetConnectedException;

public class VideoCl extends WebSocketClient implements Sendable {
    private String localSender = "Default Sender";
    private static String localEndpoint = "Default VideoCl";
    private Map<String, ImageView> videoImg;
    private VideoEventHandler handler;

    private VideoCl(URI serverURI, VideoEventHandler handler) {
        super(serverURI);
        localSender = Connector.getSender();
        localEndpoint = Connector.getEndpoint();
        this.videoImg = new HashMap<String, ImageView>();
        this.handler = handler;
    }

    public static VideoCl getInstance(String server, VideoEventHandler handler) {
        if(server == null || handler == null) {
            throw new NullPointerException();
        }
        else {
            VideoCl instance = null;
            try { 
                instance = new VideoCl(new URI(server), handler);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return instance;
        }
    }

    public void setImageView(String name, ImageView video) {
        videoImg.put(name, video);
    }

    public boolean containsImageView(ImageView imageView) {
        return videoImg.containsValue(imageView);
    }

    @Override public void onOpen(ServerHandshake handshakedata) {}

    @Override public void onClose(int code, String reason, boolean remote) {
        handler.onVideoClientClose(code, reason, remote);        
    }

    @Override public void onMessage(String message) {}

    @Override public void onError(Exception ex) {}

    @Override public void onMessage(ByteBuffer message) {
        try {
            byte[] info = message.array();
            byte[] sender = new byte[50];
            byte[] img = new byte[info.length - 50];

            System.arraycopy(info, 0, sender, 0, 50);
            System.arraycopy(info, 50, img, 0, info.length-50);
           
            String senderName = new String(sender, "UTF-8").trim();
            
            ImageView imgView;
            imgView = videoImg.get(senderName);
            if(imgView==null) {
                System.out.println("[info] (VideoCl onMessage) Receive image but unset image view."); 
            }
            else {
                Image image = ImgUtil.byteToImage(img);
                if(image!=null) {
                    imgView.setImage(image);
                }
                else {}
            }
        } catch(Exception e) {
            //Ignore
        }
    }

    @Override public boolean isConnected() {
        return this.isOpen();
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
}
