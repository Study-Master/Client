package studymaster.invigilator.ViewController;

import studymaster.all.ViewController.HomeViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.invigilator.App;
import org.json.JSONObject;

public class CourseView extends HomeViewController {

	@Override

	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
		try {
			JSONObject msg = new JSONObject(message);
        	String event = msg.getString("event");
        	String endpoint = msg.getString("endpoint");
        	JSONObject content = msg.getJSONObject("content");

        	//add code here
		} catch (Exception e) {
			System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
		}
	}

}

