package studymaster.all.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public abstract class TestViewController2 extends ViewController {
    @FXML protected TextField accountField;
    @FXML protected Label nameField;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources){
        super.initialize(location, resources);
        nameField.setText(connector.getSender());
    }

    /**
	 * Binded to login button
	 */
	@FXML
	public final void testAction() {
		System.out.println("[info] (" + getClass().getSimpleName() + " testAction): Trying to test...");
		//String account = accountField.getText();
		test();
	}

	/**
	 * Binded to exit button
	 */
	@FXML
	public final void exitAction() {
		System.out.println("[info] (" + getClass().getSimpleName() + " exitAction): Exiting...");
		System.exit(0);
	}

         /**
	 * Binded to BACK button
	 */
     @FXML
        public final void backAction() {
                System.out.println("[info] (" + getClass().getSimpleName() + " backAction): back to previous page");
        }

         /**
	 * Binded to NEXT button
	 */
 /*       @FXML
        public final void nextAction() {
                System.out.println("[info] (" + getClass().getSimpleName() + " nextAction): go to next test page");

        }
 */
        /**
	 * Binded to FINISH button
	 */
  /*      @FXML
        public final void finishAction() {
                System.out.println("[info] (" + getClass().getSimpleName() + " finishAction): finish of test");
        }
 */
    /**
     * Audio and webcam test
     */
    public abstract void test();
}
