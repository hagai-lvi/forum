package unit_tests.Content;

import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.ForumI;
import main.interfaces.ForumPolicyI;
import main.interfaces.UserI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by tomgond on 4/11/15.
 */
public class ForumTest {

        private ForumI forum;

        @Before
        public void setUp() throws Exception {
            ForumPolicyI policy = new ForumPolicy(false, 5,".*", 365);
            forum = new Forum("ForumName", policy);
        }

        @After
        public void tearDown(){
            try {
                Forum.delete("ForumName");
            } catch (ForumNotFoundException e) {

            }
        }

        @Test
        public void testRegisterGood() throws Exception {
            UserI user = forum.register("username", "pass", "user@somemail.com");
            assertEquals(user.getUsername(), "username");
            assertEquals(user.getPassword(), "pass");
            assertEquals(user.getEmail(), "user@somemail.com");
        }


        @Test(expected = UserAlreadyExistsException.class)
        public void testRegisterBad() throws UserAlreadyExistsException{
            try {
                forum.register("username", "pass", "user@somemail.com");
                forum.register("username", "pass", "user@somemail.com");
            } catch (InvalidUserCredentialsException e) {
                fail(e.getMessage());
            } catch (DoesNotComplyWithPolicyException e) {
                e.printStackTrace();
            }

            try {
                forum.register("username", "pass", "mail.com");
            } catch (InvalidUserCredentialsException e) {
                fail(e.getMessage());
            } catch (DoesNotComplyWithPolicyException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void testRegisterInvalidDetails() {
            try {
               forum.register("", null, "user@somemail.com");
            }
            catch(UserAlreadyExistsException | DoesNotComplyWithPolicyException e){
                fail("should not throw this exception");
            }
            catch (InvalidUserCredentialsException e) {
                assertTrue(true);   // we had an exception for bad details
            }

            try {
                forum.register("user", "pass", "somemail.com");
            } catch (UserAlreadyExistsException | DoesNotComplyWithPolicyException e) {
                fail("should not throw this exception");
            } catch (InvalidUserCredentialsException e) {
                assertTrue(true);
            }
        }

        @Test
        public void testSendMail() throws Exception {
            UserI user = null;
            try {
                user = forum.register("tomgond_new1", "my_pass", "someinvalidemail@fufufu.com"); // TODO: change to valid email to check if sent
            }
            catch(UserAlreadyExistsException e){
                fail("user already exists!");  // we should raise exception caus the user exists
            }
            catch (InvalidUserCredentialsException e) {
                fail("bad user credentials!");   // we had an exception for bad details
            }
            try {
                forum.sendAuthenticationEMail(user);
            }
            catch(Exception e){
                assertTrue(false);
            }
            assertTrue(true);  // if we got here we sent.
        }

        @Test
        public void testViewStatistics() {
            fail("not yet implemented");
        }

        @Test
         public void testAddSubForum() {
            try {
                forum.addSubForum("newSF");
            } catch (SubForumAlreadyExistException e) {
                fail("subforum did not exist");
            }
            try {
                forum.addSubForum("newSF");
            } catch (SubForumAlreadyExistException e) {
                assertTrue(true);
                return;
            }
            fail("added a subforum twice");
        }

        @Test
         public void testSetPolicy() {
            ForumPolicyI fp = new ForumPolicy(false, 1, "aaa", 365);
            forum.setPolicy(fp);
            try {
                forum.register("test", "test", "test@test.test");
            } catch (UserAlreadyExistsException e) {
                fail("user did not exist");
            } catch (InvalidUserCredentialsException e) {
                fail("invalid user credentials");
            } catch (DoesNotComplyWithPolicyException e) {
                assertTrue(true);
            }
            try {
                forum.register("test", "aaa", "test@test.test");
            } catch (DoesNotComplyWithPolicyException | UserAlreadyExistsException | InvalidUserCredentialsException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void testDeleteSubForum() {
            try {
                forum.addSubForum("newsf");
            } catch (SubForumAlreadyExistException e) {
                fail("failed to add subforum");
            }

            try {
                forum.deleteSubForum("newsf");
            } catch (SubForumDoesNotExistException e) {
                fail("failed remove subforum");
            }

            try {
                forum.deleteSubForum("newsf");
            } catch (SubForumDoesNotExistException e) {
                assertTrue(true);
                return;
            }
            fail("deleted a subforum twice");
        }

    @Test
    public void testDeleteNonExistentSubForum() {
        try {
            forum.deleteSubForum("newsf2");
        } catch (SubForumDoesNotExistException e) {
            assertTrue(true);
            return;
        }
        fail("deleted non-existent subforum");
    }


    @Test
    public void testLogin() {
        UserI user = null;
        try {
            user = forum.register("user", "pass", "mail@mail.com");
        } catch (UserAlreadyExistsException | InvalidUserCredentialsException | DoesNotComplyWithPolicyException e) {
            fail("failed to register");
        }
        try {
            forum.enterUserAuthenticationString(user, user.getUserAuthString());
        } catch (InvalidUserCredentialsException e) {
            e.printStackTrace();
        }
        try {
            forum.login("user", "pass");
        } catch (InvalidUserCredentialsException | PasswordNotInEffectException | EmailNotAuthanticatedException | NeedMoreAuthParametersException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoginNotAuthenticated() {
        UserI user = null;
        try {
            user = forum.register("user", "pass", "mail@mail.com");
            forum.login("user", "pass");
        } catch (UserAlreadyExistsException | InvalidUserCredentialsException | DoesNotComplyWithPolicyException e) {
            fail("failed to register");
        } catch (PasswordNotInEffectException | NeedMoreAuthParametersException e) {
            fail("failed to log in");
        } catch (EmailNotAuthanticatedException e) {
            assertTrue(true);
            return;
        }
        fail("logged in unauthenticated");
    }

    @Test
    public void testLoginToSecured() {
        UserI user = null;
        try {
            user = forum.register("user", "pass", "mail@mail.com");
        } catch (UserAlreadyExistsException | InvalidUserCredentialsException | DoesNotComplyWithPolicyException e) {
            fail("failed to register");
        }
        forum.setPolicy(new ForumPolicy(true, 2, ".*", 365));
        try {
            forum.enterUserAuthenticationString(user, user.getUserAuthString());
        } catch (InvalidUserCredentialsException e) {
            fail("could not authenticate");
        }
        try {
            forum.login("user", "pass");
        } catch (InvalidUserCredentialsException | EmailNotAuthanticatedException | PasswordNotInEffectException e) {
           fail("failed to log on");
        } catch (NeedMoreAuthParametersException e) {
            assertTrue(true);
            return;
        }
        fail("logged in without security questions");
    }


    @Test
    public void testLoginPasswordExpired() {
        UserI user = null;
        try {
            user = forum.register("user", "pass", "mail@mail.com");
        } catch (UserAlreadyExistsException | InvalidUserCredentialsException | DoesNotComplyWithPolicyException e) {
            fail("failed to register");
        }
        forum.setPolicy(new ForumPolicy(false, 2, ".*", 0));
        try {
            forum.enterUserAuthenticationString(user, user.getUserAuthString());
        } catch (InvalidUserCredentialsException e) {
            fail("could not authenticate");
        }
        try {
            forum.login("user", "pass");
        } catch (InvalidUserCredentialsException | EmailNotAuthanticatedException | NeedMoreAuthParametersException e) {
            fail("failed to log on");
        } catch (PasswordNotInEffectException e) {
            assertTrue(true);
            return;
        }
        fail("logged in with with expired password");
    }


}