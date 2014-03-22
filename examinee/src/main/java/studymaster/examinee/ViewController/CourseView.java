package studymaster.examinee.ViewController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
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
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.json.JSONException;

public class CourseView extends HomeViewController {
  static GridPane List;
  static String Account;

  @Override
	public void onMessage(String message) {
    System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
    try {
      JSONObject msg = new JSONObject(message);
      String event = msg.getString("event");
      String endpoint = msg.getString("endpoint");
      final JSONObject content = msg.getJSONObject("content");
      Account = content.getString("account");
      //Button button = new Button();

      if(event.equals("profile")) {
        showCourseList(content);
      }
      else if (event.equals("cancel")) {
        //dialog - inform student
        final JSONObject cancelInfo = content;
        if (cancelInfo.getString("status").equals("successful")) {
          System.out.println("[Info] Successfully cancel the booking!");
          //alert
          alert("Your " + cancelInfo.getString("code") + " exam booking is successfully canceled.");
          try{
            final String examStartTime = cancelInfo.getString("start_time");
            final String courseCode = cancelInfo.getString("code");
            javafx.application.Platform.runLater(new Runnable() {
            @Override
              public void run() {

                final CancelButton cancelButton = (CancelButton) List.lookup("#toDelete");
                cancelButton.setId("Deleted");
                // System.out.println("[Info] "+cancelButton.getId());
                // final int row=List.getRowIndex(cancelButton);
                // javafx.application.Platform.runLater(new Runnable() {
                //   @Override
                //   public void run() {
                //     List.getChildren().remove(cancelButton);
                //     BookButton button = new BookButton(examStartTime, cancelInfo.getString("code"), row);
                //     button.setOnAction(new EventHandler<ActionEvent>() {
                //       @Override public void handle(ActionEvent e) {
                //         setBookingMsg(cancelInfo.getString("code"), cancelInfo.getString("account"));
                //         director.pushStageWithFXML(getClass().getResource("/fxml/bookingView.fxml"));
                //       }
                //     });
                //     button.setPrefWidth(120);
                //     List.add(button, 2, row);
                //   }
                // });
                int row = List.getRowIndex(cancelButton);
                createBookButton(examStartTime, courseCode, row);
                List.getChildren().remove(cancelButton);
              }
            });
          }
          catch (Exception e) {
            System.err.println("[err] Fail to change CancelButton to BookButton");
          }
        }      
        else {
          //alert
            alert("Can't cancel this exam. " + cancelInfo.getString("error"));
            
        }
      }
    } 
    catch (Exception e) {
      System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
    }
  }

  private void showCourseList(final JSONObject content) {

    javafx.application.Platform.runLater(new Runnable() {
      @Override
      public void run() {
        try {
          //Find the node #ap from FXML
          final AnchorPane pane = (AnchorPane) director.getScene().getRoot();
          final ScrollPane sp = (ScrollPane) pane.lookup("#scrollpane");
          final AnchorPane ap = (AnchorPane) (sp.lookup("#ap"));

          //Create a gridpane - courseList
          final JSONObject profile = content.getJSONObject("profile");
          JSONArray courses = profile.getJSONArray("courses");
          ArrayList<JSONObject> coursesArray = new ArrayList<JSONObject>();
          String status;
          GridPane courseList = new GridPane();
          List = courseList;
          AnchorPane.setTopAnchor(courseList, 20.0);
          AnchorPane.setLeftAnchor(courseList, 20.0);
          AnchorPane.setRightAnchor(courseList, 10.0);
          AnchorPane.setBottomAnchor(courseList, 25.0);
          ColumnConstraints col1 = new ColumnConstraints();
          col1.setPercentWidth(15);
          ColumnConstraints col2 = new ColumnConstraints();
          col2.setPercentWidth(65);
          ColumnConstraints col3 = new ColumnConstraints();
          col3.setPercentWidth(20);
          courseList.getColumnConstraints().addAll(col1,col2,col3);
          courseList.setVgap(25);
          col3.setHalignment(HPos.RIGHT);

          //Add courses into an ArrayList
          for (int i=0; i<courses.length(); i++) {
            coursesArray.add(courses.getJSONObject(i));
          }

          //Sort the ArrayList
          Collections.sort(coursesArray, new Comparator<JSONObject>() {
            @Override public int compare(JSONObject course1, JSONObject course2)
            {
              if ( ("closed".equals(course1.getString("status")) || "finished".equals(course1.getString("status"))) && 
                    ("closed".equals(course2.getString("status")) || "finished".equals(course2.getString("status"))) ) {
                return course1.getString("code").compareTo(course2.getString("code"));
              }
              else if ("closed".equals(course1.getString("status")) || "finished".equals(course1.getString("status"))) {
                return 1;
              }
              else if ("closed".equals(course2.getString("status")) || "finished".equals(course2.getString("status"))) {
                return -1;
              }
              else {
                return course1.getString("code").compareTo(course2.getString("code"));
              }
            }
          });

          //Display the courses
          for(int i=0; i<coursesArray.size(); i++) {
            final JSONObject course = (JSONObject)coursesArray.get(i);

            //First 2 columns
            Label code = new Label(course.getString("code"));
            code.setStyle("-fx-text-fill: black;");
            Label name = new Label(course.getString("name"));
            name.setStyle("-fx-text-fill: black;");            

            courseList.add(code, 0, i);
            courseList.add(name, 1, i);

            status = course.getString("status");
            String examStartTime = course.getString("start_time");
            String courseCode = course.getString("code");

            if (status.equals("closed")) {
              //Close label
              Label label = new Label("Closed");
              courseList.add(label, 2, i);
            }
            else if (status.equals("finished")) {
              //Finish label
              Label label = new Label("Finished");
              courseList.add(label, 2, i);
            }
            else if (status.equals("unbooked")) {
              //Book button
              
              createBookButton(examStartTime, courseCode, i);

            }
            else if (status.equals("booked")) {
              try {

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date currentTime = new Date();
                Date startTime = dateFormat.parse(examStartTime);
                long diff = Math.abs(startTime.getTime() - currentTime.getTime());
                long diffDays = diff / (24 * 60 * 60 * 1000);
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;

                if (diffDays>=3) {

                  //Cancel button
                  createCancelButton(examStartTime, courseCode, i);


                }
                else if (diffMinutes>=15) {


                  //Countdown label
                  createCountDownLabel(examStartTime, courseCode, i);

                }
                else {
                  //Exam button

                  createExamButton(examStartTime, courseCode, i);
                }

                
              } catch (Exception e){
                System.err.println("[err] ("+ getClass().getSimpleName() +")Error when parsing string");
              }
            }
          }
          
          ap.getChildren().addAll(courseList);
        } catch (JSONException e) {
          System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when adding component.");
        }
      }
    });
  }

  public static void createBookButton(final String examStartTime, final String courseCode, final int row) {
     javafx.application.Platform.runLater(new Runnable() {
      @Override
      public void run() {
    BookButton button = new BookButton(examStartTime, courseCode, row);
    button.setPrefWidth(120);

    button.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        setBookingMsg(courseCode, Account);
        Director.pushStageWithFXML(getClass().getResource("/fxml/bookingView.fxml"));
      }
    });

    List.add(button, 2, row);
    }
  });
  }

  public static void createCountDownLabel(String examStartTime, String courseCode, int row) {

    CountDown timeLabel = new CountDown(examStartTime, courseCode, row);
    List.add(timeLabel, 2, row);
  }

  public static void createCancelButton(final String examStartTime, final String courseCode, final int row) {
    javafx.application.Platform.runLater(new Runnable() {
      @Override
      public void run() {
    final CancelButton button = new CancelButton(examStartTime, courseCode, row);
    button.setPrefWidth(120);
    button.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        button.setText("");
        Image LoadingIcon = new Image(getClass().getResourceAsStream("/image/Loading.gif"));
        button.setGraphic(new ImageView(LoadingIcon));
        button.setStyle("-fx-padding-left: 0; -fx-background-color: rgba(0, 102, 153, 1);");
        button.setDisable(true);
        button.setId("toDelete");

        //Send msg to server
        JSONObject sendMsg = new JSONObject();
        JSONObject sendContent = new JSONObject();
        sendMsg.put("event", "cancel");
        sendMsg.put("endpoint", "Java Client");
        sendMsg.put("content", sendContent);
        sendContent.put("code", courseCode);
        sendContent.put("account", Account);
        Connector.setMessageContainer(sendMsg.toString());
        Connector.getInstance().sendMessageContainer();
      }
    });
    List.add(button, 2, row);
  }
});

  }

  public static void createExamButton(final String examStartTime, final String courseCode, final int row) {
    javafx.application.Platform.runLater(new Runnable() {
      @Override
      public void run() {
    final ExamButton button = new ExamButton(examStartTime, courseCode, row);
    button.setPrefWidth(120);
    button.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        button.setText("");
        Image LoadingIcon = new Image(getClass().getResourceAsStream("/image/Loading.gif"));
        button.setGraphic(new ImageView(LoadingIcon));
        button.setStyle("-fx-padding-left: 0; -fx-background-color: rgba(0, 102, 153, 1);");
        button.setDisable(true);
        setExamMsg(courseCode);
        Director.pushStageWithFXML(getClass().getResource("/fxml/authView.fxml"));
      }
    });
    List.add(button, 2, row);
}
});
  }

  public static void setBookingMsg(String course, String account) {
    JSONObject Msg = new JSONObject();
    JSONObject Content = new JSONObject();
    Msg.put("event", "booking");
    Msg.put("endpoint", "Client");
    Content.put("code", course);
    Content.put("account", account);
    Msg.put("content", Content);
    Connector.setMessageContainer(Msg.toString());
  }

  public static void setExamMsg(String course) {
    JSONObject Msg = new JSONObject();
    JSONObject Content = new JSONObject();
    Msg.put("event", "exam");
    Msg.put("endpoint", "Client");
    Content.put("code", course);
    Content.put("account", Account);
    Msg.put("content", Content);
    Connector.setMessageContainer(Msg.toString());
  }

}

class CountDown extends Label {
  public CountDown(String examStartTime, String courseCode, int row) {
    bindToTime(examStartTime, courseCode, row);
  }

  private void bindToTime(final String examStartTime, final String courseCode, final int row) {
    Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
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

class BookButton extends Button {
  public BookButton(String examStartTime, String courseCode, int row){
    setText(" Book ");
    bindToTime(examStartTime, courseCode, row);
  }
  private void bindToTime(final String examStartTime, String courseCode, final int row) {
    Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
            try {
              DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
              Date currentTime = new Date();
              Date startTime = dateFormat.parse(examStartTime);
              long diff = startTime.getTime() - currentTime.getTime();
              long diffDays = diff / (24 * 60 * 60 * 1000);
              if (diffDays<3) {


              // javafx.application.Platform.runLater(new Runnable() {
              // @Override
              // public void run() {

                  ObservableList<Node> childrens = CourseView.List.getChildren();
                  Node button = null;
                  for(Node node : childrens) {
                    if(CourseView.List.getRowIndex(node) == row && CourseView.List.getColumnIndex(node) == 2) {
                        button = node;
                        break;
                    }
                  }
                  Label closedLabel = new Label("Closed");

                  CourseView.List.getChildren().remove(button);
                  CourseView.List.add(closedLabel, 2, row);
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


class CancelButton extends Button {
  public CancelButton(String examStartTime, String courseCode, int row){
    setText("Cancel");
    bindToTime(examStartTime, courseCode, row);
  }
  private void bindToTime(final String examStartTime, final String courseCode, final int row) {
    Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
            try {
              DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
              Date currentTime = new Date();
              Date startTime = dateFormat.parse(examStartTime);
              long diff = startTime.getTime() - currentTime.getTime();
              long diffDays = diff / (24 * 60 * 60 * 1000);
              if (diffDays<3) {
                // javafx.application.Platform.runLater(new Runnable() {
                // @Override
                // public void run() {
                ObservableList<Node> childrens = CourseView.List.getChildren();
                Node button = null;
                for(Node node : childrens) {
                  if(CourseView.List.getRowIndex(node) == row && CourseView.List.getColumnIndex(node) == 2) {
                    button = node;
                    break;
                  }
                }
                // CountDown timeLabel = new CountDown(examStartTime, courseCode, row);
                // CourseView.List.add(timeLabel, 2, row);
                
                CourseView.List.getChildren().remove(button);
                CourseView.createCountDownLabel(examStartTime, courseCode, row);                
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

class ExamButton extends Button {
  public ExamButton(String examStartTime, String courseCode, int row){
    setText(" Exam ");
    bindToTime(examStartTime, courseCode, row);
  }
  private void bindToTime(final String examStartTime, String courseCode, final int row) {
    Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
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
                  ObservableList<Node> childrens = CourseView.List.getChildren();
                  Node button = null;
                  for(Node node : childrens) {
                    if(CourseView.List.getRowIndex(node) == row && CourseView.List.getColumnIndex(node) == 2) {
                        button = node;
                        break;
                    }
                  }
                  Label closedLabel = new Label("Closed");
    
                  CourseView.List.add(closedLabel, 2, row);
                  CourseView.List.getChildren().remove(button);
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
