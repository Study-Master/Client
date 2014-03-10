package studymaster.examinee.ViewController;

import studymaster.all.ViewController.TestViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TestView extends TestViewController {
        boolean test1 = false;
        boolean test2 = false;
    
	@Override
	public void onMessage(String message) {
		System.out.println("[info] ("+ getClass().getSimpleName() +" onMessage) Receive message: " + message);
	}
        
        @FXML
        @Override
        public void nextAction() {
                super.nextAction();
                if (!test2 && test1) {
                    director.pushStageWithFXML(getClass().getResource("/fxml/testView2.fxml"));
                }
                else if (test1 && test2) {
                    director.pushStageWithFXML(getClass().getResource("/fxml/courseView.fxml"));
                }
        }
     

	@Override
        //public boolean test() {};
	public void test() {
            if (!test1 && !test2) test1 = true;
            if (test1 && !test2) test2 = true;
	}
}
