package main.interfaces;

import main.User.Permissions;
import main.exceptions.*;

import java.util.GregorianCalendar;
import java.util.Map;

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
	Map<String, SubForumPermissionI> getSubForumsPermissions();

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

	SubForumI createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException, ForumNotFoundException, SubForumDoesNotExistException;

	/**
	 * Delete a subForum from this forum
	 */
	void deleteSubForum(String toDelete) throws PermissionDeniedException, SubForumDoesNotExistException, ForumNotFoundException;

	/**
	 * create a thread in the subforum
	 */
	ThreadI createThread(String title, String text, String subforum) throws PermissionDeniedException, DoesNotComplyWithPolicyException, SubForumDoesNotExistException;

		/**
         * reply to a specific message
         */
	int replyToMessage(String subforum, MessageI original, String msgTitle, String msgBody) throws PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException, SubForumDoesNotExistException;

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(SubForumI subforum, String moderatorUsername, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException;

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(String subForum, String thread, MessageI mes) throws PermissionDeniedException, MessageNotFoundException, SubForumDoesNotExistException;

	/**
	 * Set new forum administrator
	 */
	void setAdmin(UserI admin) throws PermissionDeniedException, ForumNotFoundException, UserNotFoundException, CloneNotSupportedException;

	/**
	 * Set policy for forum
	 */
	void setPolicy(ForumPolicyI policy) throws PermissionDeniedException, ForumNotFoundException;

    String viewStatistics() throws PermissionDeniedException;

	/**
	 * Set moderator for subforum
	 */
	void setModerator(String subForum, UserI moderator) throws PermissionDeniedException, SubForumNotFoundException, ForumNotFoundException, CloneNotSupportedException;

	/**
	 * Ban moderator
	 */
	void banModerator(SubForumI subForum, UserI moderatorToBan, long time)throws PermissionDeniedException;

	/**
	 * Add permissions
	 */
	void addSubForumPermission(String subforum);

	void updatePasswordCreationDate();

	GregorianCalendar getPasswordCreationDate();

	/**
	 *
	 */
	void setSecurityQuestion(String quest);
	void setSecurityAnswer(String ans);

	boolean isAdmin();

	boolean canReply(String subForum) throws SubForumDoesNotExistException, PermissionDeniedException;

	boolean canAddThread(String subForum) throws SubForumDoesNotExistException, PermissionDeniedException;

	void editMessage(String subforum,ThreadI thread, int messageId, String title, String text) throws SubForumDoesNotExistException, MessageNotFoundException, PermissionDeniedException;

	void removeModerator(String subforum, String moderatorName) throws SubForumDoesNotExistException;

	String getForumStatus() throws SubForumDoesNotExistException;

	String getSubForumStatus(String subForum) throws SubForumDoesNotExistException;

	boolean isOwnerOfMessage(MessageI message);

	void setAuthenticatedAdmin();

	//void becomeAdmin();

	UserI cloneAs(Permissions permission) throws ForumNotFoundException, CloneNotSupportedException;

	boolean isGuest();

	/*void viewSubforum(String subforum)*/ ;
}
