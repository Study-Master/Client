package studymaster.examinee.ViewController;

import studymaster.all.ViewController.TestViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;

public class TestView extends TestViewController {

	@Override
	public void onMessage(String message) {
		System.out.println("[info] (TestView onMessage) Receive message: " + message);
		try {
            System.out.println("to be implemented");
			}
		} catch (Exception e) {
			System.err.println("[err] (LoginView onMessage) Error when decoding JSON response string.");
		}
	}

	@Override
	public void test(String account) {

	}
}
