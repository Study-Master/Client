package studymaster.examinee.ViewController;

import studymaster.all.ViewController.TestViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TestView extends TestViewController {

    /*
	@Override
	public void onMessage(String message) {
		System.out.println("[info] (TestView onMessage) Receive message: " + message);

		try {
            // JSONObject
            // event = test1/test2
            // endpoint = endpoint
            // content.status = success/failed
			JSONObject msg = new JSONObject(message);
        	String event = msg.getString("event");
        	String endpoint = msg.getString("endpoint");
        	JSONObject content = msg.getJSONObject("content");

			if(event.equals("test1")) {
				String status = content.getString("status");

				if(status.equals("success")) {
					System.out.println("[info] (TestView onMessage) Audio Test successfully.");
					director.pushStageWithFXML(getClass().getResource("/fxml/testView2.fxml"));
				}

				else if(status.equals("failed")) {
					System.out.println("[info] (TestView onMessage) Audio Test failed.");
					director.pushStageWithFXML(getClass().getResource("/fxml/testView1.fxml"));
				}

				else {
					System.err.println("[err] (TestView onMessage) Unexpected JSON response string.");
				}
			}

            else if(event.equals("test2")) {
				String status = content.getString("status");

				if(status.equals("success")) {
					System.out.println("[info] (TestView onMessage) Webcam Test successfully.");
					
					director.pushStageWithFXML(getClass().getResource("/fxml/examView.fxml"));
				}

				else if(status.equals("failed")) {
					System.out.println("[info] (TestView onMessage) Webcam failed.");
					director.pushStageWithFXML(getClass().getResource("/fxml/testView1.fxml"));
				}

				else {
					System.err.println("[err] (LoginView onMessage) Unexpected JSON response string.");
				}
			}

		} catch (Exception e) {
			System.err.println("[err] (LoginView onMessage) Error when decoding JSON response string.");
		}
	}
    */

	@Override
	public void test(String account) {

	}
}
