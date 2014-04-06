package studymaster.examinee.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import studymaster.all.ViewController.TestViewController;
import studymaster.media.SoundUtil;

public class TestView extends TestViewController {
    @FXML Button AudioTestButton;

    @Override public void onMessage(String message) {
        System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
    }

    @FXML @Override public void nextAction() {
        super.nextAction();
        director.pushStageWithFXML(getClass().getResource("/fxml/testView2.fxml"));
    }

    @FXML @Override public void backAction() {
        super.backAction();
        director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
    }

     @FXML public final void testAction() {
        System.out.println("[info] (" + getClass().getSimpleName() + " testAction): Stat testing");
        SoundUtil.startRecord();
        AudioTestButton.setText("Release to Play");
    }

    @FXML public void aftertestAction() {
        System.out.println("[info] ("+ getClass().getSimpleName() +" aftertestAction) Finish testing");
        SoundUtil.stopRecord();
        SoundUtil.playAudio();
        AudioTestButton.setText("Hold to Talk");
	}
}
