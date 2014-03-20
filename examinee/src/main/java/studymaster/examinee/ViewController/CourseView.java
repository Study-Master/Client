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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.json.JSONException;

public class CourseView extends HomeViewController {
  GridPane List;

  @Override
	public void onMessage(String message) {
    System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
    try {
      JSONObject msg = new JSONObject(message);
      String event = msg.getString("event");
      String endpoint = msg.getString("endpoint");
      final JSONObject content = msg.getJSONObject("content");
      //Button button = new Button();

      if(event.equals("profile")) {
        showCourseList(content);
      }
      // else if (event.equals("cancel")) {
      //   //dialog - inform student
      //   JSONObject cancelInfo = content;
      //   if (cancelInfo.getString("status").equals("successful")) {
      //     System.out.println("[Info] Successfully cancel the booking!");
      //     //alert
      //     try{
      //       String examStartTime = cancelInfo.getString("start_time");
      //       int row=1;
      //       ObservableList<Node> childrens = List.getChildren();
      //         Node cancelButton = null;
      //         for(Node node : childrens) {
      //           System.out.println("+1");
      //           if (node.isDisabled()) {
      //             cancelButton = node;
      //             row = List.getRowIndex(node);
                  
      //             break;
      //           }
      //         }
      //       List.getChildren().remove(cancelButton);
      //       BookButton button = new BookButton(examStartTime, List, row);
      //     }
      //     catch (Exception e) {
      //       System.err.println("[err] Fail to change CancelButton to BookButton");
      //     }
      //   }
      //   else {
      //     //alert
      //   }
      // }
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
          final AnchorPane pane = (AnchorPane) director.getScene().getRoot();
    // //final AnchorPane spane = (AnchorPane) (pane.lookup("#spane"));
    // try {
    final ScrollPane sp = (ScrollPane) pane.lookup("#scrollpane");
    System.out.println(sp.getId());
    final AnchorPane ap = (AnchorPane) (sp.lookup("#ap"));
    System.out.println(ap.getId());
    //sp.getChildren().remove(ap);
  // }
  // catch (Exception e){
  //   System.err.println(e.getMessage());
  // }
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

          //courseList.setStyle("-fx-border: 2px solid; -fx-border-color: red; -fx-border-insets: 5;");

          for (int i=0; i<courses.length(); i++) {
            coursesArray.add(courses.getJSONObject(i));
          }

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

          for(int i=0; i<coursesArray.size(); i++) {
            final JSONObject course = (JSONObject)coursesArray.get(i);

            Label code = new Label(course.getString("code"));
            code.setStyle("-fx-text-fill: black;");
            Label name = new Label(course.getString("name"));
            name.setStyle("-fx-text-fill: black;");            
            courseList.add(code, 0, i);
            courseList.add(name, 1, i);

            status = course.getString("status");

            if (status.equals("closed")) {
              Label label = new Label("Closed");
              courseList.add(label, 2, i);
            }
            else if (status.equals("finished")) {
              Label label = new Label("Finished");
              courseList.add(label, 2, i);
            }
            else if (status.equals("unbooked")) {
              String examStartTime = course.getString("start_time");
              BookButton button = new BookButton(examStartTime, courseList, i);
              button.setPrefWidth(120);

              button.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                  setBookingMsg(course.getString("code"), content.getString("account"));
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
                long diff = Math.abs(startTime.getTime() - currentTime.getTime());
                long diffDays = diff / (24 * 60 * 60 * 1000);
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;

                if (diffDays>=3) {
                  final CancelButton button = new CancelButton(examStartTime, courseList, i);
                  button.setPrefWidth(120);
                  button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                      button.setText("");
                      Image LoadingIcon = new Image(getClass().getResourceAsStream("/image/Loading.gif"));
                      button.setGraphic(new ImageView(LoadingIcon));
                      button.setStyle("-fx-padding-left: 0; -fx-background-color: rgba(0, 102, 153, 1);");
                      button.setDisable(true);
                      //send msg to server
                      JSONObject sendMsg = new JSONObject();
                      JSONObject sendContent = new JSONObject();
                      sendMsg.put("event", "cancel");
                      sendMsg.put("endpoint", "Java Client");
                      sendMsg.put("content", sendContent);
                      sendContent.put("code", course.getString("code"));
                      sendContent.put("account", content.getString("account"));
                      Connector.setMessageContainer(sendMsg.toString());
                      Connector.getInstance().sendMessageContainer();
                    }
                  });
                  courseList.add(button, 2, i);
                }
                else if (diffMinutes>=15) {
                  CountDown timeLabel = new CountDown(examStartTime, courseList, i);
                  courseList.add(timeLabel, 2, i);
                }
                else {
                  ExamButton button = new ExamButton(examStartTime, courseList, i);
                  button.setPrefWidth(120);
                  button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                      setExamMsg(course.getString("code"));
                      director.pushStageWithFXML(getClass().getResource("/fxml/authView.fxml"));
                    }
                  });
                  courseList.add(button, 2, i);
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
    Msg.put("content", Content);
    Connector.setMessageContainer(Msg.toString());
  }

}

class CountDown extends Label {
  public CountDown(String examStartTime, GridPane courseList, int row) {
    bindToTime(examStartTime, courseList, row);
  }

  private void bindToTime(final String examStartTime, final GridPane courseList, final int row) {
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
                  ObservableList<Node> childrens = courseList.getChildren();
                  Node label = null;
                  for(Node node : childrens) {
                    if(courseList.getRowIndex(node) == row && courseList.getColumnIndex(node) == 2) {
                      label = node;
                      break;
                    }
                  }
                  ExamButton examButton = new ExamButton(examStartTime, courseList, row);
                  examButton.setPrefWidth(120);
                  examButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                      studymaster.examinee.ViewController.CourseView.setExamMsg(Connector.getInstance().getSender());
                      studymaster.all.ViewController.Director.pushStageWithFXML(getClass().getResource("/fxml/examView.fxml"));
                    }
                  });
                  courseList.add(examButton, 2, row);
                  courseList.getChildren().remove(label);
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
  public BookButton(String examStartTime, GridPane courseList, int row){
    setText(" Book ");
    bindToTime(examStartTime, courseList, row);
  }
  private void bindToTime(final String examStartTime, final GridPane courseList, final int row) {
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
                  ObservableList<Node> childrens = courseList.getChildren();
                  Node button = null;
                  for(Node node : childrens) {
                    if(courseList.getRowIndex(node) == row && courseList.getColumnIndex(node) == 2) {
                        button = node;
                        break;
                    }
                  }
                  Label closedLabel = new Label("Closed");
                  courseList.add(closedLabel, 2, row);
                  courseList.getChildren().remove(button);
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
  public CancelButton(String examStartTime, GridPane courseList, int row){
    setText("Cancel");
    //Image LoadingIcon = new Image(getClass().getResourceAsStream("/image/Loading.gif"));
    //setGraphic(new ImageView(LoadingIcon));
    bindToTime(examStartTime, courseList, row);
  }
  private void bindToTime(final String examStartTime, final GridPane courseList, final int row) {
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
                ObservableList<Node> childrens = courseList.getChildren();
                Node button = null;
                for(Node node : childrens) {
                  if(courseList.getRowIndex(node) == row && courseList.getColumnIndex(node) == 2) {
                    button = node;
                    break;
                  }
                }
                CountDown timeLabel = new CountDown(examStartTime, courseList, row);
                courseList.getChildren().remove(button);
                courseList.add(timeLabel, 2, row);
                
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
  public ExamButton(String examStartTime, GridPane courseList, int row){
    setText(" Exam ");
    bindToTime(examStartTime, courseList, row);
  }
  private void bindToTime(final String examStartTime, final GridPane courseList, final int row) {
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
                  ObservableList<Node> childrens = courseList.getChildren();
                  Node button = null;
                  for(Node node : childrens) {
                    if(courseList.getRowIndex(node) == row && courseList.getColumnIndex(node) == 2) {
                        button = node;
                        break;
                    }
                  }
                  Label closedLabel = new Label("Closed");
                  courseList.add(closedLabel, 2, row);
                  courseList.getChildren().remove(button);
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
