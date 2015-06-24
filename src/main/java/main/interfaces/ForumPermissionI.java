package main.interfaces;

import main.User.Permissions;
import main.exceptions.*;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ForumPermissionI {

	/**
	 * Create a subforum in this forum
	 */
	SubForumI createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException, ForumNotFoundException;

	boolean isAdmin();

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException, SubForumDoesNotExistException, ForumNotFoundException;

	/**
	 * Set this user to be forum administrator
	 */
	void setAdmin(UserI admin) throws PermissionDeniedException, ForumNotFoundException, CloneNotSupportedException, UserNotFoundException;

	/**
	 * Set policy for forum
	 */
	void setPolicy(ForumPolicyI policy) throws PermissionDeniedException, ForumNotFoundException;

	/**
	 * Get statistics
	 */
	String viewStatistics();

	/**
	 * Find a subforum in this forum according to it's name
	 */
	boolean findSubforum(String name) throws ForumNotFoundException;

	Integer getId();

	String getForumName();   // returns forum name for which the permissions relate

	ForumI getForum() throws ForumNotFoundException;

	SubForumI getSubForum(String name) throws ForumNotFoundException, SubForumDoesNotExistException;

	boolean isGuest();

	Permissions getPermission();

	//void becomeAdmin();
}
