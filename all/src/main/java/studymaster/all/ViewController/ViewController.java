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
		System.out.println("[info] (ViewController initialize): Load fxml file from " + location);
		Connector.setDelegate(this);
		director = Director.getInstance();
		connector = Connector.getInstance();
		
		if(!connector.isOpen()) {
			connector.connect();
		}
	}
}