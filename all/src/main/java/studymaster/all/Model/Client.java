package studymaster.all.Model;

import javafx.application.Application;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;

public abstract class Client extends Application {
	protected static Director director = Director.getInstance();
	protected static Connector connector = null;
}