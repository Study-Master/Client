package studymaster.all.Model;

import javafx.application.Application;
import studymaster.all.ViewController.Director;

/**
 * ViewController class is the root class of all view controller class
 */
public abstract class Client extends Application {
	protected static Director director = Director.getInstance();
}