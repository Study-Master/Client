package studymaster.examinee;

import studymaster.socket.Connector;
import studymaster.socket.AudioCl;
import studymaster.socket.VideoCl;
import studymaster.all.Model.Client;
import studymaster.all.ViewController.Director;
import javafx.scene.image.Image;

public class App extends Client {

    public static void main(String[] args) throws Exception {
        //Configure
        Connector.setServer(Configure.SERVER);
        Connector.setEndpoint(Configure.ENDPOINT);
        AudioCl.setServer(Configure.AUDIOSERVER);

        //launch
        launch(args);
	System.out.println("aa");
    }

    @Override
    public void start(javafx.stage.Stage stage) throws Exception {
        Image icon = new Image("/image/typo.jpg");
        stage.getIcons().add(icon);
        stage.setTitle("Study Master");
        stage.show();
        director.setStage(stage);
        director.pushStageWithFXML(App.class.getResource("/fxml/loginView.fxml"));
    }
}
