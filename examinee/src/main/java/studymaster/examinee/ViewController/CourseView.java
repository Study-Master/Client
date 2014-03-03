package studymaster.examinee.ViewController;

import studymaster.all.ViewController.HomeViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;
import org.json.JSONArray;
import javafx.scene.layout.Region;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
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

      if(event.equals("profile")) {
        showCourseList(content);
      }

		} catch (Exception e) {
			System.err.println("[err] (CourseView onMessage) Error when decoding JSON response string.");
		}
	}

  private void showCourseList(final JSONObject content) {
    final AnchorPane pane = (AnchorPane) director.getScene().getRoot();
    javafx.application.Platform.runLater(new Runnable() {
      @Override
      public void run() {
        try {
          JSONObject profile = content.getJSONObject("profile");
          JSONArray courses = profile.getJSONArray("courses");
          GridPane courseList = new GridPane();
          courseList.setVgap(15);

          AnchorPane.setTopAnchor(courseList, 200.0);
          AnchorPane.setLeftAnchor(courseList, 70.0);
          AnchorPane.setRightAnchor(courseList, 70.0);
          ColumnConstraints col1 = new ColumnConstraints();
          col1.setPercentWidth(15);
          ColumnConstraints col2 = new ColumnConstraints();
          col2.setPercentWidth(65);
          ColumnConstraints col3 = new ColumnConstraints();
          col3.setPercentWidth(20);

          courseList.getColumnConstraints().addAll(col1,col2,col3);
          courseList.setStyle("-fx-border: 2px solid; -fx-border-color: red; -fx-border-insets: 5;"); //debug only

          for(int i=0; i<courses.length(); i++) {
            JSONObject course = courses.getJSONObject(i);
            Label code = new Label(course.getString("code"));
            Label name = new Label(course.getString("name"));
            Button button = new Button(course.getString("status"));
              //System.out.println("[status] " + course.getString("status"));
            
            courseList.add(code, 0, i);
            courseList.add(name, 1, i);
            courseList.add(button, 2, i);
          }
              
          pane.getChildren().addAll(courseList);
        } catch (Exception e) {
          System.err.println("[err] (CourseView onMessage) Error when adding component.");
        }
      }
    });
  }
}