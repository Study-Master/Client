package studymaster.all.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public abstract class TestViewController extends ViewController {
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
		test();
	}
        
        @FXML
	public final void aftertestAction() {
		System.out.println("[info] (" + getClass().getSimpleName() + " testAction): Finishing test...");
		//String account = accountField.getText();
		aftertest();
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
        public void backAction() {
                System.out.println("[info] (" + getClass().getSimpleName() + " backAction): back to previous page");
        }
 
         /**
	 * Binded to NEXT button
	 */
       @FXML
        public void nextAction() {
                System.out.println("[info] (" + getClass().getSimpleName() + " nextAction): go to next test page");
                
        }
     
    /**
     * Audio and webcam test
     */
    public abstract void test();
    public abstract void aftertest();
}
