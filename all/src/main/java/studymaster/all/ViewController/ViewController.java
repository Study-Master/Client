package studymaster.all.ViewController;

import javafx.fxml.Initializable;
import studymaster.socket.Connector;
import studymaster.socket.Callback;
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

/**
 * ViewController class is the root class of all view controller class
 */
public abstract class ViewController implements Initializable, Callback {
	protected Director director;
	protected Connector connector;

	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		javafx.scene.text.Font.loadFont(ViewController.class.getResource("/font/HelveticaLT25UltraLight.ttf").toExternalForm(), 10);
		javafx.scene.text.Font.loadFont(ViewController.class.getResource("/font/HelveticaLT45Light.ttf").toExternalForm(), 10);
		System.out.println("[info] ("+ getClass().getSimpleName() +" initialize): Load fxml file from " + location);
		Connector.setDelegate(this);
		System.out.println("[info] ("+ getClass().getSimpleName() +" initialize): Connector delegate now is " + getClass().getSimpleName());
		director = Director.getInstance();
		connector = Connector.getInstance();
	}

	@Override
	public void onOpen(short httpStatus, String httpStatusMessage) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onOpen) Socket's connection established.");
	}

	public void onClose(int code, String reason, boolean remote) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onClose) Socket's connection closed.");
		alert("Connection has been closed");
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("[err] ("+ getClass().getSimpleName() +" onError) An error has been caught.");
	}

	protected void alert(final String content) {
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				final Stage dialogStage = new Stage();
				dialogStage.initModality(Modality.WINDOW_MODAL);
				Button button = new Button("OK");
				Text text = new Text(content);
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
						connector = Connector.renew();
					}
				});
			}
		});
	}
}