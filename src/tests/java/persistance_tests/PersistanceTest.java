package persistance_tests;

import junit.framework.TestCase;
import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.ForumPermissionI;
import tests_infrastructure.HibernateSessionFactory;
import org.hibernate.Session;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created by hagai_lvi on 4/22/15.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersistanceTest extends TestCase{

    private Session session;
    private static Integer policy_id;
    private static ForumPolicy forum_p;
    private static Forum forum;
    private static String forum_id;
    private static ForumPermissionI forum_per;
    private static Integer fper_id;
    private static User user;
    private static Integer uid;

    @Override
    public void setUp() throws Exception{
        session = HibernateSessionFactory.getSessionFactory().openSession();
    }

    @Test
    public void test1PolicySave(){
        session.beginTransaction();
        ForumPolicy forum_p = new ForumPolicy(5, "abc");
        session.save(forum_p);
        policy_id = forum_p.getId();
        session.getTransaction().commit();
    }

    @Test
    public void test2Policy_Load(){
        ForumPolicy p = (ForumPolicy)session.load(ForumPolicy.class, policy_id);
        assertEquals(p.getMaxModerators(), 5);
        assertEquals(p.getPasswordRegex(), "abc");
    }

    @Test  // added ignore flag because creating a forum automatically saves to db, so no need to save.
    public void test3Forum_Save(){
        session.beginTransaction();
        Forum forum = new Forum("Some forum", forum_p);
        //session.save(forum);
        forum_id = forum.getName();
        session.getTransaction().commit();
    }

    @Test
    public void test4Forum_Load(){
        Forum f = (Forum)session.load(Forum.class, forum_id);
        assertEquals(f.getName(), "Some forum");
    }


    @Test
    public void test5ForumPermissions_Save(){
        session.beginTransaction();
        forum_per = new UserForumPermission(Permissions.PERMISSIONS_ADMIN, forum);
        session.save(forum_per);
        fper_id = forum_per.getId();
        session.getTransaction().commit();
    }

    @Test
    public void test6User_Save(){
        session.beginTransaction();
        user = new User("unammeee", "passss", "fooo@foo.com", forum_per);
        session.save(user);
        uid = user.getId();
        session.getTransaction().commit();
    }

    @Test
    public void test7User_Load(){
        User uu = (User)session.load(User.class, uid);
        assertEquals(uu.getUsername(), user.getUsername());
        assertEquals(uu.getEmail(), user.getEmail());
    }

}