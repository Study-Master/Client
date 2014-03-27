package studymaster.examinee.ViewController;

import javafx.scene.control.TextArea;
import studymaster.all.ViewController.ViewController;
import studymaster.examinee.App;
import javafx.fxml.FXML;
import org.json.JSONObject;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import studymaster.examinee.AlertInfo;
import studymaster.socket.Connector;
import studymaster.examinee.ViewController.CourseView;

public class AlertView extends ViewController {

	@FXML Label info;
	@FXML Label title;
	@FXML Button closeButton;
	@Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		info.setText(AlertInfo.getInfo());
		title.setText(AlertInfo.getTitle());
	}
			
	@Override public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
	}

	@FXML private void closeButtonAction(){
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	    System.out.println("[Info] AlertView closed.");
	    Connector.setDelegate(AlertInfo.getCourseView());
	}
}