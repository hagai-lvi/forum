package gui_tests;

import main.exceptions.ForumAlreadyExistException;
import main.exceptions.ForumNotFoundException;
import main.exceptions.PermissionDeniedException;
import main.exceptions.SessionNotFoundException;
import main.interfaces.FacadeI;
import main.services_layer.Facade;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by hagai_lvi on 6/23/15.
 */
public class GuestEntryTest {

	private static final String ADMIN_PASS = "ADMIN";
	private static final String ADMIN_USER = "ADMIN";
	private String FORUM_NAME;

	@Before
	public void beforeTest() throws PermissionDeniedException, ForumAlreadyExistException {
		FacadeI f = Facade.getFacade();
		FORUM_NAME = this.getClass().getName();
		f.addForum(ADMIN_USER, ADMIN_PASS, FORUM_NAME,false, ".*", 10, Integer.MAX_VALUE);
	}

	@After
	public void afterTest() throws ForumNotFoundException, PermissionDeniedException {
		FacadeI f = Facade.getFacade();
		f.removeForum(ADMIN_USER, ADMIN_PASS, FORUM_NAME);
	}

	@Test
	public void test() throws ForumNotFoundException, SessionNotFoundException {
		FacadeI f = Facade.getFacade();
		int sessionID = f.guestEntry(FORUM_NAME);
		f.getSubForumList(sessionID);// Make sure no exception is thrown
		Assert.assertEquals( "Got incorrect forum name",f.getCurrentForumName(sessionID), FORUM_NAME);
	}
}
