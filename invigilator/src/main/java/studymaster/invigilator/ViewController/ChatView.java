package studymaster.invigilator.ViewController;

import javafx.scene.control.TextArea;
import studymaster.all.ViewController.ViewController;
import studymaster.invigilator.Slots;
import javafx.fxml.FXML;
import org.json.JSONObject;
import javafx.scene.control.Button;
import studymaster.socket.AudioEventHandler;
import studymaster.socket.AudioCl;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatView extends ViewController implements AudioEventHandler {

    private static int counter = 0;
    @FXML private TextArea histroyArea;
    @FXML private TextArea sendArea;
    @FXML private Button sendButton;
    @FXML private Button voiceMessageButton;
    @FXML private Button playButton;
    private int label;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        label = counter;
        counter++;
        connector.retain(this);
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
        Slots data = Slots.getInstance();
        System.out.println("label: " + label);
        String name = data.getName(label);
        int exam_pk = data.getExam(label);
        Format df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = df.format(date);
        JSONObject content = new JSONObject();
        content.put("name", name);
        content.put("exam_pk", exam_pk);
        content.put("system_time", time);
        content.put("msg", message);
        connector.setAndSendMessageContainer("exam_chat", content);
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
