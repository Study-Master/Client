package studymaster.invigilator;

import studymaster.socket.Connector;
import studymaster.all.Model.Client;
import studymaster.all.ViewController.Director;

public class App extends Client {
	public static void main(String[] args) throws Exception {
		Connector.setServer(Configure.SERVER);
		Connector.setEndpoint(Configure.ENDPOINT);

		launch(args);
	}

	@Override public void start(javafx.stage.Stage stage) throws Exception {
		stage.setTitle("Study Master");
		director.setStage(stage);
		director.pushStageWithFXML(App.class.getResource("/fxml/loginView.fxml"));
	}
}
