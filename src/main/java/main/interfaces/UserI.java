package main.interfaces;

import main.exceptions.*;

import java.util.Collection;
import java.util.GregorianCalendar;

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
	Collection<SubForumPermissionI> getSubForumsPermissions();

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

	SubForumI createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException;

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException, SubForumDoesNotExistException;

	/**
	 * create a thread in the subforum
	 */
	ThreadI createThread(MessageI message, String subforum) throws PermissionDeniedException, DoesNotComplyWithPolicyException, SubForumDoesNotExistException;

	/**
	 * reply to a specific message
	 */
	void replyToMessage(String subforum, int original, String msgTitle, String msgBody) throws PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException, SubForumDoesNotExistException;

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(String subforum, String moderatorUsername, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException, SubForumDoesNotExistException;

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(int message, String subforum) throws PermissionDeniedException, MessageNotFoundException, SubForumDoesNotExistException;

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
	String viewStatistics(String forum) throws PermissionDeniedException;

	/**
	 * Set moderator for subforum
	 */
	void setModerator(SubForumI subForum, UserI moderator) throws PermissionDeniedException, SubForumNotFoundException;

	/**
	 * Ban moderator
	 */
	void banModerator(SubForumI subForum, UserI moderatorToBan, long time)throws PermissionDeniedException;

	/**
	 * Add permissions
	 */
	void addSubForumPermission(SubForumPermissionI permission);

	void updatePasswordCreationDate();

	GregorianCalendar getPasswordCreationDate();

	/**
	 *
	 */
	void setSecurityQuestion(String quest);
	void setSecurityAnswer(String ans);

	boolean isAdmin();


	void editMessage(String title, int messageId, String title1, String text) throws SubForumDoesNotExistException, MessageNotFoundException;

	void removeModerator(String subforum, String user) throws SubForumDoesNotExistException, PermissionDeniedException;
}
