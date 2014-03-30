package studymaster.examinee.ViewController;

import studymaster.all.ViewController.ViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;
import org.json.JSONArray;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import java.util.ArrayList;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javax.swing.ButtonGroup;
import javax.swing.AbstractButton;
import javafx.stage.StageStyle;
import studymaster.all.ViewController.AlertAction;
import javafx.scene.control.ScrollPane;

public class BookingView extends ViewController{

    @FXML protected Label titleLabel;
    protected GridPane timeTable;
    protected ToggleGroup buttonGroup = new ToggleGroup();
    private static String code;
    private static String name;
    private static String start_time;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        Connector.getInstance().sendMessageContainer();
    }

    public void onMessage(String message) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);

        try {
            JSONObject msg = new JSONObject(message);
            String event = msg.getString("event");
            JSONObject content = msg.getJSONObject("content");

            if(event.equals("booking")) {
                code = content.getString("code");
                name = content.getString("name");
                show(code, name, titleLabel, content.getJSONArray("examTime"), buttonGroup);
            }
            else if(event.equals("booked")) {
                if(content.getString("status").equals("success")) {

                    AlertAction action = new AlertAction() {
                        @Override public void ok(Stage stage) {
                            stage.close();
                            backView();
                        }
                    };
                    Director.invokeOneButtonAlert("Exam Booking","Exam successfully booked for " + code + "\nTime: " + start_time, action);
                }
            }

        } catch (Exception e) {
            System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
            e.printStackTrace();
        }
    }

    public String book(ToggleGroup buttonGroup) {
        return ((RadioButton)buttonGroup.getSelectedToggle()).getText();
    }

    public void selectExamTime() {
        start_time = book(buttonGroup);
        setBookedMsg();
        Connector.getInstance().sendMessageContainer();
    }

    public void backView() {
        director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
    }

    public static void setBookedMsg() {

        JSONObject content = new JSONObject();

        content.put("code", code);
        content.put("account", Connector.getInstance().getSender());
        content.put("start_time", start_time);

        Connector.setMessageContainer("booked", content);
    }

    private void show(final String code, final String name, final Label titleLabel, final JSONArray examTime, final ToggleGroup buttonGroup){
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try{
                    titleLabel.setText(code + " " + name);
                    final AnchorPane pane = (AnchorPane) director.getScene().getRoot();
                    final ScrollPane sp = (ScrollPane) pane.lookup("#scrollpane");
                    final AnchorPane ap = (AnchorPane) (sp.lookup("#ap"));

                    timeTable = new GridPane();

                    timeTable.setVgap(25);

                    for(int i=0; i<examTime.length(); i++){
                        RadioButton tempButton = new RadioButton(((JSONObject)examTime.getJSONObject(i)).getString("start_time"));
                        timeTable.add(tempButton, 0, i);
                        tempButton.setToggleGroup(buttonGroup);
                    }
                    ap.getChildren().addAll(timeTable);
                }catch (Exception e) {
                    System.out.println(e);
                    System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when adding component.");
                }
            }
        });
    }
}
