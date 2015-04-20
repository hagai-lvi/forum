package main.interfaces;

import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.exceptions.ModeratorDoesNotExistsException;
import main.exceptions.PermissionDeniedException;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface UserI {


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
	Vector<SubForumPermissionI> viewSubForums();

	/**
	 * Create a subforum in this forum
	 */
	void createSubForum(String name, ForumI forum) throws PermissionDeniedException;

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(SubForumI toDelete, ForumI forum)throws PermissionDeniedException;

	/**
	 * create a thread in the subforum
	 */
	void createThread(MessageI message, SubForumI subforum) throws PermissionDeniedException, DoesNotComplyWithPolicyException;

	/**
	 * reply to a specific message
	 */
	void replyToMessage(SubForumI subforum, MessageI original, String msgTitle, String msgBody) throws PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException;

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(SubForumI subforum, String moderatorUsername, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException;

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(MessageI message, SubForumI subForum)throws PermissionDeniedException;

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
	void banModerator(SubForumI subForum, UserI moderatorToBan, long time)throws PermissionDeniedException;

	/**
	 * Add permissions
	 */
	void addSubForumPermission(SubForumPermissionI permission);

	/**
	 * Set username
	 */
	void setUsername(String name);

	/**
	 * @return whether this user has authenticated his email address
	 */
	boolean isEmailAuthenticated();

	/**
	 * Get email message and compares to real authenticated string
	 */
	void setAuthenticated();

	void setPassword(String password);

	void setEmail(String email);

	GregorianCalendar getSignUpDate();
}
