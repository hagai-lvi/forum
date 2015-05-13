package acceptance_tests;

import com.google.common.collect.Iterables;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.*;
import main.services_layer.Facade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by hagai_lvi on 4/21/15.
 */
public class AcceptenceTest {


	public static final String USER_NAME = "user";
	public static final String PASS = "pass";
	private Forum myForum;
	private UserI user;


	@Test
	/**
	 * target: check regular user entrance: login + get Sub Forum List + view sub forum threads
	 */
/*	public void integration1(){
		FacadeI _facade = Facade.getFacade();

		ForumI forum = _facade.getForumList().iterator().next();
		try {
			UserI user = _facade.login(forum, "gil", "123456");
			assertNotNull(user);
			_facade.getSubForumList(user);
		}catch (InvalidUserCredentialsException e){
			fail("the user exist! but fail to find");
		}

	}
	@Test
	/**
	 * target: user login, remove message, and other user try to se it
	 */
/*	public void integration2() throws UserAlreadyExistsException, InvalidUserCredentialsException,
			SubForumAlreadyExistException, PermissionDeniedException, DoesNotComplyWithPolicyException, MessageNotFoundException {
		FacadeI facade = Facade.getFacade();

		SubForumPermissionI sf = Iterables.get(user.getSubForumsPermissions(), 0);
		String body = "body";
		facade.createNewThread(user, sf, "title", body);
		MessageI rootMessage = Iterables.get(user.getSubForumsPermissions(), 0).getThreads()[0].getRootMessage();
		assertEquals(body,
				rootMessage.getMessageText());
		user.deleteMessage(rootMessage, sf);

		assertEquals(0, Iterables.get(user.getSubForumsPermissions(), 0).getThreads().length);
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
	 * target: user try to be admin when he cannot (
	 */
	public void integration4(){
		//TODO this functionality is not yet implemented
	}

	@Before
	public void init() throws InvalidUserCredentialsException, PermissionDeniedException, SubForumAlreadyExistException, UserAlreadyExistsException {
		FacadeI facade = Facade.getFacade();
		ForumPolicyI policy = new ForumPolicy(1,".*");
		myForum = new Forum("MyForum", policy);
	/*	facade.addForum(myForum);

		UserI admin = facade.login(myForum, Forum.ADMIN_USERNAME, Forum.ADMIN_PASSWORD);

		facade.createSubforum("SF", admin);

		facade.register(myForum, USER_NAME, PASS, "me@me.me");
		user = facade.login(myForum, USER_NAME, PASS);
	*/
	}

	@After
	public void cleanUp(){
		Facade.dropAllData();
	}

}
