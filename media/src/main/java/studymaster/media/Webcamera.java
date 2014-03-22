package studymaster.media;

import com.github.sarxos.webcam.Webcam;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Webcamera {
	private static Webcamera instance;
	private static Webcam webcam;
	private static boolean status;
	private static class WebcamThread extends Thread {
        ImageView view;

        public WebcamThread(ImageView view) {
            this.view = view;
        }

        public void setImageView(ImageView view) {
            this.view = view;
        }

        @Override public void run() {
            while (status) {
            	wct = null;
            	try {
            		Image image = createImage();
            		view.setImage(image);
            		sleep(200);
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
            }
        }
    }
    private static class WebcamCloseThread extends Thread {
    	@Override public void run() {
    		wt = null;
    		while (!status) {
    			if(webcam.isOpen()) {
    				webcam.close();
    			}
    		}
    	}
    }
    private static WebcamThread wt;
    private static WebcamCloseThread wct;

	private Webcamera() {}

	public static Webcamera getInstance() {
		if (instance==null) {
			instance = new Webcamera();
		}
		else {}
		return instance;
	}

	public static Image createImage() {
		if(webcam==null) {
			webcam = Webcam.getDefault();
		}
		else {}

		if(!webcam.isOpen()) {
			webcam.open();
		}
		else {}
		return ImgUtil.createImage(webcam.getImage());
	}

	public static void stop() {
		status = false;
		if (wct==null) {
			wct = new WebcamCloseThread();
			wct.start();
		}
	}

	public void video(ImageView imageView) {
    	status = true;
        wt = new WebcamThread(imageView);
        wt.start();
	}
}
