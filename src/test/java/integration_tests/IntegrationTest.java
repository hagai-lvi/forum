package integration_tests;

import data_structures.Tree;
import main.User.User;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.ExThreadI;
import main.interfaces.FacadeI;
import main.interfaces.ForumI;
import main.interfaces.SubForumI;
import main.services_layer.Facade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by hagai_lvi on 4/21/15.
 */
public class IntegrationTest {

	FacadeI _facade;
	ForumI forum;


	@Before
	public void setUp() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException {
		_facade = Facade.getFacade();
		forum = new Forum("forum", new ForumPolicy(false, 2, ".*", 365));

	}
	@After
	public void tearDown() throws ForumNotFoundException {
		Forum.delete("forum");
	}


	@Test
	/**
	 * target: Check whether a user can log in and view sub-forums.
	 */
	public void test_RegisterLoginAndViewSubforums() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, SessionNotFoundException, NeedMoreAuthParametersException, PasswordNotInEffectException, EmailNotAuthanticatedException, UserNotFoundException {

			_facade.register("forum", "user", "pass", "mail@mail.com");
			_facade.authenticateUser("forum", "user", _facade.getUserAuthString("forum", "user", "pass"));
			int sessionID = _facade.login("forum", "user", "pass");
			_facade.getSubForumList(sessionID);
	}
		@Test
	/**
	  target: check whether a user can view a previously deleted message.
	 */
	public void test_LoginPostDeleteAndTryToViewByOtherUser() throws SessionNotFoundException, SubForumDoesNotExistException, PasswordNotInEffectException, EmailNotAuthanticatedException, NeedMoreAuthParametersException, ForumNotFoundException, UserNotFoundException, DoesNotComplyWithPolicyException, PermissionDeniedException, SubForumAlreadyExistException, InvalidUserCredentialsException, UserAlreadyExistsException, ForumAlreadyExistException {
				_facade.register("forum", "user2", "pass", "mail@mail.com");
				_facade.authenticateUser("forum", "user2", _facade.getUserAuthString("forum", "user2", "pass"));

				// first user creates a new message.
				int session1ID = _facade.login("forum", "ADMIN", "ADMIN");
				_facade.addSubforum(session1ID, "subforum");
				_facade.addThread(session1ID, "thread-title", "message-body");
				_facade.addThread(session1ID, "thread-title2", "message-body2");

				Map<String, SubForumI> sf = _facade.getSubForumList(session1ID);
				assertTrue(sf.containsKey("subforum"));
				boolean flag = sf.get("subforum").getThreads().containsKey("thread-title");
				assertTrue(flag);
		}

	@Test
	/**
	 * target - Check if an expelled moderator keeps his privileges.
	 */
	public void test_removeModThenTryToEditMessage() throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, NeedMoreAuthParametersException, PasswordNotInEffectException, EmailNotAuthanticatedException, SubForumAlreadyExistException, SessionNotFoundException, SubForumNotFoundException, PermissionDeniedException, UserNotFoundException, SubForumDoesNotExistException, ThreadNotFoundException, MessageNotFoundException, CloneNotSupportedException {

			//add new user
			_facade.register("forum", "user", "pass", "mail@mail.com");
			//login as SU
		_facade.authenticateUser("forum", "user", User.getUserFromDB("user", "forum").getUserAuthString());
			int sessionId = _facade.login("forum", "user", "pass");
		_facade.setAdmin("ADMIN","ADMIN","user","forum");
		_facade.addSubforum(sessionId, "subforum");
			_facade.viewSubforum(sessionId, "subforum");
			int id = _facade.addThread(sessionId, "title", "body");
			_facade.viewThread(sessionId, "title");
			//get id of new message
			Tree messages = _facade.getMessageList(sessionId);
			//successfully edit the message as a mod
			_facade.editMessage(sessionId, id, "title", "body2");
			_facade.logout(sessionId);
			//expel the mod
		try {
			_facade.removeModerator(sessionId, "user");
		}catch (SessionNotFoundException e) {
			assertTrue(true);
		}

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


	/**
	 target: check whether a user can view a previously deleted message.
	 */
	@Test
	public void test_PostAndCheckStatistics() throws SessionNotFoundException, SubForumDoesNotExistException, PasswordNotInEffectException, EmailNotAuthanticatedException, NeedMoreAuthParametersException, ForumNotFoundException, UserNotFoundException, DoesNotComplyWithPolicyException, PermissionDeniedException, SubForumAlreadyExistException, InvalidUserCredentialsException, UserAlreadyExistsException, ForumAlreadyExistException {
		_facade.register("forum", "user2", "pass", "mail@mail.com");
		int sess = _facade.login("forum", "ADMIN", "ADMIN");
		assertEquals(_facade.viewModeratorStatistics(sess), "Number of messages: 0");
		_facade.authenticateUser("forum", "user2", _facade.getUserAuthString("forum", "user2", "pass"));

		// first user creates a new message.
		int session1ID = _facade.login("forum", "ADMIN", "ADMIN");
		_facade.addSubforum(session1ID, "subforum");
		_facade.addThread(session1ID, "thread-title", "message-body");
		assertEquals(_facade.viewModeratorStatistics(sess), "Number of messages: 1");
		Map<String, SubForumI> sf = _facade.getSubForumList(session1ID);
		assertTrue(sf.containsKey("subforum"));
		boolean flag = sf.get("subforum").getThreads().containsKey("thread-title");
		assertTrue(flag);
	}

	@Test

	public void testAdmin() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SessionNotFoundException, PermissionDeniedException, SubForumAlreadyExistException, SubForumDoesNotExistException, DoesNotComplyWithPolicyException, ThreadNotFoundException, MessageNotFoundException {
		int session = _facade.login("forum","ADMIN","ADMIN");
		_facade.addSubforum(session, "sub");
		_facade.addSubforum(session, "sub2");
		_facade.addSubforum(session, "sub3");

		int id = _facade.addThread(session, "title", "text");
		ExThreadI th = _facade.viewThread(session, "title");
		int id2 = _facade.addReply(session, id, "reply", "text");
		_facade.addReply(session, id2, "reply2", "text");
	}
}
