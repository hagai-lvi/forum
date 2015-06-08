package unit_tests.User;

import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.User.UserSubforumPermission;
import main.exceptions.PermissionDeniedException;
import main.forum_contents.Forum;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumPolicy;
import main.forum_contents.SubForum;
import main.interfaces.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gabigiladov on 4/11/15.
 */
public class UserSubforumPermissionTest {
    private UserSubforumPermission permission;
    private UserSubforumPermission permission2;
    private UserSubforumPermission permission3;

    private Forum forum;
    private SubForum subforum;
    private static int dbCounter = 0;
    private ForumPolicyI policy;

    @Before
    public void setUp() throws Exception {
        int maxModerators = 1;
        String regex = "a-b";
        policy = new ForumPolicy(false, maxModerators, regex, 365);
        forum = new Forum("Sport" + dbCounter, policy);
        dbCounter += 1;
        subforum = new SubForum("Sport", policy.getSubforumPolicy());
        permission = new UserSubforumPermission(Permissions.PERMISSIONS_USER,forum, subforum);
        permission2 = new UserSubforumPermission(Permissions.PERMISSIONS_ADMIN,forum, subforum);
        permission3 = new UserSubforumPermission(Permissions.PERMISSIONS_SUPERADMIN,forum, subforum);
    }
    @Test
    public void testCreateThread() throws Exception {
        ForumPermissionI permission4 = new UserForumPermission(Permissions.PERMISSIONS_USER,forum);
        MessageI message = new ForumMessage(null, new User("Gabi", "123", "aa@mail.com", permission4), "Mega Flow1", "Flow1");
        permission2.createThread(message);
        assertTrue(permission.getSubForum().getThreads().iterator().next().getRootMessage().equals(message));
    }

    @Test
    public void testReplyToMessage() throws Exception {
        ForumPermissionI permission4 = new UserForumPermission(Permissions.PERMISSIONS_USER,forum);
        MessageI message = new ForumMessage(null, new User("Gabi", "123", "aa@mail.com", permission4), "Mega Flow2", "Flow2");
        permission2.createThread(message);
        permission2.replyToMessage(message, new ForumMessage(message, new User("Gabi", "123", "aa@mail.com", permission4), "aaa3", "Help3"));
        assertEquals(message.printSubTree(), "Flow2--> Help3");
    }

    @Test
    public void testDeleteMessageS() throws Exception {
        ForumPermissionI permission4 = new UserForumPermission(Permissions.PERMISSIONS_USER,forum);
        MessageI message = new ForumMessage(null, new User("Gabi", "123", "aa@mail.com", permission4), "Mega Flow2222", "Flow222");
        permission2.createThread(message);
        permission2.replyToMessage(message, new ForumMessage(message, new User("Gabi", "123", "aa@mail.com", permission4), "aaa", "Help"));
        assertEquals(message.printSubTree(), "Flow222--> Help");
        permission.deleteMessage(message, "Gabi");
        assertEquals(message.printSubTree(), "The message has been deleted");
    }

    @Test(expected = PermissionDeniedException.class)
    public void testDeleteMessageWithoutPermission() throws Exception {
        ForumPermissionI permission4 = new UserForumPermission(Permissions.PERMISSIONS_USER,forum);
        MessageI message = new ForumMessage(null, new User("Gabi", "123", "aa@mail.com", permission4), "Mega Flow", "Flow");
        permission2.createThread(message);
        permission.deleteMessage(message, "Victor"); // PermissionDeniedException expected
    }

    @Test
    public void testReportModerator() throws Exception {

    }

    @Test
    public void testGetThreads() throws Exception {

    }

    @Test
    public void testSetModerator() throws Exception {

    }

}