package main.interfaces;

import main.exceptions.PermissionDenied;

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
	SubForumI createSubForum(String name) throws PermissionDenied;

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(SubForumI toDelete);

	/**
	 * create a thread in the subforum
	 */
	void createThread(MessageI message);

	/**
	 * reply to a specific message
	 */
	void replyToMessage(MessageI original, MessageI reply);

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(String moderatorUsername, String reportMessage);

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(MessageI message);

	/**
	 * view threads
	 */

	ThreadI[] getThreads();

	/**
	 * Add new forum
	 */
	void addForum(ForumI forum);

	/**
	 * Set new forum administrator
	 */
	void setAdmin(UserI admin, ForumI forum);

	/**
	 * Set policy for forum
	 */
	void setPolicy(ForumI forum, ForumPolicyI policy);

	/**
	 * Get statistics
	 */
	String viewStatistics(ForumI forum);

	/**
	 * Set moderator for subforum
	 */
	void setModerator(SubForumI subForum, UserI moderator);

	/**
	 * Ban moderator
	 */
	void banModerator(UserI moderatorToBan, long time);

	/**
	 * Send friend request to another user
	 */
	void sendFriendRequest(UserI newFriend);



}
