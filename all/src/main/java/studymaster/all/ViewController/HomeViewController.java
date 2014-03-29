package studymaster.all.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * LoginViewController class is the super class of both examinee 
 * view client login view controller and invigilator client login
 * view controller.
 */
public abstract class HomeViewController extends ViewController {

	@FXML public final void gotoTestAction() {
    	director.pushStageWithFXML(getClass().getResource("/fxml/testView1.fxml"));
    }

	@Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		connector.setAndSendMessageContainer("profile", null);
	}
}