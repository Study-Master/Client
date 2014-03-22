import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javafx.scene.image.Image;

public class ScreenCapture {

    public static Image createScreenShot() {

        Image newImg;
        try{
            System.out.println("Capturing");
            System.setProperty("java.awt.headless", "false");
            Robot robot = new Robot();
            BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            newImg = ImgUtil.createImage(bufferedImage);
            System.out.println("Captured");
        }catch(Exception e){
            System.out.println(e);
        }
        return newImg
    }
}
