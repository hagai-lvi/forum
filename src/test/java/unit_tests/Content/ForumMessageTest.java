package unit_tests.Content;

import junit.framework.TestCase;
import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.forum_contents.Forum;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumPolicy;
import main.interfaces.MessageI;
import main.interfaces.UserI;

import java.time.Instant;
import java.util.Date;

/**
 * Created by victor on 6/9/2015 for ${Class}.
 */
public class ForumMessageTest extends TestCase {

    MessageI message;
    UserI user;
    public void setUp() throws Exception {
        super.setUp();
        user = new User("user", "pass", "mail@mail.mail", UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_ADMIN, new Forum("forum", new ForumPolicy(false, 2, ".*", 365))));
        message = new ForumMessage(user, "title", "body");

    }

    public void tearDown() throws Exception {

    }

    public void testEditText() throws Exception {
        message.editText("new-text");
        assertEquals(message.getMessageText(), "new-text");
    }

    public void testGetUser() throws Exception {
        assertEquals(message.getUser(), user.getUsername());
    }

    public void testGetDate() throws Exception {
        assertTrue(message.getDate().before(Date.from(Instant.now())));
    }

    public void testGetMessageText() throws Exception {
        assertEquals("body", message.getMessageText());
    }

    public void testGetMessageTitle() throws Exception {
        assertEquals("title", message.getMessageTitle());
    }

    public void testAddReply() throws Exception {
        MessageI reply = new ForumMessage(user, "reply-title", "reply-body");
        message.addReply(reply);
        assertEquals(1, message.getReplies().size());
        assertEquals(message.getReplies().iterator().next(), reply);
    }
}