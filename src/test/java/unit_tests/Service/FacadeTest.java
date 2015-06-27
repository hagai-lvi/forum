package unit_tests.Service;


import data_structures.Tree;
import main.User.User;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.interfaces.*;
import main.services_layer.Facade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Created by gabigiladov on 6/16/15.
 */
public class FacadeTest {
    private FacadeI theFacade;

    @Before
    public void setUp() {
        theFacade = Facade.getFacade();

        try {
            theFacade.addForum("ADMIN", "ADMIN", "Temp", false, "[1-9]*", 1, 10);

        } catch (PermissionDeniedException e) {
            fail("No permission to add forum");
        } catch (ForumAlreadyExistException e) {
            fail("Forum already exist");
        }

    }

    @After
    public void tearDown() {
        try {
            theFacade.removeForum("ADMIN", "ADMIN", "Temp");

        } catch (ForumNotFoundException e) {
            fail("Forum not found when trying to remove");
        } catch (PermissionDeniedException e) {
            fail("No permission to remove forum");
        }
    }

    @Test
    public void testInitialize() {
        theFacade.initialize();
        assertTrue(theFacade.getSessions().isEmpty());
    }

    @Test
    public void testGetForumList() throws PermissionDeniedException, ForumAlreadyExistException, ForumNotFoundException {
        theFacade.addForum("ADMIN", "ADMIN", "Temp2", false, ".*", 1, 1);
        ArrayList<String> forums = theFacade.getForumList();
        assertTrue(forums != null);
        assertTrue(forums.contains("Temp"));
        assertTrue(forums.contains("Temp2"));
        assertFalse(forums.contains("Zrima"));
        theFacade.removeForum("ADMIN", "ADMIN", "Temp2");
    }

    @Test
    public void testSetAdmin() throws CloneNotSupportedException, DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, UserNotFoundException, PermissionDeniedException, PasswordNotInEffectException, SessionNotFoundException, EmailNotAuthanticatedException, NeedMoreAuthParametersException {

        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        try {
            theFacade.setAdmin("ADMIN","ADMIN","Victor2","Temp2");
            fail();
        } catch (UserNotFoundException e) {
            assertTrue(true);
        }

        try {
            theFacade.setAdmin("Victor","ADMIN","Victor","Temp");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }

        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int session = theFacade.login("Temp", "Victor", "123456");
        assertFalse(theFacade.isAdmin(session));
        theFacade.setAdmin("ADMIN", "ADMIN", "Victor", "Temp");
        assertTrue(theFacade.isAdmin(session));
        theFacade.logout(session);

    }

    @Test
    public void testGetSubForumList() throws InvalidUserCredentialsException, PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, EmailNotAuthanticatedException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, UserAlreadyExistsException, DoesNotComplyWithPolicyException, UserNotFoundException, CloneNotSupportedException {

        int session = 0;
        session = theFacade.login("Temp", "ADMIN", "ADMIN");
        theFacade.addSubforum(session, "Sport");
        theFacade.addSubforum(session, "SomeSub");
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
       int session2 =  theFacade.login("Temp", "Victor", "123456");
        theFacade.setAdmin("ADMIN", "ADMIN", "Victor", "Temp");
        theFacade.addSubforum(session2, "OneMore");
        Map<String, SubForumI> list = theFacade.getSubForumList(session2);
        assertTrue(list.containsKey("Sport"));
        assertTrue(list.containsKey("SomeSub"));
        assertTrue(list.containsKey("OneMore"));
        assertFalse(list.containsKey("Zrima"));
        theFacade.logout(session);
        theFacade.logout(session2);

    }

    @Test
    public void testAddForum() {
        try {
            theFacade.addForum("ADMIN", "ADMIN", "Sport", false, ".*", 1, 10);
        } catch (PermissionDeniedException e) {
            fail("No permission to add forum");
        } catch (ForumAlreadyExistException e) {
            fail("Forum already exist");
        }

        assertTrue(isForumExist("Sport"));


        try {
            theFacade.removeForum("ADMIN", "ADMIN", "Sport");
        } catch (ForumNotFoundException e) {
            fail("Forum not found when trying to remove");
        } catch (PermissionDeniedException e) {
            fail("No permission to remove forum");
        }
        assertFalse(isForumExist("Sport"));

        try {
            theFacade.addForum("Gabi", "as00", "Sport", false, ".*", 1, 10);
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        } catch (ForumAlreadyExistException e) {
            fail("Forum already exist");
        }
        assertFalse(isForumExist("Sport"));
    }

    @Test
    public void testRegister(){
        try {
            theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        } catch (UserAlreadyExistsException e) {
            fail("User already exist");
        } catch (InvalidUserCredentialsException e) {
           fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (DoesNotComplyWithPolicyException e) {
            fail("Password does not comply with policy");
        }
        UserI user = User.getUserFromDB("Victor", "Temp");
        assertTrue(user != null);


        try {
            theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
            fail();
        } catch (UserAlreadyExistsException e) {
            assertTrue(true);
        } catch (InvalidUserCredentialsException e) {
            fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (DoesNotComplyWithPolicyException e) {
            fail("Password does not comply with policy");
        }


        try {
            theFacade.register("Temp", "Yosi", "a123456", "aa@gmail.com" );
            fail();
        } catch (UserAlreadyExistsException e) {
            fail("User already exist");
        } catch (InvalidUserCredentialsException e) {
            fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (DoesNotComplyWithPolicyException e) {
            assertTrue(true);
        }


        try {
            theFacade.register("Temp2", "Shmuel", "123456", "aa@gmail.com" );
            fail();
        } catch (UserAlreadyExistsException e) {
            fail("User already exist");
        } catch (InvalidUserCredentialsException e) {
            fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            assertTrue(true);
        } catch (DoesNotComplyWithPolicyException e) {
            fail("Password does not comply with policy");
        }
    }

    @Test
    public void testLogin() throws SessionNotFoundException {

        try {
            theFacade.login("Temp", "Victor", "123456");
            fail();
        } catch (InvalidUserCredentialsException e) {
            assertTrue(true);
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (PasswordNotInEffectException e) {
            fail("Password Not In Effect");
        } catch (NeedMoreAuthParametersException e) {
            fail("Need More Authentication Parameters");
        } catch (EmailNotAuthanticatedException e) {
            fail("Email is not authenticated");
        }

        try {
            theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        } catch (UserAlreadyExistsException e) {
            fail("User already exist");
        } catch (InvalidUserCredentialsException e) {
            fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (DoesNotComplyWithPolicyException e) {
            fail("Password does not comply with policy");
        }

        try {
            theFacade.login("Temp2", "Victor", "123456");
            fail();
        } catch (InvalidUserCredentialsException e) {
            fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            assertTrue(true);
        } catch (PasswordNotInEffectException e) {
            fail("Password Not In Effect");
        } catch (NeedMoreAuthParametersException e) {
            fail("Need More Authentication Parameters");
        } catch (EmailNotAuthanticatedException e) {
            fail("Email is not authenticated");
        }

        try {
            theFacade.login("Temp", "Victor2", "123456");
            fail();
        } catch (InvalidUserCredentialsException e) {
            assertTrue(true);
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (PasswordNotInEffectException e) {
            fail("Password Not In Effect");
        } catch (NeedMoreAuthParametersException e) {
            fail("Need More Authentication Parameters");
        } catch (EmailNotAuthanticatedException e) {
            fail("Email is not authenticated");
        }

        try {
            theFacade.login("Temp", "Victor", "123456a");
            fail();
        } catch (InvalidUserCredentialsException e) {
            assertTrue(true);
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (PasswordNotInEffectException e) {
            fail("Password Not In Effect");
        } catch (NeedMoreAuthParametersException e) {
            fail("Need More Authentication Parameters");
        } catch (EmailNotAuthanticatedException e) {
            fail("Email is not authenticated");
        }

        try {
            theFacade.login("Temp", "Victor", "123456" );
            fail();
        } catch (InvalidUserCredentialsException e) {
            fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (PasswordNotInEffectException e) {
            fail("Password Not In Effect");
        } catch (NeedMoreAuthParametersException e) {
            fail("Need More Authentication Parameters");
        } catch (EmailNotAuthanticatedException e) {
            assertTrue(true);
        }


        try {
            theFacade.authenticateUser("Temp", "Victor2", User.getUserFromDB("Victor", "Temp").getUserAuthString());
            fail();
        } catch (UserNotFoundException e) {
            assertTrue(true);
        } catch (EmailNotAuthanticatedException e) {
            fail("Wrong auth string");
        }

        try {
            theFacade.authenticateUser("Temp", "Victor", "wrongString");
            fail();
        } catch (UserNotFoundException e) {
            fail("User not found");
        } catch (EmailNotAuthanticatedException e) {
            assertTrue(true);
        }

        try {
            theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        } catch (UserNotFoundException e) {
            fail("User not found");
        } catch (EmailNotAuthanticatedException e) {
            fail("Wrong auth string");
        }

        try {
            int session = theFacade.login("Temp", "Victor", "123456" );
            theFacade.logout(session);
        } catch (InvalidUserCredentialsException e) {
            fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (PasswordNotInEffectException e) {
            fail("Password Not In Effect");
        } catch (NeedMoreAuthParametersException e) {
            fail("Need More Authentication Parameters");
        } catch (EmailNotAuthanticatedException e) {
            fail("Email is not authenticated");
        }

    }

    @Test
    public void testLogout()  {
        try {
            theFacade.logout(1000);
            fail();
        } catch (SessionNotFoundException e) {
            assertTrue(true);
        }

        try {
            theFacade.register("Temp", "Victor", "123456", "aa@gmail.com" );
        } catch (UserAlreadyExistsException e) {
            fail("User already exist");
        } catch (InvalidUserCredentialsException e) {
            fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (DoesNotComplyWithPolicyException e) {
            fail("Password does not comply with policy");
        }

        try {
            theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        } catch (UserNotFoundException e) {
            fail("User not found");
        } catch (EmailNotAuthanticatedException e) {
            fail("Wrong auth string");
        }

        try {
            theFacade.logout(theFacade.login("Temp", "Victor", "123456"));
        } catch (InvalidUserCredentialsException e) {
            fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            fail("Forum not exist");
        } catch (PasswordNotInEffectException e) {
            fail("Password Not In Effect");
        } catch (NeedMoreAuthParametersException e) {
            fail("Need More Authentication Parameters");
        } catch (EmailNotAuthanticatedException e) {
            fail("Email is not authenticated");
        } catch (SessionNotFoundException e) {
            fail("Session not found");
        }
    }

    @Test
    public void testAddReply() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, EmailNotAuthanticatedException, UserNotFoundException, PermissionDeniedException, CloneNotSupportedException, PasswordNotInEffectException, NeedMoreAuthParametersException, SubForumAlreadyExistException, SessionNotFoundException, SubForumDoesNotExistException, MessageNotFoundException, ThreadNotFoundException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        theFacade.setAdmin("ADMIN", "ADMIN", "Victor", "Temp");
        int sessionId = theFacade.login("Temp", "Victor", "123456");
        theFacade.addSubforum(sessionId, "sub");
        int tid = theFacade.addThread(sessionId, "thread", "text");

        try {
            theFacade.addReply(sessionId+1, tid, "reply", "text");
            fail();
        }  catch (SessionNotFoundException e) {
            assertTrue(true);
        }

        try {
            theFacade.addReply(sessionId, tid+1, "reply", "text");
            fail();
        } catch (MessageNotFoundException e) {
            assertTrue(true);
        }
        int id = theFacade.addReply(sessionId, tid, "reply", "text");
        int ids= theFacade.addReply(sessionId, id, "more", "body");
        int idt = theFacade.addReply(sessionId, tid, "aaa","asss");
        ExThreadI t =  theFacade.viewThread(sessionId, "thread");

        assertTrue(t.getTitle().equals("thread"));
        assertTrue(t.getMessages().getRoot().getData().getMessageText().equals("text"));
        assertEquals(t.getMessages().find(id).getMessageTitle(), "reply");
        assertEquals(t.getMessages().find(id).getMessageText(), "text");
        assertEquals(t.getMessages().find(idt).getMessageTitle(), "aaa");
        assertEquals(t.getMessages().find(idt).getMessageText(), "asss");
        assertEquals(t.getMessages().find(ids).getMessageTitle(), "more");
        assertEquals(t.getMessages().find(ids).getMessageText(), "body");
        theFacade.logout(sessionId);

    }

    @Test
    public void testCreateNewThread() throws PermissionDeniedException, ForumAlreadyExistException, PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SubForumAlreadyExistException, SessionNotFoundException, SubForumDoesNotExistException, SubForumNotFoundException, DoesNotComplyWithPolicyException, UserAlreadyExistsException, UserNotFoundException, CloneNotSupportedException, ThreadNotFoundException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        theFacade.setAdmin("ADMIN", "ADMIN", "Victor", "Temp");
        int sessionId = theFacade.login("Temp", "Victor", "123456");
        theFacade.addSubforum(sessionId, "sub");
        theFacade.addSubforum(sessionId, "sub2");
        theFacade.addThread(sessionId, "thread", "text");
        theFacade.addThread(sessionId, "thread", "text2");
        ExThreadI ex = theFacade.viewThread(sessionId, "thread");
        assertEquals(ex.getTitle(), "thread");
        assertEquals(ex.getMessages().getRoot().getData().getMessageText(), "text2");
        theFacade.logout(sessionId);

    }

    @Test
    public void testAddSubforum() throws UserNotFoundException, PermissionDeniedException, ForumNotFoundException, CloneNotSupportedException, EmailNotAuthanticatedException, UserAlreadyExistsException, InvalidUserCredentialsException, DoesNotComplyWithPolicyException, PasswordNotInEffectException, NeedMoreAuthParametersException, SubForumAlreadyExistException, SessionNotFoundException, SubForumDoesNotExistException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com" );
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int sessionId = theFacade.login("Temp", "Victor", "123456");
        try {
            theFacade.addSubforum(sessionId, "sub");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }

        try {
            theFacade.addSubforum(sessionId, "sub2");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        theFacade.setAdmin("ADMIN", "ADMIN", "Victor", "Temp");
        theFacade.addSubforum(sessionId, "sub3");
        theFacade.logout(sessionId);

    }

    @Test
    public void testGetUserAuthString()  {
        try {
            theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        } catch (UserAlreadyExistsException e) {
            fail("User already exist");
        } catch (InvalidUserCredentialsException e) {
            fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (DoesNotComplyWithPolicyException e) {
            fail("Password does not comply with policy");
        }

        try {
            String auth = theFacade.getUserAuthString("Temp", "Victor", "123456");
            theFacade.authenticateUser("Temp", "Victor", auth);
        } catch (InvalidUserCredentialsException e) {
            fail();
        } catch (UserNotFoundException e) {
            fail();
        } catch (EmailNotAuthanticatedException e) {
            fail();
        }
    }

    @Test
    public void isMessageFromCurrentUser() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, EmailNotAuthanticatedException, UserNotFoundException, PasswordNotInEffectException, NeedMoreAuthParametersException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, MessageNotFoundException, ThreadNotFoundException, SubForumNotFoundException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN");
        theFacade.addSubforum(sessionId, "sub");
        int id =  theFacade.addThread(sessionId, "zrima", "text");
        theFacade.addReply(sessionId, id, "reply", "text");
        assertTrue(theFacade.isMessageFromCurrentUser(sessionId, id));
        int session = theFacade.login("Temp", "Victor", "123456");
        theFacade.viewSubforum(session, "sub");
        theFacade.viewThread(session, "zrima");
        assertFalse(theFacade.isMessageFromCurrentUser(session, id));
        id = theFacade.addReply(session, id, "reply2", "text");
        assertTrue(theFacade.isMessageFromCurrentUser(session, id));
        theFacade.logout(sessionId);
        theFacade.logout(session);

    }

    @Test
    public void testDeleteMessage() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, EmailNotAuthanticatedException, UserNotFoundException, PasswordNotInEffectException, NeedMoreAuthParametersException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, MessageNotFoundException, ThreadNotFoundException, SubForumNotFoundException, CloneNotSupportedException, TooManyModeratorsException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN" );
        theFacade.addSubforum(sessionId, "sub");
        theFacade.addSubforum(sessionId, "sub2");
        int id = theFacade.addThread(sessionId, "thread", "text");
        int sessionId2 = theFacade.login("Temp", "Victor", "123456" );
        try {
            theFacade.viewSubforum(sessionId2, "sub2");
            theFacade.viewThread(sessionId2, "thread");
            theFacade.deleteMessage(sessionId2, id);
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        theFacade.setModerator(sessionId, "Victor");
        theFacade.deleteMessage(sessionId2, id);
        theFacade.logout(sessionId);
        theFacade.logout(sessionId2);

    }

    @Test
    public void testSetModerator() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, EmailNotAuthanticatedException, UserNotFoundException, PasswordNotInEffectException, NeedMoreAuthParametersException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, CloneNotSupportedException, SubForumNotFoundException, MessageNotFoundException, ThreadNotFoundException, TooManyModeratorsException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN");
        theFacade.addSubforum(sessionId, "sub");
        theFacade.addSubforum(sessionId, "sub2");
        int id = theFacade.addThread(sessionId, "thread", "text");
        int sessionId2 = theFacade.login("Temp", "Victor", "123456");
        try {
            theFacade.viewSubforum(sessionId2, "sub2");
            theFacade.viewThread(sessionId2, "thread");
            theFacade.deleteMessage(sessionId2, id);
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        theFacade.setModerator(sessionId, "Victor");
        theFacade.deleteMessage(sessionId2, id);
        theFacade.logout(sessionId);
        theFacade.logout(sessionId2);

    }

    @Test
    public void testGuestEntry() throws ForumNotFoundException, SessionNotFoundException {
        int s = theFacade.guestEntry("Temp");
        theFacade.logout(s);
    }

    @Test
    public void testRemoveForum() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, EmailNotAuthanticatedException, UserNotFoundException, PasswordNotInEffectException, NeedMoreAuthParametersException, PermissionDeniedException, ForumAlreadyExistException, SessionNotFoundException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com" );
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int sessionId = theFacade.login("Temp", "Victor", "123456");
        try {
            theFacade.addForum("ADMIN", "ADMIN", "Temp2", false, ".*", 1, 1);
            theFacade.removeForum("Victor", "123456", "Temp2");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        theFacade.removeForum("ADMIN", "ADMIN", "Temp2");
        theFacade.logout(sessionId);

    }


    @Test
    public void testEditMessage() throws SessionNotFoundException, SubForumDoesNotExistException, MessageNotFoundException, ThreadNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException, SubForumNotFoundException, PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SubForumAlreadyExistException, UserNotFoundException, UserAlreadyExistsException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN");
        theFacade.addSubforum(sessionId, "sub");
        theFacade.addSubforum(sessionId, "sub2");
        int id = theFacade.addThread(sessionId, "thread", "text");
        int sessionId2 = theFacade.login("Temp", "Victor", "123456");
        try {
            theFacade.viewSubforum(sessionId2, "sub2");
            theFacade.viewThread(sessionId2, "thread");
            theFacade.editMessage(sessionId2, id, "edit", "edittext");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        theFacade.editMessage(sessionId, id, "edit", "edittext");
        theFacade.logout(sessionId);
        theFacade.logout(sessionId2);

    }

    @Test
    public void testRemoveModerator() throws UserNotFoundException, SessionNotFoundException, SubForumDoesNotExistException, PermissionDeniedException, ForumNotFoundException, CloneNotSupportedException, DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException, SubForumAlreadyExistException, SubForumNotFoundException, TooManyModeratorsException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN");
        theFacade.addSubforum(sessionId, "sub");
        theFacade.addSubforum(sessionId, "sub2");
        int sessionId2 = theFacade.login("Temp", "Victor", "123456");
        theFacade.viewSubforum(sessionId2, "sub2");
        try {
            theFacade.removeModerator(sessionId2, "Victor");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        theFacade.setModerator(sessionId, "Victor");
        theFacade.removeModerator(sessionId, "Victor");
        theFacade.logout(sessionId);
        theFacade.logout(sessionId2);

    }

    @Test
    public void testViewSuperManagerStatistics() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, PermissionDeniedException, ForumAlreadyExistException, SubForumDoesNotExistException, NeedMoreAuthParametersException, PasswordNotInEffectException, EmailNotAuthanticatedException, SubForumAlreadyExistException, SessionNotFoundException {
        theFacade.addForum("ADMIN", "ADMIN", "Forum-1", false, ".*", 1, 2);
        theFacade.addForum("ADMIN", "ADMIN", "Forum-2", false, ".*", 1, 2);
        theFacade.addForum("ADMIN", "ADMIN", "Forum-3", false, ".*", 2, 2);
        int s = theFacade.login("Temp", "ADMIN", "ADMIN" );
        theFacade.addSubforum(s, "Sub-1");
        theFacade.addSubforum(s, "Sub-2");
        int s2 = theFacade.login("Forum-1", "ADMIN", "ADMIN" );
        theFacade.addSubforum(s2, "Sub-3");
        theFacade.addSubforum(s2, "Sub-4");
        int s3 = theFacade.login("Forum-2", "ADMIN", "ADMIN");
        theFacade.addSubforum(s3, "Sub-5");
        theFacade.addSubforum(s3, "Sub-6");
        int s4 = theFacade.login("Forum-3", "ADMIN", "ADMIN" );
        theFacade.addSubforum(s4, "Sub-7");
        theFacade.addSubforum(s4, "Sub-8");

        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com" );
        theFacade.register("Temp", "Gabi", "123456", "aa@gmail.com");
        theFacade.register("Forum-1", "Gabi", "123456", "aa@gmail.com");
        theFacade.register("Forum-2", "Tom", "123456", "aa@gmail.com" );
        theFacade.register("Forum-3", "Yosi", "123456", "aa@gmail.com" );
        theFacade.register("Forum-3", "Gabi", "123456", "aa@gmail.com" );

        try {
            theFacade.viewSuperManagerStatistics("Victor", "123456");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        System.out.println("\n\n\n\n" + theFacade.viewSuperManagerStatistics("ADMIN", "ADMIN") + "\n\n\n\n");
        theFacade.removeForum("ADMIN", "ADMIN", "Forum-1");
        theFacade.removeForum("ADMIN", "ADMIN", "Forum-2");
        theFacade.removeForum("ADMIN", "ADMIN", "Forum-3");
        theFacade.logout(s);
        theFacade.logout(s2);
        theFacade.logout(s3);
        theFacade.logout(s4);

    }

    @Test
    public void testGetMessage() throws ThreadNotFoundException, SessionNotFoundException, PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, PermissionDeniedException, SubForumDoesNotExistException, DoesNotComplyWithPolicyException, SubForumAlreadyExistException, UserNotFoundException, UserAlreadyExistsException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com" );
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN" );
        theFacade.addSubforum(sessionId, "sub");
        theFacade.addSubforum(sessionId, "sub2");
        int id = theFacade.addThread(sessionId, "thread", "text");
        int sessionId2 = theFacade.login("Temp", "Victor", "123456");
        ExMessageI m = theFacade.getMessage(sessionId, id);
        assertTrue(m.getMessageText().equals("text"));
        theFacade.logout(sessionId);
        theFacade.logout(sessionId2);

    }

    @Test
    public void testGetThreadsList() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, EmailNotAuthanticatedException, UserNotFoundException, PasswordNotInEffectException, NeedMoreAuthParametersException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, ThreadNotFoundException, SubForumNotFoundException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com" );
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN" );
        theFacade.addSubforum(sessionId, "sub");
        theFacade.addSubforum(sessionId, "sub2");
        theFacade.addThread(sessionId, "thread", "text");
        theFacade.addThread(sessionId, "thread2", "text2");
        theFacade.addThread(sessionId, "thread3", "text3");
        theFacade.viewSubforum(sessionId, "sub");
        theFacade.addThread(sessionId, "threaddd", "text");
        theFacade.addThread(sessionId, "thread2dd", "text2");
        theFacade.addThread(sessionId, "thread3dd", "text3");
        Map<String, ThreadI> t = theFacade.getThreadsList(sessionId);
        assertFalse(t.containsKey("thread"));
        assertFalse(t.containsKey("thread2"));
        assertFalse(t.containsKey("thread3"));
        assertTrue(t.containsKey("threaddd"));
        assertTrue(t.containsKey("thread3dd"));
        assertTrue(t.containsKey("thread2dd"));
        theFacade.logout(sessionId);

    }

    @Test
    public void testGetMessageList() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, DoesNotComplyWithPolicyException, ThreadNotFoundException, MessageNotFoundException {
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN" );
        theFacade.addSubforum(sessionId, "sub2");
        int id =theFacade.addThread(sessionId, "thread", "text");
        theFacade.addReply(sessionId, id, "title", "text");
       id= theFacade.addReply(sessionId, id, "title2", "text");
        theFacade.addReply(sessionId, id, "title3", "text");
        theFacade.addReply(sessionId, id, "title4", "text");
        Tree t = theFacade.getMessageList(sessionId);
        assertEquals(t.getRoot().getData().getMessageTitle(), "thread");
        theFacade.logout(sessionId);

    }

    @Test
    public void testGetCurrentForumName() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SessionNotFoundException {
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN" );
        assertEquals(theFacade.getCurrentForumName(sessionId), "Temp");
        theFacade.logout(sessionId);

    }

    @Test
    public void testGetCurrentUserName() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException {
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN");
        theFacade.addSubforum(sessionId, "Zrima");
        assertEquals(theFacade.getCurrentUserName(sessionId), "ADMIN");
        theFacade.logout(sessionId);

    }


    @Test
    public void testViewSubforum() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, SubForumNotFoundException {
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN");
        theFacade.addSubforum(sessionId, "Zrima");
        theFacade.addSubforum(sessionId, "Zrima2");
        theFacade.viewSubforum(sessionId, "Zrima");
        assertEquals(theFacade.getCurrentUserSubForumStatus(sessionId), "Moderator");
        theFacade.logout(sessionId);

    }

    @Test
    public void testViewThread() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, DoesNotComplyWithPolicyException, ThreadNotFoundException {
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN" );
        theFacade.addSubforum(sessionId, "Zrima");
        theFacade.addThread(sessionId, "trhread", "asdasd");
        theFacade.addThread(sessionId, "t2", "asas");
        theFacade.viewThread(sessionId);
        assertEquals(theFacade.getCurrentThread(sessionId).getTitle(), "t2");
        theFacade.logout(sessionId);

    }

    @Test
    public void testGetCurrentThread() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, DoesNotComplyWithPolicyException, ThreadNotFoundException {
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN" );
        theFacade.addSubforum(sessionId, "Zrima");
        theFacade.addThread(sessionId, "trhread", "asdasd");
        assertEquals(theFacade.getCurrentThread(sessionId).getTitle(), "trhread");
        theFacade.logout(sessionId);

    }



    @Test
    public void getCurrentUserStatus() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SubForumDoesNotExistException, SessionNotFoundException, SubForumAlreadyExistException, PermissionDeniedException, UserAlreadyExistsException, DoesNotComplyWithPolicyException, UserNotFoundException, CloneNotSupportedException, SubForumNotFoundException, TooManyModeratorsException {
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN");
        theFacade.addSubforum(sessionId, "Zrima");
        assertEquals(theFacade.getCurrentUserForumStatus(sessionId), "Admin");
        assertEquals(theFacade.getCurrentUserSubForumStatus(sessionId), "Moderator");
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com" );
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());


        int sessionId2 = theFacade.login("Temp", "Victor", "123456");
        theFacade.viewSubforum(sessionId2, "Zrima");
        assertEquals(theFacade.getCurrentUserForumStatus(sessionId2), "User");
        assertEquals(theFacade.getCurrentUserSubForumStatus(sessionId2), "User");
        theFacade.setAdmin("ADMIN", "ADMIN", "Victor", "Temp");
        assertEquals(theFacade.getCurrentUserForumStatus(sessionId2), "Admin");
        assertEquals(theFacade.getCurrentUserSubForumStatus(sessionId2), "Moderator");
        theFacade.logout(sessionId);

    }

    @Test
    public void tooManyModerators() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SubForumDoesNotExistException, SessionNotFoundException, SubForumAlreadyExistException, PermissionDeniedException, UserAlreadyExistsException, DoesNotComplyWithPolicyException, UserNotFoundException, CloneNotSupportedException, SubForumNotFoundException, TooManyModeratorsException {
        int sessionId = theFacade.login("Temp", "ADMIN", "ADMIN" );
        theFacade.addSubforum(sessionId, "Zrima");
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com" );
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int sessionId2 = theFacade.login("Temp", "Victor", "123456");
        theFacade.register("Temp", "Gabi", "123456", "aa@gmail.com" );
        theFacade.authenticateUser("Temp", "Gabi", User.getUserFromDB("Gabi", "Temp").getUserAuthString());
        theFacade.login("Temp", "Gabi", "123456" );
        theFacade.setModerator(sessionId, "Victor");
        try {
            theFacade.setModerator(sessionId, "Gabi");
            fail();
        } catch (TooManyModeratorsException e) {
            assertTrue(true);
        }

        theFacade.logout(sessionId);

    }

    private boolean isForumExist(String forumName) {
        ForumI forum = Forum.load(forumName);
        return (forum != null);
    }

    private boolean findSubforum(int id, String subforum) throws SessionNotFoundException {
        ForumI forum = Forum.load(theFacade.getCurrentForumName(id));
        return forum.getSubForums().containsKey(subforum);
    }
}