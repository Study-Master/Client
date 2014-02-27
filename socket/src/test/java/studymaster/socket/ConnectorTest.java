package studymaster.socket;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConnectorTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ConnectorTest( String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( ConnectorTest.class );
    }

    public void testGetInstance() {
        Connector connector = Connector.getInstance();
        Connector connector2 = Connector.getInstance();
        if(connector==connector2)
            assertTrue( true );
        else
            assertTrue( false );
    }
}
