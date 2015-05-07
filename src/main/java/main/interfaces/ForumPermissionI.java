package main.interfaces;

import main.exceptions.PermissionDeniedException;
import main.exceptions.SubForumAlreadyExistException;
import main.exceptions.SubForumDoesNotExsitsException;

import java.util.Collection;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ForumPermissionI {

	/**
	 * Create a subforum in this forum
	 */
	void createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException;

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException, SubForumDoesNotExsitsException;

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

	/**
	 * Check if the user has admin privileges
	 */
	boolean isAdmin();
	Integer getId();

}
