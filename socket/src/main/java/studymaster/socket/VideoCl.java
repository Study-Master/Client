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
    private static String localServer = null;
    private static String localSender = "Default Sender";
    private static String localEndpoint = "Default VideoCl";
    private String flag;
    private Map<String, ImageView> videoImg;
    private Map<String, ImageView> screenImg;
    public ImageView testView;
    private VideoEventHandler handler;

    private VideoCl(URI serverURI, VideoEventHandler handler, String flag) {
        super(serverURI);
        localSender = Connector.getSender();
        localEndpoint = Connector.getEndpoint();
        this.flag = flag;
        this.videoImg = new HashMap<String, ImageView>();
        this.screenImg = new HashMap<String, ImageView>();
        this.handler = handler;
    }

    public static VideoCl getInstance(VideoEventHandler handler, String flag) {
        if(localServer == null || handler == null) {
            throw new NullPointerException();
        }
        else {
            VideoCl instance = null;
            try { 
                instance = new VideoCl(new URI(localServer), handler, flag);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return instance;
        }
    }

    public static void setServer(String server) {
        localServer = server;
    }

    public void setImageView(String name, ImageView video, ImageView screen) {
        videoImg.put(name, video);
        screenImg.put(name, screen);      
    }

    public boolean containsImageView(ImageView imageView) {
        return (videoImg.containsValue(imageView) || screenImg.containsValue(imageView));
    }

    @Override public void onOpen(ServerHandshake handshakedata) {
        handler.onVideoClientOpen();
    }

    @Override public void onClose(int code, String reason, boolean remote) {}

    @Override public void onMessage(String message) {}

    @Override public void onError(Exception ex) {}

    @Override public void onMessage(ByteBuffer message) {
        try {
            byte[] info = message.array();
            byte[] sender = new byte[50];
            byte[] type = new byte[50];
            byte[] img = new byte[info.length - 100];

            System.arraycopy(info, 0, sender, 0, 50);
            System.arraycopy(info, 50, type, 0, 50);
            System.arraycopy(info, 100, img, 0, info.length-100);
            
            String senderName = "s";
            String typeFlag = "video";
            
            ImageView imgView;
            if(typeFlag.equals("video")) {
                imgView = videoImg.get(senderName);
            }
            else if(typeFlag.equals("screen")) {
                imgView = screenImg.get(senderName);
            }
            else {
                imgView = null;
            }

            //if(imgView==null) {
              //  System.out.println("[info] (VideoCl onMessage) Receive image but unset image view."); 
            //}
            //else {
                Image image = ImgUtil.byteToImage(img);
                if(image!=null) {
                    System.out.println("Decode successfully.");
                    //imgView.setImage(image);
                    testView.setImage(image);
                }
                else {}
            //}
        } catch(Exception e) {
            //Ignore
        }
    }

    @Override public boolean isConnected() {
        return this.isOpen();
    }

    @Override public void sendMedia(byte[] media) {
        byte[] header = new byte[100];

        byte[] senderByte = localSender.getBytes(Charset.forName("UTF-8"));
        System.arraycopy(senderByte, 0, header, 0, senderByte.length);

        byte[] flagByte = flag.getBytes(Charset.forName("UTF-8"));
        System.arraycopy(flagByte, 0, header, 50, flagByte.length);

        byte[] info = new byte[media.length + header.length];
        System.arraycopy(header, 0, info, 0, header.length);
        System.arraycopy(media, 0, info, header.length, media.length);
        send(info);
    }
}
