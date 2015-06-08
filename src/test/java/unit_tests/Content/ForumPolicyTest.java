package unit_tests.Content;
import junit.framework.TestCase;
import main.User.User;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumPolicy;
import main.interfaces.MessageI;

import java.util.GregorianCalendar;

/**
 * Created by victor on 4/11/2015.
 */
public class ForumPolicyTest extends TestCase {
    private ForumPolicy fp;
    @Override
    public void setUp() throws Exception {
        super.setUp();
        fp = new ForumPolicy(false, 3, "[a-z]*", 365);
    }

    @Override
    public void tearDown() throws Exception {
    }

    public void testIsValidPassword() throws Exception {
        String pass = "aaa";
        assertTrue(fp.isValidPassword(pass));
    }

    public void testIsNotValidPassword() throws Exception {
        String pass = "a9a";
        assertFalse(fp.isValidPassword(pass));
    }

    public void testSetPasswordRegex() throws Exception {
        fp.setPasswordRegex("a*");
        assertEquals(fp.getPasswordRegex(), "a*");
    }

    public void testSetMaxModerators() throws Exception {
        fp.setMaxModerators(7);
        assertEquals(fp.getMaxModerators(), 7);
    }

    public void testIsValidAdmin() throws Exception {
        User man = createUser();
        assertTrue(fp.isValidAdmin(man));
    }

    public void testIsNotValidAdmin() throws Exception {
        User man = createUser();
        man.setSignUpDate(new GregorianCalendar());
        assertFalse(fp.isValidAdmin(man));
    }

    public void testIsValidModerator() throws Exception {
        User man = createUser();
        assertTrue(fp.isValidModerator(man));
    }

    public void testIsNotValidModerator() throws Exception {
        User man = createUser();
        man.setSignUpDate(new GregorianCalendar());
        assertFalse(fp.isValidModerator(man));
    }

    public void testIsValidMessage() throws Exception {
        MessageI msg = new ForumMessage(null, null, "Title", "Some valid text.");
        assertTrue(fp.isValidMessage(msg));
    }

    public void testIsNotValidMessage() throws Exception {
        MessageI msg = new ForumMessage(null, null, "Title", "");
        assertFalse(fp.isValidMessage(msg));
        msg = new ForumMessage(null, null, "", "Message");
        assertFalse(fp.isValidMessage(msg));
        msg = new ForumMessage(null, null, "", "");
        assertFalse(fp.isValidMessage(msg));
    }

    public void testIsValidTitle() throws Exception {
        MessageI msg = new ForumMessage(null, null, "Title", "Some valid text.");
        assertTrue(fp.isValidMessage(msg));
    }

    public void testIsNotValidTitle() throws Exception {
        MessageI msg = new ForumMessage(null, null, "", "");
        assertFalse(fp.isValidMessage(msg));
        msg = new ForumMessage(null, null, "", "aaa");
        assertFalse(fp.isValidMessage(msg));
        msg = new ForumMessage(null, null, "aaa", "");
        assertFalse(fp.isValidMessage(msg));
    }



    private User createUser() {
        User user = new User("man", "man", "man", null);
        user.setSignUpDate(new GregorianCalendar(1948,1,1));
        return user;
    }

}