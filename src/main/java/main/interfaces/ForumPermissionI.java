package main.interfaces;

import main.exceptions.PermissionDeniedException;
import main.exceptions.SubForumAlreadyExistException;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ForumPermissionI {
	/**
	 * view subForums
	 */
	SubForumPermissionI[] viewSubForums();
	/**
	 * Create a subforum in this forum
	 */
	void createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException;

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException;

	/**
	 * Set new forum administrator
	 */
	void setAdmin(UserI admin)  throws PermissionDeniedException;

	/**
	 * Set policy for forum
	 */
	void setPolicy(ForumPolicyI policy)  throws PermissionDeniedException;

	/**
	 * Get statistics
	 */
	String viewStatistics()  throws PermissionDeniedException;

	/**
	 * Compare names
	 */
	boolean findForum(String name);

}
