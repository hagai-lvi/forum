package main.interfaces;

import main.exceptions.*;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import java.util.Vector;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface UserI {

	/**
	 * @return whether this user has authenticated his email address
	 */
	boolean isEmailAuthenticated();

	void setAuthenticated();
	/**
	 * Get the list of all of the subforums of this user
	 */
	Vector<SubForumPermissionI> getSubForumsPermissions();

	GregorianCalendar getSignUpDate();
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

	void createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException;

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException, SubForumDoesNotExsitsException;

	/**
	 * create a thread in the subforum
	 */
	void createThread(MessageI message, SubForumPermissionI subForumPermission) throws PermissionDeniedException, DoesNotComplyWithPolicyException;

	/**
	 * reply to a specific message
	 */
	void replyToMessage(SubForumPermissionI subforumPermissions, MessageI original, String msgTitle, String msgBody) throws PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException;

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(SubForumI subforum, String moderatorUsername, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException;

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(MessageI message, SubForumPermissionI subForumPermission) throws PermissionDeniedException, MessageNotFoundException;

	/**
	 * Set new forum administrator
	 */
	void setAdmin(UserI admin)throws PermissionDeniedException;

	/**
	 * Set policy for forum
	 */
	void setPolicy(ForumPolicyI policy)throws PermissionDeniedException;

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
	 * Check if this user has admin privileges
	 */
	boolean isAdmin();

	/**
	 * Return subforum with the specified name, or null if non exist.
	 * TODO throw exception if non exist?
	 */
	SubForumPermissionI getSubForumsPermissionsByName(String subForumName);
}
