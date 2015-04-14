package main.User;

import main.interfaces.UserI;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by gabigiladov on 4/11/15.
 */
public class UserPermissionTest {

    @Before
    public void setUp() throws Exception {
        UserI user = new User("Gabi", "123456", "mail");
    }

    @Test
    public void testCreateSubForum() throws Exception {

    }

    @Test
    public void testDeleteSubForum() throws Exception {

    }

    @Test
    public void testCreatThread() throws Exception {

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
}