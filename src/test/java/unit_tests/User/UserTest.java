package unit_tests.User;

import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.User.UserSubforumPermission;
import main.exceptions.*;
import main.forum_contents.*;
import main.interfaces.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

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
    public void setUp() throws Exception {
        int maxModerators = 1;
        String regex = ".*";
        boolean isSecured = false;
        int passLife = 365;
        policy = new ForumPolicy(isSecured, maxModerators, regex, passLife);
        forum = new Forum("Lifestyle", policy);
        ForumPermissionI permission = new UserForumPermission(Permissions.PERMISSIONS_USER,forum);
        ForumPermissionI permission2 = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,forum);
        ForumPermissionI permission3 = new UserForumPermission(Permissions.PERMISSIONS_SUPERADMIN,forum);
        user1 = forum.register("Gabi", "123456", "mail1@gmail.com");
        user2 = forum.register("Tom", "abcde", "mail2@gmail.com");
        user3 = forum.register("Victor", "78910", "mail3@gmail.com");
    }

    @After
    public void tearDown(){
        try {
            Forum.delete("Lifestyle");
        } catch (ForumNotFoundException e) {
            e.printStackTrace();
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
    public void testSetAuthenticated() throws Exception {
        assertFalse(user1.isEmailAuthenticated());
        user1.setAuthenticated();
        assertTrue(user1.isEmailAuthenticated());
    }

    @Test
    public void testGetUsername() throws Exception {
        assertEquals(user1.getUsername(), "Gabi");
    }


    @Test
    public void testGetSignUpDate() throws Exception {
        GregorianCalendar date = user1.getSignUpDate();
        Calendar today =  GregorianCalendar.getInstance();
        assertEquals(date.get(Calendar.YEAR), today.get(Calendar.YEAR));
        assertEquals(date.get(Calendar.MONTH), today.get(Calendar.MONTH));
        assertEquals(date.get(Calendar.DAY_OF_MONTH), today.get(Calendar.DAY_OF_MONTH));
    }


    @Test(expected=PermissionDeniedException.class)
    public void testCreateSubForumForRegularUser() throws Exception {
        user1.createSubForum("Football"); // PermissionDeniedException expected
    }

    @Test
    public void testCreateSubForumForAdmin() throws Exception {
        Collection<SubForumI> subForums = forum.getSubForums();
        SubForumI subforum = new SubForum("Football", policy.getSubforumPolicy());
        assertFalse(contains(subForums, subforum));
        user2.createSubForum("Football");
        subForums = forum.getSubForums();
        assertTrue(contains(subForums, subforum));
    }

    private boolean contains(Collection<SubForumI> subForums, SubForumI subforum) {
        for (SubForumI subf : subForums) {
            if (subf.getTitle().equals(subforum.getTitle()))
                return true;
        }
        return false;
    }

    @Test(expected=PermissionDeniedException.class)
    public void testDeleteSubForumForRegularUser() throws Exception {
        SubForumI subforum = new SubForum("Football", policy.getSubforumPolicy());
        user1.deleteSubForum(subforum); // PermissionDeniedException expected
    }

    @Test
    public void testDeleteSubForumForAdmin() throws Exception {
        Collection<SubForumI> subForums;
        SubForumI subforum = new SubForum("Baseball", policy.getSubforumPolicy());
        user2.createSubForum("Baseball");
        subForums = forum.getSubForums();
        assertTrue(contains(subForums, subforum));
        user2.deleteSubForum(subforum);
        subForums = forum.getSubForums();
        assertFalse(contains(subForums, subforum));
    }

    @Test
    public void testCreateThread() throws Exception {
        user2.createSubForum("Football");
        MessageI message = new ForumMessage(user1, "Mega Flow", "Flow");
        user2.createThread(message, "Football");
    }

    @Test
    public void testReplyToMessage() throws SubForumAlreadyExistException, PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException, SubForumDoesNotExistException {
        user2.createSubForum("Football");
        MessageI message1 = new ForumMessage(user1, "Mega Flow", "Flow");
        MessageI message2 = new ForumMessage(user2, "Mega Flow", "Help");
        System.out.println(user1.getSubForumsPermissions().size());
        System.out.println(user2.getSubForumsPermissions().size());
        System.out.println(user3.getSubForumsPermissions().size());
        user1.createThread(message1, "Football");
        user2.replyToMessage("Football", message1, "WTF", "Help");
        user1.replyToMessage("Football", message1.getReplies().iterator().next(), "WTF", "Yeah!");
        assertEquals(message1.printSubTree(), "Flow--> Help--> Yeah!");
    }

    @Test
    public void testDeleteMessageS() throws MessageNotFoundException {
        try {
            user2.createSubForum("Football");
        } catch (PermissionDeniedException | SubForumAlreadyExistException e) {
            e.printStackTrace();
        }
        SubForumPermissionI permission = new UserSubforumPermission(Permissions.PERMISSIONS_USER, forum, forum.getSubForums().iterator().next());
        MessageI message = new ForumMessage(user1, "Mega Flow", "Flow");
        try {
            user2.createThread(message, "Football");
        } catch (PermissionDeniedException | DoesNotComplyWithPolicyException | SubForumDoesNotExistException e) {
            e.printStackTrace();
        }
        assertEquals(message.printSubTree(), "Flow");
        try {
            user2.replyToMessage("Football", message, "WTF", "Help");
        } catch (PermissionDeniedException | MessageNotFoundException | DoesNotComplyWithPolicyException | SubForumDoesNotExistException e) {
            e.printStackTrace();
        }
        assertEquals(message.printSubTree(), "Flow--> Help");
        try {
            user1.replyToMessage("Football", message, "WTF", "Yeah!");
        } catch (PermissionDeniedException | MessageNotFoundException | DoesNotComplyWithPolicyException | SubForumDoesNotExistException e) {
            e.printStackTrace();
        }
        assertEquals(message.printSubTree(), "Flow--> Help--> Yeah!");
        try {
            user1.deleteMessage(message, "Football");
        } catch (PermissionDeniedException | MessageNotFoundException | SubForumDoesNotExistException e) {
            e.printStackTrace();
        }
        try {
            user1.replyToMessage("Football", message, "aaa", "bbb");
        } catch (PermissionDeniedException | DoesNotComplyWithPolicyException | SubForumDoesNotExistException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = PermissionDeniedException.class)
    public void testDeleteMessageWithoutPermission() throws Exception {
        user2.createSubForum("Football");
        SubForumPermissionI permission = new UserSubforumPermission(Permissions.PERMISSIONS_USER, forum, forum.getSubForums().iterator().next());
        MessageI message = new ForumMessage(user1, "Mega Flow", "Flow");
        user2.createThread(message, "Football");
        user2.deleteMessage(message, "Football"); // PermissionDeniedException expected
    }

    @Test(expected = PermissionDeniedException.class)
    public void testSetAdminWithoutPermission() throws Exception {
        ForumPermissionI permission = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,forum);
        user2.setAdmin(new User("Shreder", "000", "XXX@gmail.com", permission));
    }

    @Test
    public void testSetAdmin() throws Exception {
        ForumPermissionI permission = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,forum);
        user3.setAdmin(new User("Shreder", "000", "XXX@gmail.com",permission));
    }

    @Test
    public void testViewStatistics() throws Exception {

    }

    @Test
    public void testSetModerator() throws Exception {

    }

    @Test
    public void testBanModerator() throws Exception {

    }

    @Test
    public void testAddSubForumPermission() throws Exception {

    }

    @Test
    public void testReportModerator() throws Exception {
        // SubForumI subforum = new SubForum("Baseball", policy.getSubforumPolicy());
        // user1.setModerator(subforum, user2);
        // user1.reportModerator(subforum, "Gabi", "The Worst Moderator Ever");
    }
}