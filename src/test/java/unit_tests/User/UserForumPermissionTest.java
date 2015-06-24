package unit_tests.User;

import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.exceptions.*;
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
import java.util.Map;

import static org.junit.Assert.*;

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
    public void setUp() {
        int maxModerators = 1;
        String regex = "a-b";
        policy = new ForumPolicy(false, maxModerators, regex, 365);
        forum = new Forum("Gardening", policy);
        permission = new UserForumPermission(Permissions.PERMISSIONS_USER,"Gardening");
        permission2 = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,"Gardening");
        permission3 = new UserForumPermission(Permissions.PERMISSIONS_SUPERADMIN,"Gardening");

    }

    @After
    public void tearDown(){
        try {
            Forum.delete("Gardening");
        } catch (ForumNotFoundException e) {
        }
    }


    @Test
    public void testCreateSubForumForRegularUser() throws SubForumAlreadyExistException, ForumNotFoundException {
        try {
            permission.createSubForum("Football"); // PermissionDeniedException expected
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateSubForumForAdmin() throws PermissionDeniedException, ForumNotFoundException, SubForumAlreadyExistException {
        Map<String, SubForumI> subForums = forum.getSubForums();
        SubForumI subforum = new SubForum("Football", policy.getSubforumPolicy());
        assertFalse(subForums.containsKey(subforum.getTitle()));
        permission2.createSubForum("Football");
        forum = Forum.load(forum.getName());
        subForums = forum.getSubForums();
        assertTrue(subForums.containsKey(subforum.getTitle()));
    }

    private boolean contains(Collection<SubForumI> subForums, SubForumI subforum) {
        for (SubForumI subf : subForums) {
            if (subf.getTitle().equals(subforum.getTitle()))
                return true;
        }
        return false;
    }

    @Test
    public void testDeleteSubForumForRegularUser() throws SubForumDoesNotExistException, ForumNotFoundException, SubForumAlreadyExistException {
        try {
            permission.createSubForum("Zrima");
            permission.deleteSubForum("Zrima"); // PermissionDeniedException expected
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteSubForumForAdmin() throws PermissionDeniedException, ForumNotFoundException, SubForumAlreadyExistException, SubForumDoesNotExistException {
        Map<String, SubForumI> subForums;
        permission2.createSubForum("Zrima");
        forum = Forum.load(forum.getName());
        subForums = forum.getSubForums();
        assertTrue(subForums.containsKey("Zrima"));
        permission2.deleteSubForum("Zrima");
        forum = Forum.load(forum.getName());
        subForums = forum.getSubForums();
        assertFalse(subForums.containsKey("Zrima"));
    }


    @Test
    public void testSetAdminWithoutPermission() throws ForumNotFoundException, UserNotFoundException, CloneNotSupportedException {
        try {
            permission.setAdmin(new User("Shreder", "000", "XXX@gmail.com", permission));
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetAdmin() throws PermissionDeniedException, ForumNotFoundException, UserNotFoundException, CloneNotSupportedException {
        ForumPermissionI permission = new UserForumPermission(Permissions.PERMISSIONS_ADMIN,forum.getName());
        permission3.setAdmin(new User("Shreder", "000", "XXX@gmail.com",permission));
    }

    @Test
    public void testSetPolicy() throws PermissionDeniedException, ForumNotFoundException {
        try {
            permission.setPolicy(new ForumPolicy(true, 2, ".*", 10));
            fail();
        } catch (PermissionDeniedException e) {
            assertTrue(true);
            permission2.setPolicy(new ForumPolicy(true, 2, ".*", 10));
            permission3.setPolicy(new ForumPolicy(true, 2, ".*", 10));
        }
    }

    @Test
    public void testViewStatistics()   {
        fail();
    }
}