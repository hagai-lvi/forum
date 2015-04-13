package main.interfaces;

import main.exceptions.PermissionDenied;

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
	SubForumI createSubForum(String name) throws PermissionDenied;

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(SubForumI toDelete);

}
