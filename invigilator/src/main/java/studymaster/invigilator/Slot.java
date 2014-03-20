package studymaster.invigilator;

import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class Slot {
	public ImageView imageView;
	public Button authButton;
	public Button terminateButton;
	public Slot(ImageView imageView, Button authButton, Button terminateButton) {
		this.imageView = imageView;
		this.authButton = authButton;
		this.terminateButton = terminateButton;
	}
}