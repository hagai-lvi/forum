package main.Content;

import main.exceptions.InvalidUserCredentialsException;
import main.exceptions.UserAlreadyExistsException;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.ForumI;
import main.interfaces.ForumPolicyI;
import main.interfaces.UserI;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by tomgond on 4/11/15.
 */
public class ForumTest {

    private ForumI forum = null;

    @Before
    public void setUp() throws Exception {
        ForumPolicyI policy = new ForumPolicy(5,".*");
        forum = new Forum("ForumName", policy);
    }

    @Test
    public void testRegisterGood() throws Exception {
        UserI user = forum.register("username", "pass", "user@somemail.com");
        assertEquals(user.getUsername(), "username");
        assertEquals(user.getPassword(), "pass");
        assertEquals(user.getEmail(), "user@somemail.com");
    }


    @Test
    public void testRegisterBad() throws Exception {
        UserI user = null;
        try {
            user = forum.register("username", "pass", "user@somemail.com");
        }
        catch(UserAlreadyExistsException e){
            assertTrue(true);  // we should raise exception caus the user exists
            //TODO should the test fail?
        }
    }

    @Test
    public void testRegisterInvalidDetails() throws Exception {
        UserI user = null;
        try {
            user = forum.register("", null, "user@somemail.com");
        }
        catch(UserAlreadyExistsException e){
            assertTrue(false);  // we should raise exception caus the user exists
        }
        catch (InvalidUserCredentialsException e) {
            assertTrue(true);   // we had an exception for bad details
        }
    }

    @Test
    public void testSendMail() throws Exception {
        UserI user = null;
        try {
            user = forum.register("tomgond_new1", "my_pass", "someinvalidemail@fufufu.com"); // TODO: change to valid email to check if sent
        }
        catch(UserAlreadyExistsException e){
            assertTrue(false);  // we should raise exception caus the user exists
        }
        catch (InvalidUserCredentialsException e) {
            assertTrue(false);   // we had an exception for bad details
        }
        try {
            forum.sendAuthenticationEMail(user);
        }
        catch(Exception e){
            assertTrue(false);
        }
        assertTrue(true);  // if we got here we sent.
    }



}