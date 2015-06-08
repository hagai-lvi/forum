package unit_tests.service_layer;

import main.exceptions.ForumAlreadyExistException;
import main.exceptions.PermissionDeniedException;
import main.interfaces.FacadeI;
import main.services_layer.Facade;
import org.junit.Test;

/**
 * Created by hagai_lvi on 6/8/15.
 */
public class FacadeTest {

	@Test(expected = ForumAlreadyExistException.class)
	public void testAddForumMultipleTimes() throws PermissionDeniedException, ForumAlreadyExistException {
		FacadeI facade = Facade.dropAllData();
		facade.addForum("ADMIN", "ADMIN", false, "MyForum", ".*", 10, Integer.MAX_VALUE);


		facade.addForum("ADMIN", "ADMIN", false, "MyForum", ".*", 10, Integer.MAX_VALUE);
	}
}
