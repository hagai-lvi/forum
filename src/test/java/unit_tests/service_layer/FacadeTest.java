package unit_tests.service_layer;

import main.exceptions.ForumAlreadyExistException;
import main.exceptions.PermissionDeniedException;
import main.interfaces.FacadeI;
import main.services_layer.Facade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by hagai_lvi on 6/8/15.
 */
public class FacadeTest {

	@Test(expected = ForumAlreadyExistException.class)
	public void testAddForumMultipleTimes() throws PermissionDeniedException, ForumAlreadyExistException {
		FacadeI facade = Facade.dropAllData();
		facade.addForum("ADMIN", "ADMIN", "MyForum", false, ".*", 10, Integer.MAX_VALUE);
		facade.addForum("ADMIN", "ADMIN", "MyForum", false, ".*", 10, Integer.MAX_VALUE);
	}

	@Before
	public void setUp() throws Exception {


	}
	@After
	public void tearDown() throws Exception {

	}
	@Test
	public void testInitilize() throws Exception {

	}
	@Test
	public void testGetForumList() throws Exception {

	}
	@Test
	public void testGetSubForumList() throws Exception {

	}
	@Test
	public void testAddForum() throws Exception {

	}
	@Test
	public void testCreateSubforum() throws Exception {

	}
	@Test
	public void testRegister() throws Exception {

	}
	@Test
	public void testLogin() throws Exception {

	}
	@Test
	public void testLogout() throws Exception {

	}
	@Test
	public void testAddReply() throws Exception {

	}
	@Test
	public void testCreateNewThread() throws Exception {

	}
	@Test
	public void testReportModerator() throws Exception {

	}
	@Test
	public void testGetUserAuthString() throws Exception {

	}
	@Test
	public void testDeleteMessage() throws Exception {

	}
	@Test
	public void testSetModerator() throws Exception {

	}
	@Test
	public void testGuestEntry() throws Exception {

	}
	@Test
	public void testAddUserType() throws Exception {

	}
	@Test
	public void testRemoveForum() throws Exception {

	}
	@Test
	public void testSetPolicies() throws Exception {

	}
	@Test
	public void testEditMessage() throws Exception {

	}
	@Test
	public void testRemoveModerator() throws Exception {

	}
	@Test
	public void testViewModeratorStatistics() throws Exception {

	}
	@Test
	public void testViewSuperManagerStatistics() throws Exception {

	}
	@Test
	public void testViewSessions() throws Exception {

	}
	@Test
	public void testGetMessage() throws Exception {

	}
	@Test
	public void testGetThreadsList() throws Exception {

	}
	@Test
	public void testGetMessageList() throws Exception {

	}
	@Test
	public void testGetCurrentForumName() throws Exception {

	}
	@Test
	public void testGetCurrentUserName() throws Exception {

	}
	@Test
	public void testIsAdmin() throws Exception {

	}
	@Test
	public void testViewSubforum() throws Exception {

	}
	@Test
	public void testViewSubforum1() throws Exception {

	}
	@Test
	public void testViewThread() throws Exception {

	}
	@Test
	public void testGetCurrentThread() throws Exception {

	}
	@Test
	public void testAuthenticateUser() throws Exception {

	}
	@Test
	public void testGetFacade() throws Exception {

	}
	@Test
	public void testDropAllData() throws Exception {

	}
}
