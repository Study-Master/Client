package studymaster.all.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import org.apache.commons.codec.digest.DigestUtils;

public abstract class TestViewController extends ViewController {

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
		System.out.println("[info] (LoginViewController, exitAction): Exiting...");
		System.exit(0);
	}

    // test: audio test and webcam test
    public abstract void test(String account);
}
