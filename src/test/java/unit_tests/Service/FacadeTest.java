package unit_tests.Service;


import main.exceptions.ForumNotFoundException;
import main.exceptions.SessionNotFoundException;
import main.interfaces.FacadeI;
import main.interfaces.ForumI;
import main.interfaces.SubForumI;
import main.services_layer.Facade;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by gabigiladov on 6/16/15.
 */
public class FacadeTest {
    private FacadeI theFacade;

    @Before
    public void setUp() throws Exception {
        theFacade = Facade.getFacade();
    }

    @Test
    public void testInitialize() throws Exception {
        theFacade.initialize();
        assertTrue(theFacade.getForumList().isEmpty());
    }

    @Test
    public void testGetForumList() throws Exception {
        theFacade.addForum("ADMIN", "ADMIN", "Sport", false, ".*", 1, 10);
        Collection<ForumI> list = theFacade.getForumList();
        assertTrue(findForum("Sport"));
        theFacade.removeForum("ADMIN", "ADMIN", "Sport");
        assertFalse(findForum("Sport"));
    }

    @Test
    public void testGetSubForumList() throws Exception {
        theFacade.addForum("ADMIN", "ADMIN", "Zrima", false, ".*", 2, 20);
        theFacade.register("Zrima", "Gabi", "0000", "a@a.com");
        int session = theFacade.login("Zrima", "Gabi", "0000");
        ForumI forum = theFacade.getForumList().iterator().next();
        forum.createSubForum("Baseball");
        forum.createSubForum("Tennis");
        Collection<SubForumI> list = theFacade.getSubForumList(session);
        assertTrue(findSubforum(session, "Baseball"));
        assertTrue(findSubforum(session, "Tennis"));
    }

    @Test
    public void testAddForum() throws Exception {

    }

    @Test
    public void testCreateSubforum() throws Exception {

    }

    @Test
    public void testRegister() throws Exception {

    }

    @Test
    public void testLogin() throws Exception {

    }

    @Test
    public void testLogout() throws Exception {

    }

    @Test
    public void testAddReply() throws Exception {

    }

    @Test
    public void testCreateNewThread() throws Exception {

    }

    @Test
    public void testReportModerator() throws Exception {

    }

    @Test
    public void testGetUserAuthString() throws Exception {

    }

    @Test
    public void testDeleteMessage() throws Exception {

    }

    @Test
    public void testSetModerator() throws Exception {

    }

    @Test
    public void testGuestEntry() throws Exception {

    }

    @Test
    public void testAddUserType() throws Exception {

    }

    @Test
    public void testRemoveForum() throws Exception {

    }

    @Test
    public void testSetPolicies() throws Exception {

    }

    @Test
    public void testEditMessage() throws Exception {

    }

    @Test
    public void testRemoveModerator() throws Exception {

    }

    @Test
    public void testViewModeratorStatistics() throws Exception {

    }

    @Test
    public void testViewSuperManagerStatistics() throws Exception {

    }

    @Test
    public void testViewSessions() throws Exception {

    }

    @Test
    public void testGetMessage() throws Exception {

    }

    @Test
    public void testGetThreadsList() throws Exception {

    }

    @Test
    public void testGetMessageList() throws Exception {

    }

    @Test
    public void testGetCurrentForumName() throws Exception {

    }

    @Test
    public void testGetCurrentUserName() throws Exception {

    }

    @Test
    public void testIsAdmin() throws Exception {

    }

    @Test
    public void testViewSubforum() throws Exception {

    }

    @Test
    public void testViewSubforum1() throws Exception {

    }

    @Test
    public void testViewThread() throws Exception {

    }

    @Test
    public void testGetCurrentThread() throws Exception {

    }

    @Test
    public void testAuthanticateUser() throws Exception {

    }

    @Test
    public void testGetFacade() throws Exception {

    }

    @Test
    public void testDropAllData() throws Exception {

    }

    private boolean findForum(String forumName) throws ForumNotFoundException {
        for (ForumI f: theFacade.getForumList()){
            if (f.getName().equals(forumName)){
                return true;
            }
        }
        return false;
    }

    private boolean findSubforum(int id, String subforum) throws SessionNotFoundException {
        for (SubForumI f: theFacade.getSubForumList(id)){
            if (f.getTitle().equals(subforum)){
                return true;
            }
        }
        return false;
    }
}