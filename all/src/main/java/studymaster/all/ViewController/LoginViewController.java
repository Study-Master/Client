package studymaster.all.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import org.apache.commons.codec.digest.DigestUtils;

public abstract class LoginViewController extends ViewController{
    @FXML protected TextField accountField;
    @FXML protected PasswordField passwordField;
    @FXML protected Button loginButton;
    @FXML protected Button exitButton;
    @FXML protected ImageView loadingImgView;

    @FXML public final void loginAction() {
        System.out.println("[info] (" + getClass().getSimpleName() + " loginAction) Trying to login...");
        String account = accountField.getText();
        String password = DigestUtils.md5Hex(passwordField.getText());
        loginStart();
        login(account, password);
    }

    @FXML public final void exitAction() {
        System.out.println("[info] (" + getClass().getSimpleName() + " exitAction) Exiting");
        System.exit(0);
    }

    protected abstract void login(String account, String password);

    protected void loginStart() {
        loadingImgView.setVisible(true);
        accountField.setDisable(true);
        passwordField.setDisable(true);
        loginButton.setDisable(true);
        loginButton.setVisible(false);
        exitButton.setVisible(false);
    }

    protected void loginFailed(String reason) {
        AlertAction action = new AlertAction() {
            @Override public void ok(Stage stage) {
                connector.renew();
                stage.close();
                loadingImgView.setVisible(false);
                loginButton.setDisable(false);
                accountField.setDisable(false);
                passwordField.setDisable(false);
                passwordField.clear();
                loginButton.setVisible(true);
                exitButton.setVisible(true);
            }
        };
        director.invokeErrorAlert(reason, action);
    }
}
