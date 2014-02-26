package studymaster.all.ViewController;

import javafx.fxml.Initializable;
import studymaster.socket.Connector;
import studymaster.socket.Callback;

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
		System.out.println("[info] (ViewController initialize): Load fxml file from " + location);
		Connector.setDelegate(this);
		System.out.println("[info] (ViewController initialize): Connector delegate now is " + this.getClass());
		director = Director.getInstance();
		connector = Connector.getInstance();
		
		if(!connector.isOpen()) {
			connector.connect();
		}
	}

	public abstract void nextView();
}