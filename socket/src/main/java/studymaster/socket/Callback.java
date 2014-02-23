package studymaster.socket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public interface Callback {

	public void onOpen(short httpStatus, String httpStatusMessage);

	public void onClose(int code, String reason, boolean remote);

	public void onMessage(String message);

	public void onError(Exception ex);
	
}