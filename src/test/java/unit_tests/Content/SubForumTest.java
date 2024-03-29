package unit_tests.Content;

import junit.framework.TestCase;
import main.User.User;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.*;
import org.junit.After;
import org.junit.Before;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 6/5/2015 for ${Class}.
 */
public class SubForumTest extends TestCase {

    ForumI forum;
    UserI user;

    @Before
    public void setUp() throws SubForumAlreadyExistException, DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException {
        forum = new Forum("forum", new ForumPolicy(false, 2, "aaa", 365));
        forum.addSubForum("subforum");
        forum.register("user", "aaa", "aaa@aaa.aaa");
        user = User.getUserFromDB("user", "forum");

    }
    @After
    public void tearDown() throws ForumNotFoundException {
        Forum.delete("forum");
    }

    public void testGetModerators() throws TooManyModeratorsException {
        SubForumI subforum = Forum.load("forum").getSubForums().get("subforum");
        subforum.setModerator(User.getUserFromDB("user", "forum"));
        Map<String, UserI> mods = subforum.getModerators();
        assertTrue(mods.containsKey(user.getUsername()));
        assertTrue(mods.containsKey("user"));
    }

    public void testCreateThread() throws DoesNotComplyWithPolicyException {
        ThreadI thread = forum.getSubForums().get("subforum").addThread("user", "title", "body");
        SubForumI subforum = Forum.load("forum").getSubForums().get("subforum");
        assertTrue(subforum.getThreads().containsKey("title"));
        assertEquals("body", thread.getRootMessage().getMessageText());
        assertEquals("title", thread.getRootMessage().getMessageTitle());
        assertEquals(1, subforum.getThreads().size());
        assertEquals(thread.getMessages().getRoot().children.size(), 0);
        try{
            subforum.addThread("user", "", "");
        } catch (DoesNotComplyWithPolicyException e){
            assertTrue(true);
            return;
        }
        fail("did not catch invalid message");
    }

    public void testReplyToMessage() throws DoesNotComplyWithPolicyException, MessageNotFoundException {
        ForumI forum = Forum.load("forum");
        MessageI rootMessage = forum.getSubForums().get("subforum").addThread("user", "title", "body").getRootMessage();
        forum.getSubForums().get("subforum").replyToMessage(rootMessage, "user", "reply-title", "reply-body");
        SubForumI subforum = forum.getSubForums().get("subforum");
        ThreadI thread = subforum.getThreads().get("title");

        assertEquals("body", thread.getRootMessage().getMessageText());
        assertEquals("title", thread.getRootMessage().getMessageTitle());
        assertEquals(1, thread.getRootMessage().getReplies().size());
        assertEquals(2, subforum.getMessagesCount());
        assertEquals("reply-body", thread.getMessages().getRoot().children.iterator().next().data.getMessageText());
        assertEquals("reply-title", thread.getMessages().getRoot().children.iterator().next().data.getMessageTitle());
        assertEquals(subforum.getThreads().size(), 1);
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
            Forum.load("forum").getSubForums().get("subforum").replyToMessage(null, "user", "def", "abc");
        } catch (MessageNotFoundException e) {
            assertTrue(true);
            return;
        }
        fail("did not catch non-existent message reply");
    }

    public void testSetModerator() throws TooManyModeratorsException {
        Forum.load("forum").getSubForums().get("subforum").setModerator(user);
        assertEquals(1, Forum.load("forum").getSubForums().get("subforum").getModerators().size());
        assertTrue(Forum.load("forum").getSubForums().get("subforum").getModerators().containsKey(user.getUsername()));
    }

    public void testRemoveModerator() throws TooManyModeratorsException {
        Forum.load("forum").getSubForums().get("subforum").setModerator(user);
        Forum.load("forum").getSubForums().get("subforum").removeModerator(user.getUsername());
        assertEquals(0, Forum.load("forum").getSubForums().get("subforum").getModerators().size());
    }

    public void testGetTitle()  {
        assertEquals(Forum.load("forum").getSubForums().get("subforum").getTitle(), "subforum");
    }

    public void testGetThreads() throws DoesNotComplyWithPolicyException {
        ThreadI thread = Forum.load("forum").getSubForums().get("subforum").addThread("user", "msg", "thread");
        assertEquals(Forum.load("forum").getSubForums().get("subforum").getThreads().size(), 1);
        assertTrue(Forum.load("forum").getSubForums().get("subforum").getThreads().containsKey(thread.getTitle()));
    }

    public void testEditMessage() throws DoesNotComplyWithPolicyException, MessageNotFoundException, PermissionDeniedException {
        ThreadI thread = null;
        thread = Forum.load("forum").getSubForums().get("subforum").addThread("Gabi", "title", "body");
        Forum.load("forum").getSubForums().get("subforum").editMessage(thread, thread.getRootMessage().getId(), "newBody", "newTitle", "Gabi");
        assertEquals("newTitle", thread.getRootMessage().getMessageTitle());
        assertEquals("newBody", thread.getRootMessage().getMessageText());
    }

    public void testSeveralThreads() throws DoesNotComplyWithPolicyException {
        List<ThreadI> threads = new LinkedList<>();
        for (int i=0; i < 10; i++) {
            System.out.println("*** adding title" + i);
            ThreadI thread = Forum.load("forum").getSubForums().get("subforum").addThread("user", "title"+ i, "body" );
            threads.add(thread);
        }
        for (int i=0; i < 10; i++) {
            assertTrue(Forum.load("forum").getSubForums().get("subforum").getThreads().containsKey(threads.get(i).getTitle()));
        }
        assertEquals(Forum.load("forum").getSubForums().get("subforum").getThreads().values().size(), 10);
    }
}