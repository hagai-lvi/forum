package unit_tests.Content;

import junit.framework.TestCase;
import main.forum_contents.UserType;

/**
 * Created by victor on 6/9/2015 for ${Class}.
 */
public class UserTypeTest extends TestCase {

    UserType type;
    public void setUp() throws Exception {
        super.setUp();
        type = new UserType("Test_Type");

    }

    public void testGetId() throws Exception {
        type.setId(1);
        assertEquals(1, type.getId());
    }

    public void testSetId() throws Exception {
        type.setId(1);
        assertEquals(1, type.getId());
        type.setId(2);
        assertEquals(2, type.getId());
    }
}