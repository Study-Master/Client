package studymaster.examinee.ViewController;

import studymaster.all.ViewController.HomeViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;
import org.json.JSONArray;

import javafx.*;
import org.apache.commons.codec.digest.DigestUtils;

import javafx.fxml.Initializable;
import studymaster.socket.Connector;
import studymaster.socket.Callback;

public class BookingView extends ViewController{

	Scene 
	

	@FXML protected Label title;
	
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

		public void onOpen(String courseName) {
		System.out.println("[info] (CourseView onOpen) Socket's connection established.");
	}

	public void onClose(int code, String reason, boolean remote) {
		System.out.println("[info] (CourseView onClose) Socket's connection closed.");
	}

	public void onMessage(String message) {
		System.out.println("[info] (CourseView onMessage) Receive message: " + message);
		try {
			JSONObject msg = new JSONObject(message);
        	String event = msg.getString("event");
        	String endpoint = msg.getString("endpoint");
        	JSONObject content = msg.getJSONObject("content");
        	String courseName = content.getString("courseName");
        	JSONArray time = content.getJSONArray("examTime");

        	if(event.equals("booking")) {
        		title = courseName;
        		timeTable.setRow
        		for(int i=0; i++; i<time.length){
        			timeTable.
        		}
				

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
}