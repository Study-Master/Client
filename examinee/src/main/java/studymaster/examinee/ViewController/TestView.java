package studymaster.examinee.ViewController;

import studymaster.all.ViewController.TestViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;

public class LoginView extends LoginViewController {

	@Override
	public void onMessage(String message) {
        System.out.println("[info] (LoginView onMessage) Receive message: " + message);

	}

}
