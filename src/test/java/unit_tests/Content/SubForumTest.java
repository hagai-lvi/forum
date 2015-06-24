package unit_tests.Content;

import junit.framework.TestCase;
import main.User.User;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.ForumI;
import main.interfaces.SubForumI;
import main.interfaces.ThreadI;
import main.interfaces.UserI;
import org.junit.After;
import org.junit.Before;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by victor on 6/5/2015 for ${Class}.
 */
public class SubForumTest extends TestCase {

    ForumI forum;
    SubForumI subforum;
    UserI user;

    @Before
    public void setUp() throws SubForumAlreadyExistException, DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException {
        forum = new Forum("forum", new ForumPolicy(false, 2, "aaa", 365));
        forum.addSubForum("subforum");
        subforum = forum.getSubForums().get("subforum");
        forum.register("user", "aaa", "aaa@aaa.aaa");
        user = User.getUserFromDB("user", "forum");

    }
    @After
    public void tearDown() throws ForumNotFoundException {
        Forum.delete("forum");
    }

    public void testGetModerators()   {

        Forum.load("forum").getSubForums().get("subforum").setModerator(user);
        assertSame(user, Forum.load("forum").getSubForums().get("subforum").getModerators().get(user.getUsername()));


    }

    public void testCreateThread() throws DoesNotComplyWithPolicyException {
        ThreadI thread = Forum.load("forum").getSubForums().get("subforum").addThread("user", "msg", "msg");
        assertTrue(Forum.load("forum").getSubForums().get("subforum").getThreads().containsKey("msg"));
        assertEquals(thread.getRootMessage().getMessageText(), "msg");
        assertEquals(thread.getRootMessage().getMessageTitle(), "msg");
        assertEquals(Forum.load("forum").getSubForums().get("subforum").getThreads().size(), 1);
        assertEquals(thread.getMessages().getRoot().children.size(), 0);
        try{
            Forum.load("forum").getSubForums().get("subforum").addThread("user", "", "");
        } catch (DoesNotComplyWithPolicyException e){
            assertTrue(true);
            return;
        }
        fail("did not catch invalid message");
    }

    public void testReplyToMessage() throws DoesNotComplyWithPolicyException, MessageNotFoundException {
        ThreadI thread = Forum.load("forum").getSubForums().get("subforum").addThread("user", "msg", "msg");
        Forum.load("forum").getSubForums().get("subforum").replyToMessage(thread.getRootMessage(), "user", "gsm", "gsm");
        assertEquals(thread.getRootMessage().getMessageText(), "msg");
        assertEquals(thread.getRootMessage().getMessageTitle(), "msg");
        assertEquals(thread.getMessages().getRoot().children.size(), 1);
        assertEquals(thread.getMessages().getRoot().children.iterator().next().data.getMessageText(), "gsm");
        assertEquals(thread.getMessages().getRoot().children.iterator().next().data.getMessageTitle(),"gsm");
        assertEquals(Forum.load("forum").getSubForums().get("subforum").getThreads().size(), 1);
    }

    public void testReplyToInvalidMessage() throws MessageNotFoundException {
        try {
            ThreadI thread = Forum.load("forum").getSubForums().get("subforum").addThread("user", "msg", "msg");
            Forum.load("forum").getSubForums().get("subforum").replyToMessage(thread.getRootMessage(), "user", "", "");
        } catch (DoesNotComplyWithPolicyException e) {
            assertTrue(true);
            return;
        }
        fail("did not catch invalid message reply");
    }

    public void testReplyToNonExistentMessage() throws DoesNotComplyWithPolicyException {
        try {
            ThreadI thread = Forum.load("forum").getSubForums().get("subforum").addThread("user", "msg", "msg");
            Forum.load("forum").getSubForums().get("subforum").replyToMessage(thread.getRootMessage(), "user", "def", "abc");
        } catch (MessageNotFoundException e) {
            assertTrue(true);
            return;
        }
        fail("did not catch non-existent message reply");
    }

    public void testSetModerator()  {
        Forum.load("forum").getSubForums().get("subforum").setModerator(user);
        assertEquals(Forum.load("forum").getSubForums().get("subforum").getModerators().size(), 1);
        assertSame(Forum.load("forum").getSubForums().get("subforum").getModerators().get(user.getUsername()), user);
    }

    public void testReportModerator()  {
        fail("Not yet implemented");
    }

    public void testDeleteMessage() throws DoesNotComplyWithPolicyException, MessageNotFoundException {
        ThreadI thread = Forum.load("forum").getSubForums().get("subforum").addThread("user", "msg", "msg");
        Forum.load("forum").getSubForums().get("subforum").replyToMessage(thread.getRootMessage(), "user", "gsm", "gsm");
        Forum.load("forum").getSubForums().get("subforum").deleteMessage(thread.getRootMessage(), user.getUsername());
        assertEquals(thread.getMessages().getRoot().children.size(), 0);
        Forum.load("forum").getSubForums().get("subforum").deleteMessage(thread.getRootMessage(), "user");
        assertEquals(Forum.load("forum").getSubForums().get("subforum").getThreads().size(), 0);

        try{
            Forum.load("forum").getSubForums().get("subforum").deleteMessage(thread.getRootMessage(), user.getUsername());
        } catch (MessageNotFoundException e){
            assertTrue(true);
            return;
        }
        fail("did not catch double-deletion message");

    }

    public void testRemoveModerator()   {
        Forum.load("forum").getSubForums().get("subforum").setModerator(user);
        Forum.load("forum").getSubForums().get("subforum").removeModerator(user.getUsername());
        assertEquals(Forum.load("forum").getSubForums().get("subforum").getModerators().size(), 0);
    }

    public void testGetTitle()  {
        assertEquals(Forum.load("forum").getSubForums().get("subforum").getTitle(), "subforum");
    }

    public void testGetThreads() throws DoesNotComplyWithPolicyException {
        ThreadI thread = Forum.load("forum").getSubForums().get("subforum").addThread("user", "msg", "thread");
        assertEquals(Forum.load("forum").getSubForums().get("subforum").getThreads().size(), 1);
        assertTrue(Forum.load("forum").getSubForums().get("subforum").getThreads().containsKey(thread.getTitle()));
    }

    public void testEditMessage() {
        ThreadI thread = null;
        try {
            thread = Forum.load("forum").getSubForums().get("subforum").addThread("user", "title", "body");
        } catch (DoesNotComplyWithPolicyException e) {
            e.printStackTrace();
        }
        try {
            Forum.load("forum").getSubForums().get("subforum").editMessage(thread, thread.getRootMessage().getId(), "newTitle", "newBody");
        } catch (MessageNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals("newTitle", thread.getRootMessage().getMessageTitle());
        assertEquals("newBody", thread.getRootMessage().getMessageText());
    }

    public void testSeveralThreads() throws DoesNotComplyWithPolicyException {
        List<ThreadI> threads = new LinkedList<>();
        for (int i=0; i < 10; i++) {
            ThreadI thread = Forum.load("forum").getSubForums().get("subforum").addThread("user", "msg", "msg" + i);
            threads.add(thread);
        }
        for (int i=0; i < 10; i++) {
            assertTrue(Forum.load("forum").getSubForums().get("subforum").getThreads().containsKey(threads.get(i).getTitle()));
        }
        assertEquals(Forum.load("forum").getSubForums().get("subforum").getThreads().values().size(), 10);
    }
}