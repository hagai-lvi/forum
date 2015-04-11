package main_test;

import org.apache.log4j.Logger;
import org.junit.*;
import main.interfaces.*;
import main.exceptions.*;
import main.forum_contents.*;

import java.util.Collection;
import java.util.HashMap;


/**
 * Created by hagai_lvi on 4/6/15.
 */
public class MainTest {
	FacadeI _facade = Facade.getFacade();
	Collection<ForumI> _forumCollection = _facade.getForumList();

	private static Logger logger = Logger.getLogger(MainTest.class.getName());

	@BeforeClass
	public static void beforeClass(){
		System.out.println("Before class");
	}

	@Before
	public void beforeTest(){
		logger.error("fdsaf");
	}

	@After
	public void afterTest(){
		System.out.println("After test");

	}

	@AfterClass
	public static void afterClass(){
		System.out.println("After class");
	}


	/**
	 * UseCases:
	 * 		1. initilize
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
	 *		16. CancelForum
	 *
	 *
	 */
	@Test
	public void Test_initilize(){

	}

	@Test
	public void Test_CreateForum(){

	}

	@Test
	public void Test_SetPolicies(){

	}

	@Test
	public void Test_GuestEntry(){

	}

	@Test
	public void Test_Register(){

	}

	@Test
	public void Test_Login(){

	}

	@Test
	public void Test_Logout(){

	}

	@Test
	public void Test_CreateSubForum(){

	}

	@Test
	public void Test_ViewSubForum(){

	}

	@Test
	public void Test_PostThread(){

	}

	@Test
	public void Test_PostMessage(){

	}

	@Test
	public void Test_FriendType(){

	}

	@Test
	public void Test_ComplainOnModerator(){

	}

	@Test
	public void Test_EmailAuthentication(){

	}

	@Test
	public void Test_RemoveMessage(){

	}

	@Test
	public void Test_CancelForum(){

	}

}