package studymaster.invigilator.ViewController;

import javafx.scene.control.TextArea;
import studymaster.all.ViewController.ViewController;
import studymaster.invigilator.App;
import javafx.fxml.FXML;
import org.json.JSONObject;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import studymaster.socket.AudioEventHandler;
import studymaster.socket.AudioSS;
import studymaster.socket.AudioCl;

public class ChatView extends ViewController {


	@FXML TextArea histroyArea;
	@FXML TextArea sendArea;
	@FXML Button messageButton;
	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
	}

	@Override
	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
	}

	public final void sendAction() {
		String message = sendArea.getText();
		message = connector.getSender() + ": " + message + "\n";
		histroyArea.appendText(message);
		sendArea.clear();

		//TODO: Send message to server.
	}

	public final void onVoiceMessagePressed() {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onVoiceMessagePressed)");
		//TODO: Send a voice message.
		AudioCl.startRecord();
		messageButton.setText("Release To Send");

	}

	public final void onVoiceMessageReleased() {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onVoiceMessageReleased)");
		//TODO: Send a voice message.
		messageButton.setText("Hold To Talk");
		AudioCl.stopRecord();
		AudioCl.submit();

	}

	public final void playMessage(){
		AudioSS.playAudio();
	}
}
