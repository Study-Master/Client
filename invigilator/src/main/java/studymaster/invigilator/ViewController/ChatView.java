package studymaster.invigilator.ViewController;

import javafx.scene.control.TextArea;
import studymaster.all.ViewController.ViewController;
import javafx.fxml.FXML;
import org.json.JSONObject;
import javafx.scene.control.Button;
import studymaster.socket.AudioEventHandler;
import studymaster.socket.AudioCl;

public class ChatView extends ViewController implements AudioEventHandler {

    @FXML private TextArea histroyArea;
    @FXML private TextArea sendArea;
    @FXML private Button sendButton;
    @FXML private Button voiceMessageButton;
    @FXML private Button playButton;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        voiceMessageButton.setDisable(true);
        playButton.setDisable(true);
    }

    @Override public void onMessage(String message) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
    }

    @FXML public final void sendAction() {
        String message = sendArea.getText();
        message = connector.getSender() + ": " + message + "\n";
        histroyArea.appendText(message);
        sendArea.clear();

        //TODO: Send message to server.
    }

    @FXML public final void onVoiceMessagePressed() {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onVoiceMessagePressed)");
        //TODO: Record a voice message
        voiceMessageButton.setText("Release To Send");

    }

    @FXML public final void onVoiceMessageReleased() {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onVoiceMessageReleased)");
        //TODO: Send a voice message
        voiceMessageButton.setText("Hold To Talk");
    }

    @FXML public final void playMessage(){
        //TODO: Play a voice message
    }

    @Override public void onAudioClientOpen() {
        voiceMessageButton.setDisable(false);
        playButton.setDisable(false);
    }
}
