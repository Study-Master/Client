package studymaster.examinee;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import studymaster.examinee.ViewController.CourseView;

public class ExamButton extends Button {

    public ExamButton(String examStartTime, String courseCode, int row){
        setText("Exam");
        bindToTime(examStartTime, courseCode, row);
        System.out.println("[Info] A ExamButton created!");
    }
    
    private void bindToTime(final String examStartTime, String courseCode, final int row) {
        Timeline timeline = new Timeline(
                                         new KeyFrame(Duration.seconds(0),
                                                      new EventHandler<ActionEvent>() {
                                                          @Override public void handle(ActionEvent actionEvent) {
                                                              try {
                                                                  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                                  Date currentTime = new Date();
                                                                  Date startTime = dateFormat.parse(examStartTime);
                                                                  long diff = Math.abs(startTime.getTime() - currentTime.getTime());
                                                                  long diffMinutes = diff / (60 * 1000) % 60;
                                                                  if (diffMinutes>=15) {
                                                                      // javafx.application.Platform.runLater(new Runnable() {
                                                                      // @Override
                                                                      // public void run() {
                                                                      ObservableList<Node> childrens = CourseView.getList().getChildren();
                                                                      Node button = null;
                                                                      for(Node node : childrens) {
                                                                          if(CourseView.getList().getRowIndex(node) == row && CourseView.getList().getColumnIndex(node) == 2) {
                                                                              button = node;
                                                                              break;
                                                                          }
                                                                      }
                                                                      Label closedLabel = new Label("Closed");
                                                                      CourseView.getList().add(closedLabel, 2, row);
                                                                      CourseView.getList().getChildren().remove(button);
                                                                      //   }
                                                                      // });
                                                                  }
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
}
