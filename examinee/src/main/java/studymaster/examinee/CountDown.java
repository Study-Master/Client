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
import javafx.scene.control.Label;
import javafx.util.Duration;
import studymaster.examinee.ViewController.CourseView;

public class CountDown extends Label {
    
    public CountDown(String examStartTime, String courseCode, int row) {
        bindToTime(examStartTime, courseCode, row);
    }

    private void bindToTime(final String examStartTime, final String courseCode, final int row) {
        Timeline timeline = new Timeline(
                                         new KeyFrame(Duration.seconds(0),
                                                      new EventHandler<ActionEvent>() {
                                                          @Override public void handle(ActionEvent actionEvent) {
                                                              try {
                                                                  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                                  Date currentTime = new Date();
                                                                  Date startTime = dateFormat.parse(examStartTime);
                                                                  long diff = startTime.getTime() - currentTime.getTime();
                                                                  long diffDays = diff / (24 * 60 * 60 * 1000);
                                                                  long diffMinutes = diff / (60 * 1000) % 60;
                                                                  long diffHours = diff / (60 * 60 * 1000) % 24;
                                                                  long diffSeconds = diff / 1000 % 60;
                                                                  if (diffDays==0 && diffHours==0 && diffMinutes<15 ) {
                                                                      // javafx.application.Platform.runLater(new Runnable() {
                                                                      // @Override
                                                                      // public void run() {

                                                                      ObservableList<Node> childrens = CourseView.getList().getChildren();
                                                                      Node label = null;
                                                                      for(Node node : childrens) {
                                                                          if(CourseView.getList().getRowIndex(node) == row && CourseView.getList().getColumnIndex(node) == 2) {
                                                                              label = node;
                                                                              break;
                                                                          }
                                                                      }
                                                                      CourseView.getList().getChildren().remove(label);
                                                                      CourseView.createExamButton(examStartTime, courseCode, row);
                                                                      //   }
                                                                      // });
                                                                  }
                                                                  else {
                                                                      String remainingTime;
                                                                      if (diffDays>0) {
                                                                            remainingTime = Long.toString(diffDays) + " Days" ;
                                                                        }
                                                                        else {
                                                                            remainingTime = Long.toString(diffHours) + ":" + Long.toString(diffMinutes) + ":" + Long.toString(diffSeconds) ;
                                                                        }
                                                                      setText(remainingTime);
                                                                      if (remainingTime.contains(":")) {
                                                                          setStyle("-fx-text-fill: red;");
                                                                      }
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
