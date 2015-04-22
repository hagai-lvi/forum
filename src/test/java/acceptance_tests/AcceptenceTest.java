package acceptance_tests;

import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.*;
import main.services_layer.Facade;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hagai_lvi on 4/21/15.
 */
public class AcceptenceTest {


	@Test
	/**
	 * target: user login, remove message, and other user try to se it
	 */
	public void integration2() throws UserAlreadyExistsException, InvalidUserCredentialsException,
			SubForumAlreadyExistException, PermissionDeniedException, DoesNotComplyWithPolicyException, MessageNotFoundException {
		//init
		FacadeI facade = Facade.getFacade();
		final String user = "user";
		final String pass = "pass";


		ForumPolicyI policy = new ForumPolicy(1,".*");
		Forum forum = new Forum("MyForum",policy);
		facade.addForum(forum);


		UserI admin = facade.login(forum, Forum.ADMIN_USERNAME, Forum.ADMIN_PASSWORD);

		facade.createSubforum("SF",admin);

		facade.register(forum, user, pass, "me@me.me");
		UserI u = facade.login(forum, user, pass);
		//Test

		SubForumPermissionI sf = u.viewSubForums().get(0);
		String body = "body";
		facade.createNewThread(u, sf, "title", body);
		MessageI rootMessage = u.viewSubForums().get(0).getThreads()[0].getRootMessage();
		assertEquals(body,
				rootMessage.getMessageText());
		u.deleteMessage(rootMessage, sf);


		assertEquals(0, u.viewSubForums().get(0).getThreads().length);
	}

	@Test
	/**
	 * target: change policy that have conflict with the former policy.
	 */
	public void integration3(){

	}

	@Test
	/**
	 * target: user try to be admin when he cannot (
	 */
	public void integration4(){

	}
}
