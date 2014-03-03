package studymaster.examinee.ViewController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.fxml.FXML;
import javafx.util.Duration;

public class CourseView extends HomeViewController {

	@Override
	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
		try {
			JSONObject msg = new JSONObject(message);
      String event = msg.getString("event");
      String endpoint = msg.getString("endpoint");
      final JSONObject content = msg.getJSONObject("content");

      if(event.equals("profile")) {
        showCourseList(content);
      }

    } catch (Exception e) {
     System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
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
        String status;
        GridPane courseList = new GridPane();
        AnchorPane.setTopAnchor(courseList, 150.0);
        AnchorPane.setLeftAnchor(courseList, 90.0);
        AnchorPane.setRightAnchor(courseList, 90.0);
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
            courseList.add(code, 0, i);
            courseList.add(name, 1, i);

            status = course.getString("status");
            if (status.equals("unbooked")) {
              Button button = new Button(" Book ");

              button.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                  director.pushStageWithFXML(getClass().getResource("/fxml/bookingView.fxml"));
                }
              });

              courseList.add(button, 2, i);
            }
            else if (status.equals("booked")) {      
              try {
                String examStartTime = course.getString("start_time");
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date currentTime = new Date();
                Date startTime = dateFormat.parse(examStartTime);
                long diff = startTime.getTime() - currentTime.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);
                //System.out.println(diffDays);
                if (diffDays<3){
                  CountDown timeLabel = new CountDown(examStartTime);
                  courseList.add(timeLabel, 2, i);
                }
                else {
                  Button button = new Button("Cancel");
                  courseList.add(button, 2, i);            
                }
              }
              catch (Exception e){
                System.err.println("[err] Error when parsing string");
              }
              
              
            }
          }
          pane.getChildren().addAll(courseList);
        } catch (Exception e) {
          System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when adding component.");
        }
      }
    });
}


}

class CountDown extends Label {
  public CountDown(String remainingTime) {
    bindToTime(remainingTime);
  }

  // the digital clock updates once a second.
  private void bindToTime(final String examStartTime) {
    Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        new EventHandler<ActionEvent>() {
          @Override 
          public void handle(ActionEvent actionEvent) {
            try {              
              setText(getRemainingTime(examStartTime));
            } catch (ParseException ex) {
              Logger.getLogger(CountDown.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
        ),
      new KeyFrame(Duration.seconds(1))
      );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
  
  public static String getRemainingTime(String examStartTime) throws ParseException {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date currentTime = new Date();
    Date startTime = dateFormat.parse(examStartTime);
    
    long diff = startTime.getTime() - currentTime.getTime();
    long diffSeconds = diff / 1000 % 60;
    long diffMinutes = diff / (60 * 1000) % 60;
    long diffHours = diff / (60 * 60 * 1000) % 24;
    long diffDays = diff / (24 * 60 * 60 * 1000);
    if (diffDays>0) {
      return diffDays + "D " + diffHours + "H " + diffMinutes + "M" ;
    }
    else {
      return diffHours + "H " + diffMinutes + "M " + diffSeconds + "S" ;       
    }
  }
  
}
