package unit_tests.Content;

import junit.framework.TestCase;
import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.exceptions.MessageNotFoundException;
import main.forum_contents.Forum;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumPolicy;
import main.forum_contents.ForumThread;
import main.interfaces.ForumI;
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

    ForumI forum;
    ThreadI thread;
    MessageI msg;
    UserI user;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        forum = new Forum("forum", new ForumPolicy(false, 2, ".*", 365));
        user = new User("user", "pass", "aaa@aaa.aaa", UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_ADMIN, forum.getName()));
        msg = new ForumMessage("user", "title", "body");
        thread = new ForumThread("user", "title", "text");
    }
    @After
    public void tearDown() throws Exception {
        Forum.delete("forum");
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
    public void testAddReply() throws MessageNotFoundException {
        MessageI reply;
        reply = thread.addReply(new ForumMessage("user", "title", "text"), "reply-title", "reply-body", "user");
        assertEquals(1, thread.getMessages().getRoot().children.size());
        assertEquals("reply-title", thread.getMessages().getRoot().children.iterator().next().getData().getMessageTitle());
        assertEquals("reply-body", thread.getMessages().getRoot().children.iterator().next().getData().getMessageText());
        assertEquals(thread.getMessages().findNode(reply).getMessageTitle(), "reply-title");
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(thread.contains(msg));
        MessageI newmsg =thread.addReply(msg, "aaa", "bbb", "user");
        assertTrue(thread.contains(newmsg));
    }
    @Test
    public void testRemove() throws Exception {
        MessageI newmsg =thread.addReply(msg, "aaa", "bbb", "user");
        thread.remove(newmsg);
        assertFalse(thread.contains(newmsg));
        MessageI msg =thread.addReply(newmsg, "ccc", "ddd", "user");
        thread.remove(msg);
        assertFalse(thread.contains(newmsg));
        assertFalse(thread.contains(msg));
    }
    @Test
    public void testEditMessage(){
        try {
            thread.editMessage(thread.getRootMessage(),"newTitle", "newBody");
        } catch (MessageNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals("newTitle", thread.getRootMessage().getMessageTitle());
        assertEquals("newBody", thread.getRootMessage().getMessageText());

        try {
            thread.editMessage(thread.getRootMessage(), null, null);
        } catch (MessageNotFoundException e) {
            assertTrue(true);
        }
////        try {
////            thread.editMessage(msg, "", "");
////        } catch (MessageNotFoundException e) {
////            assertTrue(true);
////            return;
////        }
//        fail("edited message to null");

    }
}