package studymaster.examinee.ViewController;

import studymaster.all.ViewController.HomeViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;
import javafx.scene.layout.AnchorPane;

public class CourseView extends HomeViewController {

	@Override
	public void onMessage(String message) {
		System.out.println("[info] (CourseView onMessage) Receive message: " + message);
		try {
			JSONObject msg = new JSONObject(message);
        	String event = msg.getString("event");
        	String endpoint = msg.getString("endpoint");
        	JSONObject content = msg.getJSONObject("content");

			final AnchorPane pane = (AnchorPane) director.getScene().getRoot();
        	
        	javafx.application.Platform.runLater(new Runnable() {
  				@Override
  				public void run() {
  					try {
  						//add code here
  					} catch (Exception e) {
  						System.err.println("[err] (CourseView onMessage) Error when adding component.");
  					}
  				}
			});
		} catch (Exception e) {
			System.err.println("[err] (CourseView onMessage) Error when decoding JSON response string.");
		}
	}

	@Override
	public void nextView(){}
}