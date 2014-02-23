package studymaster.socket;

import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.channels.NotYetConnectedException;

public final class Connector extends WebSocketClient{
    private static String localServer;
    private static Connector instance;
    private static Callback localDelegate;
    private static String localSender;
    private static String localClient;

    private Connector(URI serverURI) {
        super(serverURI);
    }

    public static Connector getInstance() {
        if(instance==null){
            if(localServer==null && localDelegate==null && localClient==null)
                return null;
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

    public static void setClient(String client) {
        localClient = client;
    }

    public static void destroy() {
        instance = null;
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

    public void login(String password) throws NotYetConnectedException {
        JSONObject msg = new JSONObject();
        JSONObject event = new JSONObject();
        msg.put("account", localSender);
        msg.put("password", password);
        event.put("event", "login");
        event.put("client", localClient);
        event.put("content", msg);
        super.send(event.toString());
    }

    public void talk(String receiver, String text) throws NotYetConnectedException {
        JSONObject msg = new JSONObject();
        JSONObject event = new JSONObject();
        msg.put("sender", localSender);
        msg.put("receiver", receiver);
        msg.put("text", text);
        event.put("event", "talk");
        event.put("client", localClient);
        event.put("content", msg);
        super.send(event.toString());
    }
}