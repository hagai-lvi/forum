package integration_tests;

import data_structures.Tree;
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
public class IntegrationTest {

	FacadeI _facade;

	@Test
	/**
	 * target: Check whether a user can log in and view sub-forums.
	 */
	public void test_RegisterLoginAndViewSubforums() throws InvalidUserCredentialsException, UserAlreadyExistsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException {
		try {
			_facade.register("forum", "user", "pass", "mail@mail.com");
			int sessionID = _facade.login("forum", "user", "pass");
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
			SubForumAlreadyExistException, PermissionDeniedException, DoesNotComplyWithPolicyException, MessageNotFoundException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException {
		try {
			// create both users.*	_facade.register("forum", "user1", "pass", "mail@mail.com");
			_facade.register("forum", "user2", "pass", "mail@mail.com");

			// first user creates a new message.
			int session1ID = _facade.login("forum", "user1", "pass");
			_facade.createSubforum(session1ID, "subforum");
			int id = _facade.createNewThread(session1ID, "thread-title", "message-body");
			Collection<SubForumI> sf = _facade.getSubForumList(session1ID);
			SubForumI newSF = sf.iterator().next();
			assertEquals(newSF.getName(), "subforum");
			ThreadI newThread = newSF.getThreads().iterator().next();
			assertEquals(newThread.getTitle(), "thread-title");
			ExMessageI newMessage = newThread.getMessages().find(id);
			assertEquals(newMessage.getBody(), "message-body");

			// user deletes message.
			_facade.deleteMessage(session1ID, newMessage.getId());

			// login as second user.
			int session2ID = _facade.login("forum", "user2", "pass");
			sf = _facade.getSubForumList(session2ID);
			newSF = sf.iterator().next();
			newThread = newSF.getThreads().iterator().next();
			newMessage = newThread.getMessages().find(id);
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
			//pass
		}

	}

	@Test
	/**
	 * target - Check if an expelled moderator keeps his privileges.
	 */
	public void test_removeModThenTryToEditMessage() throws NeedMoreAuthParametersException {
		try {
			//add new user
			_facade.register("forum", "user", "pass", "mail@mail.com");
			//login as SU
			int sessionId = _facade.login("forum", "admin", "pass");
			_facade.viewSubforum(sessionId, "subforum");
			//set user as subforum mod
			_facade.setModerator(sessionId, "user");
			_facade.createNewThread(sessionId, "title", "body");
			_facade.viewThread(sessionId, "title");
			//get id of new message
			Tree messages = _facade.getMessageList(sessionId);
			int messageId = messages.getId();
			int modSessionId = _facade.login("forum", "user", "pass");
			//successfully edit the message as a mod
			_facade.editMessage(modSessionId, messageId, "title2", "body2");
			_facade.logout(modSessionId);
			//expel the mod
			_facade.removeModerator(sessionId, "user");
			modSessionId = _facade.login("forum", "user", "pass");
			//try to edit the message again
			_facade.editMessage(modSessionId, messageId, "title", "body");
			fail("message edited although not permitted");
		} catch (UserAlreadyExistsException | EmailNotAuthanticatedException | SubForumAlreadyExistException | PasswordNotInEffectException | DoesNotComplyWithPolicyException | InvalidUserCredentialsException e) {
			e.printStackTrace();
		} catch (PermissionDeniedException e) {
			//pass
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
	public void test_LogInUnprivilegedAndTryToDeleteForum() throws UserAlreadyExistsException, InvalidUserCredentialsException, PermissionDeniedException, ForumNotFoundException {

		try {
			_facade.register("forum", "user", "pass", "mail@mail.com");
			_facade.login("forum", "user", "pass");
			_facade.removeForum("user", "pass", "forum");
			fail("unauthorized removal of a forum");
		} catch (UserAlreadyExistsException | EmailNotAuthanticatedException | PasswordNotInEffectException | ForumNotFoundException | InvalidUserCredentialsException | NeedMoreAuthParametersException e) {
			e.printStackTrace();
		} catch (PermissionDeniedException e) {
			//pass
		}

	}

	@Before
	public void init() {
		_facade = Facade.getFacade();
		try {
			_facade.addForum("admin", "pass", "forum", "pass", 3);
		} catch (PermissionDeniedException e) {
			e.printStackTrace();
		} catch (ForumAlreadyExistException e) {
			e.printStackTrace();
		}
	}

	@After
	public void cleanUp(){
		Facade.dropAllData();
	}

}
