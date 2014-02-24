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
		System.out.println("Socket's connection established.");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {

	}

	@Override
	public void onMessage(String message) {
		System.out.println("Message: " + message);
		JSONObject event = new JSONObject(message);

		if(event.getString("event").equals("login")) {
			JSONObject content = event.getJSONObject("content");
			if(content.getString("status").equals("success")) {
				javafx.application.Platform.runLater(new Runnable() {
  					@Override
  					public void run() {
  						try {
  							director.pushStageWithFXML(App.class.getResource("/fxml/home.fxml"));
  						} catch (Exception e) {
  							System.out.println(e);
  						}	
  					}
				});
			}
			else {
				//do something if login faild.
			}
		}
	}

	@Override
	public void onError(Exception ex) {

	}

	@Override
	public void login(String account, String password) {
		if(connector.isOpen()) {
			Connector.setSender(account);
			connector.login(password);
		}
		else {
			System.err.println("Waiting for connection.");
		}
	}
}