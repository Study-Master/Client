package studymaster.examinee.ViewController;

import studymaster.all.ViewController.ViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;
import org.json.JSONArray;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import java.util.ArrayList;
import javafx.scene.Parent;

import org.apache.commons.codec.digest.DigestUtils;

import javafx.fxml.Initializable;
import studymaster.socket.Connector;
import studymaster.socket.Callback;


public class BookingView extends ViewController{

	
	

 

	

	@FXML protected Label titleLabel;
	@FXML protected GridPane timeTable;
	
	/**
	 * Binded to login button
	 */
	// @FXML
	// public final void loginAction() {
	// 	System.out.println("[info] (LoginViewController, loginAction): Trying to login...");
	// 	String account = accountField.getText();
	// 	String password = DigestUtils.md5Hex(passwordField.getText());
	// 	login(account, password);
	// }


	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		
		//just for test
		connector.send("{'event': 'booking','endpoint': 'Server','content':{'courseName': 'CZ2006','examTime': [{'date': '11/11/1111','timeSlot': '11:11 - 11:11'}, {'date': '22/22/2222','timeSlot': '22:22 - 22:22'}, {'date': '33/33/3333','timeSlot': '33:33 - 33:33'}]}}");
		System.out.println("initialized");
	}

	public void onOpen(String courseName) {
		System.out.println("[info] (CourseView onOpen) Socket's connection established.");

	}

	public void onClose(int code, String reason, boolean remote) {
		System.out.println("[info] (CourseView onClose) Socket's connection closed.");
	}

	public void onMessage(String message) {
		System.out.println("[info] (BookingView onMessage) Receive message: " + message);
		
		try {
			JSONObject msg = new JSONObject(message);
        	String event = msg.getString("event");
        	String endpoint = msg.getString("endpoint");
        	JSONObject content = msg.getJSONObject("content");
        	String courseName = content.getString("courseName");
        	JSONArray time = content.getJSONArray("examTime");

        	if(event.equals("booking")) {
        		
        		titleLabel.setText(courseName);
        		showTimeTable(time, timeTable);
			}
        

		} catch (Exception e) {
			System.err.println("[err] (CourseView onMessage) Error when decoding JSON response string.");
		}
	}

	public void onError(Exception ex) {
		System.err.println("[err] (CourseView onError) An error has been caught.");
	}

	public void nextView() {
		javafx.application.Platform.runLater(new Runnable() {
  			@Override
  			public void run() {
  				try {
  					director.pushStageWithFXML(App.class.getResource("/fxml/courseView.fxml"));
  				} catch (Exception e) {
  					System.err.println("[err] (LoginView nextView) Error when switching scene");
  				}
  			}
		});
	}

	private void showTimeTable(final JSONArray time, final GridPane timeTable){
		
		final AnchorPane pane = (AnchorPane) director.getScene().getRoot();
		
       	/*The Json message we assume to used here:
			{
	    		"event": "booking",
    			"endpoint": "Server",
    			"content":  {
            					"courseName": "*****",
            					"examTime": [{
												"date": "dd/mm/yyyy",
            									"timeSlot": "**:** - **:**"
            								}, {}, {}]//json array
        					}
			}
		*/
		
		javafx.application.Platform.runLater(new Runnable() {
      		@Override
      		public void run() {
      			try{
      				// GridPane timeTable = new GridPane();
					ArrayList<Label> temp = new ArrayList<Label>();
       				ArrayList<RadioButton> tempButton = new ArrayList<RadioButton>();

					for(int i=0; i<time.length(); i++){
						temp.add(new Label()); 
						System.out.println(((JSONObject)time.get(i)).getString("date") + " " + ((JSONObject)time.get(i)).getString("timeSlot"));
   						temp.get(i).setText(((JSONObject)time.get(i)).getString("date") + " " + ((JSONObject)time.get(i)).getString("timeSlot"));
       					tempButton.add(new RadioButton());
					}

					for(int i=0; i<time.length(); i++){
						timeTable.add(temp.get(i), 0, i);
						timeTable.add(tempButton.get(i), 1, i);
					}
					pane.getChildren().addAll(timeTable);
				}catch (Exception e) {
          			System.err.println("[err] (CourseView onMessage) Error when adding component.");
        		}
			}
		});
	}
}