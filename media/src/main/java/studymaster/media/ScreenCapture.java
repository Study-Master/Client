package studymaster.media;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ScreenCapture {
    private static ScreenCapture instance = null;
    private static class captureThread extends Thread {
        ImageView view;

        public captureThread(ImageView view) {
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
    private captureThread cap;

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
        cap.yield();
        cap = null;
    }

    public void capture(ImageView imageView) {
        if (cap!=null) {
            cap.setImageView(imageView);
        }
        else {
            cap = new captureThread(imageView);
            cap.start();
        }
    }
}
