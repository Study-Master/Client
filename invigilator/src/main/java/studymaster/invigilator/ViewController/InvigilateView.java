package studymaster.invigilator.ViewController;

import studymaster.all.ViewController.ViewController;
import studymaster.all.ViewController.AlertAction;
import studymaster.socket.VideoCl;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import studymaster.socket.VideoEventHandler;
import studymaster.socket.AudioEventHandler;
import org.json.JSONObject; 
import java.nio.ByteBuffer;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class InvigilateView extends ViewController implements VideoEventHandler, AudioEventHandler {

    @FXML private ImageView imgView0;
    @FXML private ImageView screenView0;
    @FXML private Button button0;
    @FXML private Button terminateButton0;
    private Stage chatWindow0;

    @FXML private ImageView imgView1;
    @FXML private ImageView screenView1;
    @FXML private Button button1;
    @FXML private Button terminateButton1;
    private Stage chatWindow1;

    @FXML private ImageView imgView2;
    @FXML private ImageView screenView2;
    @FXML private Button button2;
    @FXML private Button terminateButton2;
    private Stage chatWindow2;
    ArrayList<Slot> slots;

    private VideoCl videoCl;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        connector.retain(this);
        videoCl = VideoCl.getInstance(this, "receiver");
        videoCl.connect();
        slots = new ArrayList();
        chatWindow0 = director.initStageWithFXML(getClass().getResource("/fxml/chatView.fxml"));
        chatWindow1 = director.initStageWithFXML(getClass().getResource("/fxml/chatView.fxml"));
        chatWindow2 = director.initStageWithFXML(getClass().getResource("/fxml/chatView.fxml"));
        chatWindow0.setResizable(false);
        chatWindow1.setResizable(false);
        chatWindow2.setResizable(false);
        
        slots.add(new Slot(imgView0, screenView0, button0, terminateButton0, chatWindow0));
        slots.add(new Slot(imgView1, screenView1, button1, terminateButton1, chatWindow1));
        slots.add(new Slot(imgView2, screenView2, button2, terminateButton2, chatWindow2));

        for(int i=0; i<slots.size(); i++) {
            final int id = i;
            final Button button = slots.get(i).button;
            final Button terminateButton = slots.get(i).terminate;
            final Stage alert = slots.get(i).chatWindow;
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    AlertAction action = new AlertAction() {
                        @Override public void ok(Stage stage) {
                            //TODO: Send message that auth successfully
                            
                            button.setText("Chat");
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    System.out.println("[info] (" + InvigilateView.class.getSimpleName() + " chatAction" + id +")");
                                    director.toggleStage(alert);
                                }
                            });
                            stage.close();
                        }
                    };
                    director.invokeTwoButtonAlert("Auth", "Confirm to auth", action);
                }
            });
            terminateButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    System.out.println("[info] (" + InvigilateView.class.getSimpleName() + " terminateAction" + id + ")");
                    AlertAction action = new AlertAction() {
                        @Override public void ok(Stage stage, TextArea textarea) {
                            System.out.println("[info] (" + InvigilateView.class.getSimpleName() + " reason" + id + ") " + textarea.getText());
                            //TODO: Send message that auth successfully
                            stage.close();
                        }
                    };
                    director.invokeInputAlert("Reason", action);
                }
            });
        }
        connector.setDelegate(this);
        videoCl.testView = imgView0;
    }

    @Override public void onMessage(String message){
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
        try {
            JSONObject msg = new JSONObject(message);
            String event = msg.getString("event");
            String endpoint = msg.getString("endpoint");
            final JSONObject content = msg.getJSONObject("content");

            if (event.equals("examinee_come_in")) {
                String name = content.getString("name");
                for(int i=0; i<3; i++) {
                    if( videoCl.containsImageView(slots.get(i).imgView) || videoCl.containsImageView(slots.get(i).screenView)) {}
                    else {
                        slots.get(i).name = name;
                        videoCl.setImageView(name, slots.get(i).imgView, slots.get(i).screenView);
                    }
                }
            }
        } catch(Exception e) {
            System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
        }
    }

    @Override public void onVideoClientOpen() {}

    @Override public void onAudioClientOpen() {}

}

class Slot {
    protected String name;
    protected ImageView imgView;
    protected ImageView screenView;
    protected Button button;
    protected Button terminate;
    protected Stage chatWindow;

    protected Slot(ImageView imgView, ImageView screenView, Button button, Button terminate, Stage chatWindow) {
        this.imgView = imgView;
        this.screenView = screenView;
        this.button = button;
        this.terminate = terminate;
        this.chatWindow = chatWindow;
    }
}
