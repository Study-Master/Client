package studymaster.socket;

import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class VideoSS extends WebSocketServer {

<<<<<<< HEAD
	private static class defaultDelegate implements VideoEventHandler {
		public void onMessage( WebSocket conn, ByteBuffer message ) {
			System.out.println("[debug] (VideoSS.defaultDelegate onMessage): Receiving byte message, using default delegate.");
		}

		public void onMessage(WebSocket conn, String message) {
			System.out.println("[debug] (VideoSS.defaultDelegate onMessage): Receiving message, using default delegate.");
		}

    	public void onOpen(WebSocket conn, ClientHandshake handshake) {
    		System.out.println("[debug] (VideoSS.defaultDelegate onOpen): Open new connection, using default delegate.");
    	}

    	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    		System.out.println("[debug] (VideoSS.defaultDelegate onClose): Close connection, using default delegate.");
    	}

    	public void onError(WebSocket conn, Exception ex) {
    		System.err.println("[err] (VideoSS.defaultDelegate onError): An error, using default delegate.");
    	}
	}
	private static VideoSS instance = null;
	private static VideoEventHandler localDelegate = null;

	public static VideoSS getInstance() {
		if(instance == null) {
			if(localDelegate == null)
				localDelegate = new defaultDelegate();
			String localhost = "0.0.0.0";
			int port = 8089;
			InetSocketAddress address = new InetSocketAddress(localhost, port);
			instance = new VideoSS(address);
		}
		return instance;
	}

	private VideoSS(InetSocketAddress address) {
        super(address);
    }

    public static void setDelegate(VideoEventHandler delegate) {
    	localDelegate = delegate;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        localDelegate.onOpen(conn, handshake);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        localDelegate.onClose(conn, code, reason, remote);
=======
    private static class defaultDelegate implements VideoEventHandler {
        public void onMessage( WebSocket conn, ByteBuffer message ) {
            System.out.println("[debug] (VideoSS.defaultDelegate onMessage): Receiving byte message, using default delegate.");
        }

        public void onMessage(WebSocket conn, String message) {
            System.out.println("[debug] (VideoSS.defaultDelegate onMessage): Receiving message, using default delegate.");
        }

        public void onOpen(WebSocket conn, ClientHandshake handshake) {
           System.out.println("[debug] (VideoSS.defaultDelegate onOpen): Open new connection, using default delegate.");
       }

       public void onClose(WebSocket conn, int code, String reason, boolean remote) {
           System.out.println("[debug] (VideoSS.defaultDelegate onClose): Close connection, using default delegate.");
       }

       public void onError(WebSocket conn, Exception ex) {
           System.err.println("[err] (VideoSS.defaultDelegate onError): An error, using default delegate.");
       }
   }
   private static VideoSS instance = null;
   private static VideoEventHandler localDelegate = null;

   public static VideoSS getInstance() {
    if(instance == null) {
        if(localDelegate == null)
            localDelegate = new defaultDelegate();
        String localhost = "0.0.0.0";
        int port = 8089;
        InetSocketAddress address = new InetSocketAddress(localhost, port);
        instance = new VideoSS(address);
>>>>>>> Audio-Test
    }
    return instance;
}

<<<<<<< HEAD
    @Override
    public void onMessage(WebSocket conn, String message) {
    	localDelegate.onMessage(conn, message);
    }
=======
private VideoSS(InetSocketAddress address) {
    super(address);
}
>>>>>>> Audio-Test

public static void setDelegate(VideoEventHandler delegate) {
   localDelegate = delegate;
}

<<<<<<< HEAD
    @Override
    public void onError(WebSocket conn, Exception ex) {
        localDelegate.onError(conn, ex);
    }
=======
@Override
public void onOpen(WebSocket conn, ClientHandshake handshake) {
    localDelegate.onOpen(conn, handshake);
}

@Override
public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    localDelegate.onClose(conn, code, reason, remote);
}

@Override
public void onMessage(WebSocket conn, String message) {
   localDelegate.onMessage(conn, message);
}

@Override
public void onMessage( WebSocket conn, ByteBuffer message ) {
   localDelegate.onMessage(conn, message);
}

@Override
public void onError(WebSocket conn, Exception ex) {
    localDelegate.onError(conn, ex);
}
>>>>>>> Audio-Test
}