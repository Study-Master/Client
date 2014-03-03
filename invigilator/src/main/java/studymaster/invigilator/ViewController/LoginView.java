package studymaster.invigilator.ViewController;

import studymaster.all.ViewController.LoginViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.invigilator.App;
import org.json.JSONObject;

public class LoginView extends LoginViewController {

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
					director.pushStageWithFXML(App.class.getResource("/fxml/courseView.fxml"));
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
	public void login(String account, String password) {
		Connector.setSender(account);

		try {
			boolean connected = true;
			if(!connector.isOpen())
				connected = connector.connectBlocking();
			if(connected) {
				connector.login(password);
			}
		} catch(Exception e) {
			System.err.println("[err] (LoginView login) An error is caught, no connection.");
		}
	}
}