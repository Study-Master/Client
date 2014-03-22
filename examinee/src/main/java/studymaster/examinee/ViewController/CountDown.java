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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.util.Duration;
import studymaster.examinee.ViewController.CourseView;

class CountDown extends Label {
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
                                                                  if (diffDays==0 && diffHours==0 && diffMinutes<15 ) {
                                                                      // javafx.application.Platform.runLater(new Runnable() {
                                                                      // @Override
                                                                      // public void run() {

                                                                      ObservableList<Node> childrens = CourseView.List.getChildren();
                                                                      Node label = null;
                                                                      for(Node node : childrens) {
                                                                          if(CourseView.List.getRowIndex(node) == row && CourseView.List.getColumnIndex(node) == 2) {
                                                                              label = node;
                                                                              break;
                                                                          }
                                                                      }
                                                                      // final ExamButton examButton = new ExamButton(examStartTime, courseCode, row);
                                                                      // examButton.setPrefWidth(120);
                                                                      // examButton.setOnAction(new EventHandler<ActionEvent>() {
                                                                      //   @Override public void handle(ActionEvent e) {
                                                                      //     examButton.setText("");
                                                                      //     Image LoadingIcon = new Image(getClass().getResourceAsStream("/image/Loading.gif"));
                                                                      //     examButton.setGraphic(new ImageView(LoadingIcon));
                                                                      //     studymaster.examinee.ViewController.CourseView.setExamMsg(Connector.getInstance().getSender());
                                                                      //     studymaster.all.ViewController.Director.pushStageWithFXML(getClass().getResource("/fxml/examView.fxml"));
                                                                      //   }
                                                                      // });
                                                                      // CourseView.List.add(examButton, 2, row);

                                                                      CourseView.List.getChildren().remove(label);
                                                                      CourseView.createExamButton(examStartTime, courseCode, row);
                                                                      //   }
                                                                      // });
                                                                  }
                                                                  else {
                                                                      String remainingTime;
                                                                      remainingTime = getRemainingTime(examStartTime);
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
            return diffDays + " Days" ;
        }
        else {
            return diffHours + ":" + diffMinutes + ":" + diffSeconds ;
        }
    }
}
