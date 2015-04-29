package main.User;

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

    private ForumPolicyI policy;

    @Before
    public void setUp() throws Exception {
        int maxModerators = 1;
        String regex = "a-b";
        policy = new ForumPolicy(maxModerators, regex);
        forum = new Forum("Sport", policy);
        subforum = new SubForum("Sport", policy.getSubforumPolicy());
        permission = new UserSubforumPermission(UserSubforumPermission.PERMISSIONS.PERMISSIONS_USER,forum, subforum);
        permission2 = new UserSubforumPermission(UserSubforumPermission.PERMISSIONS.PERMISSIONS_ADMIN,forum, subforum);
        permission3 = new UserSubforumPermission(UserSubforumPermission.PERMISSIONS.PERMISSIONS_SUPERADMIN,forum, subforum);
    }
    @Test
    public void testCreateThread() throws Exception {
        ForumPermissionI permission4 = new UserForumPermission(UserForumPermission.PERMISSIONS.PERMISSIONS_USER,forum);
        MessageI message = new ForumMessage(null, new User("Gabi", "123", "aa@mail.com", permission4), "Flow", "Mega Flow");
        permission2.createThread(message);
        assertTrue(permission.getSubForum().getThreads().iterator().next().getRootMessage().equals(message));
    }

    @Test
    public void testReplyToMessage() throws Exception {
        ForumPermissionI permission4 = new UserForumPermission(UserForumPermission.PERMISSIONS.PERMISSIONS_USER,forum);
        MessageI message = new ForumMessage(null, new User("Gabi", "123", "aa@mail.com", permission4), "Flow", "Mega Flow");
        permission2.createThread(message);
        permission2.replyToMessage(message, new ForumMessage(message, new User("Gabi", "123", "aa@mail.com", permission4), "Help", "aaa"));
        assertEquals(message.printSubTree(), "Flow--> Help");
    }

    @Test
    public void testDeleteMessageS() throws Exception {
        ForumPermissionI permission4 = new UserForumPermission(UserForumPermission.PERMISSIONS.PERMISSIONS_USER,forum);
        MessageI message = new ForumMessage(null, new User("Gabi", "123", "aa@mail.com", permission4), "Flow", "Mega Flow");
        permission2.createThread(message);
        permission2.replyToMessage(message, new ForumMessage(message, new User("Gabi", "123", "aa@mail.com", permission4), "Help", "aaa"));
        assertEquals(message.printSubTree(), "Flow--> Help");
        permission.deleteMessage(message, "Gabi");
        assertEquals(message.printSubTree(), "The message has been deleted");
    }

    @Test(expected = PermissionDeniedException.class)
    public void testDeleteMessageWithoutPermission() throws Exception {
        ForumPermissionI permission4 = new UserForumPermission(UserForumPermission.PERMISSIONS.PERMISSIONS_USER,forum);
        MessageI message = new ForumMessage(null, new User("Gabi", "123", "aa@mail.com", permission4), "Flow", "Mega Flow");
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