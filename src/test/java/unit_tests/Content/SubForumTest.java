package unit_tests.Content;

import junit.framework.TestCase;
import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.forum_contents.Forum;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumPolicy;
import main.forum_contents.ForumThread;
import main.interfaces.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by victor on 6/5/2015 for ${Class}.
 */
public class SubForumTest extends TestCase {

    ForumI forum;
    SubForumI subforum;
    UserI user;
    private static int dbCount =0;
    public void setUp() throws Exception {
        super.setUp();
        forum = new Forum("forum" + dbCount++, new ForumPolicy(false, 2, "aaa", 365));
        forum.createSubForum("subforum");
        subforum = forum.getSubForums().iterator().next();
        forum.register("user", "aaa", "aaa@aaa.aaa");
        user = forum.getUserList().iterator().next();
    }

    public void tearDown() throws Exception {
    }

    public void testGetModerators() throws Exception {

        subforum.setModerator(user);
        assertSame(user, subforum.getModerators().get(user.getUsername()));


    }

    public void testCreateThread() throws Exception {
        ThreadI thread = subforum.createThread(new ForumMessage(null, user, "msg", "msg"));
        assertTrue(subforum.getThreads().contains(thread));
        assertEquals(thread.getRootMessage().getMessageText(), "msg");
        assertEquals(thread.getRootMessage().getMessageTitle(), "msg");
        assertEquals(subforum.getThreads().size(), 1);
        assertEquals(thread.getMessages().getRoot().children.size(), 0);
        try{
            subforum.createThread(new ForumMessage(null, user, "", ""));
        } catch (DoesNotComplyWithPolicyException e){
            assertTrue(true);
            return;
        }
        fail("did not catch invalid message");
    }

    public void testReplyToMessage() throws Exception {
        ThreadI thread = subforum.createThread(new ForumMessage(null, user, "msg", "msg"));
        MessageI newmsg = new ForumMessage(thread.getRootMessage(), user, "gsm", "gsm");
        subforum.replyToMessage(thread.getRootMessage(), newmsg);
        assertEquals(thread.getRootMessage().getMessageText(), "msg");
        assertEquals(thread.getRootMessage().getMessageTitle(), "msg");
        assertEquals(thread.getMessages().getRoot().children.size(), 1);
        assertEquals(thread.getMessages().getRoot().children.iterator().next().data.getMessageText(), newmsg.getMessageText());
        assertEquals(thread.getMessages().getRoot().children.iterator().next().data.getMessageTitle(),newmsg.getMessageTitle());
        assertEquals(subforum.getThreads().size(), 1);
    }

    public void testReplyToInvalidMessage() throws Exception {
        try {
            ThreadI thread = subforum.createThread(new ForumMessage(null, user, "msg", "msg"));
            MessageI newmsg = new ForumMessage(thread.getRootMessage(), user, "", "");
            subforum.replyToMessage(thread.getRootMessage(), newmsg);
        } catch (DoesNotComplyWithPolicyException e) {
            assertTrue(true);
            return;
        }
        fail("did not catch invalid message reply");
    }

    public void testReplyToNonExistentMessage() throws Exception {
        try {
            setUp();
            ThreadI thread = subforum.createThread(new ForumMessage(null, user, "msg", "msg"));
            MessageI newmsg = new ForumMessage(thread.getRootMessage(), user, "abc", "def");
            subforum.replyToMessage(newmsg, newmsg);
        } catch (MessageNotFoundException e) {
            assertTrue(true);
            return;
        }
        fail("did not catch non-existent message reply");
    }

    public void testSetModerator() throws Exception {
        subforum.setModerator(user);
        assertEquals(subforum.getModerators().size(), 1);
        assertSame(subforum.getModerators().get(user.getUsername()), user);
    }

    public void testReportModerator() throws Exception {
        fail("Not yet implemented");
    }

    public void testDeleteMessage() throws Exception {
        ThreadI thread = subforum.createThread(new ForumMessage(null, user, "msg", "msg"));
        MessageI newmsg = new ForumMessage(thread.getRootMessage(), user, "gsm", "gsm");
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

    public void testRemoveModerator() throws Exception {
        subforum.setModerator(user);
        subforum.removeModerator(user);
        assertEquals(subforum.getModerators().size(), 0);
    }

    public void testGetTitle() throws Exception {
        assertEquals(subforum.getTitle(), "subforum");
    }

    public void testGetThreads() throws Exception {
        ThreadI thread = subforum.createThread(new ForumMessage(null, user, "thread", "msg"));
        assertEquals(subforum.getThreads().size(), 1);
        assertEquals(subforum.getThreads().iterator().next().getTitle(), thread.getTitle());
    }

    public void testLoad() throws Exception {
        fail("Not yet implemented");
    }

    public void testSeveralThreads() throws Exception {
        List<ThreadI> threads = new LinkedList<>();
        for (int i=0; i < 10; i++) {
            ThreadI thread = subforum.createThread(new ForumMessage(null, user, "msg" + i, "msg"));
            threads.add(thread);
        }
        for (int i=0; i < 10; i++) {
            ThreadI thread = new ForumThread(new ForumMessage(null, user, "msg" + i, "msg"));
            assertTrue(subforum.getThreads().contains(threads.get(i)));
        }
        assertEquals(subforum.getThreads().size(), 10);
    }
}