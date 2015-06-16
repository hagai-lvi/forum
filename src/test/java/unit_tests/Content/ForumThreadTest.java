package unit_tests.Content;

import junit.framework.TestCase;
import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.exceptions.MessageNotFoundException;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumThread;
import main.interfaces.MessageI;
import main.interfaces.ThreadI;
import main.interfaces.UserI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by victor on 6/5/2015 for ${Class}.
 */
public class ForumThreadTest extends TestCase {

    ThreadI thread;
    MessageI msg;
    UserI user;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        user = new User("user", "pass", "aaa@aaa.aaa", new UserForumPermission(Permissions.PERMISSIONS_ADMIN, null));
        msg = new ForumMessage(user, "title", "body");
        thread = new ForumThread(msg);
    }
    @After
    public void tearDown() throws Exception {

    }
    @Test
    public void testGetTitle() throws Exception {
        assertEquals(thread.getTitle(), "title");
    }
    @Test
    public void testGetMessages() throws Exception {
        assertEquals(thread.getMessages().getRoot().getData().getMessageTitle(), "title");
        assertEquals(thread.getMessages().getRoot().getData().getMessageText(), "body");

    }
    @Test
    public void testGetRootMessage() throws Exception {
        assertEquals(thread.getRootMessage().getMessageTitle(), "title");
        assertEquals(thread.getRootMessage().getMessageText(), "body");
    }
    @Test
    public void testAddReply() {
        MessageI reply = new ForumMessage(user, "reply-title", "reply-body");
        try {
            thread.addReply(reply, msg);
        } catch (MessageNotFoundException e) {
            fail("message not found");
        }
        assertEquals(1, thread.getMessages().getRoot().children.size());
        assertEquals(reply.getMessageTitle(), thread.getMessages().getRoot().children.iterator().next().getData().getMessageTitle());
        assertEquals(reply.getMessageText(), thread.getMessages().getRoot().children.iterator().next().getData().getMessageText());
        assertEquals(thread.getMessages().findNode((ForumMessage) reply).getMessageTitle(), reply.getMessageTitle());
        try {
            thread.addReply(reply, null);
        } catch (MessageNotFoundException e) {
            return;
        }
        fail("reply to non-existant message");
    }
    @Test
    public void testContains() throws Exception {
        assertTrue(thread.contains(msg));
        MessageI newmsg = new ForumMessage(user, "aaa", "bbb");
        assertFalse(thread.contains(newmsg));
        thread.addReply(newmsg, msg);
        assertTrue(thread.contains(newmsg));
    }
    @Test
    public void testRemove() throws Exception {
        MessageI newmsg = new ForumMessage(user, "aaa", "bbb");
        thread.addReply(newmsg, msg);
        thread.remove(newmsg);
        assertFalse(thread.contains(newmsg));
        thread.addReply(newmsg, msg);
        thread.remove(msg);
        assertFalse(thread.contains(newmsg));
        assertFalse(thread.contains(msg));
    }
    @Test
    public void testEditMessage(){
        MessageI msg = new ForumMessage(user, "newTitle", "newBody");
        try {
            thread.editMessage(thread.getRootMessage(), msg);
        } catch (MessageNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(msg.getMessageTitle(), thread.getRootMessage().getMessageTitle());
        assertEquals(msg.getMessageText(), thread.getRootMessage().getMessageText());

        try {
            thread.editMessage(thread.getRootMessage(), null);
        } catch (MessageNotFoundException e) {
            assertTrue(true);
        }
        try {
            thread.editMessage(null, msg);
        } catch (MessageNotFoundException e) {
            assertTrue(true);
            return;
        }
        fail("edited message to null");

    }
}