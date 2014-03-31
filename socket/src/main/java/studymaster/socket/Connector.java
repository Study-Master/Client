package studymaster.socket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.HashSet;
import org.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.nio.channels.NotYetConnectedException;

public final class Connector extends WebSocketClient {
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
    private static Set<Callback> localDelegateList;
    private static Callback localDelegate = null;
    public static String localSender = "Default Sender";
    private static String localEndpoint = "Default Connector";
    private static String messageContainer = "Default Message";

    private Connector(URI serverURI) {
        super(serverURI);
        localDelegateList = new HashSet<Callback>();
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

    public static boolean retain(){
        return localDelegateList.add(localDelegate);
    }

    public static boolean retain(Callback delegate) {
        return localDelegateList.add(delegate);
    }

    public static boolean release() {
        return localDelegateList.remove(localDelegate);
    }

    public static boolean release(Callback delegate) {
        return localDelegateList.remove(delegate);
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

    public static String getEndpoint() {
        return localEndpoint;
    }

    public static void setMessageContainer(String event, JSONObject content) {
        JSONObject msg = new JSONObject();
        msg.put("event", event);
        msg.put("endpoint", localEndpoint);
        if (content==null) {
            msg.put("content", "null");
        }
        else {
            msg.put("content", content);
        }
        messageContainer = msg.toString();

        System.out.println("[info] (" + Connector.class.getSimpleName() + " setMessageContainer) Set messageContainer to " + messageContainer);
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
        for(Callback delegate : localDelegateList) {
            if(delegate==localDelegate) {
                continue;
            }
            else {
                if(delegate == null) {
                    localDelegateList.remove(delegate);
                    continue;
                }
                else {
                    delegate.onOpen(handshakedata.getHttpStatus(), handshakedata.getHttpStatusMessage());
                }
            }
        }
    }

    @Override public void onClose(int code, String reason, boolean remote) {
        localDelegate.onClose(code, reason, remote);
        for(Callback delegate : localDelegateList) {
            if(delegate==localDelegate) {
                continue;
            }
            else {
                if(delegate == null) {
                    localDelegateList.remove(delegate);
                    continue;
                }
                else {
                    delegate.onClose(code, reason, remote);
                }
            }
        }
    }

    @Override public void onMessage(String message) {
        localDelegate.onMessage(message);
        for(Callback delegate : localDelegateList) {
            if(delegate==localDelegate) {
                continue;
            }
            else {
                if(delegate == null) {
                    localDelegateList.remove(delegate);
                    continue;
                }
                else {
                    delegate.onMessage(message);
                }
            }
        }

    }

    @Override public void onError(Exception ex) {
        localDelegate.onError(ex);
        for(Callback delegate : localDelegateList) {
            if(delegate==localDelegate) {
                continue;
            }
            else {
                if(delegate == null) {
                    localDelegateList.remove(delegate);
                    continue;
                }
                else {
                    delegate.onError(ex);
                }
            }
        }

    }
}
