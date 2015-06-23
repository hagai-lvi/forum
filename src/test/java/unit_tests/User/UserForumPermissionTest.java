package unit_tests.User;

import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.exceptions.ForumNotFoundException;
import main.exceptions.PermissionDeniedException;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.forum_contents.SubForum;
import main.interfaces.ForumI;
import main.interfaces.ForumPermissionI;
import main.interfaces.ForumPolicyI;
import main.interfaces.SubForumI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by gabigiladov on 4/25/15.
 */
public class UserForumPermissionTest {
    private UserForumPermission permission;
    private UserForumPermission permission2;
    private UserForumPermission permission3;

    private ForumI forum;
    private ForumPolicyI policy;

    @Before
    public void setUp() throws Exception {
        int maxModerators = 1;
        String regex = "a-b";
        policy = new ForumPolicy(false, maxModerators, regex, 365);
        forum = new Forum("Gardening", policy);
        permission = new UserForumPermission(Permissions.PERMISSIONS_USER,forum);
        permission2 = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,forum);
        permission3 = new UserForumPermission(Permissions.PERMISSIONS_SUPERADMIN,forum);

    }

    @After
    public void tearDown(){
        try {
            Forum.delete("Gardening");
        } catch (ForumNotFoundException e) {
        }
    }


    @Test(expected=PermissionDeniedException.class)
    public void testCreateSubForumForRegularUser() throws Exception {
        permission.createSubForum("Football"); // PermissionDeniedException expected
    }

    @Test
    public void testCreateSubForumForAdmin() throws Exception {
        Collection<SubForumI> subForums = forum.getSubForums();
        SubForumI subforum = new SubForum("Football", policy.getSubforumPolicy());
        assertFalse(contains(subForums, subforum));
        permission2.createSubForum("Football");
        subForums = forum.getSubForums();
        assertTrue(contains(subForums, subforum));
    }

    private boolean contains(Collection<SubForumI> subForums, SubForumI subforum) {
        for (SubForumI subf : subForums) {
            if (subf.getTitle().equals(subforum.getTitle()))
                return true;
        }
        return false;
    }

    @Test(expected=PermissionDeniedException.class)
    public void testDeleteSubForumForRegularUser() throws Exception {
        SubForumI subforum = new SubForum("Football", policy.getSubforumPolicy());
        permission.deleteSubForum(subforum); // PermissionDeniedException expected
    }

    @Test
    public void testDeleteSubForumForAdmin() throws Exception {
        Collection<SubForumI> subForums;
        SubForumI subforum = new SubForum("Baseball", policy.getSubforumPolicy());
        permission2.createSubForum("Baseball");
        subForums = forum.getSubForums();
        assertTrue(contains(subForums, subforum));
        permission2.deleteSubForum(subforum);
        subForums = forum.getSubForums();
        assertFalse(contains(subForums, subforum));
    }


    @Test(expected = PermissionDeniedException.class)
    public void testSetAdminWithoutPermission() throws Exception {
        ForumPermissionI permission = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,forum);
        permission2.setAdmin(new User("Shreder", "000", "XXX@gmail.com", permission));
    }

    @Test
    public void testSetAdmin() throws Exception {
        ForumPermissionI permission = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,forum);
        permission3.setAdmin(new User("Shreder", "000", "XXX@gmail.com",permission));
    }

    @Test
    public void testSetPolicy() throws Exception {

    }

    @Test
    public void testViewStatistics() throws Exception {

    }
}