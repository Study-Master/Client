package studymaster.invigilator;

import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class Slot {
	public ImageView imageView;
	public ImageView screenView;
	public Button authButton;
	public Button terminateButton;

	public Slot(ImageView imageView, Button authButton, Button terminateButton) {
		this.imageView = imageView;
		this.authButton = authButton;
		this.terminateButton = terminateButton;
	}

	public Slot(ImageView imageView, ImageView screenView, Button authButton, Button terminateButton) {
		this.imageView = imageView;
		this.screenView = screenView;
		this.authButton = authButton;
		this.terminateButton = terminateButton;
	}
}