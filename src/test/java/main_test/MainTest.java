package main_test;

import junit.framework.*;
import static org.junit.Assert.*;

import main.User.User;
import org.apache.log4j.Logger;
import org.junit.*;
import main.interfaces.*;
import main.exceptions.*;
import main.forum_contents.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


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
		_facade = Facade.getFacade();
		//_facade.`
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
		assert(_facade.InitilizeSystem());
	}

	/**
	 * target: check adding forum to the system.
	 */
	@Test
	public void Test_CreateForum(){
		/*
		ForumI newForum;
		ForumPolicyI newPolicy = new ForumPolicy_R1(newForum, 2, "[a-z]*[!@#][a-z]*");
		newForum = new Forum(newPolicy);
		int numOfForums = _forumCollection.size();

		_facade.addForum(newForum);

		Collection<ForumI> newCollection =  _facade.getForumList();
		assertEquals(numOfForums + 1, newCollection.size());


		assert (newCollection.contains(newForum));*/
	}

	@Test
	public void Test_SetPolicies(){

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

		MessageI msg = new ForumMessage();
		SubForumPermissionI subForumPermission = subForumPermissionsCollection.iterator().next();

		//try create thread
		try {
			subForumPermission.createThread(msg);
			fail("a guest cannot create a thread");
			//} catch (PermissionDenied e){

			//}
		}catch (Exception e){
			fail("a guest cannot create a thread");
		}

		//try reply to message
		try{
			//this test will not be include yet
			//subForumPermission.replyToMessage(msg,msg);
			MessageI reply = new ForumMessage();
			subForumPermission.replyToMessage(msg, reply);
			fail("a guest cannot reply to message");
		//} catch (PermissionDenied e){

		} catch (Exception e){
			fail("a guest cannot reply to message");
		}

		//try report moderator
		try{
			subForumPermission.reportModerator("Moshe","He is so bad Moderator");
			fail("a guest cannot create a report on moderator");
		//} catch (PermissionDenied e){

		} catch (Exception e){
			fail("a guest cannot create a report on moderator");
		}

		//try view threads
		ThreadI[] threads = subForumPermission.getThreads();
		MessageI rootMessage = threads[0].getRootMessage();

		//try delete message
		try{
			subForumPermission.deleteMessage(rootMessage);
			fail("a guest cannot delete message");
		//} catch (PermissionDenied e){

		} catch (Exception e){
			fail("a guest cannot delete message");
		}
	}

	@Test
	/**
	 * target: check register usecase for new user
	 * check that the user exist in the list after register and that user cannot register twice
	 */
	public void Test_Register() {

		ForumI forum = _forumCollection.iterator().next();
		UserI user =new User("gilgilmor", "morgil12345", "gilmor89@gmail.com");
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
			assert (users.contains(user));
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
			user = forum.register("gilgilmor", "morgil12345", "gilmor89@gmail.com");
			UserI sameUser = forum.login("gilgilmor", "morgil12345");
			assertSame("Not the same user when login", user, sameUser);
		}catch (Throwable e) {
			fail("fail to register new user");
		}

		try{
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
			user = forum.register("gilgilmor", "morgil12345", "gilmor89@gmail.com");
			UserI sameUser = forum.login("gilgilmor", "morgil12345");
			forum.logout(sameUser);
		}catch (Throwable e) {
			fail("fail to logout");
		}

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
			ThreadI[] threads = subForumPermission.getThreads();
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
		int n = subForumPermission.getThreads().length;
		subForumPermission.createThread(new ForumMessage());
		assertEquals(n+1,subForumPermission.getThreads().length);
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