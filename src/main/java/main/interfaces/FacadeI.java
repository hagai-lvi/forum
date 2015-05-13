package main.interfaces;

import main.exceptions.*;

import java.util.Collection;
import java.util.GregorianCalendar;

/**
 * Created by hagai_lvi on 4/11/15.
 */
public interface FacadeI {
	/**
	 * Initialize
	 */
	void initilize();

	/**
	 * Get the list of forums in the system
	 */
	Collection<ExForumI> getForumList();

	/**
	 * Get the list of subforums in the specified forum
	 */
	Collection<ExSubForumI> getSubForumList(int sessionId);

	/**
	 * Add a forum to the system, requires login with permissions
	 * TODO who checks the permissions
	 */
	void addForum(String username, String password, String forumName, String regex, int numberOfModerators);

	/**
	 * Create a subforum in the specified forum.
	 * The Policy will be derived from the forum
	 */
	void createSubforum(int sessionId, String subforumName) throws PermissionDeniedException, SubForumAlreadyExistException;

	/**
	 * register a user to the specified forum
	 */
	void register(String forumName, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException;

	/**
	 * Login to the specified forum
	 * Returns session id
	 */
	int login(String forumName, String userName, String password) throws InvalidUserCredentialsException;


	/**
	 * TODO what should happen?
	 */
	void logout(int sessionId);

	/**
	 * Reply to a specific message
	 */
	void addReply(int sessionId, int srcMessageId, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException;

	/**
	 * Create a new thread in the specified subforum
	 */
	void createNewThread(int sessionId, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException;

	/**
	 * submit a complaint about a moderator
	 */
	void reportModerator(int sessionId, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException;


	/**
	 * Get authentication string
	 */
	String getUserAuthString(String forumName, String username, String password, String authenticationString) throws InvalidUserCredentialsException;


	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(int sessionId, int messageId) throws PermissionDeniedException, MessageNotFoundException;

	/**
	 * Set moderator for subforum
	 */
	void setModerator(int sessionId, String moderatorName)throws PermissionDeniedException;

	/**
	 * guest entry usecase, return session id
	 */
	int guestEntry(String forumName);

	/**
	 * user type, connectionTime = the time the user was login
	 */
	void addUserType(int sessionId, String typeName, int seniority, int numOfMessages, int connectionTime);

	/**
	 * remove forum
	 */

	void removeForum(String username, String password, String forumName);


	/**
	 * edit message
	 * @param sessionId
	 * @param messageId
	 */
	void editMessage(int sessionId, int messageId, String title, String text);

	void removeModerator(int sessionId, String moderatorName);

	String viewModeratorStatistics(int sessionsId);

	String viewSuperManagerStatistics(int sessionId);

	String viewSessions(int sessionId);


}
