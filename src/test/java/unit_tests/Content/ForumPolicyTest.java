package unit_tests.Content;
import junit.framework.TestCase;
import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.exceptions.ForumNotFoundException;
import main.exceptions.UserNotFoundException;
import main.forum_contents.Forum;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumPolicy;
import main.interfaces.ForumI;
import main.interfaces.MessageI;
import org.junit.After;
import org.junit.Before;

import java.util.GregorianCalendar;

/**
 * Created by victor on 4/11/2015.
 */
public class ForumPolicyTest extends TestCase {
    private ForumPolicy fp;
    private ForumI forum;
    User man = createUser();

    @Before
    public void setUp() {
        fp = new ForumPolicy(false, 3, "[a-z]*", 365);
        forum = new Forum("forum", fp);
    }

    @After
    public void tearDown() {
        try {
            User.delete("man", "forum");
        } catch (UserNotFoundException e) {
        }

        try {
            Forum.delete("forum");
        } catch (ForumNotFoundException e) {
        }
    }

    public void testIsValidPassword() {
        String pass = "aaa";
        assertTrue(fp.isValidPassword(pass));
    }

    public void testIsNotValidPassword() {
        String pass = "a9a";
        assertFalse(fp.isValidPassword(pass));
    }

    public void testSetPasswordRegex() {
        fp.setPasswordRegex("a*");
        assertEquals(fp.getPasswordRegex(), "a*");
    }

    public void testSetMaxModerators() {
        fp.setMaxModerators(7);
        assertEquals(fp.getMaxModerators(), 7);
    }

    public void testIsValidAdmin() {
        User man = createUser();
        assertTrue(fp.isValidAdmin(man));
    }

    public void testIsNotValidAdmin()   {
        User man = createUser();
        man.setSignUpDate(new GregorianCalendar());
        assertFalse(fp.isValidAdmin(man));
    }

    public void testIsValidModerator()   {
        User man = createUser();
        assertTrue(fp.isValidModerator(man));
    }

    public void testIsNotValidModerator()  {
        man.setSignUpDate(new GregorianCalendar());
        assertFalse(fp.isValidModerator(man));
    }

    public void testIsValidMessage()   {

        MessageI msg = new ForumMessage(null, "Title", "Some valid text.");
        assertTrue(fp.isValidMessage(msg));
    }

    public void testIsNotValidMessage()   {
        MessageI msg = new ForumMessage(null, "Title", "");
        assertFalse(fp.isValidMessage(msg));
        msg = new ForumMessage(null, "", "Message");
        assertFalse(fp.isValidMessage(msg));
        msg = new ForumMessage(null, "", "");
        assertFalse(fp.isValidMessage(msg));
    }

    public void testIsValidTitle()   {
        MessageI msg = new ForumMessage(null, "Title", "Some valid text.");
        assertTrue(fp.isValidMessage(msg));
    }

    public void testIsNotValidTitle()   {
        MessageI msg = new ForumMessage(null, "", "");
        assertFalse(fp.isValidMessage(msg));
        msg = new ForumMessage(null, "", "aaa");
        assertFalse(fp.isValidMessage(msg));
        msg = new ForumMessage(null, "aaa", "");
        assertFalse(fp.isValidMessage(msg));
    }



    private User createUser() {
        User user = new User("man", "man", "man", UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_ADMIN, forum.getName()));
        user.setSignUpDate(new GregorianCalendar(1948,1,1));
        return user;
    }

}