package studymaster.invigilator.ViewController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import studymaster.all.ViewController.HomeViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.invigilator.CountDown;
import studymaster.invigilator.InvigilateButton;
import org.json.JSONObject;
import org.json.JSONArray;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Label;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import org.json.JSONException;
import studymaster.all.ViewController.AlertAction;

public class TaskView extends HomeViewController {
    protected static GridPane List;

    public static GridPane getList() {
        return List;
    }

    // @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
    //  super.initialize(location, resources);
    //  JSONObject content = new JSONObject();
    //  JSONObject exam = new JSONObject("{'exams': [{'code': 'CZ2001','name': 'Java','status': 'waiting','start_time': '2014/05/01 00:00:00'},{'code': 'CZ2002','name': 'Java2','status': 'finished','start_time': '2014/05/03 00:00:00'},{'code': 'CZ2006', 'name': 'Java6','status': 'invigilate','start_time': '2014/05/03 00:00:00'}]}");
    //  content.put("profile", exam);
    //  Connector.getInstance().setAndSendMessageContainer("profile_invigilator", content);
    //  System.out.println(content);
    // }

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        connector.setAndSendMessageContainer("profile_invigilator", null);
    }

    @Override public void onMessage(String message) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
        try {
            JSONObject msg = new JSONObject(message);
            String event = msg.getString("event");
            String endpoint = msg.getString("endpoint");
            final JSONObject content = msg.getJSONObject("content");

            if(event.equals("profile_invigilator")) {
                showCourseList(content);
            }
            else if(event.equals("enable_invigilation")) {
                enableInvigilation(content);
            }
            else if (event.equals("logout")) {
                logout(content);
            }
        }
        catch (Exception e) {
            System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
        }
    }

    private void logout(JSONObject content) {
        String status = content.getString("status");
        System.out.println("[Info] Logout success.");
        if(status.equals("success")) {
            System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Login successfully.");
            AlertAction action = new AlertAction() {
                @Override public void ok(Stage stage) {
                    Director.pushStageWithFXML(getClass().getResource("/fxml/loginView.fxml"));
                    connector.renew();
                    stage.close();
                }
            };
            Director.invokeOneButtonAlert("Logout", "You have logged out the system!", action);
        }

        else if(status.equals("failed")) {
            System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Login failed.");
            systemErrorAlert(content.getString("reason"));
        }
    }

    private void enableInvigilation(final JSONObject content) {
        //Remove count dwon label, add exam button
        //pass the test
        final String examCode = content.getString("code");
        final String examStartTime = content.getString("start_time");
        try {
            javafx.application.Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ObservableList<Node> childrens = TaskView.getList().getChildren();
                    CountDown label = null;
                    for (Node node : childrens) {
                        if (node instanceof CountDown) {
                            if(content.getString("code").equals(((CountDown)node).getCourseCode()) && content.getString("start_time").equals(((CountDown)node).getStartTime())) {
                                label = (CountDown)node;
                                break;
                            }
                        }
                    }
                    if (label == null) {
                        System.err.println("[Err] Wrong message from server! Don't find the node!");
                    }
                    else {
                        createInvigilationButton(examCode, examStartTime, label.getRow());
                        List.getChildren().remove(label);
                    }
                }
            });
        }
        catch (Exception e) {
            System.err.println("Error occurred in exam_enabled");
        }
    }

    private void showCourseList(final JSONObject content) {
        //AlertInfo.setCourseView(this);
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
                        JSONArray exams = profile.getJSONArray("exams");
                        ArrayList<JSONObject> examsArray = new ArrayList<JSONObject>();
                        String status;
                        GridPane examList = new GridPane();
                        List = examList;
                        AnchorPane.setTopAnchor(examList, 20.0);
                        AnchorPane.setLeftAnchor(examList, 60.0);
                        AnchorPane.setRightAnchor(examList, 60.0);
                        AnchorPane.setBottomAnchor(examList, 25.0);
                        ColumnConstraints col1 = new ColumnConstraints();
                        col1.setPercentWidth(15);
                        ColumnConstraints col2 = new ColumnConstraints();
                        col2.setPercentWidth(35);
                        ColumnConstraints col3 = new ColumnConstraints();
                        col3.setPercentWidth(30);
                        ColumnConstraints col4 = new ColumnConstraints();
                        col4.setPercentWidth(20);
                        examList.getColumnConstraints().addAll(col1,col2,col3,col4);
                        examList.setVgap(25);
                        col4.setHalignment(HPos.RIGHT);
                        //Add exams into an ArrayList
                        for (int i=0; i<exams.length(); i++) {
                            examsArray.add(exams.getJSONObject(i));
                        }

                        //Sort the ArrayList
                        // Collections.sort(examsArray, new Comparator<JSONObject>() {
                        //      @Override public int compare(JSONObject exam1, JSONObject exam2)
                        //      {
                        //          if ( ("closed".equals(exam1.getString("status")) || "finished".equals(exam1.getString("status"))) &&
                        //              ("closed".equals(exam2.getString("status")) || "finished".equals(exam2.getString("status"))) ) {
                        //              return exam1.getString("code").compareTo(exam2.getString("code"));
                        //          }
                        //          else if ("closed".equals(exam1.getString("status")) || "finished".equals(exam1.getString("status"))) {
                        //              return 1;
                        //          }
                        //          else if ("closed".equals(exam2.getString("status")) || "finished".equals(exam2.getString("status"))) {
                        //              return -1;
                        //          }
                        //          else {
                        //              return exam1.getString("code").compareTo(exam2.getString("code"));
                        //          }
                        //      }
                        //  });
                        //Display the exams
                        for(int i=0; i<examsArray.size(); i++) {
                            final JSONObject exam = (JSONObject)examsArray.get(i);
                            //First 2 columns
                            Label code = new Label(exam.getString("code"));
                            code.setStyle("-fx-text-fill: black;");
                            Label name = new Label(exam.getString("name"));
                            name.setStyle("-fx-text-fill: black;");
                            Label start_time = new Label(exam.getString("start_time"));
                            start_time.setStyle("-fx-text-fill: rgba(255, 214, 90, 1);-fx-font-size: 20;");


                            examList.add(code, 0, i);
                            examList.add(name, 1, i);
                            examList.add(start_time, 2, i);

                            status = exam.getString("status");
                            String examCode = exam.getString("code");
                            String examStartTime = exam.getString("start_time");

                            if (status.equals("waiting")) {
                                //Countdown label
                                createCountDownLabel(examCode, examStartTime, i);
                            }
                            else if (status.equals("finished")) {
                                //Finish label
                                Label label = new Label("Finished");
                                examList.add(label, 3, i);
                            }
                            else if (status.equals("invigilate")) {
                                //invigilation button
                                createInvigilationButton(examCode, examStartTime, i);
                            }
                        }
                        ap.getChildren().addAll(examList);
                    } catch (JSONException e) {
                        System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when adding component.");
                        e.printStackTrace();
                    }
                }
            });
    }

    public static void createCountDownLabel(String examCode, String examStartTime, int row) {
        CountDown timeLabel = new CountDown(examCode, examStartTime, row);
        List.add(timeLabel, 3, row);
    }

    public static void createInvigilationButton(final String examCode, final String examStartTime, final int row) {
        javafx.application.Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    final InvigilateButton button = new InvigilateButton(examCode, examStartTime, row);
                    button.setPrefWidth(150);
                    button.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                button.setText("");
                                Image LoadingIcon = new Image(getClass().getResourceAsStream("/image/Loading.gif"));
                                button.setGraphic(new ImageView(LoadingIcon));
                                button.setStyle("-fx-padding-left: 0; -fx-background-color: rgba(0, 102, 153, 1);");
                                button.setDisable(true);
                                setAndSendInvigilationMsg(button.getCourseCode(), button.getExamStartTime());
                                Director.pushStageWithFXML(getClass().getResource("/fxml/invigilateView.fxml"));
                            }
                        });
                    List.add(button, 3, row);
                }
            });
    }

    public static void setAndSendInvigilationMsg(String exam, String start_time) {
        JSONObject content = new JSONObject();
        content.put("code", exam);
        content.put("start_time", start_time);
        Connector.getInstance().setAndSendMessageContainer("invigilate", content);
        System.out.println("\n"+content+"\n");
    }
}
