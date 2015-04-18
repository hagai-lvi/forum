package main.User;

import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.ForumI;
import main.interfaces.ForumPolicyI;
import main.interfaces.UserI;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gabigiladov on 4/18/15.
 */
public class UserTest {

    private UserI user;

    @Before
    public void setUp() throws Exception {
        user = new User("Gabi", "123456", "mail@gmail.com");
    }

    @Test
    public void testSetUsername() throws Exception {
        String name = "Orli";
        assertFalse(user.getUsername().equals(name));
        user.setUsername(name);
        assertTrue(user.getUsername().equals(name));
    }

    @Test
    public void testIsEmailAuthenticated() throws Exception {
        assertFalse(user.isEmailAuthenticated());
        ForumI forum = new Forum("Sport", new ForumPolicy(1, "someRegex"));
        forum.sendAuthenticationEMail(user);
        assertFalse(forum.enterUserAuthenticationString(user, "bullshit"));
        assertTrue(forum.enterUserAuthenticationString(user, user.getUserAuthString()));
    }

    @Test
    public void testSetAuthenticated() throws Exception {

    }

    @Test
    public void testGetSubForumPermission() throws Exception {

    }

    @Test
    public void testGetUsername() throws Exception {

    }

    @Test
    public void testGetPassword() throws Exception {

    }

    @Test
    public void testSetPassword() throws Exception {

    }

    @Test
    public void testGetEmail() throws Exception {

    }

    @Test
    public void testSetEmail() throws Exception {

    }

    @Test
    public void testGetSignUpDate() throws Exception {

    }

    @Test
    public void testSetSignUpDate() throws Exception {

    }

    @Test
    public void testGetSeniorityInDays() throws Exception {

    }

    @Test
    public void testSetSeniorityInDays() throws Exception {

    }

    @Test
    public void testGetNumOfMessages() throws Exception {

    }

    @Test
    public void testSetNumOfMessages() throws Exception {

    }

    @Test
    public void testGetUserAuthString() throws Exception {

    }

    @Test
    public void testViewSubForums() throws Exception {

    }

    @Test
    public void testCreateSubForum() throws Exception {

    }

    @Test
    public void testDeleteSubForum() throws Exception {

    }

    @Test
    public void testCreateThread() throws Exception {

    }

    @Test
    public void testReplyToMessage() throws Exception {

    }

    @Test
    public void testReportModerator() throws Exception {

    }

    @Test
    public void testDeleteMessage() throws Exception {

    }

    @Test
    public void testSetAdmin() throws Exception {

    }

    @Test
    public void testSetPolicy() throws Exception {

    }

    @Test
    public void testViewStatistics() throws Exception {

    }

    @Test
    public void testSetModerator() throws Exception {

    }

    @Test
    public void testBanModerator() throws Exception {

    }

    @Test
    public void testAddSubForumPermission() throws Exception {

    }

    @Test
    public void testGetSubForumsPermissions() throws Exception {

    }

    @Test
    public void testSetSubForumsPermissions() throws Exception {

    }


    @Test
    public void testSetUsername1() throws Exception {

    }

    @Test
    public void testAddForum1() throws Exception {

    }

    @Test
    public void testIsEmailAuthnticated1() throws Exception {

    }

    @Test
    public void testSetAuthenticated1() throws Exception {

    }

    @Test
    public void testGetSubForumPermission1() throws Exception {

    }

    @Test
    public void testGetUsername1() throws Exception {

    }

    @Test
    public void testGetPassword1() throws Exception {

    }

    @Test
    public void testSetPassword1() throws Exception {

    }

    @Test
    public void testGetEmail1() throws Exception {

    }

    @Test
    public void testSetEmail1() throws Exception {

    }

    @Test
    public void testGetSignUpDate1() throws Exception {

    }

    @Test
    public void testSetSignUpDate1() throws Exception {

    }

    @Test
    public void testGetSeniorityInDays1() throws Exception {

    }

    @Test
    public void testSetSeniorityInDays1() throws Exception {

    }

    @Test
    public void testGetNumOfMessages1() throws Exception {

    }

    @Test
    public void testSetNumOfMessages1() throws Exception {

    }

    @Test
    public void testGetUserAuthString1() throws Exception {

    }

    @Test
    public void testViewSubForums1() throws Exception {

    }

    @Test
    public void testCreateSubForum1() throws Exception {

    }

    @Test
    public void testDeleteSubForum1() throws Exception {

    }

    @Test
    public void testCreateThread1() throws Exception {

    }

    @Test
    public void testReplyToMessage1() throws Exception {

    }

    @Test
    public void testReportModerator1() throws Exception {

    }

    @Test
    public void testDeleteMessage1() throws Exception {

    }

    @Test
    public void testSetAdmin1() throws Exception {

    }

    @Test
    public void testSetPolicy1() throws Exception {

    }

    @Test
    public void testViewStatistics1() throws Exception {

    }

    @Test
    public void testSetModerator1() throws Exception {

    }

    @Test
    public void testBanModerator1() throws Exception {

    }

    @Test
    public void testAddSubForumPermission1() throws Exception {

    }

    @Test
    public void testGetSubForumsPermissions1() throws Exception {

    }

    @Test
    public void testSetSubForumsPermissions1() throws Exception {

    }
}