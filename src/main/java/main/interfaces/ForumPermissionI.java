package main.interfaces;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ForumPermissionI {

	/**
	 * Create a subforum in this forum
	 */
	SubForumI createSubForum(String name);

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(SubForumI toDelete);
}
