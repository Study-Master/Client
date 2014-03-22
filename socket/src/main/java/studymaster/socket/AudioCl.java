package studymaster.socket;

import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.channels.NotYetConnectedException;

public class AudioCl extends WebSocketClient {
    private static AudioCl instance = null;
    private static String localServer = null;
    private static String localSender = "Default Sender";
    private static String localEndpoint = "Default AudioCl";

    private AudioCl(URI serverURI) {
        super(serverURI);
    }

    //override methods
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        request();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(String message) {
        System.out.println("[info] (AudioCl onMessage) Receive message: " + message);
        JSONObject msg = new JSONObject(message);
        String event = msg.getString("event");
        String endpoint = msg.getString("endpoint");
        JSONObject content = msg.getJSONObject("content");

        if(event.equals("register")) {
            String status = content.getString("status");
        }
    }

    @Override
    public void onError(Exception ex) {}

    //Methods
    public static AudioCl getInstance() {
        if(localServer == null && SoundUtil.getByteArray() == null){
            return null;
        }
        else {
            if(instance == null) {
                try {
                    instance = new AudioCl(new URI(localServer));
                } catch(Exception e) {
                    instance = null;
                }
                return instance;
            }
            return instance;
        }
    }

    public static void setServer(String server) {
        localServer = server;
    }

    public static void setSender(String sender) {
        localSender = sender;
    }

    public static void setEndpoint(String endpoint) {
        localEndpoint = endpoint;
    }

    //Methods called by AudioView
    public static void submit(){
        System.out.println("submitting");
        AudioCl audioClient = AudioCl.getInstance();
        audioClient.send(SoundUtil.getByteArray());
        System.out.println("sent!");
    }

    public static void startRecord(){
        SoundUtil.startRecord();
    }

    public static void stopRecord(){
        SoundUtil.stopRecord();
    }

    private void request() {
        JSONObject msg = new JSONObject();
        JSONObject content = new JSONObject();
        msg.put("event", "register");
        msg.put("endpoint", localEndpoint);
        msg.put("content", content);
        super.send(msg.toString());
    }
}
