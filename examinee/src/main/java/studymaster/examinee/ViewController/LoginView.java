package studymaster.examinee.ViewController;

import studymaster.all.ViewController.LoginViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Callback;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;

public class LoginView extends LoginViewController {

	@Override
	public void onOpen(short httpStatus, String httpStatusMessage) {
		System.out.println("[info] (LoginView onOpen) Socket's connection established.");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("[info] (LoginView onClose) Socket's connection closed.");
	}

	@Override
	public void onMessage(String message) {
		System.out.println("[info] (LoginView onMessage) Receive message: " + message);
		try {
			JSONObject msg = new JSONObject(message);
        	String event = msg.getString("event");
        	String endpoint = msg.getString("endpoint");
        	JSONObject content = msg.getJSONObject("content");

			if(event.equals("login")) {
				String status = content.getString("status");

				if(status.equals("success")) {
					System.out.println("[info] (LoginView onMessage) Login successfully.");
					javafx.application.Platform.runLater(new Runnable() {
  						@Override
  						public void run() {
  							try {
  								director.pushStageWithFXML(App.class.getResource("/fxml/home.fxml"));
  							} catch (Exception e) {
  								System.err.println("[err] (LoginView onMessage) Error when switching scene");
  							}
  						}
					});
				}

				else if(status.equals("failed")) {
					System.out.println("[info] (LoginView onMessage) Login failed.");
				}

				else {
					System.err.println("[err] (LoginView onMessage) Unexpected JSON response string.");
				}
			}
		} catch (Exception e) {
			System.err.println("[err] (LoginView onMessage) Error when decoding JSON response string.");
		}
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("[err] (LoginView onError) An error has been caught.");
	}

	@Override
	public void login(String account, String password) {
		if(connector.isOpen()) {
			Connector.setSender(account);
			connector.login(password);
		}
		else {
			System.err.println("[info] (LoginView login) No connection. Waiting for connection.");
		}
	}
}