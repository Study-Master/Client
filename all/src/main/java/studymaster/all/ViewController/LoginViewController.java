package studymaster.all.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * LoginViewController class is the super class of both examinee 
 * view client login view controller and invigilator client login
 * view controller.
 */
public abstract class LoginViewController extends ViewController{
	@FXML protected TextField accountField;
	@FXML protected PasswordField passwordField;
	@FXML protected Button loginButton;

	/**
	 * Binded to login button
	 */
	@FXML
	public final void loginAction() {
		System.out.println("[info] (" + getClass().getSimpleName() + " loginAction): Trying to login...");
		String account = accountField.getText();
		String password = DigestUtils.md5Hex(passwordField.getText());
		loginButton.setText("");
        Image LoadingIcon = new Image(getClass().getResourceAsStream("/image/Loading.gif"));
        loginButton.setGraphic(new ImageView(LoadingIcon));
        loginButton.setStyle("-fx-padding-left: 0; -fx-background-color: rgba(0, 102, 153, 1);");
        loginButton.setDisable(true);
		login(account, password);
	}

	/**
	 * Binded to exit button
	 */
	@FXML
	public final void exitAction() {
		System.out.println("[info] (" + getClass().getSimpleName() + " exitAction): Exiting...");
		System.exit(0);
	}

	/**
	 * Do the actuall login process
	 * @param account  account
	 * @param password password in md5Hex
	 */
	public abstract void login(String account, String password);
}