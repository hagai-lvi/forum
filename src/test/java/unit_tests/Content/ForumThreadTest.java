package unit_tests.Content;

import junit.framework.TestCase;
import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.exceptions.ForumNotFoundException;
import main.exceptions.MessageNotFoundException;
import main.exceptions.ThreadFinalMessageDeletedException;
import main.forum_contents.Forum;
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
    public void setUp()  {
        forum = new Forum("forum", new ForumPolicy(false, 2, ".*", 365));
        user = new User("user", "pass", "aaa@aaa.aaa", UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_ADMIN, forum.getName()));
        thread = new ForumThread("user", "title", "text");
        msg = thread.getRootMessage();

    }
    @After
    public void tearDown() throws ForumNotFoundException {
        Forum.delete("forum");
    }
    @Test
    public void testGetTitle()  {
        assertEquals(thread.getTitle(), "title");
    }
    @Test
    public void testGetMessages()  {
        assertEquals(thread.getMessages().getRoot().getData().getMessageTitle(), "title");
        assertEquals(thread.getMessages().getRoot().getData().getMessageText(), "text");

    }
    @Test
    public void testGetRootMessage()  {
        assertEquals(thread.getRootMessage().getMessageTitle(), "title");
        assertEquals(thread.getRootMessage().getMessageText(), "text");
    }




    @Test
    public void testRootMessageRemove() throws MessageNotFoundException {
        try {
            thread.remove(msg);
        } catch (ThreadFinalMessageDeletedException e) {
            assertTrue(true);
            return;
        }
        fail("deletion of last message not reported");
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
        assertEquals(msg.getId(), thread.getRootMessage().getId());

        try {
            thread.editMessage(thread.getRootMessage(), null, null);
            thread.editMessage(msg, "", "");
            fail("edited message to null");
        } catch (MessageNotFoundException e) {
            assertTrue(true);
            return;
        }
        fail("edited message to invalid value");


    }
}