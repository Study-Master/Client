package studymaster.media;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ScreenCapture {
    private static ScreenCapture instance = null;
    private static class CaptureThread extends Thread {
        ImageView view;

        public CaptureThread(ImageView view) {
            this.view = view;
        }

        public void setImageView(ImageView view) {
            this.view = view;
        }

        @Override public void run() {
            while(true) {
                try {
                    Image image = createScreenShot();
                    view.setImage(image);
                    sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private CaptureThread ct;

    private ScreenCapture(){}

    public static ScreenCapture getInstance() {
        if (instance==null) {
            instance = new ScreenCapture();
        }
        else {}
        return instance;
    }

    public static Image createScreenShot() {
        Image newImg = null;
        try{
            System.setProperty("java.awt.headless", "false");
            Robot robot = new Robot();
            BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            newImg = ImgUtil.createImage(screenShot);
        }catch(Exception e){
            System.out.println("[info] (" + ScreenCapture.class.getSimpleName() + " createScreenShot)");
            e.printStackTrace();
        }
        return newImg;
    }

    public void stop() {
        ct.yield();
        ct = null;
    }

    public void capture(ImageView imageView) {
        if (ct!=null) {
            ct.setImageView(imageView);
        }
        else {
            ct = new CaptureThread(imageView);
            ct.start();
        }
    }
}
