package main.interfaces;

import main.exceptions.PermissionDeniedException;
import main.exceptions.SubForumAlreadyExistException;
import main.exceptions.SubForumDoesNotExistException;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ForumPermissionI {

	/**
	 * Create a subforum in this forum
	 */
	void createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException;

	boolean isAdmin();

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException, SubForumDoesNotExistException;

	/**
	 * Set this user to be forum administrator
	 */
	void setAdmin(UserI admin) throws PermissionDeniedException;

	/**
	 * Set policy for forum
	 */
	void setPolicy(ForumPolicyI policy)  throws PermissionDeniedException;

	/**
	 * Get statistics
	 */
	String viewStatistics();

	/**
	 * Find a subforum in this forum according to it's name
	 */
	boolean findSubforum(String name);

	Integer getId();

	String getForumName();   // returns forum name for which the permissions relate

	ForumI getForum();

	SubForumI getSubForum(String name);
}
