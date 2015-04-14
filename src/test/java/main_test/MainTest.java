package main_test;

import main.User.User;
import main.exceptions.*;
import main.forum_contents.Facade;
import main.forum_contents.Forum;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumPolicy_R1;
import main.interfaces.*;
import org.apache.log4j.Logger;
import org.junit.*;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;


/**
 * Created by hagai_lvi on 4/6/15.
 */
public class MainTest {
	FacadeI _facade;
	Collection<ForumI> _forumCollection;

	private static Logger logger = Logger.getLogger(MainTest.class.getName());


	@Before
	public void setUp() throws Exception
	{
		String[] Names = {"gil","tom","hagai", "gabi", "victor", "aria", "yoni", "moshe",
						  "tal", "chen", "bibi", "mor", "david", "dudinka", "aaa"};
		_facade = Facade.getFacade();
		ForumPolicyI policy = new ForumPolicy_R1(3,"[a-zA-Z]*[!@#$][a-zA-Z]");
		for(int i=0;i<5;i++) {
			ForumI newForum = new Forum(policy);
			_facade.addForum(newForum);

			//add users to forums
			for (int j=0;j<3;j++) {
				try {
					newForum.register(Names[i * 3 + j], "123456", "nobodyemail@nobody.com");
				}catch (Throwable e){

				}
				newForum.createSubForum("SubForum "+j +" In Forum" + i);
			}

		}



		_forumCollection = _facade.getForumList();


	}


	@BeforeClass
	public static void beforeClass(){
		System.out.println("Before class");
	}

	@Before
	public void beforeTest(){
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

	/**
	 * target: check initilize, should return true.
	 */
	@Test
	public void Test_initilize(){
		assertTrue(_facade.InitilizeSystem());
	}

	/**
	 * target: check adding forum to the system.
	 */
	@Test
	public void Test_CreateForum(){

		ForumI newForum;
		ForumPolicyI newPolicy = new ForumPolicy_R1(2, "[a-z]*[!@#][a-z]*");
		newForum = new Forum(newPolicy);
		int numOfForums = _forumCollection.size();

		_facade.addForum(newForum);

		Collection<ForumI> newCollection =  _facade.getForumList();
		assertEquals(numOfForums + 1, newCollection.size());


		assert (newCollection.contains(newForum));
	}

	@Test
	/**
	 * target: set new policy for forum
	 */
	public void Test_SetPolicies(){
		ForumPolicyI newPolicy = new ForumPolicy_R1(2, "[a-z]*[!@#\\d]*[\\d]*");
		ForumI forum = _forumCollection.iterator().next();
		forum.setPolicy(newPolicy);


	}

	@Test
	/**
	 * target: the test check the permission given to a guest.
	 * check negative test like getting the right exception on violate his permission
	 */
	public void Test_GuestEntry(){
		ForumI forum = _forumCollection.iterator().next();
		UserI guest = forum.guestLogin();

		Collection<SubForumPermissionI> subForumPermissionsCollection = guest.getSubForumPermission();

		MessageI msg = new ForumMessage(null ,guest,"I TRY TO CREATE MESSAGE NANA", "");
		SubForumPermissionI subForumPermission = subForumPermissionsCollection.iterator().next();

		//try create thread
		try {
			subForumPermission.createThread(msg);
			fail("a guest cannot create a thread");
		}catch (DoesNotComplyWithPolicyException e) {
			fail("Expected PermissionDeniedException");
		} catch (PermissionDeniedException e) {
			//expected exception
		}

		//try reply to message
		try{
			//this test will not be include yet
			//subForumPermission.replyToMessage(msg,msg);
			MessageI reply = new ForumMessage(null, guest, "I TRY TO CREATE REPLY NANA", "");
			subForumPermission.replyToMessage(msg, reply);
			fail("a guest cannot reply to message");
		} catch (PermissionDeniedException e) {
			fail("User could not reply to message");
		} catch (Exception e){
			fail("a guest cannot reply to message");
		}

		//try report moderator
		try{
			subForumPermission.reportModerator("Moshe","He is so bad Moderator",guest);
			fail("a guest cannot create a report on moderator");
		//} catch (PermissionDenied e){

		} catch (Exception e){
			fail("a guest cannot create a report on moderator");
		} catch (ModeratorDoesNotExistsException e) {
			//expected exception
		}

		//try view threads
		ThreadI[] threads = new ThreadI[0];
		try {
			threads = subForumPermission.getThreads();
		} catch (PermissionDeniedException e) {
			e.printStackTrace();
		}
		MessageI rootMessage = threads[0].getRootMessage();

		//try delete message
		try{
			subForumPermission.deleteMessage(rootMessage,guest);
			fail("a guest cannot delete message");

		} catch (PermissionDeniedException e) {
			// expected exception
		}
	}

	@Test
	/**
	 * target: check register usecase for new user
	 * check that the user exist in the list after register and that user cannot register twice
	 * check if you get email authentication message in your inbox
	 */
	public void Test_Register() {

		ForumI forum = _forumCollection.iterator().next();
		UserI user = new User("gilgilmor", "morgil12345", "gilmor89@gmail.com");
		try {
			user = forum.register("gilgilmor", "morgil12345", "gilmor89@gmail.com");
			forum.register("gilgilmor", "morgil12345", "gilmor89@gmail.com");
			fail("register the same user");
		} catch (UserAlreadyExistsException alreadyExistE){
			assertTrue(true);
		} catch (InvalidUserCredentialsException invalidE){
			fail("wrong exception, register the same user");
		}finally {
			Collection<UserI> users = forum.getUserList();
			assertTrue(users.contains(user));
			System.out.println("Asserted : " + users.contains(user));
		}
	}

	/**
	 *  targer: check login usecase, try login to non exist user
	 */
	@Test
	public void Test_Login(){
		ForumI forum = _forumCollection.iterator().next();
		UserI user;
		try {
			user = forum.register("gilgilmor2", "morgil12345", "gilmor89@gmail.com");
			UserI sameUser = forum.login("gilgilmor2", "morgil12345");
			assertSame("Not the same user when login", user, sameUser);
		}catch (Throwable e) {
			fail("fail to register new user");
		}

		try {
			forum.login("notexist", "user");
			fail("should throw exception");
		}catch (InvalidUserCredentialsException e) {
			assertTrue(true);
		}catch (Throwable e){
			fail("should throw exception");
		}

	}

	@Test
	/**
	 * target: check logout usecase
	 */
	public void Test_Logout(){
		ForumI forum = _forumCollection.iterator().next();
		UserI user;
		try {
			user = forum.register("some_new_user", "morgil12345", "gilmor89@gmail.com");
			UserI sameUser = forum.login("some_new_user", "morgil12345");
			forum.logout(sameUser);
		}catch (Throwable e) {
			fail("fail to logout");
		}
		assertTrue(true);

	}

	@Test
	/**
	 * taget: check Create sub forum usecase
	 */
	public void Test_CreateSubForum(){
		ForumI forum = _forumCollection.iterator().next();
		try {
			forum.createSubForum("juggling");
		}catch (SubForumAlreadyExistException e){
			fail("cannot create new sub forum");
		}
	}

	@Test
	/**
	 * target: view sub forum
	 */
	public void Test_ViewSubForum(){
		ForumI forum = _forumCollection.iterator().next();
		UserI user = forum.getUserList().iterator().next();
		Collection<SubForumPermissionI> subForumPermissionCol = user.getSubForumPermission();
		for (Iterator<SubForumPermissionI> itr = subForumPermissionCol.iterator(); itr.hasNext();){
			SubForumPermissionI subForumPermission = itr.next();
			try {
				ThreadI[] threads = subForumPermission.getThreads();
			} catch (PermissionDeniedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	/**
	 * target: test usecase post thread
	 */
	public void Test_PostThread(){
		ForumI forum = _forumCollection.iterator().next();
		UserI user = forum.getUserList().iterator().next();
		Collection<SubForumPermissionI> subForumPermissionCol = user.getSubForumPermission();
		SubForumPermissionI subForumPermission = subForumPermissionCol.iterator().next();
		int n = 0;
		try {
			n = subForumPermission.getThreads().length;
		} catch (PermissionDeniedException e) {
			e.printStackTrace();
		}
		try {
			subForumPermission.createThread(new ForumMessage(null, user, "I created THREADDDDDD!@!@!@!@", ""));
		} catch (PermissionDeniedException e) {
			e.printStackTrace();
		} catch (DoesNotComplyWithPolicyException e) {
			e.printStackTrace();
		}
		try {
			assertEquals(n + 1, subForumPermission.getThreads().length);
		} catch (PermissionDeniedException e) {
			e.printStackTrace();
		}
	}

	@Test
	/**
	 * target: test post message usecase
	 */
	public void Test_PostMessage(){
		ForumI forum = _forumCollection.iterator().next();
		UserI user = forum.getUserList().iterator().next();
		Collection<SubForumPermissionI> subForumPermissionCol = user.getSubForumPermission();
		SubForumPermissionI subForumPermission = subForumPermissionCol.iterator().next();
		ThreadI firstThread = null;
		try {
			firstThread = subForumPermission.getThreads()[0];
		} catch (PermissionDeniedException e) {
			e.printStackTrace();
		}
		firstThread.getRootMessage().reply(new ForumMessage(null, user, "I post Message", ""));
	}

	@Test
	/**
	 * target test Friend Type requirement
	 */
	public void Test_FriendType(){
		ForumI forum = _forumCollection.iterator().next();
		int n = forum.getUserTypes().length;
		forum.addUserType("GoldenX");
		assertEquals(n + 1, forum.getUserTypes().length);
	}


	/**
	@Test
	 * target: test remove message usecase, check that user can remove only
	 * 			his messages.

	 public void Test_RemoveMessage(){
	 ForumI forum = _forumCollection.iterator().next();
	 Iterator<UserI> userItr = forum.getUserList().iterator();
	 UserI userA = userItr.next();
	 UserI userB = userItr.next();

	 MessageI msg = new ForumMessage(userA,"userA message");
	 userA.addMessage(msg);
	 try {
	 userB.removeMessage(msg);
	 fail("user should not bre able to remove other user's message");
	 }catch (PermissionDenied e){
	 assertTrue(true);
	 }
	 }
	 */
	@Test
	/**
	 * target: test cancel forum usecase
	 */
	public void Test_CancelForum(){
		ForumI forum = _forumCollection.iterator().next();
		UserI userA = forum.getUserList().iterator().next();


	}

	@Test
	/**
	 * target: check use case send report on moderator
	 */
	public void Test_ComplainOnModerator(){
		ForumI forum = _forumCollection.iterator().next();
		UserI user = forum.getUserList().iterator().next();
		Collection<SubForumPermissionI> subForumPermissionCol = user.getSubForumPermission();
		SubForumPermissionI subForumPermission = subForumPermissionCol.iterator().next();

		//add check to see if moshe his a moderator.
		try {
			subForumPermission.reportModerator("Moshe","he is not behave well!!", user);
		} catch (PermissionDeniedException e) {
			e.printStackTrace();
		} catch (ModeratorDoesNotExistsException e) {
			e.printStackTrace();
		}

	}

}