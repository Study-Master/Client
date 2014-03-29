package studymaster.media;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import studymaster.socket.VideoCl;

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
                Image image = ImgUtil.createImage(createImage());
                view.setImage(image);
                isOpening = false;
            }
        }
    }
    private static class WebcamStreamThread extends Thread {
        private ImageView view;
        private VideoCl videoCl;

        public WebcamStreamThread(ImageView view) {
            videoCl = VideoCl.getInstance();
            this.view = view;
        }

        public void setImageView(ImageView view) {
            this.view = view;
        }

        @Override public void run() {
            while (isClosing);
            while (isStreaming) {
                BufferedImage bufferedImage = createImage();
                Image image = ImgUtil.createImage(bufferedImage);
                byte[] byteImage = ImgUtil.toByte(bufferedImage);
                videoCl.send(byteImage);
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
    private static WebcamStreamThread wst;
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

    public static BufferedImage createImage() {
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
        return webcam.getImage();
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

    public void startStreaming(ImageView imageView) {
       if (isStreaming) {}
       else {
           isStreaming = true;
           wst = new WebcamStreamThread(imageView);
           wst.start();
       }
    }
}
