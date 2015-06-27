package unit_tests.Service;


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
           // theFacade.addForum("ADMIN", "ADMIN", "Temp2", true, "[1-9]*", 1, 10);

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
          //  theFacade.removeForum("ADMIN", "ADMIN", "Temp2");

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
    public void testGetForumList() {
        ArrayList<String> forums = theFacade.getForumList();
        assertTrue(forums != null);
        assertTrue(forums.contains("Temp"));
        assertTrue(forums.contains("Temp2"));
        assertFalse(forums.contains("Zrima"));
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
    }

    @Test
    public void testGetSubForumList() throws InvalidUserCredentialsException, PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, EmailNotAuthanticatedException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, UserAlreadyExistsException, DoesNotComplyWithPolicyException, UserNotFoundException, CloneNotSupportedException {

        int session = 0;
        session = theFacade.login("Temp", "ADMIN", "ADMIN");
        theFacade.addSubforum(session, "Sport");
        theFacade.addSubforum(session, "SomeSub");
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
       session =  theFacade.login("Temp", "Victor", "123456");
        theFacade.setAdmin("ADMIN", "ADMIN", "Victor", "Temp");
        theFacade.addSubforum(session, "OneMore");
        Map<String, SubForumI> list = theFacade.getSubForumList(session);
        assertTrue(list.containsKey("Sport"));
        assertTrue(list.containsKey("SomeSub"));
        assertTrue(list.containsKey("OneMore"));
        assertFalse(list.containsKey("Zrima"));
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
            theFacade.register("Temp", "Yosi", "a123456", "aa@gmail.com");
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
            theFacade.register("Temp2", "Shmuel", "123456", "aa@gmail.com");
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
    public void testLogin() {

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
            theFacade.login("Temp", "Victor", "123456");
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
            theFacade.login("Temp", "Victor", "123456");
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
            theFacade.logout(1);
            fail();
        } catch (SessionNotFoundException e) {
            assertTrue(true);
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
    }

    @Test
    public void testAddSubforum() throws UserNotFoundException, PermissionDeniedException, ForumNotFoundException, CloneNotSupportedException, EmailNotAuthanticatedException, UserAlreadyExistsException, InvalidUserCredentialsException, DoesNotComplyWithPolicyException, PasswordNotInEffectException, NeedMoreAuthParametersException, SubForumAlreadyExistException, SessionNotFoundException, SubForumDoesNotExistException {
        theFacade.register("Temp", "Victor", "123456", "aa@gmail.com");
        theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
        int sessionId = theFacade.login("Temp", "Victor", "123456");
        try {
            theFacade.addSubforum(sessionId, "sub");
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        theFacade.setAdmin("ADMIN", "ADMIN", "Victor", "Temp");
        theFacade.addSubforum(sessionId, "sub2");
        fail();
        try {
            theFacade.addSubforum(sessionId, "sub2");
        } catch (SubForumAlreadyExistException e) {
            assertTrue(true);
        }
        theFacade.addSubforum(sessionId, "sub3");
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
    }

    @Test
    public void testReportModerator() {
        fail();
    }

    @Test
    public void testDeleteMessage()  {
        fail();
    }

    @Test
    public void testSetModerator() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, EmailNotAuthanticatedException, UserNotFoundException, PasswordNotInEffectException, NeedMoreAuthParametersException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, CloneNotSupportedException, SubForumNotFoundException, MessageNotFoundException, ThreadNotFoundException {
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
    }

    @Test
    public void testGuestEntry() throws ForumNotFoundException {
        theFacade.guestEntry("Temp");
    }

    @Test
    public void testAddUserType() {
        fail();
    }

    @Test
    public void testRemoveForum() {
        fail();
    }

    @Test
    public void testSetPolicies()  {
        fail();
    }

    @Test
    public void testEditMessage() {
        fail();
    }

    @Test
    public void testRemoveModerator()  {
        fail();
    }

    @Test
    public void testViewModeratorStatistics() {
        fail();
    }

    @Test
    public void testViewSuperManagerStatistics()  {
        fail();
    }

    @Test
    public void testViewSessions()  {
        fail();
    }

    @Test
    public void testGetMessage()  {
        fail();
    }

    @Test
    public void testGetThreadsList()  {
        fail();
    }

    @Test
    public void testGetMessageList()  {
        fail();
    }

    @Test
    public void testGetCurrentForumName()  {
        fail();
    }

    @Test
    public void testGetCurrentUserName() {
        fail();
    }

    @Test
    public void testIsAdmin() {
        fail();
    }

    @Test
    public void testViewSubforum()  {
        fail();
    }


    @Test
    public void testViewThread()  {
        fail();
    }

    @Test
    public void testGetCurrentThread()  {
        fail();
    }

    @Test
    public void testAuthanticateUser()  {
        fail();
    }

    @Test
    public void testGetFacade() {
        fail();
    }

    @Test
    public void testDropAllData()  {
        fail();
    }



    @Test
    public void getCurrentUserStatus()  {
        fail();
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