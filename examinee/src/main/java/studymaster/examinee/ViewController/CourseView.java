package studymaster.examinee.ViewController;

import studymaster.all.ViewController.HomeViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;
import org.json.JSONArray;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CourseView extends HomeViewController {

	@Override
	public void onMessage(String message) {
		System.out.println("[info] (CourseView onMessage) Receive message: " + message);
		try {
			JSONObject msg = new JSONObject(message);
        	String event = msg.getString("event");
        	String endpoint = msg.getString("endpoint");
        	final JSONObject content = msg.getJSONObject("content");

			final AnchorPane pane = (AnchorPane) director.getScene().getRoot();
        	
        	javafx.application.Platform.runLater(new Runnable() {
  				@Override
  				public void run() {
  					try {
  						JSONObject profile = content.getJSONObject("profile");
  						JSONArray courses = profile.getJSONArray("courses");

  						GridPane courseList = new GridPane();

  						for(int i=0; i<courses.length(); i++) {
  							JSONObject course = courses.getJSONObject(i);
  							Label code = new Label(course.getString("code"));
  							Label name = new Label(course.getString("name"));
                Button button = new Button(course.getString("status"));
  							courseList.add(code, 1, i+1);
  							courseList.add(name, 2, i+1);
                courseList.add(button, 3, i+1);
  						}
  						
     					pane.getChildren().addAll(courseList);
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