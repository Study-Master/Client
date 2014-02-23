package studymaster.all.ViewController;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for LoginViewController.
 */
public class DirectorTest extends TestCase {
    public static Director director = Director.getInstance();
    /**
     * Create the test case
     * @param  testName name of the test case
     */
    public DirectorTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DirectorTest.class);
    }

    /**
     * test getInstance method
     */
    public void testGetInstance() {
        Director testDirector = Director.getInstance();
        if(director == testDirector) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
}