package studymaster.media;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ScreenCapture {
    private static ScreenCapture instance = null;
    private static volatile boolean isStreaming; 
    private static class CaptureThread extends Thread {
        private ImageView view;
        private Sendable videoCl;

        public CaptureThread(ImageView view, Sendable videoCl) {
            this.view = view;
            this.videoCl = videoCl;
        }

        public void setImageView(ImageView view) {
            this.view = view;
        }

        @Override public void run() {
            while(isStreaming) {
                try {
                    BufferedImage bufferedImage = createScreenShot();
                    Image image = ImgUtil.createImage(bufferedImage);
                    byte[] byteImage = ImgUtil.toByte(bufferedImage);
                    if(videoCl.isConnected()) {
                        videoCl.sendMedia(byteImage);
                    }
                    if(view!=null) {
                        view.setImage(image);
                    }
                    sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private CaptureThread ct;

    private ScreenCapture(){
        isStreaming = false;
    }

    public static ScreenCapture getInstance() {
        if (instance==null) {
            instance = new ScreenCapture();
        }
        else {}
        return instance;
    }

    public static BufferedImage createScreenShot() {
        BufferedImage screenShot = null;
        try{
            System.setProperty("java.awt.headless", "false");
            Robot robot = new Robot();
            screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        }catch(Exception e){
            System.out.println("[info] (" + ScreenCapture.class.getSimpleName() + " createScreenShot)");
            e.printStackTrace();
        }
        return screenShot;
    }

    public void stop() {
        isStreaming = false;
    }

    public void captureStreaming(ImageView imageView, Sendable client) {
        if(isStreaming) {
            ct.setImageView(imageView);
        }
        else {
            isStreaming = true;
            ct = new CaptureThread(imageView, client);
            ct.start();
        }
    }
}
