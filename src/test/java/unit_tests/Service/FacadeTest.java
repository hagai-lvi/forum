package unit_tests.Service;


import main.User.User;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.interfaces.FacadeI;
import main.interfaces.ForumI;
import main.interfaces.UserI;
import main.services_layer.Facade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
           // theFacade.removeForum("ADMIN", "ADMIN", "Temp2");

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
    }

    @Test
    public void testSetAdmin() {
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
            theFacade.setAdmin("ADMIN","ADMIN","Victor2","Temp");
            fail();
        } catch (UserNotFoundException e) {
            assertTrue(true);
        } catch (PermissionDeniedException e) {
            fail();
        } catch (ForumNotFoundException e) {
            fail();
        }

        try {
            theFacade.setAdmin("ADMIN","ADMIN","Victor","Temp2");
            fail();
        } catch (UserNotFoundException e) {
            assertTrue(true);
        } catch (PermissionDeniedException e) {
            fail();
        } catch (ForumNotFoundException e) {
            fail();
        }

        try {
            theFacade.setAdmin("Victor","ADMIN","Victor","Temp");
            fail();
        } catch (UserNotFoundException e) {
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        } catch (ForumNotFoundException e) {
            fail();
        }

        try {
            theFacade.authenticateUser("Temp", "Victor", User.getUserFromDB("Victor", "Temp").getUserAuthString());
            int session = theFacade.login("Temp", "Victor", "123456");
            assertFalse(theFacade.isAdmin(session));
            theFacade.setAdmin("ADMIN", "ADMIN", "Victor", "Temp");
            assertTrue(theFacade.isAdmin(session));
        } catch (UserNotFoundException | PasswordNotInEffectException | SessionNotFoundException | InvalidUserCredentialsException | ForumNotFoundException | EmailNotAuthanticatedException | NeedMoreAuthParametersException | PermissionDeniedException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetSubForumList() {
        fail();
       /* try {
            theFacade.addForum("ADMIN", "ADMIN", "Sport", false, ".*", 2, 20);
        } catch (PermissionDeniedException e) {
            fail("No permission to add forum");
        } catch (ForumAlreadyExistException e) {
           fail("Forum already exist");
        }

        int session = 0;
        try {
            session = theFacade.login("Sport", "ADMIN", "ADMIN");
        } catch (InvalidUserCredentialsException e) {
            fail("Invalid credentials");
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (PasswordNotInEffectException e) {
            fail("Password Not In Effect");
        } catch (NeedMoreAuthParametersException e) {
            fail("Need More Authentication Parameters");
        } catch (EmailNotAuthanticatedException e) {
            fail("Email Not Authanticated");
        }

        //TODO - getLIst of forums from database
        ForumI forum = theFacade.getForumList().iterator().next();
        try {
            forum.addSubForum("Baseball");
            forum.addSubForum("Tennis");
        } catch (SubForumAlreadyExistException e) {
            fail("Sub forum already exist");
        }

        try {
            Collection<SubForumI> list = theFacade.getSubForumList(session);
        } catch (SessionNotFoundException e) {
            fail("Session not found");
        }
        try {
            assertTrue(findSubforum(session, "Baseball"));
            assertTrue(findSubforum(session, "Tennis"));
        } catch (SessionNotFoundException e) {
            fail("Session not found");
        }
        try {
            theFacade.removeForum("ADMIN", "ADMIN", "Sport");
        } catch (ForumNotFoundException e) {
            fail("Forum not found");
        } catch (PermissionDeniedException e) {
            fail("No permission to remove forum");
        }*/
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
            theFacade.register("Temp", "Victor", "123456","aa@gmail.com");
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
    public void testAddReply()  {
        fail();
    }

    @Test
    public void testCreateNewThread() throws PermissionDeniedException, ForumAlreadyExistException, PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SubForumAlreadyExistException, SessionNotFoundException, SubForumDoesNotExistException, SubForumNotFoundException, DoesNotComplyWithPolicyException {
        FacadeI f = Facade.getFacade();
        f.addForum("ADMIN", "ADMIN", "A", false, ".*", 5, 365);
        int sessionID = f.login("A", "ADMIN", "ADMIN");
        f.addSubforum(sessionID, "A sf1");
        f.getSubForumList(sessionID);
        f.viewSubforum(sessionID, "A sf1");
        f.addThread(sessionID, "TITLEEEE", "BODYYY");
        f.viewModeratorStatistics(sessionID);


    }

    @Test
    public void testReportModerator() {
        fail();

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
    public void testDeleteMessage()  {
        fail();
    }

    @Test
    public void testSetModerator()  {
        fail();
    }

    @Test
    public void testGuestEntry()  {

    }

    @Test
    public void testAddUserType() {

    }

    @Test
    public void testRemoveForum() {

    }

    @Test
    public void testSetPolicies()  {

    }

    @Test
    public void testEditMessage() {
    }

    @Test
    public void testRemoveModerator()  {

    }

    @Test
    public void testViewModeratorStatistics() {

    }

    @Test
    public void testViewSuperManagerStatistics()  {

    }

    @Test
    public void testViewSessions()  {

    }

    @Test
    public void testGetMessage()  {

    }

    @Test
    public void testGetThreadsList()  {

    }

    @Test
    public void testGetMessageList()  {

    }

    @Test
    public void testGetCurrentForumName()  {

    }

    @Test
    public void testGetCurrentUserName() {

    }

    @Test
    public void testIsAdmin() {

    }

    @Test
    public void testViewSubforum()  {

    }

    @Test
    public void testViewSubforum1()  {

    }

    @Test
    public void testViewThread()  {

    }

    @Test
    public void testGetCurrentThread()  {

    }

    @Test
    public void testAuthanticateUser()  {

    }

    @Test
    public void testGetFacade() {

    }

    @Test
    public void testDropAllData()  {

    }

    @Test
    public void isMessageFromCurrentUser()  {

    }

    @Test
    public void getCurrentUserStatus()  {

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