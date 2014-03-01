package studymaster.socket;

import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.channels.NotYetConnectedException;

public final class Connector extends WebSocketClient{
    private static class defaultDelegate implements Callback {
        public void onOpen(short httpStatus, String httpStatusMessage) {
            System.out.println("[debug] (Connector.defaultDelegate onOpen): Connection open, using default delegate.");
        }

        public void onClose(int code, String reason, boolean remote) {
            System.out.println("[debug] (Connector.defaultDelegate onClose): Connection close, using default delegate.");
        }

        public void onMessage(String message) {
            System.out.println("[debug] (Connector.defaultDelegate onMessage): Receiving message, using default delegate.");
        }

        public void onError(Exception ex) {
            System.err.println("[error] (Connector.defaultDelegate onError): An error, using default delegate.");
        }
    }
    private static String localServer = "ws://localhost";
    private static Connector instance = null;
    private static Callback localDelegate = null;
    private static String localSender = "Default Sender";
    private static String localEndpoint = "Default Connector";

    private Connector(URI serverURI) {
        super(serverURI);
    }

    public static Connector getInstance() {
        if(instance==null){
            if(localDelegate==null)
                localDelegate = new defaultDelegate();
            try {
                instance = new Connector(new URI(localServer));
            } catch (URISyntaxException e) {
                System.err.println(e);
                instance = null;
            }
        }
        return instance;
    }

    public static void setServer(String server) {
        localServer = server;
    }

    public static void setDelegate(Callback delegate) {
        localDelegate = delegate;
    }

    public static void setSender(String sender) {
        localSender = sender;
    }

    public static void setEndpoint(String endpoint) {
        localEndpoint = endpoint;
    }

    public static Connector renew() {
        instance = null;
        return getInstance();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        localDelegate.onOpen(handshakedata.getHttpStatus(), handshakedata.getHttpStatusMessage());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        localDelegate.onClose(code, reason, remote);
    }

    @Override
    public void onMessage(String message) {
        localDelegate.onMessage(message);
    }

    @Override
    public void onError(Exception ex) {
        localDelegate.onError(ex);
    }

    /**
     * Send login request in JSON format.
     * @param  password                 user's password in md5Hex
     * @throws NotYetConnectedException not connected
     */
    public void login(String password) throws NotYetConnectedException {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject msg = new JSONObject();
        JSONObject content = new JSONObject();

        msg.put("event", "login");
        msg.put("endpoint", localEndpoint);

        content.put("account", localSender);
        content.put("password", password);
        content.put("time", df.format(new java.util.Date()));

        msg.put("content", content);

        super.send(msg.toString());
    }

    /**
     * Send get profile request in JSON format
     * @throws NotYetConnectedException not connected
     */
    public void profile() throws NotYetConnectedException{
        JSONObject msg = new JSONObject();
        JSONObject content = new JSONObject();

        msg.put("event", "profile");
        msg.put("endpoint", localEndpoint);

        content.put("account", localSender);

        msg.put("content", content);

        super.send(msg.toString());
    }

    public void talk(String receiver, String text) throws NotYetConnectedException {
        JSONObject msg = new JSONObject();
        JSONObject event = new JSONObject();
        msg.put("sender", localSender);
        msg.put("receiver", receiver);
        msg.put("text", text);
        event.put("event", "talk");
        event.put("client", localEndpoint);
        event.put("content", msg);
        super.send(event.toString());
    }
}