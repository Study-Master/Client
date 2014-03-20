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
	JSONObject newMsg = new JSONObject();
	JSONObject newContent = new JSONObject();

	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		Connector.getInstance().sendMessageContainer(); 
	}

	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
		
		try {
			JSONObject msg = new JSONObject(message);
        	String event = msg.getString("event");
        	
        	JSONObject content = msg.getJSONObject("content");
        	String studentID = content.getString("account");
        	String courseCode = content.getString("code");
        	JSONArray time = content.getJSONArray("examTime");
        	newMsg.put("event", "booked");
        	newMsg.put("endpoint", "Examinee");
			newMsg.put("content", newContent);
			newContent.put("account", studentID);
			newContent.put("code", courseCode);

        	if(event.equals("booking")) {
        		titleLabel.setText(courseCode);
        		showTimeTable(time, timeTable, buttonGroup);
			}
        

		} catch (Exception e) {
			System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
		}
	}

	private void showTimeTable(final JSONArray time, final GridPane timeTable, final ToggleGroup buttonGroup){	
		final AnchorPane pane = (AnchorPane) director.getScene().getRoot();
		javafx.application.Platform.runLater(new Runnable() {
      		@Override
      		public void run() {
      			try{
       				ArrayList<RadioButton> tempButton = new ArrayList<RadioButton>();

					for(int i=0; i<time.length(); i++){
       					tempButton.add(new RadioButton(((JSONObject)time.getJSONObject(i)).getString("start_time")));
       					
       					timeTable.add(tempButton.get(i), 0, i);
       					tempButton.get(i).setToggleGroup(buttonGroup);
					}

				}catch (Exception e) {
					System.out.println(e);
          			System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when adding component.");
        		}
			}
		});
	}

	public String book(ToggleGroup bg){
		return ((RadioButton)bg.getSelectedToggle()).getText();
	}

	public void selectExamTime(){
		returnMessage();
	}

	public void backView(){
		director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
	}

	private void returnMessage(){
		newContent.put("examTime", book(buttonGroup));
		connector.send(newMsg.toString());
	}
}

