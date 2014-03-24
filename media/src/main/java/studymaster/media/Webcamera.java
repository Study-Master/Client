package studymaster.media;

import com.github.sarxos.webcam.Webcam;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Webcamera {
	private static Webcamera instance;
	private static Webcam webcam;
    private static boolean isRunning;
	public static volatile boolean isStreaming;
    public static volatile boolean isOpening;
    public static volatile boolean isClosing;
	private static class WebcamThread extends Thread {
        private ImageView view;


        public WebcamThread(ImageView view) {
            this.view = view;
        }

        public void setImageView(ImageView view) {
            this.view = view;
        }

        @Override public void run() {
            while (isClosing);
            while (isStreaming) {
                Image image = createImage();
                view.setImage(image);
                isOpening = false;
            }
        }
    }
    private static class WebcamCloseThread extends Thread {
    	@Override public void run() {
    		while (isOpening);
    		if (!isStreaming) {
    			if(webcam.isOpen() && webcam!=null) {
                    isClosing = true;
    				webcam.close();
                    isClosing = false;
    			}
    		}
    	}
    }
    private static WebcamThread wt;
    private static WebcamCloseThread wct;

	private Webcamera() {
        isRunning = false;
        isStreaming = false;
        isOpening = false;
        isClosing = false;
        wt = new WebcamThread(null);
    }

	public static Webcamera getInstance() {
		if (instance == null) {
			instance = new Webcamera();
		}
		else {}
		return instance;
	}

	public static Image createImage() {
		if(webcam == null) {
            isOpening = true;
			webcam = Webcam.getDefault();
		}
		else {}

		if(!webcam.isOpen()) {
            isOpening = true;
			webcam.open(false);
		}
		else {}
		return ImgUtil.createImage(webcam.getImage());
	}

	public static void stop() {
        if (isStreaming) {
            isStreaming = false;
            wct = new WebcamCloseThread();
            wct.start();
        }
		else {}
	}

	public void start(ImageView imageView) {
        if (isStreaming) {
            wt.setImageView(imageView);
        } else {
            isStreaming = true;
            wt = new WebcamThread(imageView);
            wt.start();
        }
	}
}
