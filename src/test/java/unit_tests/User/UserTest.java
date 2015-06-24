package unit_tests.User;

import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.User.UserSubforumPermission;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.forum_contents.SubForum;
import main.interfaces.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by gabigiladov on 4/18/15.
 */
public class UserTest {

    private UserI user1;
    private UserI user2;
    private UserI user3;
    private ForumI forum;
    private ForumPolicyI policy;

    @Before
    public void setUp() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, PermissionDeniedException, ForumNotFoundException, UserNotFoundException, CloneNotSupportedException {
        int maxModerators = 1;
        String regex = ".*";
        boolean isSecured = false;
        int passLife = 365;
        policy = new ForumPolicy(isSecured, maxModerators, regex, passLife);
        forum = new Forum("Lifestyle", policy);

        user1 = forum.register("Gabi", "123456", "mail1@gmail.com");
        user2 = forum.register("Tom", "abcde", "mail2@gmail.com");
        user3 = forum.register("Victor", "78910", "mail3@gmail.com");

        User.getUserFromDB("ADMIN", "Lifestyle").setAdmin(user2);
    }

    @After
    public void tearDown(){
        try {
            Forum.delete("Lifestyle");
        } catch (ForumNotFoundException e) {

        }
    }


    @Test
    public void testIsEmailAuthenticated() {
        assertFalse(user1.isEmailAuthenticated());
        forum.sendAuthenticationEMail(user1);
        try {
            assertFalse(forum.enterUserAuthenticationString(user1, "some string"));
        } catch (InvalidUserCredentialsException e) {
            e.printStackTrace();
        }
        try {
            assertTrue(forum.enterUserAuthenticationString(user1, user1.getUserAuthString()));
        } catch (InvalidUserCredentialsException e) {
            fail("correct authentication string not accepted.");
        }
        assertTrue(user1.isEmailAuthenticated());
    }

    @Test
    public void testSetAuthenticated()  {
        assertFalse(user1.isEmailAuthenticated());
        user1.setAuthenticated();
        assertTrue(user1.isEmailAuthenticated());
    }

    @Test
    public void testGetUsername()  {
        assertEquals(user1.getUsername(), "Gabi");
    }


    @Test
    public void testGetSignUpDate()  {
        GregorianCalendar date = user1.getSignUpDate();
        Calendar today =  GregorianCalendar.getInstance();
        assertEquals(date.get(Calendar.YEAR), today.get(Calendar.YEAR));
        assertEquals(date.get(Calendar.MONTH), today.get(Calendar.MONTH));
        assertEquals(date.get(Calendar.DAY_OF_MONTH), today.get(Calendar.DAY_OF_MONTH));
    }


    @Test
    public void testCreateSubForumForRegularUser() throws SubForumAlreadyExistException, ForumNotFoundException, SubForumDoesNotExistException {
        try {
            user1.createSubForum("Football"); // PermissionDeniedException expected
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateSubForumForAdmin() throws PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, ForumNotFoundException {
        Map<String, SubForumI> subForums = forum.getSubForums();
        SubForumI subforum = new SubForum("Football", policy.getSubforumPolicy());
        assertFalse(subForums.containsKey(subforum));
        user2.createSubForum("Football");
        subForums = forum.getSubForums();
        assertTrue(subForums.containsKey(subforum));
    }


    @Test
    public void testDeleteSubForumForRegularUser() throws SubForumDoesNotExistException, ForumNotFoundException {
        SubForumI subforum = new SubForum("Football", policy.getSubforumPolicy());
        try {
            user1.deleteSubForum(subforum); // PermissionDeniedException expected
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteSubForumForAdmin() throws PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, ForumNotFoundException {
        Map<String, SubForumI> subForums;
        SubForumI subforum = new SubForum("Baseball", policy.getSubforumPolicy());
        user2.createSubForum("Baseball");
        forum = Forum.load(forum.getName());
        subForums = forum.getSubForums();
        assertTrue(subForums.containsKey(subforum));
        user2.deleteSubForum(subforum);
        forum = Forum.load(forum.getName());
        subForums = forum.getSubForums();
        assertFalse(subForums.containsKey(subforum));
    }

    @Test
    public void testCreateThread() throws PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, ForumNotFoundException, DoesNotComplyWithPolicyException {
        user2.createSubForum("Football");
        user2.createThread("Mega Flow", "Flow", "Football");
    }

    @Test
    public void testReplyToMessage() throws SubForumAlreadyExistException, PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException, SubForumDoesNotExistException, ForumNotFoundException {
        User.getUserFromDB("Tom", "Lifestyle").createSubForum("Football");
        ThreadI thread = User.getUserFromDB("Tom", "Lifestyle").createThread("Mega Flow", "Flow", "Football");
        User.getUserFromDB("Tom", "Lifestyle").replyToMessage("Football", thread.getRootMessage(), "WTF", "Help");
        User.getUserFromDB("Tom", "Lifestyle").replyToMessage("Football", thread.getRootMessage().getReplies().iterator().next(), "WTF", "Yeah!");
        assertEquals(thread.getRootMessage().printSubTree(), "Flow--> Help--> Yeah!");
    }

    @Test
    public void testDeleteMessageS() throws MessageNotFoundException, ForumNotFoundException, SubForumDoesNotExistException, SubForumAlreadyExistException, PermissionDeniedException, DoesNotComplyWithPolicyException {
        user2.createSubForum("Football");
        ThreadI thread = user2.createThread("Football", "Mega Flow", "Flow");
        assertEquals(thread.getRootMessage().printSubTree(), "Flow");
        user2.replyToMessage("Football", thread.getRootMessage(), "WTF", "Help");
        assertEquals(thread.getRootMessage().printSubTree(), "Flow--> Help");
        user1.replyToMessage("Football", thread.getRootMessage(), "WTF", "Yeah!");
        assertEquals(thread.getRootMessage().printSubTree(), "Flow--> Help--> Yeah!");
        user1.deleteMessage(thread.getRootMessage(), "Football");
        user1.replyToMessage("Football", thread.getRootMessage(), "aaa", "bbb");
    }

    @Test
    public void testDeleteMessageWithoutPermission() throws SubForumAlreadyExistException, ForumNotFoundException, SubForumDoesNotExistException, DoesNotComplyWithPolicyException, MessageNotFoundException {
        ThreadI thread = null;
        try {
            user2.createSubForum("Football");
        } catch (PermissionDeniedException e) {
            fail();
        }
        SubForumPermissionI permission = new UserSubforumPermission(Permissions.PERMISSIONS_USER, forum.getName(), "Football");
        try {
           thread = user2.createThread("Football", "Mega Flow", "Flow");
        } catch (PermissionDeniedException e) {
            fail();
        }
        try {
            user2.deleteMessage(thread.getRootMessage(), "Football"); // PermissionDeniedException expected
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetAdminWithoutPermission() throws ForumNotFoundException, UserNotFoundException, CloneNotSupportedException {
        ForumPermissionI permission = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,forum.getName());
        try {
            user3.setAdmin(new User("Shreder", "000", "XXX@gmail.com", permission));
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetAdmin() throws PermissionDeniedException, ForumNotFoundException, UserNotFoundException, CloneNotSupportedException {
        ForumPermissionI permission = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,forum.getName());
        user2.setAdmin(new User("Shreder", "000", "XXX@gmail.com",permission));
    }

    @Test
    public void testViewStatistics()  {
        fail();
    }


    @Test
    public void testCloneAs() throws ForumNotFoundException, CloneNotSupportedException, SubForumDoesNotExistException, PermissionDeniedException {
        UserI clone = user1.cloneAs(Permissions.PERMISSIONS_ADMIN);
        assertTrue(clone.isAdmin());
        System.out.println(user1.isEmailAuthenticated()+ " - " + clone.isEmailAuthenticated());
        assertEquals(user1.isEmailAuthenticated(), clone.isEmailAuthenticated());
        assertEquals(user1.getUsername(), clone.getUsername());
        assertEquals(user1.getEmail(), clone.getEmail());
        assertEquals(user1.getForumStatus(), clone.getForumStatus());
        assertEquals(user1.getPassword(), clone.getPassword());
        assertEquals(user1.getUserAuthString(), clone.getUserAuthString());
        assertEquals(user1.getSignUpDate(), clone.getSignUpDate());
        assertEquals(user1.getPasswordCreationDate(), clone.getPasswordCreationDate());
        assertEquals(user1.viewStatistics(), clone.viewStatistics());
    }


    @Test
    public void testSetModerator() {
        fail();
    }

    @Test
    public void testBanModerator()  {
        fail();
    }

    @Test
    public void testAddSubForumPermission()  {
        fail();
    }

    @Test
    public void testReportModerator()  {
        fail();
        // SubForumI subforum = new SubForum("Baseball", policy.getSubforumPolicy());
        // user1.setModerator(subforum, user2);
        // user1.reportModerator(subforum, "Gabi", "The Worst Moderator Ever");
    }
}