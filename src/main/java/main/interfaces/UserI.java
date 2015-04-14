package main.interfaces;

import main.exceptions.PermissionDeniedException;

import java.util.Collection;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface UserI {

	/**
	 * @return whether this user has authenticated his email address
	 */
	boolean isEmailAuthnticated();

	/**
	 * Get the list of all of the subforums of this user
	 */
	Collection<SubForumPermissionI> getSubForumPermission();

	/**
	 * Get username
	 */
    String getUsername();

	/**
	 * Get password
	 */
    String getPassword();

	/**
	 * Get Mail
	 */
	String getEmail();

	/**
	 * Get authentication string
	 */
	String getUserAuthString();

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
	void deleteSubForum(SubForumI toDelete)throws PermissionDeniedException;

	/**
	 * create a thread in the subforum
	 */
	public void createThread(MessageI message) throws PermissionDeniedException;

	/**
	 * reply to a specific message
	 */
	void replyToMessage(MessageI original, MessageI reply)throws PermissionDeniedException;

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(String moderatorUsername, String reportMessage)throws PermissionDeniedException;

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(MessageI message)throws PermissionDeniedException;

	/**
	 * view threads
	 */

	ThreadI[] getThreads();

	/**
	 * Add new forum
	 */
	void addForum(ForumI forum)throws PermissionDeniedException;

	/**
	 * Set new forum administrator
	 */
	void setAdmin(UserI admin, ForumI forum)throws PermissionDeniedException;

	/**
	 * Set policy for forum
	 */
	void setPolicy(ForumI forum, ForumPolicyI policy)throws PermissionDeniedException;

	/**
	 * Get statistics
	 */
	String viewStatistics(ForumI forum) throws PermissionDeniedException;

	/**
	 * Set moderator for subforum
	 */
	void setModerator(SubForumI subForum, UserI moderator)throws PermissionDeniedException;

	/**
	 * Ban moderator
	 */
	void banModerator(UserI moderatorToBan, long time)throws PermissionDeniedException;

	/**
	 * Send friend request to another user
	 */
	void sendFriendRequest(UserI newFriend)throws PermissionDeniedException;

	/**
	 * Add permissions
	 */
	void addSubForumPermission(SubForumPermissionI permission);
}
