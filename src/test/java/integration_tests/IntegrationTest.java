package integration_tests;

import data_structures.Tree;
import main.exceptions.*;
import main.interfaces.FacadeI;
import main.interfaces.SubForumI;
import main.interfaces.ThreadI;
import main.services_layer.Facade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by hagai_lvi on 4/21/15.
 */
public class IntegrationTest {

	FacadeI _facade;

	@Test
	/**
	 * target: Check whether a user can log in and view sub-forums.
	 */
	public void test_RegisterLoginAndViewSubforums() {
		try {
			_facade.addForum("ADMIN", "ADMIN", "forum", false, "pass", 3, 365);
		} catch (PermissionDeniedException e) {
			fail();
		} catch (ForumAlreadyExistException e) {
			fail();
		}
		try {
			_facade.register("forum", "user", "pass", "mail@mail.com");
			_facade.authenticateUser("forum", "user", _facade.getUserAuthString("forum", "user", "pass"));
			int sessionID = _facade.login("forum", "user", "pass");
			_facade.getSubForumList(sessionID);

			_facade.removeForum("user", "pass", "forum");
		} catch (PasswordNotInEffectException | InvalidUserCredentialsException | NeedMoreAuthParametersException | DoesNotComplyWithPolicyException | SessionNotFoundException | EmailNotAuthanticatedException | UserNotFoundException | PermissionDeniedException | UserAlreadyExistsException | ForumNotFoundException e) {
			fail();
		}
	}
		@Test
	/**
	  target: check whether a user can view a previously deleted message.
	 */
	public void test_LoginPostDeleteAndTryToViewByOtherUser() {
		try {
			_facade.addForum("ADMIN", "ADMIN", "forum", false, ".*", 3, 365);
		} catch (PermissionDeniedException | ForumAlreadyExistException e) {
				fail();
		}
			try {
			_facade.register("forum", "user2", "pass", "mail@mail.com");
			_facade.authenticateUser("forum", "user2", _facade.getUserAuthString("forum", "user2", "pass"));

			// first user creates a new message.
			int session1ID = _facade.login("forum", "ADMIN", "ADMIN");
			_facade.addSubforum(session1ID, "subforum");
			_facade.addThread(session1ID, "thread-title", "message-body");
			Collection<SubForumI> sf = _facade.getSubForumList(session1ID);
			SubForumI newSF = sf.iterator().next();
			assertEquals(newSF.getTitle(), "subforum");
			ThreadI newThread = newSF.getThreads().iterator().next();
			assertEquals(newThread.getTitle(), "thread-title");
			//ExMessageI newMessage = newThread.getMessages().find(id);
			//assertEquals(newMessage.getMessageText(), "message-body");
			// user deletes message.
			//_facade.deleteMessage(session1ID, newMessage.getId());

			// login as second user.
			int session2ID = _facade.login("forum", "user2", "pass");
			sf = _facade.getSubForumList(session2ID);
			newSF = sf.iterator().next();
			newThread = newSF.getThreads().iterator().next();
			//newMessage = newThread.getMessages().find(id);
			//_facade.getMessage(session2ID, newMessage.getId());
				_facade.removeForum("user2", "pass", "forum");

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
		} catch (UserNotFoundException e) {
			fail("user not found!");
		} catch (ForumNotFoundException e) {
			fail("forum not found!");
		} catch (SessionNotFoundException | SubForumDoesNotExistException | PasswordNotInEffectException | EmailNotAuthanticatedException | NeedMoreAuthParametersException e) {
			fail();
			}
		}

	@Test
	/**
	 * target - Check if an expelled moderator keeps his privileges.
	 */
	public void test_removeModThenTryToEditMessage() {
		try {
			_facade.addForum("ADMIN", "ADMIN", "forum", false, "pass", 3, 365);
		} catch (PermissionDeniedException e) {
			e.printStackTrace();
		} catch (ForumAlreadyExistException e) {
			e.printStackTrace();
		}
		try {
			//add new user
			_facade.register("forum", "user", "pass", "mail@mail.com");
			//login as SU
			int sessionId = _facade.login("forum", "admin", "pass");
			_facade.viewSubforum(sessionId, "subforum");
			//set user as subforum mod
			_facade.setModerator(sessionId, "user");
			_facade.addThread(sessionId, "title", "body");
			_facade.viewThread(sessionId, "title");
			//get id of new message
			Tree messages = _facade.getMessageList(sessionId);
			int messageId = messages.getId();
			int modSessionId = _facade.login("forum", "user", "pass");
			//successfully edit the message as a mod
			_facade.editMessage(modSessionId, messageId, "title", "body2");
			_facade.logout(modSessionId);
			//expel the mod
			_facade.removeModerator(sessionId, "user");
			modSessionId = _facade.login("forum", "user", "pass");
			//try to edit the message again
			_facade.editMessage(modSessionId, messageId, "title", "body");
			fail("message edited although not permitted");
		} catch (UserAlreadyExistsException | EmailNotAuthanticatedException | SubForumAlreadyExistException | PasswordNotInEffectException | DoesNotComplyWithPolicyException | InvalidUserCredentialsException | SessionNotFoundException | ThreadNotFoundException | ForumNotFoundException | UserNotFoundException | SubForumNotFoundException | SubForumDoesNotExistException | MessageNotFoundException e) {
			e.printStackTrace();
		} catch (PermissionDeniedException e) {
			//pass
		} catch (NeedMoreAuthParametersException e) {
			fail();
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
	public void test_LogInUnprivilegedAndTryToDeleteForum()  {
		try {
			_facade.addForum("ADMIN", "ADMIN", "forum", false, "pass", 3, 365);
		} catch (PermissionDeniedException e) {
			e.printStackTrace();
		} catch (ForumAlreadyExistException e) {
			e.printStackTrace();
		}
		try {
			_facade.register("forum", "user", "pass", "mail@mail.com");

			try {
				_facade.authenticateUser("forum", "user", _facade.getUserAuthString("forum", "user", "pass"));
			} catch (UserNotFoundException e) {
				e.printStackTrace();
			}
			_facade.login("forum", "user", "pass");
			_facade.removeForum("user", "pass", "forum");
			fail("unauthorized removal of a forum");
		} catch (UserAlreadyExistsException | EmailNotAuthanticatedException | PasswordNotInEffectException | ForumNotFoundException | InvalidUserCredentialsException | NeedMoreAuthParametersException | DoesNotComplyWithPolicyException e) {
			e.printStackTrace();
		} catch (PermissionDeniedException e) {
			//pass
		}

	}

	@Before
	public void setUp() {
		_facade = Facade.getFacade();
	}

	@After
	public void cleanUp(){

	}

}
