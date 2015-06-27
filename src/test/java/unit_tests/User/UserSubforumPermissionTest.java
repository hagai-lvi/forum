package unit_tests.User;

import main.User.Permissions;
import main.User.UserSubforumPermission;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.forum_contents.SubForum;
import main.interfaces.ForumPolicyI;
import main.interfaces.MessageI;
import main.interfaces.ThreadI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;
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
    public void setUp() throws SubForumAlreadyExistException {
        int maxModerators = 1;
        String regex = "a-b";
        policy = new ForumPolicy(false, maxModerators, regex, 365);
        forum = new Forum("Sport", policy);
        forum.addSubForum("Sport");
        permission = new UserSubforumPermission(Permissions.PERMISSIONS_USER,"Sport", "Sport");
        permission2 = new UserSubforumPermission(Permissions.PERMISSIONS_ADMIN,"Sport", "Sport");
        permission3 = new UserSubforumPermission(Permissions.PERMISSIONS_SUPERADMIN,"Sport", "Sport");
    }

    @After
    public void tearDown(){
        try {
            Forum.delete("Sport");
        } catch (ForumNotFoundException e) {

        }
    }

    @Test
    public void testCreateThread()   {
        try {
            permission2.createThread("Gabi", "Mega Flow1", "Flow1");
        } catch (PermissionDeniedException e) {
            fail("No permission to add thread");
        } catch (DoesNotComplyWithPolicyException e) {
            fail();
        }
        assertTrue(permission.getSubForum().getThreads().containsKey("Mega Flow1"));
    }

    @Test
    public void testReplyToMessage()   {
        MessageI message = null;
        try {
            message = permission2.createThread("Gabi", "Flow2", "body").getRootMessage();
        } catch (PermissionDeniedException e) {
            fail("No permission to add thread");
        } catch (DoesNotComplyWithPolicyException e) {
            fail();
        }
        try {
            permission2.replyToMessage(message, "Gabi", "Help3", "text");
        } catch (MessageNotFoundException e) {
            fail();
        } catch (DoesNotComplyWithPolicyException e) {
            fail();
        } catch (PermissionDeniedException e) {
            fail();
        }
        assertEquals(message.printSubTree(), "Flow2--> Help3");
    }

    @Test
    public void testDeleteMessageS() throws PermissionDeniedException, DoesNotComplyWithPolicyException, MessageNotFoundException {
        MessageI message = null;

        message = permission2.createThread("Gabi", "Flow222", "ddd").getRootMessage();
        permission2.replyToMessage(message, "Gabi", "Help", "ddddd");
        assertEquals(message.printSubTree(), "Flow222--> Help");
        permission.deleteMessage("Gabi", "Flow222", message);
        try {
            permission2.replyToMessage(message, "Gabi", "Flow222", "sadasdasd");
            fail();
        } catch (MessageNotFoundException e) {
            assertTrue(true);
        }
        try {
            permission2.deleteMessage("Gabi","Flow222" , message);
            fail();
        } catch (MessageNotFoundException e) {
            assertTrue(true);
        }
        try {
            ThreadI thread = Forum.load("Sport").getSubForums().get("Sport").getThreads().get(message.getMessageTitle());
            permission2.editMessage(thread, message.getId(), message.getMessageTitle(), message.getMessageText(),"Gabi" );
            fail();
        } catch (MessageNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteMessageWithoutPermission()   {
        MessageI message = null;

        try {
            message = permission2.createThread("Gabi", "title", "sdfsadsf").getRootMessage();
        } catch (PermissionDeniedException e) {
            fail();
        } catch (DoesNotComplyWithPolicyException e) {
            fail();
        }
        try {
            permission.deleteMessage("Victor", "title", message); // PermissionDeniedException expected
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        } catch (MessageNotFoundException e) {
            fail();
        }
    }

    @Test
    public void testGetThreads() throws PermissionDeniedException, DoesNotComplyWithPolicyException {
        permission.createThread("Gabi", "title2", "text");
        permission.createThread("Gabi", "title", "text");
        permission.createThread("Gabi", "title3", "text");
        permission.createThread("Gabi", "title4", "text");
        assertEquals(permission.getThreads().length, 4);
        assertEquals(permission.getThreads()[0].getTitle(), "title2");
        assertEquals(permission.getThreads()[1].getTitle(), "title3");
        assertEquals(permission.getThreads()[2].getTitle(), "title");
        assertEquals(permission.getThreads()[3].getTitle(), "title4");

    }

}