package acceptance_tests;

import data_structures.Tree;
import junit.framework.TestCase;
import main.User.User;
import main.User.UserForumPermission;
import main.exceptions.*;
import main.interfaces.*;
import main.User.Permissions;
import main.services_layer.Facade;
import org.junit.Ignore;
import org.junit.Test;
import java.sql.*;
import java.util.Collection;
import java.util.Objects;


//TODO - what should be the session id and how to edit session object from inside.
/**
 * Created by gabigiladov on 4/25/15.
 */
public class AcceptanceTest extends TestCase {
    private static final int GUEST_SESSION = -1;
    private FacadeI _facade;
   // private Collection<ExForumI> _forumCollection;

    @Override
    public void setUp() throws Exception {
        _facade = Facade.getFacade();
        String[] names = {"gil", "tom", "hagai", "gabi", "victor", "aria", "yoni", "moshe",
                "tal", "chen", "bibi", "mor", "david", "dudinka", "aaa"};

            //add forums
            for (int i = 0; i < 5; i++) {
                _facade.addForum("admin", "admin", false, "Forum " + Integer.toString(i), "a+", 2, 365);

            //add users to forums and create threads
            for (int j = 0; j < 3; j++) {
                _facade.register("forum " + Integer.toString(i), names[i * 3 + j], "123456", "nobodyemail@nobody.com");
                _facade.createSubforum(0, "SubForum " + j + " In Forum" + i);
                _facade.createNewThread(0, "message " + j, "body " + j);
            }
        }

       // _forumCollection = _facade.getForumList();

    }


    /**
     * UseCases:
     * 		1. Initialize
     *		2. CreateForum
     *		3. SetPolicies
     *		4. GuestEntry
     *		5. Register
     *		6. Login
     *		7. Logout
     *		8. CreateSubForum
     *		9. ViewSubForum
     *		10. PostThread
     *		11. PostMessage
     *		12. FriendType
     *		13. ComplainOnModerator
     *		14. EmailAuthentication
     *		15. RemoveMessage
     *		16. RemoveForum
     *      17. EditMessage
     *      18. SetModerator
     *      19. CancelModerator
     *      20. GetUpdatesFromModerator
     *      21. GetUpdatesFromSuperManager
     *      22. ViewSessions
     */

    /**
     * target: check initilize, should return true.
     */
    public void testInitialize() {
        assertTrue(false);
        //TODO
    }


    @Test
    @Ignore
    public void testConnectToDB() throws SQLException {
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/world?" +
                            "user=root&password=Aa123456");
            // Do something with the Connection

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        assert conn != null;
        Statement stmt = conn.createStatement();
        String query = "select * from CITY;";
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        System.out.println("querying SELECT * FROM XXX");
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
    }


    /**
     * target: check adding forum to the system.
     */
    @Test
    public void createForumTest() throws PermissionDeniedException, ForumAlreadyExistException {

        int numOfForums = _facade.getForumList().size();
        _facade.addForum("admin", "admin", false, "Forum CreateForumTest", "[a-z]*[!@#][a-z]*", 2, 365);
        assertEquals(numOfForums + 1, _facade.getForumList().size());

    }

    @Test
    /**
     * target: set new policy for forum
     */
    public void setPoliciesTest() {
        _facade.setPolicies(0, false, "a*", 1, 365);
        // test pass regex
        try {
            _facade.register("Forum1", "user", "aaa", "mail@mail.mail");
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        } catch (InvalidUserCredentialsException e) {
            e.printStackTrace();
        }
        // test mod number
        try {
            _facade.setModerator(0, "hagai");
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
        _facade.setPolicies(0, false, "a*", 2, 365);
        try {
            _facade.setModerator(0, "hagai");
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * target: the test check the permission given to a guest.
     * check negative test like getting the right exception on violate his permission
     */
    public void guestEntryTest() throws MessageNotFoundException {
        int session = _facade.guestEntry("Forum1");
        //try create thread
        try {
           _facade.createNewThread(session, "I TRY TO CREATE MESSAGE NANA", "");
            fail("a guest cannot create a thread");
        } catch (DoesNotComplyWithPolicyException e) {
            fail("Expected PermissionDeniedException");
        } catch (PermissionDeniedException e) {
            //expected exception
        }

        int msgid = 1;//_facade.getMessageList(session).

        //try reply to message
        try {
            _facade.addReply(session, msgid, "I TRY TO CREATE REPLY NANA", "");
            fail("a guest cannot reply to message");
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        } catch (DoesNotComplyWithPolicyException e) {
            e.printStackTrace();
        }

        //try report moderator
        try {
            _facade.reportModerator(session, "Moshe", "He is so bad Moderator");
            fail("a guest cannot create a report on moderator");
        } catch (ModeratorDoesNotExistsException e) {//TODO need to define the expected result
            e.printStackTrace();
        } catch (PermissionDeniedException e){
            assertTrue(true);
        }

        //try delete message
        try {
            _facade.deleteMessage(session, msgid);
            fail("a guest cannot delete message");
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
    }

    @Test
    /**
     * target: check register usecase for new user
     * check that the user exist in the list after register and that user cannot register twice
     * check if you get email authentication message in your inbox
     */
    public void registerTest() {

        // tests register existing username
        try {
            _facade.register("Forum0", "gil", "morgil12345", "gilmor89@gmail.com");
            fail("register the same user");
        } catch (UserAlreadyExistsException e) {
            assertTrue(true);
        } catch (InvalidUserCredentialsException e) {
            e.printStackTrace();
        }

        //test register

        try {
            _facade.register("Forum0", "gilgilmor", "morgil12345", "gilmor89@gmail.com");
            fail("register the same user");
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        } catch (InvalidUserCredentialsException e) {
            e.printStackTrace();

            Collection<UserI> users = _facade.getForumList().iterator().next().getUserList();

            for (UserI user : users) {
                if (user.getUsername().equals("gilgilmor")) assertTrue(true);
            }
            fail("registered user not found");
        }
    }

    @Test
    /**
     * target: check login usecase, try login to non exist user
     */
    public void loginTest() {
        int session = GUEST_SESSION;
        try {
            session = _facade.login("Forum0", "gil", "123456");
        } catch (InvalidUserCredentialsException | PasswordNotInEffectException | EmailNotAuthanticatedException | NeedMoreAuthParametersException e) {
            e.printStackTrace();
        }
        assert(GUEST_SESSION != session);
        try {
            _facade.login("Forum0", "notexist", "user");
            fail("should throw exception");
        } catch (InvalidUserCredentialsException e) {
            assertTrue(true);
        } catch (Throwable e) {
            fail("should throw exception");
        }

    }

    @Test
    /**
     * target: check logout usecase
     */
    public void logoutTest() {
        int session = 0;
        //test login and out
        try {
            session = _facade.login("Forum0", "gil", "123456");
            _facade.logout(session);
        } catch (InvalidUserCredentialsException | PasswordNotInEffectException | EmailNotAuthanticatedException | NeedMoreAuthParametersException e) {
            e.printStackTrace();
        }
        assertTrue(true);

        //test post when logged out
        try {
            _facade.createNewThread(session, "Title", "Body");
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        } catch (DoesNotComplyWithPolicyException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * taget: check Create sub forum usecase
     */
    public void createSubForumTest() {

        //test creation
        try {
            _facade.createSubforum(0, "newSF");
        } catch (SubForumAlreadyExistException e) {
            fail("cannot create new sub forum");
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * target: view sub forum
     */
    public void viewSubForumTest() {
       fail("not implemented");
       //TODO - how?
    }

    @Test
    /**
     * target: test usecase post thread
     */
    public void postThreadTest() {

        int n = _facade.getSubForumList(0).size();
        try {
            _facade.createNewThread(0, "Title", "Body");
        } catch (PermissionDeniedException | DoesNotComplyWithPolicyException e) {
            e.printStackTrace();
        }
        assertEquals(n + 1, _facade.getSubForumList(0).size());
    }

    @Test
    /**
     * target: test post message usecase
     */
    public void postMessageTest() {

        Tree msgs = _facade.getMessageList(0);
        int msgid = 1;//msgs.iterator().next().getId(); //TODO tree iterator
        try {
            _facade.addReply(0, msgid ,"a", "b");
        } catch (MessageNotFoundException | PermissionDeniedException | DoesNotComplyWithPolicyException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * target test Friend Type requirement
     */
    public void friendTypeTest() {
        //TODO - user types?!
        _facade.addUserType(0, "SuperPooper", 2, 200, 2);
    }



    @Test
     /** target: test remove message usecase, check that user can remove only
     * 			his messages.
      */
     public void removeMessageTest() {
        int session = 0;
        try {
            session = _facade.login("Forum0", "gil", "123456");
            _facade.createNewThread(session, "Title", "Body");
        } catch (InvalidUserCredentialsException | PermissionDeniedException | DoesNotComplyWithPolicyException | PasswordNotInEffectException | EmailNotAuthanticatedException | NeedMoreAuthParametersException e) {
            e.printStackTrace();
        }

        try {
            _facade.deleteMessage(session, 0);//TODO - how to get message ID?
        } catch (PermissionDeniedException | MessageNotFoundException e) {
            e.printStackTrace();
        }


    }
    @Test
    /**
     * target: test cancel forum usecase
     */
    public void cancelForumTest() {
        try {
            _facade.removeForum("admin", "admin", "Forum1");
        } catch (ForumNotFoundException | PermissionDeniedException e) {
            e.printStackTrace();
        }

        try {
            _facade.createNewThread(0, "title", "body");
        } catch (PermissionDeniedException | DoesNotComplyWithPolicyException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * target: check use case send report on moderator
     */
    public void complainOnModeratorTest() {

        try {
            _facade.reportModerator(0, "Moshe", "he is not behave well!!");
        } catch (PermissionDeniedException | ModeratorDoesNotExistsException e) {
            e.printStackTrace();
        }

    }



    @Test
    /**
     * target: test email authentication use case
     */
    public void emailAuthenticationTest() {

        int session = 0;
        try {
            _facade.register("Forum0", "tomgond_new1", "my_pass", "gabi.giladov@gmail.com");
            session = _facade.login("Forum0", "tomgond_new1", "my_pass");
        }
        catch(UserAlreadyExistsException e){
            fail("User already exists!");
        }
        catch (InvalidUserCredentialsException e) {
            fail("Invalid user credentials!");
        } catch (PasswordNotInEffectException e) {
            e.printStackTrace();
        } catch (EmailNotAuthanticatedException | NeedMoreAuthParametersException e) {
            //accept
        }

        Collection<UserI> users = _facade.getForumList().iterator().next().getUserList();
        UserI the_user = null;
        for (UserI user : users){
            if (user.getUsername().equals("tomgond_new1")){
                the_user = user;
                break;
            }
        }
        the_user.setAuthenticated();
        try {
            _facade.login("Forum0", "tomgond_new1", "my_pass");
        } catch (InvalidUserCredentialsException | PasswordNotInEffectException | NeedMoreAuthParametersException e) {
            e.printStackTrace();
        } catch (EmailNotAuthanticatedException e) {
            fail("email authantication failed");
        }
    }



    @Test
    /**
    * target: test editing message by publisher use case
    */
    public void editOwnMessageTest() {
        int session = 0;

        try {
            _facade.login("Forum0", "gil", "123456");
        } catch (InvalidUserCredentialsException | EmailNotAuthanticatedException | PasswordNotInEffectException | NeedMoreAuthParametersException e) {
            e.printStackTrace();
        }

        try {
            _facade.createNewThread(session, "T", "B");
        } catch (PermissionDeniedException | DoesNotComplyWithPolicyException e) {
            e.printStackTrace();
        }

        _facade.editMessage(session, 0, "Title", "Body");

       assertTrue(Objects.equals("TODO - check message title updated", "a"));
        assertTrue(Objects.equals("TODO - check message body updated", "a"));

    }

    @Test
    /**
     * target: test setting moderator to sub forum use case
    */
    public void setModeratorTest() {



        try {
            _facade.setModerator(0, "gil");
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
        }
        //TODO - how to check if user is a moderator.
    }

    @Test
    /**
     * target: test canceling moderator use case
    */
    public void cancelModeratorTest() {
        try {
            _facade.setModerator(0, "gabi");
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
        }
        _facade.removeModerator(0, "gabi");

        //TODO - how to check if user is not a moderator.
    }

    @Test
    /**
     * target: test getting updates and info from moderator use case
    */
    public void getUpdatesFromModeratorTest() {
        ForumI forum =_facade.getForumList().iterator().next();
        ForumPermissionI permission = new UserForumPermission(Permissions.PERMISSIONS_USER,forum);
        ForumPermissionI permission2 = new UserForumPermission(Permissions.PERMISSIONS_MODERATOR,forum);
        UserI user = new User("Gabi", "123456", "mail@gmail.com", permission);
        UserI user2 = new User("Victor", "abcde", "mail2@gmail.com", permission2);
        SubForumI subf = forum.getSubForums().iterator().next();

        try {
            user.viewStatistics(forum);
        } catch (PermissionDeniedException e) {
            assertTrue(true); // user has not permission to set moderator
        }
        try {
            user2.viewStatistics(forum);
        } catch (PermissionDeniedException e) {
            assertTrue(false);
        }
    }

    @Test
    /**
     * target: test getting updates and info from super manager use case
     */
    public void getUpdatesFromSuperManagerTest() {
        ForumI forum =_facade.getForumList().iterator().next();
        ForumPermissionI permission = new UserForumPermission(Permissions.PERMISSIONS_USER,forum);
        ForumPermissionI permission2 = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,forum);
        UserI user = new User("Gabi", "123456", "mail@gmail.com", permission);
        UserI user2 = new User("Victor", "abcde", "mail2@gmail.com", permission2);
        SubForumI subf = forum.getSubForums().iterator().next();

        try {
            user.viewStatistics(forum);
        } catch (PermissionDeniedException e) {
            assertTrue(true); // user has not permission to set moderator
        }
        try {
            user2.viewStatistics(forum);
        } catch (PermissionDeniedException e) {
            assertTrue(false);
        }
    }


}
