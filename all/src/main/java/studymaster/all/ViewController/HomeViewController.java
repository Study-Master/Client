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

	public final void gotoTestAction() {
    	director.pushStageWithFXML(getClass().getResource("/fxml/testView1.fxml"));
    }

    public final void onLogout() {
    connector.setAndSendMessageContainer("logout", null);
    }
}
