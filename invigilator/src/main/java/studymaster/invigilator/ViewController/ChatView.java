package studymaster.invigilator.ViewController;

import javafx.scene.control.TextArea;
import studymaster.all.ViewController.ViewController;
import studymaster.media.SoundUtil;
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
    private AudioCl audioCl;
    private byte[] receiveAudio;
    private int label;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        audioCl = AudioCl.getInstance();
        audioCl.addDelegate(this);
        label = counter;
        counter++;
        connector.retain(this);
    }

    @Override public void onMessage(String message) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
        try {
            JSONObject msg = new JSONObject(message);
            String event = msg.getString("event");
            String endpoint = msg.getString("endpoint");
            final JSONObject content = msg.getJSONObject("content");

            if(event.equals("exam_chat")) {
                Slots data = Slots.getInstance();
                String name = content.getString("account");
                if(name.equals(data.getName(label))) {
                    String time = content.getString("system_time");
                    String m = content.getString("msg");
                    m = name + ": " + time + "\n" + m + "\n";
                    histroyArea.appendText(m);
                }
            }
        } catch(Exception e) {
            System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
        }
    }

    @FXML public final void sendAction() {
        Format df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = df.format(date);
        
        String message = sendArea.getText();
        String m = connector.getSender() + ": " + time + "\n" + message + "\n";
        histroyArea.appendText(m);
        sendArea.clear();
        Slots data = Slots.getInstance();
        String name = data.getName(label);
        int exam_pk = data.getExam(label);
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
        SoundUtil.startRecord();
        voiceMessageButton.setText("Release To Send");

    }

    @FXML public final void onVoiceMessageReleased() {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onVoiceMessageReleased)");
        //TODO: Send a voice message
        byte[] audio = SoundUtil.stopRecord();
        Slots data = Slots.getInstance();
        String name = data.getName(label);
        if(audioCl.isConnected()) {
            System.out.println("[info](ChatView Sending audio)");
            audioCl.sendMedia(audio, name);
        }
        voiceMessageButton.setText("Hold To Talk");
    }

    @FXML public final void playMessage(){        
        System.out.println("[info] ("+ getClass().getSimpleName() +" playMessage)");
        if(receiveAudio!=null) {
            SoundUtil.playAudio(receiveAudio);
        }
    }

    @Override public void onAudioMessage(String name, byte[] receive) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onAudioMessage)");
        Slots data = Slots.getInstance();
        String target = data.getName(label);
        
        if (target!=null && name.equals(target)) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onAudioMessage update)");
            receiveAudio = receive;
        }
    }

}
