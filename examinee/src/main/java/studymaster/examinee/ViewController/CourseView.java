package studymaster.examinee.ViewController;

import studymaster.all.ViewController.HomeViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;

public class CourseView extends HomeViewController {

	@Override
	public void onOpen(short httpStatus, String httpStatusMessage) {
		System.out.println("[info] (CourseView onOpen) Socket's connection established.");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("[info] (CourseView onClose) Socket's connection closed.");
	}

	@Override
	public void onMessage(String message) {
		System.out.println("[info] (CourseView onMessage) Receive message: " + message);
		try {
			JSONObject msg = new JSONObject(message);
        	String event = msg.getString("event");
        	String endpoint = msg.getString("endpoint");
        	JSONObject content = msg.getJSONObject("content");

        	//add code here
		} catch (Exception e) {
			System.err.println("[err] (CourseView onMessage) Error when decoding JSON response string.");
		}
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("[err] (CourseView onError) An error has been caught.");
	}

	@Override
	public void nextView(){}
}