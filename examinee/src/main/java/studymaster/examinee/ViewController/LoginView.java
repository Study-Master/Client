package studymaster.examinee.ViewController;

import studymaster.all.ViewController.LoginViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;
import javax.sound.sampled.*;

public class LoginView extends LoginViewController {


	@Override
	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
		try {
			JSONObject msg = new JSONObject(message);
        	String event = msg.getString("event");
        	String endpoint = msg.getString("endpoint");
        	final JSONObject content = msg.getJSONObject("content");

			if(event.equals("login")) {
				String status = content.getString("status");

				if(status.equals("success")) {
					System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Login successfully.");
					director.pushStageWithFXML(getClass().getResource("/fxml/loginView.fxml"));
				}

				else if(status.equals("failed")) {
					System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Login failed.");
					alert(content.getString("reason"));
				}

				else {
					System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Unexpected JSON response string.");
				}
			}
		} catch (Exception e) {
			System.err.println(e);
			System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
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
			System.err.println("[err] ("+ getClass().getSimpleName() +" login) An error is caught, no connection.");
		}
	}
}
