package studymaster.all.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import org.apache.commons.codec.digest.DigestUtils;

public abstract class LoginViewController extends ViewController{
    @FXML protected TextField accountField;
    @FXML protected PasswordField passwordField;
    @FXML protected Button loginButton;

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
        loginButton.setText("");
        Image LoadingIcon = new Image(getClass().getResourceAsStream("/image/Loading.gif"));
        loginButton.setGraphic(new ImageView(LoadingIcon));
        loginButton.setStyle("-fx-padding-left: 0; -fx-background-color: rgba(0, 102, 153, 1);");
        loginButton.setDisable(true);
    }

    protected void loginFailed() {
        AlertAction action = new AlertAction() {
            @Override public void ok(Stage stage) {
                connector.renew();
                stage.close();
                loginButton.setDisable(false);
                loginButton.setText("Login");
                loginButton.setGraphic(null);
                loginButton.setStyle("-fx-background-color: white;");
            }
        };
        director.invokeErrorAlert("Error, please check your Internet connection or restart the program.", action);
    }
}
