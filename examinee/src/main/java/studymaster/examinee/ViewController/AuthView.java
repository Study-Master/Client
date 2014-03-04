package studymaster.examinee.ViewController;

import studymaster.all.ViewController.ViewController;
import studymaster.all.ImgUtil;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.image.BufferedImage;
import java.io.IOException;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamUtils;
import javafx.fxml.FXML;
import javax.swing.JFrame;
import studymaster.examinee.App;

public class AuthView extends ViewController {

	@FXML protected ImageView imgView;
	WebCamera webcam;

	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		connector.auth();
	}
	
	@Override
	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
		WebCamera webcam = new WebCamera();
		webcam.setView(imgView);
		webcam.run();
	}
}

class WebCamera extends Thread {

	private Webcam webcam;
	private ImageView view;

	public WebCamera() {
		this.webcam = Webcam.getDefault();
	}

	public void close() {
		this.webcam.close();
	}

	public void setView(ImageView view) {
		this.view = view;
	}

	private void view() {
		BufferedImage bufferedImage = webcam.getImage();
		view.setImage(ImgUtil.createImage(bufferedImage));
	}

	@Override
	public void run() {
		if(!webcam.isOpen())
			webcam.open();
		while(true) {
			try {
				this.view();
				Thread.sleep(100);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}