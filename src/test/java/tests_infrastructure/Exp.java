package tests_infrastructure;

import main.exceptions.EmailNotAuthanticatedException;
import main.exceptions.InvalidUserCredentialsException;
import main.exceptions.PasswordNotInEffectException;
import main.exceptions.UserAlreadyExistsException;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.UserI;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hagai_lvi on 4/21/15.
 */
public class Exp {

	@Test
	public void createUserTest() throws UserAlreadyExistsException, InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException {
		ForumPolicy policy = new ForumPolicy(10,".*");
		Forum f = new Forum("a", policy);
		UserI user = f.register("a", "a", "a");
		UserI user2 = f.login("a","a");
		assertEquals(user.getUsername(), user2.getUsername());
		assertEquals(user.getPassword(), user2.getPassword());
	}

}
