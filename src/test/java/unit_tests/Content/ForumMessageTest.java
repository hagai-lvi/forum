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
import main.interfaces.UserI;

import java.time.Instant;
import java.util.Date;

/**
 * Created by victor on 6/9/2015 for ${Class}.
 */
public class ForumMessageTest extends TestCase {

    MessageI message;
    UserI user;
    public void setUp() {
        ForumI forum = new Forum("forum2", new ForumPolicy(false, 2, ".*", 365));
        user = new User("user", "pass", "mail@mail.mail", UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_ADMIN, "forum2"));
        message = new ForumMessage("user", "title", "body");
    }

    public void tearDown(){
        try {
            Forum.delete("forum2");
        } catch (ForumNotFoundException e) {
        }
        try {
            User.delete("user", "forum2");
        } catch (UserNotFoundException e) {
        }

    }
    public void testEditText()  {
        message.editText("new-text");
        assertEquals(message.getMessageText(), "new-text");
    }

    public void testGetUser()  {
        assertEquals(message.getUser(), user.getUsername());
    }

    public void testGetDate()  {
        assertTrue(message.getDate().before(Date.from(Instant.now())));
    }

    public void testGetMessageText()  {
        assertEquals("body", message.getMessageText());
    }

    public void testGetMessageTitle()  {
        assertEquals("title", message.getMessageTitle());
    }

    public void testAddReply()  {
        MessageI reply = new ForumMessage("user", "reply-title", "reply-body");
        message.addReply(reply);
        assertEquals(1, message.getReplies().size());
        assertEquals(message.getReplies().iterator().next(), reply);
    }
}