package acceptance_tests;

import main.exceptions.*;
import main.interfaces.*;
import main.services_layer.Facade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import static org.junit.Assert.*;

/**
 * Created by hagai_lvi on 4/21/15.
 */
public class AcceptanceTests {

	FacadeI _facade;

	@Test
	/**
	 * target: Check whether a user can log in and view sub-forums.
	 */
	public void test_RegisterLoginAndViewSubforums(){


		try {
			_facade.register("forum", "user", "pass", "mail@mail.com");
			int sessionID = _facade.login("forum", "user", "pass");
			assertNotEquals(sessionID, 0);
			_facade.getSubForumList(sessionID);
		}catch (InvalidUserCredentialsException e){
			fail("User failed to log in!");
		}
		catch (UserAlreadyExistsException e){
			fail("User Already Exists!");
		}
	}

	@Test
	/**
	  target: check whether a user can view a previously deleted message.
	 */
	public void test_LoginPostDeleteAndTryToViewByOtherUser() throws UserAlreadyExistsException, InvalidUserCredentialsException,
			SubForumAlreadyExistException, PermissionDeniedException, DoesNotComplyWithPolicyException, MessageNotFoundException {
		try {
			// create both users.
			_facade.register("forum", "user1", "pass", "mail@mail.com");
			_facade.register("forum", "user2", "pass", "mail@mail.com");

			// first user creates a new message.
			int session1ID = _facade.login("forum", "user1", "pass");
			_facade.createSubforum(session1ID, "subforum");
			_facade.createNewThread(session1ID, "thread-title", "message-body");
			Collection<ExSubForumI> sf = _facade.getSubForumList(session1ID);
			ExSubForumI newSF = sf.iterator().next();
			assertEquals(newSF.getTitle(), "subforum");
			ExThreadI newThread = newSF.getThreads().iterator().next();
			assertEquals(newThread.getTitle(), "thread-title");
			ExMessageI newMessage = newThread.getMessages().iterator().next();
			assertEquals(newMessage.getBody(), "message-body");

			// user deletes message.
			_facade.deleteMessage(session1ID, newMessage.getId());

			// login as second user.
			int session2ID = _facade.login("forum", "user2", "pass");
			sf = _facade.getSubForumList(session2ID);
			newSF = sf.iterator().next();
			newThread = newSF.getThreads().iterator().next();
			newMessage = newThread.getMessages().iterator().next();
			_facade.getMessage(session2ID, newMessage.getId());

		} catch (UserAlreadyExistsException e) {
			fail("User already exists!");
		} catch (InvalidUserCredentialsException e) {
			fail("User failed to log in!");
		} catch (SubForumAlreadyExistException e) {
			fail("Subforum already exists!");
		} catch (PermissionDeniedException e) {
			fail("Permissions denied!");
		} catch (DoesNotComplyWithPolicyException e) {
			fail("Policy violation!");
		} catch (MessageNotFoundException e) {
			assertFalse(false);
		}
	}

	@Test
	/**
	 * target: change policy that have conflict with the former policy.
	 */
	public void integration3(){
		//TODO this functionality is not yet implemented
	}

	@Test
	/**
	 * target: Check whether a user without admin privileges can delete a sub-forum.
	 */
	public void test_LogInUnprivilegedAndTryToDeleteSubforum() throws UserAlreadyExistsException, InvalidUserCredentialsException, PermissionDeniedException, ForumNotFoundException {

		try {
			_facade.register("forum", "user", "pass", "mail@mail.com");
			_facade.addForum("admin", "pass", "forum", "a*", 1);
			_facade.login("forum", "user", "pass");
			_facade.removeForum("user", "pass", "forum");
			assertFalse(true);
		} catch (UserAlreadyExistsException e) {
			fail("User already exists!");
		} catch (InvalidUserCredentialsException e) {
			fail("User failed to log in!");
		} catch (PermissionDeniedException e) {
			assertFalse(false);
		} catch (ForumNotFoundException e) {
			fail("Policy violation!");
		}
	}

	@Before
	public void init() {
		_facade = Facade.getFacade();
	}

	@After
	public void cleanUp(){
		Facade.dropAllData();
	}

}
