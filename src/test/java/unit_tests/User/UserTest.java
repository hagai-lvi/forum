package unit_tests.User;

import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.User.UserSubforumPermission;
import main.exceptions.PermissionDeniedException;
import main.forum_contents.*;
import main.interfaces.*;
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

    private UserI user;
    private UserI user2;
    private UserI user3;
    private ForumI forum;
    private ForumPolicyI policy;

    @Before
    public void setUp() throws Exception {
        int maxModerators = 1;
        String regex = "a-b";
        boolean isSecured = false;
        int passLife = 365;
        policy = new ForumPolicy(isSecured, maxModerators, regex, passLife);
        forum = new Forum("Sport", policy);
        ForumPermissionI permission = new UserForumPermission(Permissions.PERMISSIONS_USER,forum);
        ForumPermissionI permission2 = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,forum);
        ForumPermissionI permission3 = new UserForumPermission(Permissions.PERMISSIONS_SUPERADMIN,forum);
        user = new User("Gabi", "123456", "mail@gmail.com", permission);
        user2 = new User("Victor", "abcde", "mail2@gmail.com", permission2);
        user3 = new User("Tom", "abcde", "mail2@gmail.com", permission3);

    }


    @Test
    public void testIsEmailAuthenticated() throws Exception {
        assertFalse(user.isEmailAuthenticated());
        forum.sendAuthenticationEMail(user);
        assertFalse(forum.enterUserAuthenticationString(user, "bullshit"));
        assertTrue(forum.enterUserAuthenticationString(user, user.getUserAuthString()));
    }

    @Test
    public void testSetAuthenticated() throws Exception {
        assertFalse(user.isEmailAuthenticated());
        user.setAuthenticated();
        assertTrue(user.isEmailAuthenticated());
    }

    @Test
    public void testGetUsername() throws Exception {
        assertEquals(user.getUsername(), "Gabi");
    }


    @Test
    public void testGetSignUpDate() throws Exception {
        GregorianCalendar date = user.getSignUpDate();
        Calendar today =  GregorianCalendar.getInstance();
        assertEquals(date.get(Calendar.YEAR), today.get(Calendar.YEAR));
        assertEquals(date.get(Calendar.MONTH), today.get(Calendar.MONTH));
        assertEquals(date.get(Calendar.DAY_OF_MONTH), today.get(Calendar.DAY_OF_MONTH));
    }


    @Test(expected=PermissionDeniedException.class)
    public void testCreateSubForumForRegularUser() throws Exception {
        user.createSubForum("Football"); // PermissionDeniedException expected
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
        user.deleteSubForum(subforum); // PermissionDeniedException expected
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
        SubForumPermissionI permission = new UserSubforumPermission(Permissions.PERMISSIONS_USER, forum, forum.getSubForums().iterator().next());
        MessageI message = new ForumMessage(null, user, "Flow", "Mega Flow");
        user2.createThread(message, permission);
        assertTrue(permission.getSubForum().getThreads().iterator().next().getRootMessage().equals(message));
    }

    @Test
    public void testReplyToMessage() throws Exception {
        user2.createSubForum("Football");
        SubForumPermissionI permission = new UserSubforumPermission(Permissions.PERMISSIONS_USER, forum, forum.getSubForums().iterator().next());
        MessageI message = new ForumMessage(null, user, "Flow", "Mega Flow");
        user2.createThread(message, permission);
        user2.replyToMessage(permission, message, "WTF", "Help");
        user.replyToMessage(permission, message, "WTF", "Yeah!");
        assertEquals(message.printSubTree(), "Flow--> Help--> Yeah!");
    }

    @Test
    public void testDeleteMessageS() throws Exception {
        user2.createSubForum("Football");
        SubForumPermissionI permission = new UserSubforumPermission(Permissions.PERMISSIONS_USER, forum, forum.getSubForums().iterator().next());
        MessageI message = new ForumMessage(null, user, "Flow", "Mega Flow");
        user2.createThread(message, permission);
        user2.replyToMessage(permission, message, "WTF", "Help");
        user.replyToMessage(permission, message, "WTF", "Yeah!");
        assertEquals(message.printSubTree(), "Flow--> Help--> Yeah!");
        user.deleteMessage(message, permission);
        assertEquals(message.printSubTree(), "The message has been deleted");
    }

    @Test(expected = PermissionDeniedException.class)
    public void testDeleteMessageWithoutPermission() throws Exception {
        user2.createSubForum("Football");
        SubForumPermissionI permission = new UserSubforumPermission(Permissions.PERMISSIONS_USER, forum, forum.getSubForums().iterator().next());
        MessageI message = new ForumMessage(null, user, "Flow", "Mega Flow");
        user2.createThread(message, permission);
        user2.deleteMessage(message, permission); // PermissionDeniedException expected
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
        // user.setModerator(subforum, user2);
        // user.reportModerator(subforum, "Gabi", "The Worst Moderator Ever");
    }
}