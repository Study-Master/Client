package studymaster.all.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * LoginViewController class is the super class of both examinee 
 * view client login view controller and invigilator client login
 * view controller.
 */
public abstract class LoginViewController extends ViewController{
	@FXML protected TextField accountField;
	@FXML protected PasswordField passwordField;

	/**
	 * Binded to login button
	 * @throws Exception Any exception in login process
	 */
	@FXML
	public final void loginAction() {
		System.out.println("[loginAction] Login...");
		String account = accountField.getText();
		String password = DigestUtils.md5Hex(passwordField.getText());
		login(account, password);
	}

	/**
	 * Do the actuall login process
	 * @param account  account
	 * @param password password in md5Hex
	 */
	public abstract void login(String account, String password);
}