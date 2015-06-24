package unit_tests.Content;

import junit.framework.TestCase;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumPolicy;
import main.forum_contents.ForumThread;
import main.interfaces.*;
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
        user = forum.getUserList().iterator().next();

    }
    @After
    public void tearDown() throws ForumNotFoundException {
        Forum.delete("forum");
    }

    public void testGetModerators()   {

        subforum.setModerator(user);
        assertSame(user, subforum.getModerators().get(user.getUsername()));


    }

    public void testCreateThread() throws DoesNotComplyWithPolicyException {
        ThreadI thread = subforum.addThread(new ForumMessage(user, "msg", "msg"));
        assertTrue(subforum.getThreads().containsKey("msg"));
        assertEquals(thread.getRootMessage().getMessageText(), "msg");
        assertEquals(thread.getRootMessage().getMessageTitle(), "msg");
        assertEquals(subforum.getThreads().size(), 1);
        assertEquals(thread.getMessages().getRoot().children.size(), 0);
        try{
            subforum.addThread(new ForumMessage(user, "", ""));
        } catch (DoesNotComplyWithPolicyException e){
            assertTrue(true);
            return;
        }
        fail("did not catch invalid message");
    }

    public void testReplyToMessage() throws DoesNotComplyWithPolicyException, MessageNotFoundException {
        ThreadI thread = subforum.addThread(new ForumMessage(user, "msg", "msg"));
        MessageI newmsg = new ForumMessage(user, "gsm", "gsm");
        subforum.replyToMessage(thread.getRootMessage(), newmsg);
        assertEquals(thread.getRootMessage().getMessageText(), "msg");
        assertEquals(thread.getRootMessage().getMessageTitle(), "msg");
        assertEquals(thread.getMessages().getRoot().children.size(), 1);
        assertEquals(thread.getMessages().getRoot().children.iterator().next().data.getMessageText(), newmsg.getMessageText());
        assertEquals(thread.getMessages().getRoot().children.iterator().next().data.getMessageTitle(),newmsg.getMessageTitle());
        assertEquals(subforum.getThreads().size(), 1);
    }

    public void testReplyToInvalidMessage() throws MessageNotFoundException {
        try {
            ThreadI thread = subforum.addThread(new ForumMessage(user, "msg", "msg"));
            MessageI newmsg = new ForumMessage(user, "", "");
            subforum.replyToMessage(thread.getRootMessage(), newmsg);
        } catch (DoesNotComplyWithPolicyException e) {
            assertTrue(true);
            return;
        }
        fail("did not catch invalid message reply");
    }

    public void testReplyToNonExistentMessage() throws DoesNotComplyWithPolicyException {
        try {
            ThreadI thread = subforum.addThread(new ForumMessage(user, "msg", "msg"));
            MessageI newmsg = new ForumMessage(user, "def", "abc");
            subforum.replyToMessage(newmsg, newmsg);
        } catch (MessageNotFoundException e) {
            assertTrue(true);
            return;
        }
        fail("did not catch non-existent message reply");
    }

    public void testSetModerator()  {
        subforum.setModerator(user);
        assertEquals(subforum.getModerators().size(), 1);
        assertSame(subforum.getModerators().get(user.getUsername()), user);
    }

    public void testReportModerator()  {
        fail("Not yet implemented");
    }

    public void testDeleteMessage() throws DoesNotComplyWithPolicyException, MessageNotFoundException {
        ThreadI thread = subforum.addThread(new ForumMessage(user, "msg", "msg"));
        MessageI newmsg = new ForumMessage(user, "gsm", "gsm");
        subforum.replyToMessage(thread.getRootMessage(), newmsg);
        subforum.deleteMessage(newmsg, user.getUsername());
        assertEquals(thread.getMessages().getRoot().children.size(), 0);
        subforum.deleteMessage(thread.getRootMessage(), "user");
        assertEquals(subforum.getThreads().size(), 0);

        try{
            subforum.deleteMessage(newmsg, user.getUsername());
        } catch (MessageNotFoundException e){
            assertTrue(true);
            return;
        }
        fail("did not catch double-deletion message");

    }

    public void testRemoveModerator()   {
        subforum.setModerator(user);
        subforum.removeModerator(user.getUsername());
        assertEquals(subforum.getModerators().size(), 0);
    }

    public void testGetTitle()  {
        assertEquals(subforum.getTitle(), "subforum");
    }

    public void testGetThreads() throws DoesNotComplyWithPolicyException {
        ThreadI thread = subforum.addThread(new ForumMessage(user, "msg", "thread"));
        assertEquals(subforum.getThreads().size(), 1);
        assertTrue(subforum.getThreads().containsKey(thread.getTitle()));
    }

    public void testEditMessage() {
        MessageI msg = new ForumMessage(user, "title", "body");
        MessageI newmsg = new ForumMessage(user, "newTitle", "newBody");
        ThreadI thread = null;
        try {
            thread = subforum.addThread(msg);
        } catch (DoesNotComplyWithPolicyException e) {
            e.printStackTrace();
        }
        try {
            subforum.editMessage(thread, thread.getRootMessage().getId(), "newTitle", "newBody");
        } catch (MessageNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(newmsg.getMessageTitle(), thread.getRootMessage().getMessageTitle());
        assertEquals(newmsg.getMessageText(), thread.getRootMessage().getMessageText());
    }

    public void testSeveralThreads() throws DoesNotComplyWithPolicyException {
        List<ThreadI> threads = new LinkedList<>();
        for (int i=0; i < 10; i++) {
            ThreadI thread = subforum.addThread(new ForumMessage(user, "msg", "msg" + i));
            threads.add(thread);
        }
        for (int i=0; i < 10; i++) {
            ThreadI thread = new ForumThread(new ForumMessage(user, "msg", "msg" + i));
            assertTrue(subforum.getThreads().containsKey(threads.get(i)));
        }
        assertEquals(subforum.getThreads().size(), 10);
    }
}