package studymaster.examinee.ViewController;

import studymaster.all.ViewController.LoginViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.VBoxBuilder;
import javafx.event.ActionEvent;
import javafx.event.*;
import javafx.stage.StageStyle;


public class LoginView extends LoginViewController {

	@Override
	public void onMessage(String message) {
		System.out.println("[info] (LoginView onMessage) Receive message: " + message);
		try {
			JSONObject msg = new JSONObject(message);
        	String event = msg.getString("event");
        	String endpoint = msg.getString("endpoint");
        	final JSONObject content = msg.getJSONObject("content");

			if(event.equals("login")) {
				String status = content.getString("status");

				if(status.equals("success")) {
					System.out.println("[info] (LoginView onMessage) Login successfully.");
					director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
				}

				else if(status.equals("failed")) {
					System.out.println("[info] (LoginView onMessage) Login failed.");

					javafx.application.Platform.runLater(new Runnable() {
						@Override
						public void run() {
							final Stage dialogStage = new Stage();
							dialogStage.initModality(Modality.WINDOW_MODAL);
							Button button = new Button("OK");
							Text text = new Text(content.getString("reason"));
							dialogStage.initStyle(StageStyle.UNDECORATED);
							//hide the title bar of the alert window

							dialogStage.setScene(new Scene(VBoxBuilder.create()
																	  .children(text, button)
																	  .alignment(Pos.CENTER)
																	  .padding(new Insets(8))
																	  .minHeight(100)
																	  .minWidth(200)
																	  .maxWidth(400)
																	  .build()));
							dialogStage.show();


							button.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									dialogStage.close();
								}
							});

							connector = Connector.renew();
						}
					});
				}

				else {
					System.err.println("[err] (LoginView onMessage) Unexpected JSON response string.");
				}
			}
		} catch (Exception e) {
			System.err.println(e);
			System.err.println("[err] (LoginView onMessage) Error when decoding JSON response string.");
		}
	}

	@Override
	public void login(String account, String password) {
		Connector.setSender(account);

		try {
			boolean connected = true;
			if(!connector.isOpen())
				connected = connector.connectBlocking();
			if(connected) {
				connector.login(password);
			}
		} catch(Exception e) {
			System.err.println("[err] (LoginView login) An error is caught, no connection.");
		}
	}
        
	public void onEnter() {
		loginAction();
	}

}