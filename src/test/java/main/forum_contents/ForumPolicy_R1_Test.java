package main.forum_contents;
import junit.framework.TestCase;
import main.User.User;
import java.util.GregorianCalendar;

/**
 * Created by victor on 4/11/2015.
 */
public class ForumPolicy_R1_Test extends TestCase {
    ForumPolicy_R1 fp;
    public void setUp() throws Exception {
        super.setUp();
        fp = new ForumPolicy_R1(3, "[a-z]*");
    }

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
        User man = new User("man", "man", "man");
        man.setSignUpDate(new GregorianCalendar(2013, 12, 12));
        assertTrue(fp.isValidAdmin(man));
    }

    public void testIsNotValidAdmin() throws Exception {
        User man = new User("man", "man", "man");
        man.setSignUpDate(new GregorianCalendar(2015,12,12));
        assertFalse(fp.isValidAdmin(man));
    }

    public void testIsValidModerator() throws Exception {
        User man = new User("man", "man", "man");
        man.setSignUpDate(new GregorianCalendar(2013,12,12));
        assertTrue(fp.isValidModerator(man));
    }

    public void testIsNotValidModerator() throws Exception {
        User man = new User("man", "man", "man");
        man.setSignUpDate(new GregorianCalendar(2015,12,12));
        assertFalse(fp.isValidModerator(man));
    }
}