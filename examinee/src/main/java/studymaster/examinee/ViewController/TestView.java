package studymaster.examinee.ViewController;

import studymaster.all.ViewController.TestViewController;
import studymaster.all.ViewController.Director;
import studymaster.socket.Connector;
import studymaster.examinee.App;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TestView extends TestViewController {

    
	@Override
	public void onMessage(String message) {
		System.out.println("[info] (TestView onMessage) Receive message: " + message);

		try {
                    System.out.println("[info] (TestView onMessage) Test test");

		} catch (Exception e) {
			System.err.println("[err] (TestView onMessage)");
		}
	}
   

	@Override
	public void test() {
            String test1 = "success";
            try {
                if (test1.equals("success"))
                    director.pushStageWithFXML(getClass().getResource("/fxml/testView2.fxml"));
            } catch (Exception e) {
                System.err.println("[err] (testView) Error when enter testView stage");
            }
	}
}
