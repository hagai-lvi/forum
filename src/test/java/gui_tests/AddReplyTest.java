package gui_tests;

import main.exceptions.*;
import main.interfaces.ExThreadI;
import main.interfaces.FacadeI;
import main.services_layer.Facade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by hagai_lvi on 6/26/15.
 */
public class AddReplyTest {

	private static final String ADMIN_PASS = "ADMIN";
	private static final String ADMIN_USER = "ADMIN";
	private static String SUBFORUM_NAME;
	private static String FORUM_NAME;
	private int sessionId;
	private String MSG_TITLE;
	private String MSG_BODY;
	private int MSG_ID;

	private String REPLY_TITLE;
	private String REPLY_BODY;


	@Before
	public void beforeTest() throws PermissionDeniedException, ForumAlreadyExistException, PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SubForumAlreadyExistException, SessionNotFoundException, SubForumDoesNotExistException, SubForumNotFoundException, DoesNotComplyWithPolicyException, ThreadNotFoundException {
		FacadeI f = Facade.getFacade();
		FORUM_NAME = this.getClass().getName();
		SUBFORUM_NAME = this.getClass().getName() + " subforum";
		MSG_TITLE = this.getClass().getName() + " title";
		MSG_BODY = this.getClass().getName() + " body";
		REPLY_TITLE = this.getClass().getName() + " reply title";
		REPLY_BODY = this.getClass().getName() + " reply body";

		f.addForum(ADMIN_USER, ADMIN_PASS, FORUM_NAME,false, ".*", 10, Integer.MAX_VALUE);
		sessionId = f.login(FORUM_NAME, ADMIN_USER, ADMIN_PASS);
		f.addSubforum(sessionId, SUBFORUM_NAME);
		f.viewSubforum(sessionId, SUBFORUM_NAME);
		f.addThread(sessionId, MSG_TITLE, MSG_BODY);
		ExThreadI thread = f.viewThread(sessionId, MSG_TITLE);
		MSG_ID = thread.getMessages().getRoot().getId();
	}

	@After
	public void afterTest() throws ForumNotFoundException, PermissionDeniedException {
		FacadeI f = Facade.getFacade();
		f.removeForum(ADMIN_USER, ADMIN_PASS, FORUM_NAME);
	}

	@Test
	public void test() throws PasswordNotInEffectException, ForumNotFoundException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, MessageNotFoundException, PermissionDeniedException, SubForumDoesNotExistException, ThreadNotFoundException, SessionNotFoundException, DoesNotComplyWithPolicyException {
		FacadeI facade = getFacade();
		facade.addReply(sessionId, MSG_ID, REPLY_TITLE, REPLY_BODY);
		facade.viewThread(sessionId, MSG_TITLE);
	}

	private FacadeI getFacade() {
		return Facade.getFacade();
	}
}
