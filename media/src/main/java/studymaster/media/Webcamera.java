package studymaster.media;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Webcamera {
    private static Webcamera instance;
    private static Webcam webcam;
    private static volatile boolean isStreaming;
    private static volatile boolean isOpening;
    private static volatile boolean isClosing;
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
                if(view!=null) {
                    view.setImage(image);
                }
                else {}
                isOpening = false;
                try {
                    Thread.sleep(50);
                } catch(Exception e) {}
            }
        }
    }
    private static class WebcamStreamThread extends Thread {
        private ImageView view;
        private Sendable videoCl;

        public WebcamStreamThread(ImageView view, Sendable videoCl) {
            this.videoCl = videoCl;
            this.view = view;
        }

        public void setImageView(ImageView view) {
            this.view = view;
        }

        @Override public void run() {
            while (isClosing);
            while (isStreaming) {
                BufferedImage bufferedImage = createImage();
                if(bufferedImage==null){
                    continue;
                }
                Image image = ImgUtil.createImage(bufferedImage);
                byte[] byteImage = ImgUtil.toByte(bufferedImage);
                if(videoCl.isConnected()) {
                    videoCl.sendMedia(byteImage);
                }
                else{}
                if(view!=null) {
                    view.setImage(image);
                }
                else {}
                isOpening = false;
                try {
                    Thread.sleep(50);
                } catch(Exception e) {}

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

    public static void renew() {
        if (instance == null) {
            return;
        }
        else {
            if(isStreaming) {
                Webcamera.stop();
            }
            instance = new Webcamera();
        }
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

    public void startStreaming(ImageView imageView, Sendable client) {
       if (isStreaming) {
           wst.setImageView(imageView);
       }
       else {
           isStreaming = true;
           wst = new WebcamStreamThread(imageView, client);
           wst.start();
       }
    }
}
