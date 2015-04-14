package main.interfaces;

import main.exceptions.PermissionDeniedException;

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
	SubForumI createSubForum(String name) throws PermissionDeniedException;

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException;

	/**
	 * Set new forum administrator
	 */
	void setAdmin(UserI admin, ForumI forum)  throws PermissionDeniedException;

	/**
	 * Set policy for forum
	 */
	void setPolicy(ForumI forum, ForumPolicyI policy)  throws PermissionDeniedException;

	/**
	 * Get statistics
	 */
	String viewStatistics(ForumI forum)  throws PermissionDeniedException;

	/**
	 * Add new forum
	 */
	void addForum(ForumI forum)  throws PermissionDeniedException;
}
