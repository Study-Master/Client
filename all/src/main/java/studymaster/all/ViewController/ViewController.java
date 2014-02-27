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
		System.out.println("[info] (ViewController initialize): Connector delegate now is " + getClass().getSimpleName());
		director = Director.getInstance();
		connector = Connector.getInstance();
	}

	@Override
	public void onOpen(short httpStatus, String httpStatusMessage) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onOpen) Socket's connection established.");
	}

	public void onClose(int code, String reason, boolean remote) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onClose) Socket's connection closed.");
		connector = Connector.renew();
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("[err] ("+ getClass().getSimpleName() +" onError) An error has been caught.");
		connector = Connector.renew();
	}

	public abstract void nextView();
}