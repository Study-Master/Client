package studymaster.invigilator.ViewController;

import studymaster.all.ViewController.LoginViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.invigilator.App;
import org.json.JSONObject;

public class LoginView extends LoginViewController {

	@Override
<<<<<<< HEAD
	public void onOpen(short httpStatus, String httpStatusMessage) {
		System.out.println("[info] (LoginView onOpen) Socket's connection established.");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("[info] (LoginView onClose) Socket's connection closed.");
		connector = Connector.renew();
	}

	@Override
=======
>>>>>>> origin/master
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
<<<<<<< HEAD
					nextView();
=======
					director.pushStageWithFXML(App.class.getResource("/fxml/courseView.fxml"));
>>>>>>> origin/master
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
<<<<<<< HEAD
	public void onError(Exception ex) {
		System.err.println("[err] (LoginView onError) An error has been caught.");
		connector = Connector.renew();
	}

	@Override
	public void nextView() {
		javafx.application.Platform.runLater(new Runnable() {
  			@Override
  			public void run() {
  				try {
  					director.pushStageWithFXML(App.class.getResource("/fxml/courseView.fxml"));
  				} catch (Exception e) {
  					System.err.println("[err] (LoginView nextView) Error when switching scene");
  				}
  			}
		});
	}

	@Override
=======
>>>>>>> origin/master
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
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/master
