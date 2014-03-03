package studymaster.all.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
<<<<<<< HEAD
import org.apache.commons.codec.digest.DigestUtils;

public abstract class TestViewController extends ViewController {
    @FXML protected TextField accountField;
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources){
        super.initialize(location, resources);
=======
import javafx.scene.control.Label;

public abstract class TestViewController extends ViewController {
    @FXML protected TextField accountField;
    @FXML protected Label nameField;
    
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources){
        super.initialize(location, resources);
        nameField.setText(connector.getSender());
>>>>>>> origin/master
    }

    /**
	 * Binded to login button
	 */
	@FXML
	public final void testAction() {
		System.out.println("[info] (TestViewController, testAction): Trying to test...");
		String account = accountField.getText();
		test(account);
	}

	/**
	 * Binded to exit button
	 */
	@FXML
	public final void exitAction() {
<<<<<<< HEAD
		System.out.println("[info] (LoginViewController, exitAction): Exiting...");
=======
		System.out.println("[info] (TestViewController, exitAction): Exiting...");
>>>>>>> origin/master
		System.exit(0);
	}

    // test: audio test and webcam test
    public abstract void test(String account);
}
