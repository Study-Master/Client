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
            System.out.println("[debug] (" + defaultDelegate.class.getSimpleName() + " onOpen): Connection open, using default delegate.");
        }

        public void onClose(int code, String reason, boolean remote) {
            System.out.println("[debug] ("  + defaultDelegate.class.getSimpleName() + " onClose): Connection close, using default delegate.");
        }

        public void onMessage(String message) {
            System.out.println("[debug] (" + defaultDelegate.class.getSimpleName() + " onMessage): Receiving message, using default delegate.");
        }

        public void onError(Exception ex) {
            System.err.println("[error] (" + defaultDelegate.class.getSimpleName() + " onError): An error, using default delegate.");
        }
    }
    private static String localServer = "ws://localhost";
    private static Connector instance = null;
    private static Callback localDelegate = null;
    public static String localSender = "Default Sender";
    private static String localEndpoint = "Default Connector";
    private static String messageContainer = "Default Message";

    private Connector(URI serverURI) {
        super(serverURI);
        System.out.println("[info] (" + Connector.class.getSimpleName() + " Connector) Create Connector instance");
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
        else {}
        return instance;
    }

    public static Connector renew() {
         System.out.println("[info] (" + Connector.class.getSimpleName() + " renew)");
        instance = null;
        return getInstance();
    }

    public static void setServer(String server) {
        System.out.println("[info] (" + Connector.class.getSimpleName() + " setServer) Set local server to " + server);
        localServer = server;
    }

    public static void setDelegate(Callback delegate) {
        System.out.println("[info] (" + Connector.class.getSimpleName() + " setDelegate) Set Callback to " + delegate.getClass().getSimpleName());
        localDelegate = delegate;
    }

    public static void setSender(String sender) {
        System.out.println("[info] (" + Connector.class.getSimpleName() + " setSender) Set local sender to " + sender);
        localSender = sender;
    }

    public static void setEndpoint(String endpoint) {
        System.out.println("[info] (" + Connector.class.getSimpleName() + " setEndpoint) Set local endpoint to " + endpoint);
        localEndpoint = endpoint;
    }

    public static String getSender() {
        return localSender;
    }

    public static void setMessageContainer(String event, JSONObject content) {
        System.out.println("[info] (" + Connector.class.getSimpleName() + " setMessageContainer) Set messageContainer to " + content);

        JSONObject msg = new JSONObject();
        msg.put("event", event);
        msg.put("endpoint", localEndpoint);
        msg.put("content", content);

        messageContainer = msg.toString();
    }

    public void sendMessageContainer() throws NotYetConnectedException {
        super.send(messageContainer);
    }

    public void setAndSendMessageContainer(String event, JSONObject content) throws NotYetConnectedException {
        setMessageContainer(event, content);
        sendMessageContainer();
    }

    @Override public void onOpen(ServerHandshake handshakedata) {
        localDelegate.onOpen(handshakedata.getHttpStatus(), handshakedata.getHttpStatusMessage());
    }

    @Override public void onClose(int code, String reason, boolean remote) {
        localDelegate.onClose(code, reason, remote);
    }

    @Override public void onMessage(String message) {
        localDelegate.onMessage(message);
    }

    @Override public void onError(Exception ex) {
        localDelegate.onError(ex);
    }

    public void login(String password) throws NotYetConnectedException {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject msg = new JSONObject();
        JSONObject content = new JSONObject();

        msg.put("event", "login");
        msg.put("endpoint", localEndpoint);

        content.put("account", localSender);
        content.put("password", password);
        content.put("time", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

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

    public void auth() throws NotYetConnectedException {
        JSONObject msg = new JSONObject();
        JSONObject content = new JSONObject();

        msg.put("event", "auth");
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
