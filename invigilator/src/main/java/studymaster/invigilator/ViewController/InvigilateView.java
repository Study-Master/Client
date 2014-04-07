package studymaster.invigilator.ViewController;

import studymaster.all.ViewController.ViewController;
import studymaster.all.ViewController.AlertAction;
import studymaster.socket.AudioCl;
import studymaster.invigilator.Configure;
import studymaster.socket.VideoCl;
import studymaster.invigilator.Slots;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import studymaster.socket.VideoEventHandler;
import studymaster.socket.AudioEventHandler;
import org.json.JSONObject;
import java.nio.ByteBuffer;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class InvigilateView extends ViewController implements VideoEventHandler {

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
    private VideoCl screenCl;

    private AudioCl audioCl;

    //private Set<String> clients;
    private Map<String, Slot> clients;

    @Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        connector.retain(this);
        videoCl = VideoCl.getInstance(Configure.VIDEOSERVER, this);
        videoCl.connect();
        screenCl = VideoCl.getInstance(Configure.SCREENSERVER, this);
        screenCl.connect();
        audioCl = AudioCl.getInstance();
        audioCl.connect();
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
                            JSONObject content = new JSONObject();
                            content.put("name", slots.get(id).name);
                            connector.setAndSendMessageContainer("auth_successful", content);
                            button.setText("Chat");
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    System.out.println("[info] (" + InvigilateView.class.getSimpleName() + " chatAction" + id +")");
                                    slots.get(id).button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5)");
                                    director.toggleStage(alert);
                                }
                            });
                            stage.close();
                        }
                    };
                    director.invokeTwoButtonAlert("Authentication", "Confirm to authenticate?", action);
                }
            });
            terminateButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    System.out.println("[info] (" + InvigilateView.class.getSimpleName() + " terminateAction" + id + ")");
                    AlertAction action = new AlertAction() {
                        @Override public void ok(Stage stage, TextArea textarea) {
                            System.out.println("[info] (" + InvigilateView.class.getSimpleName() + " reason" + id + ") " + textarea.getText());
                            JSONObject content = new JSONObject();
                            content.put("name", slots.get(id).name);
                            content.put("exam_pk", slots.get(id).exam_pk);
                            content.put("reason", textarea.getText());
                            connector.setAndSendMessageContainer("terminate", content);
                            examineeOut(slots.get(id).name);
                            stage.close();
                        }
                    };
                    director.invokeInputAlert("Reason", action);
                }
            });
        }
        clients = new HashMap();
    }

    @Override public void onMessage(String message){
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
        Slots data = Slots.getInstance();
        try {
            int position = -1;
            JSONObject msg = new JSONObject(message);
            String event = msg.getString("event");
            String endpoint = msg.getString("endpoint");
            final JSONObject content = msg.getJSONObject("content");

            if (event.equals("examinee_come_in")) {
                String type = content.getString("type");
                String name = content.getString("name");
                int exam_pk = content.getInt("exam_pk");
                Slot emptySlot = null;
                for(int i=0; i<3; i++) {
                    if (slots.get(i).name.equals(name)) {
                        emptySlot = slots.get(i);
                        position = i;
                        break;
                    }
                }
                if(emptySlot == null ) {
                    for(int i=0;i<3; i++) {
                        if(slots.get(i).name.equals("")) {
                            emptySlot = slots.get(i);
                            position = i;
                            break;
                        }
                    }
                }

                if (emptySlot==null) {
                    System.out.println("[info] no slot.");
                    return;
                }
                clients.put(name, emptySlot);
                videoCl.setImageView(name, clients.get(name).imgView);
                screenCl.setImageView(name, clients.get(name).screenView);
                clients.get(name).name = name;
                clients.get(name).exam_pk = exam_pk;
                clients.get(name).button.setDisable(false);
                clients.get(name).terminate.setDisable(false);
                if(position!=-1) {
                    System.out.println("[info] (" + getClass().getSimpleName() + " onMessage) Set slot on " + position + " as name=" + name + ", exam_pk=" + exam_pk);
                    data.setName(position, name);
                    data.setExam(position, exam_pk);
                }
            }

            else if (event.equals("exam_chat")) {
                String name = content.getString("account");
                Slot responce = clients.get(name);
                if(!responce.chatWindow.isShowing()) {
                    responce.button.setStyle("-fx-background-color: rgb(255, 0, 0, 1)");
                }
            }

            else if (event.equals("submission_successful")) {
                String name = content.getString("name");
                examineeOut(name);
            }
        } catch(Exception e) {
            System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Error when decoding JSON response string.");
        }
    }

    @Override public void onVideoClientClose(int code, String reason, boolean remote){}

    private void examineeOut(String name) {
       Slot out = clients.get(name);
        if(out!=null) {
            System.out.println("[info] ("+getClass().getSimpleName() + " examineeOut) examinee " + name +" logout");
            clients.remove(name);
            videoCl.removeImageView(name);
            screenCl.removeImageView(name);
            out.name = "disabled";
            out.exam_pk = 0;
            Image defaultPhoto = new Image(getClass().getResourceAsStream("/image/user.png"));
            out.screenView.setImage(defaultPhoto);
            out.imgView.setImage(defaultPhoto);
            out.screenView.setImage(defaultPhoto);
            out.button.setDisable(true);
            out.terminate.setDisable(true);
        }
    }

    public void backView() {
        AlertAction action = new AlertAction() {
            @Override public void ok(Stage stage) {
                for (int i=0; i<3; i++) {
                slots.get(i).chatWindow.close();
                }
                director.pushStageWithFXML(getClass().getResource("/fxml/taskView.fxml"));
                stage.close();
            }
        };
        director.invokeTwoButtonAlert("Go back?", "Confirm to quit invigilation?", action);
    }
}

class Slot {
    protected String name;
    protected int exam_pk;
    protected ImageView imgView;
    protected ImageView screenView;
    protected Button button;
    protected Button terminate;
    protected Stage chatWindow;

    protected Slot(ImageView imgView, ImageView screenView, Button button, Button terminate, Stage chatWindow) {
        this.name = "";
        this.exam_pk = 0;
        this.imgView = imgView;
        this.screenView = screenView;
        this.button = button;
        this.terminate = terminate;
        this.chatWindow = chatWindow;
    }
}
