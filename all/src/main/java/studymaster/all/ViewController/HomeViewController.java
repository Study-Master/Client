package studymaster.all.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * LoginViewController class is the super class of both examinee 
 * view client login view controller and invigilator client login
 * view controller.
 */
public abstract class HomeViewController extends ViewController implements AlertAction{

	@FXML public final void gotoTestAction() {
    	director.pushStageWithFXML(getClass().getResource("/fxml/testView1.fxml"));
    }

	@Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		connector.setAndSendMessageContainer("profile", null);
	}

	@Override public void ok(Stage stage) {
		stage.close();
		System.out.println("[Info] Alert stage is closed.");
	}

	@Override public void cancel(Stage stage) {
		stage.close();
		System.out.println("[Info] Alert stage is closed.");
	}
}