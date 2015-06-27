package acceptance_tests;

import data_structures.Tree;
import main.User.User;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.interfaces.ExThreadI;
import main.interfaces.FacadeI;
import main.interfaces.ForumI;
import main.interfaces.UserI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tests_infrastructure.Driver;

import java.util.ArrayList;

import static org.junit.Assert.*;






/**
 //     * UseCases:
 //     * 		1. Initialize
 //     *		2. CreateForum
 //     *		3. SetPolicies
 //     *		4. GuestEntry
 //     *		5. Register
 //     *		6. Login
 //     *		7. Logout
 //     *		8. CreateSubForum
 //     *		9. ViewSubForum
 //     *		10. PostThread
 //     *		11. PostMessage
 //     *		12. FriendType
 //     *		13. ComplainOnModerator
 //     *		14. EmailAuthentication
 //     *		15. RemoveMessage
 //     *		16. RemoveForum
 //     *      17. EditMessage
 //     *      18. SetModerator
 //     *      19. CancelModerator
 //     *      20. GetUpdatesFromModerator
 //     *      21. GetUpdatesFromSuperManager
 //     *      22. ViewSessions
 //     */

/**
 * Created by gabigiladov on 6/16/15.
 */
public class AcceptanceTest {
    private FacadeI driver;

    @Before
    public void setUp() throws PermissionDeniedException, ForumAlreadyExistException {
        driver = Driver.getBridge();
        driver.addForum("ADMIN", "ADMIN", "Flowww", false, "[1-9]*", 1, 10);

    }

    @After
    public void tearDown() throws ForumNotFoundException, PermissionDeniedException {
        driver.removeForum("ADMIN", "ADMIN", "Flowww");

    }

    @Test
    public void testInitialize() {
        driver.initialize();

        assertTrue(driver.getSessions().isEmpty());
    }

    @Test
    public void testGetForumList() throws PermissionDeniedException, ForumAlreadyExistException, ForumNotFoundException {
        driver.addForum("ADMIN", "ADMIN", "Flowww2", false, ".*", 1, 1);
        ArrayList<String> forums = driver.getForumList();
        assertTrue(forums != null);
        assertTrue(forums.contains("Flowww"));
        assertTrue(forums.contains("Flowww2"));
        assertFalse(forums.contains("Zrima"));
        driver.removeForum("ADMIN", "ADMIN", "Flowww2");
    }

    @Test
    public void testSetAdmin() throws CloneNotSupportedException, DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, UserNotFoundException, PermissionDeniedException, PasswordNotInEffectException, SessionNotFoundException, EmailNotAuthanticatedException, NeedMoreAuthParametersException {
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com");
        try {
            driver.setAdmin("ADMIN","ADMIN","Victor2","Flowww2");
            fail();
        } catch (UserNotFoundException e) {
            assertTrue(true);
        }
        try {
            driver.setAdmin("Victor","ADMIN","Victor","Flowww");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }

        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        int session = driver.login("Flowww", "Victor", "123456");
        assertFalse(driver.isAdmin(session));
        driver.setAdmin("ADMIN", "ADMIN", "Victor", "Flowww");
        assertTrue(driver.isAdmin(session));
        driver.logout(session);
    }

    @Test
    public void testAddForum() throws PermissionDeniedException, ForumAlreadyExistException, ForumNotFoundException {
        driver.addForum("ADMIN", "ADMIN", "Sport", false, ".*", 1, 10);
        assertTrue(isForumExist("Sport"));
        driver.removeForum("ADMIN", "ADMIN", "Sport");
        assertFalse(isForumExist("Sport"));

        try {
            driver.addForum("Gabi", "as00", "Sport", false, ".*", 1, 10);
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        assertFalse(isForumExist("Sport"));
    }

    @Test
    public void testRegister() throws UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, DoesNotComplyWithPolicyException {
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com");
        UserI user = User.getUserFromDB("Victor", "Flowww");
        assertTrue(user != null);
        try {
            driver.register("Flowww", "Victor", "123456", "aa@gmail.com");
            fail();
        } catch (UserAlreadyExistsException e) {
            assertTrue(true);
        }
        try {
            driver.register("Flowww", "Yosi", "a123456", "aa@gmail.com" );
            fail();
        } catch (DoesNotComplyWithPolicyException e) {
            assertTrue(true);
        }
        try {
            driver.register("Flowww2", "Shmuel", "123456", "aa@gmail.com" );
            fail();
        } catch (ForumNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testLogin() throws SessionNotFoundException, ForumNotFoundException, PasswordNotInEffectException, NeedMoreAuthParametersException, EmailNotAuthanticatedException, UserAlreadyExistsException, InvalidUserCredentialsException, DoesNotComplyWithPolicyException, UserNotFoundException {
        try {
            driver.login("Flowww", "Victor", "123456");
            fail();
        } catch (InvalidUserCredentialsException e) {
            assertTrue(true);
        }
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com");
        try {
            driver.login("Flowww2", "Victor", "123456");
            fail();
        } catch (ForumNotFoundException e) {
            assertTrue(true);
        }
        try {
            driver.login("Flowww", "Victor2", "123456");
            fail();
        } catch (InvalidUserCredentialsException e) {
            assertTrue(true);
        }
        try {
            driver.login("Flowww", "Victor", "123456a");
            fail();
        } catch (InvalidUserCredentialsException e) {
            assertTrue(true);
        }
        try {
            driver.login("Flowww", "Victor", "123456" );
            fail();
        } catch (EmailNotAuthanticatedException e) {
            assertTrue(true);
        }
        try {
            driver.authenticateUser("Flowww", "Victor2", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
            fail();
        } catch (UserNotFoundException e) {
            assertTrue(true);
        }

        try {
            driver.authenticateUser("Flowww", "Victor", "wrongString");
            fail();
        } catch (EmailNotAuthanticatedException e) {
            assertTrue(true);
        }
        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        int session = driver.login("Flowww", "Victor", "123456" );
        driver.logout(session);
    }

    @Test
    public void testLogout() throws UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, DoesNotComplyWithPolicyException, UserNotFoundException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException, SessionNotFoundException {
        try {
            driver.logout(1000);
            fail();
        } catch (SessionNotFoundException e) {
            assertTrue(true);
        }
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com");
        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        driver.logout(driver.login("Flowww", "Victor", "123456"));
    }

    @Test
    public void testAddReply() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, EmailNotAuthanticatedException, UserNotFoundException, PermissionDeniedException, CloneNotSupportedException, PasswordNotInEffectException, NeedMoreAuthParametersException, SubForumAlreadyExistException, SessionNotFoundException, SubForumDoesNotExistException, MessageNotFoundException, ThreadNotFoundException {
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com");
        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        driver.setAdmin("ADMIN", "ADMIN", "Victor", "Flowww");
        int sessionId = driver.login("Flowww", "Victor", "123456");
        driver.addSubforum(sessionId, "sub");
        int tid = driver.addThread(sessionId, "thread", "text");
        try {
            driver.addReply(sessionId+1, tid, "reply", "text");
            fail();
        }  catch (SessionNotFoundException e) {
            assertTrue(true);
        }
        try {
            driver.addReply(sessionId, tid+1, "reply", "text");
            fail();
        } catch (MessageNotFoundException e) {
            assertTrue(true);
        }
        int id = driver.addReply(sessionId, tid, "reply", "text");
        int ids= driver.addReply(sessionId, id, "more", "body");
        int idt = driver.addReply(sessionId, tid, "aaa","asss");
        ExThreadI t =  driver.viewThread(sessionId, "thread");
        assertTrue(t.getTitle().equals("thread"));
        assertTrue(t.getMessages().getRoot().getData().getMessageText().equals("text"));
        assertEquals(t.getMessages().find(id).getMessageTitle(), "reply");
        assertEquals(t.getMessages().find(id).getMessageText(), "text");
        assertEquals(t.getMessages().find(idt).getMessageTitle(), "aaa");
        assertEquals(t.getMessages().find(idt).getMessageText(), "asss");
        assertEquals(t.getMessages().find(ids).getMessageTitle(), "more");
        assertEquals(t.getMessages().find(ids).getMessageText(), "body");
        driver.logout(sessionId);
    }

    @Test
    public void testCreateNewThread() throws PermissionDeniedException, ForumAlreadyExistException, PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SubForumAlreadyExistException, SessionNotFoundException, SubForumDoesNotExistException, SubForumNotFoundException, DoesNotComplyWithPolicyException, UserAlreadyExistsException, UserNotFoundException, CloneNotSupportedException, ThreadNotFoundException {
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com");
        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        driver.setAdmin("ADMIN", "ADMIN", "Victor", "Flowww");
        int sessionId = driver.login("Flowww", "Victor", "123456");
        driver.addSubforum(sessionId, "sub");
        driver.addSubforum(sessionId, "sub2");
        driver.addThread(sessionId, "thread", "text");
        driver.addThread(sessionId, "thread", "text2");
        ExThreadI ex = driver.viewThread(sessionId, "thread");
        assertEquals(ex.getTitle(), "thread");
        assertEquals(ex.getMessages().getRoot().getData().getMessageText(), "text2");
        driver.logout(sessionId);

    }

    @Test
    public void testAddSubforum() throws UserNotFoundException, PermissionDeniedException, ForumNotFoundException, CloneNotSupportedException, EmailNotAuthanticatedException, UserAlreadyExistsException, InvalidUserCredentialsException, DoesNotComplyWithPolicyException, PasswordNotInEffectException, NeedMoreAuthParametersException, SubForumAlreadyExistException, SessionNotFoundException, SubForumDoesNotExistException {
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com" );
        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        int sessionId = driver.login("Flowww", "Victor", "123456");
        try {
            driver.addSubforum(sessionId, "sub");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }

        try {
            driver.addSubforum(sessionId, "sub2");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        driver.setAdmin("ADMIN", "ADMIN", "Victor", "Flowww");
        driver.addSubforum(sessionId, "sub3");
        driver.logout(sessionId);

    }

    @Test
    public void testDeleteMessage() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, EmailNotAuthanticatedException, UserNotFoundException, PasswordNotInEffectException, NeedMoreAuthParametersException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, MessageNotFoundException, ThreadNotFoundException, SubForumNotFoundException, CloneNotSupportedException, TooManyModeratorsException {
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com");
        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        int sessionId = driver.login("Flowww", "ADMIN", "ADMIN" );
        driver.addSubforum(sessionId, "sub");
        driver.addSubforum(sessionId, "sub2");
        int id = driver.addThread(sessionId, "thread", "text");
        int sessionId2 = driver.login("Flowww", "Victor", "123456" );
        try {
            driver.viewSubforum(sessionId2, "sub2");
            driver.viewThread(sessionId2, "thread");
            driver.deleteMessage(sessionId2, id);
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        driver.setModerator(sessionId, "Victor");
        driver.deleteMessage(sessionId2, id);
        driver.logout(sessionId);
        driver.logout(sessionId2);

    }

    @Test
    public void testSetModerator() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, EmailNotAuthanticatedException, UserNotFoundException, PasswordNotInEffectException, NeedMoreAuthParametersException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, CloneNotSupportedException, SubForumNotFoundException, MessageNotFoundException, ThreadNotFoundException, TooManyModeratorsException {
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com");
        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        int sessionId = driver.login("Flowww", "ADMIN", "ADMIN");
        driver.addSubforum(sessionId, "sub");
        driver.addSubforum(sessionId, "sub2");
        int id = driver.addThread(sessionId, "thread", "text");
        int sessionId2 = driver.login("Flowww", "Victor", "123456");
        try {
            driver.viewSubforum(sessionId2, "sub2");
            driver.viewThread(sessionId2, "thread");
            driver.deleteMessage(sessionId2, id);
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        driver.setModerator(sessionId, "Victor");
        driver.deleteMessage(sessionId2, id);
        driver.logout(sessionId);
        driver.logout(sessionId2);

    }

    @Test
    public void testGuestEntry() throws ForumNotFoundException, SessionNotFoundException {
        int s = driver.guestEntry("Flowww");
        driver.logout(s);
    }

    @Test
    public void testRemoveForum() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, EmailNotAuthanticatedException, UserNotFoundException, PasswordNotInEffectException, NeedMoreAuthParametersException, PermissionDeniedException, ForumAlreadyExistException, SessionNotFoundException {
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com" );
        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        int sessionId = driver.login("Flowww", "Victor", "123456");
        try {
            driver.addForum("ADMIN", "ADMIN", "Flowww2", false, ".*", 1, 1);
            driver.removeForum("Victor", "123456", "Flowww2");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        driver.removeForum("ADMIN", "ADMIN", "Flowww2");
        driver.logout(sessionId);
    }

    @Test
    public void testEditMessage() throws SessionNotFoundException, SubForumDoesNotExistException, MessageNotFoundException, ThreadNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException, SubForumNotFoundException, PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SubForumAlreadyExistException, UserNotFoundException, UserAlreadyExistsException {
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com");
        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        int sessionId = driver.login("Flowww", "ADMIN", "ADMIN");
        driver.addSubforum(sessionId, "sub");
        driver.addSubforum(sessionId, "sub2");
        int id = driver.addThread(sessionId, "thread", "text");
        int sessionId2 = driver.login("Flowww", "Victor", "123456");
        try {
            driver.viewSubforum(sessionId2, "sub2");
            driver.viewThread(sessionId2, "thread");
            driver.editMessage(sessionId2, id, "edit", "edittext");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        driver.editMessage(sessionId, id, "edit", "edittext");
        driver.logout(sessionId);
        driver.logout(sessionId2);

    }

    @Test
    public void testRemoveModerator() throws UserNotFoundException, SessionNotFoundException, SubForumDoesNotExistException, PermissionDeniedException, ForumNotFoundException, CloneNotSupportedException, DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException, SubForumAlreadyExistException, SubForumNotFoundException, TooManyModeratorsException {
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com");
        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        int sessionId = driver.login("Flowww", "ADMIN", "ADMIN");
        driver.addSubforum(sessionId, "sub");
        driver.addSubforum(sessionId, "sub2");
        int sessionId2 = driver.login("Flowww", "Victor", "123456");
        driver.viewSubforum(sessionId2, "sub2");
        try {
            driver.removeModerator(sessionId2, "Victor");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        driver.setModerator(sessionId, "Victor");
        driver.removeModerator(sessionId, "Victor");
        driver.logout(sessionId);
        driver.logout(sessionId2);

    }

    @Test
    public void testViewSuperManagerStatistics() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, PermissionDeniedException, ForumAlreadyExistException, SubForumDoesNotExistException, NeedMoreAuthParametersException, PasswordNotInEffectException, EmailNotAuthanticatedException, SubForumAlreadyExistException, SessionNotFoundException {
        driver.addForum("ADMIN", "ADMIN", "Forum-1", false, ".*", 1, 2);
        driver.addForum("ADMIN", "ADMIN", "Forum-2", false, ".*", 1, 2);
        driver.addForum("ADMIN", "ADMIN", "Forum-3", false, ".*", 2, 2);
        int s = driver.login("Flowww", "ADMIN", "ADMIN" );
        driver.addSubforum(s, "Sub-1");
        driver.addSubforum(s, "Sub-2");
        int s2 = driver.login("Forum-1", "ADMIN", "ADMIN" );
        driver.addSubforum(s2, "Sub-3");
        driver.addSubforum(s2, "Sub-4");
        int s3 = driver.login("Forum-2", "ADMIN", "ADMIN");
        driver.addSubforum(s3, "Sub-5");
        driver.addSubforum(s3, "Sub-6");
        int s4 = driver.login("Forum-3", "ADMIN", "ADMIN" );
        driver.addSubforum(s4, "Sub-7");
        driver.addSubforum(s4, "Sub-8");

        driver.register("Flowww", "Victor", "123456", "aa@gmail.com" );
        driver.register("Flowww", "Gabi", "123456", "aa@gmail.com");
        driver.register("Forum-1", "Gabi", "123456", "aa@gmail.com");
        driver.register("Forum-2", "Tom", "123456", "aa@gmail.com" );
        driver.register("Forum-3", "Yosi", "123456", "aa@gmail.com" );
        driver.register("Forum-3", "Gabi", "123456", "aa@gmail.com" );

        try {
            driver.viewSuperManagerStatistics("Victor", "123456");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        System.out.println("\n\n\n\n" + driver.viewSuperManagerStatistics("ADMIN", "ADMIN") + "\n\n\n\n");
        driver.removeForum("ADMIN", "ADMIN", "Forum-1");
        driver.removeForum("ADMIN", "ADMIN", "Forum-2");
        driver.removeForum("ADMIN", "ADMIN", "Forum-3");
        driver.logout(s);
        driver.logout(s2);
        driver.logout(s3);
        driver.logout(s4);

    }

    @Test
    public void testGetMessageList() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, DoesNotComplyWithPolicyException, ThreadNotFoundException, MessageNotFoundException {
        int sessionId = driver.login("Flowww", "ADMIN", "ADMIN" );
        driver.addSubforum(sessionId, "sub2");
        int id =driver.addThread(sessionId, "thread", "text");
        driver.addReply(sessionId, id, "title", "text");
        id= driver.addReply(sessionId, id, "title2", "text");
        driver.addReply(sessionId, id, "title3", "text");
        driver.addReply(sessionId, id, "title4", "text");
        Tree t = driver.getMessageList(sessionId);
        assertEquals(t.getRoot().getData().getMessageTitle(), "thread");
        driver.logout(sessionId);

    }

    @Test
    public void getCurrentUserStatus() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SubForumDoesNotExistException, SessionNotFoundException, SubForumAlreadyExistException, PermissionDeniedException, UserAlreadyExistsException, DoesNotComplyWithPolicyException, UserNotFoundException, CloneNotSupportedException, SubForumNotFoundException, TooManyModeratorsException {
        int sessionId = driver.login("Flowww", "ADMIN", "ADMIN");
        driver.addSubforum(sessionId, "Zrima");
        assertEquals(driver.getCurrentUserForumStatus(sessionId), "Admin");
        assertEquals(driver.getCurrentUserSubForumStatus(sessionId), "Moderator");
        driver.register("Flowww", "Victor", "123456", "aa@gmail.com" );
        driver.authenticateUser("Flowww", "Victor", User.getUserFromDB("Victor", "Flowww").getUserAuthString());
        int sessionId2 = driver.login("Flowww", "Victor", "123456");
        driver.viewSubforum(sessionId2, "Zrima");
        assertEquals(driver.getCurrentUserForumStatus(sessionId2), "User");
        assertEquals(driver.getCurrentUserSubForumStatus(sessionId2), "User");
        driver.setAdmin("ADMIN", "ADMIN", "Victor", "Flowww");
        assertEquals(driver.getCurrentUserForumStatus(sessionId2), "Admin");
        assertEquals(driver.getCurrentUserSubForumStatus(sessionId2), "Moderator");
        driver.logout(sessionId);

    }

    private boolean isForumExist(String forumName) {
        ForumI forum = Forum.load(forumName);
        return (forum != null);
    }

}