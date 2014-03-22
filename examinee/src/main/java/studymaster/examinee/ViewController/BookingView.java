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
import javax.swing.ButtonGroup;
import javax.swing.AbstractButton;

public class BookingView extends ViewController{

    @FXML protected Label titleLabel;
    @FXML protected GridPane timeTable;
    protected ToggleGroup buttonGroup = new ToggleGroup();
    private static String code;
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
                titleLabel.setText(code);
                showTimeTable(content.getJSONArray("examTime"), timeTable, buttonGroup);
            }
            else if(event.equals("booked")){
                if(content.getString("status").equals("success")){
                    alert("Exam successfully booked for " + code + "\nTime: " + start_time);
                    backView();
                }
            }

        } catch (Exception e) {
            System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
        }
    }

    public String book(ToggleGroup bg){
        return ((RadioButton)bg.getSelectedToggle()).getText();
    }

    public void selectExamTime(){
        start_time = book(buttonGroup);
        setBookedMsg();
        Connector.getInstance().sendMessageContainer();
    }

    public void backView(){
        director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
    }

    public static void setBookedMsg() {

        JSONObject content = new JSONObject();

        content.put("code", code);
        content.put("account", Connector.getInstance().getSender());
        content.put("start_time", start_time);

        Connector.setMessageContainer("booked", content);
    }

    private void showTimeTable(final JSONArray examTime, final GridPane timeTable, final ToggleGroup buttonGroup){
        final AnchorPane pane = (AnchorPane) director.getScene().getRoot();
        javafx.application.Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        ArrayList<RadioButton> radioButtonList = new ArrayList<RadioButton>();

                        for(int i=0; i<examTime.length(); i++){
                            radioButtonList.add(new RadioButton(((JSONObject)examTime.getJSONObject(i)).getString("start_time")));
                            timeTable.add(radioButtonList.get(i), 0, i);
                            radioButtonList.get(i).setToggleGroup(buttonGroup);
                        }

                    }catch (Exception e) {
                        System.out.println(e);
                        System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when adding component.");
                    }
                }
            });
    }
}
