package studymaster.examinee.ViewController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import studymaster.all.ViewController.HomeViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.CancelButton;
import studymaster.examinee.BookButton;
import studymaster.examinee.CountDown;
import studymaster.examinee.ExamButton;
import studymaster.examinee.QuestionDatabase;
import org.json.JSONObject;
import org.json.JSONArray;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Label;
import javafx.geometry.HPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONException;

public class CourseView extends HomeViewController {
    protected static GridPane List;
    protected static String Account;

    public static GridPane getList() {
        return List;
    }

    public static String getAccount() {
        return Account;
    }

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
            else if(event.equals("exam_question")) {
                JSONArray question_set = content.getJSONArray("question_set");
                QuestionDatabase database = QuestionDatabase.getInstance();
                //sort according to question number
                //from first to last, add the question from the question_set to the QuestionDatabase
                //once we add it, remove it from the jsonArray
                database.setCourseCode(content.getString("code"));//course code is put at database[0]
                System.out.println("set course code:" + content.getString("code") + "\n\n");
                while (question_set.length() != 0) {
                    for (int i=0; i<question_set.length(); i++) {
                        if ((question_set.getJSONObject(i).getInt("number")-1) == database.getQuestionSetSize()) {
                            database.addQuestion(question_set.getJSONObject(i));
                            System.out.println("\n" + question_set.getJSONObject(i).toString() + "\n");
                            question_set.remove(i);
                        }
                    }
                }
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
                    System.out.println("[Info] Cancel failed");
                    alert("Can't cancel this exam. " + cancelInfo.getString("error"));         
                }
            }
            else if (event.equals("showCountDown")) {
                
            }
            else if (event.equals("showExamButton")) {

            }
            else if (event.equals("showCloseLabel")) {
                
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
                                JSONObject sendContent = new JSONObject();
                                sendContent.put("code", courseCode);
                                sendContent.put("account", Account);
                                Connector.getInstance().setAndSendMessageContainer("cancel", sendContent);
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
        JSONObject content = new JSONObject();
        content.put("code", course);
        //content.put("account", account);
        Connector.setMessageContainer("booking", content);
    }

    public static void setExamMsg(String course) {
        JSONObject content = new JSONObject();
        content.put("code", course);
        //content.put("account", Account);
        Connector.getInstance().setAndSendMessageContainer("exam", content);
    }
}







