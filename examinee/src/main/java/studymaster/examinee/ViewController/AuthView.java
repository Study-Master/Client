package studymaster.examinee.ViewController;

import studymaster.all.ViewController.ViewController;
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
	Webcam webcam;

	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		super.initialize(location, resources);
		connector.auth();
	}
	
	@Override
	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
		Webcam webcam = Webcam.getDefault();
		webcam.open();
		while(true) {
			try {
				BufferedImage bufferedImage = webcam.getImage();
				imgView.setImage(createImage(bufferedImage));
				Thread.sleep(100);
			} catch(Exception e) {
				System.err.println("[err] ("+ getClass().getSimpleName() +" onMessage) Thread error.");
			}
		}
	}

	public javafx.scene.image.Image createImage(java.awt.Image image) {
		try {
			if (!(image instanceof java.awt.image.RenderedImage)) {
				BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				java.awt.Graphics g = bufferedImage.createGraphics();
				g.drawImage(image, 0, 0, null);
				g.dispose();

				image = bufferedImage;
			}

			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
			javax.imageio.ImageIO.write((java.awt.image.RenderedImage) image, "png", out);
			out.flush();
			java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(out.toByteArray());
			return new javafx.scene.image.Image(in);
		} catch (java.io.IOException e) {
			System.err.println("[err] ("+ getClass().getSimpleName() +" createImage) Error when createImage.");
			return null;
		}
	}
}