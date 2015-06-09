package main.interfaces;

import data_structures.Tree;
import main.exceptions.*;

import java.util.Collection;

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
	Collection<ForumI> getForumList();

	/**
	 * Get the list of subforums in the specified forum
	 */
	Collection<SubForumI> getSubForumList(int sessionId) throws SessionNotFoundException;

	/**
	 * Add a forum to the system, requires login with permissions
	 */
	void addForum(String username, String password, boolean isSecured, String forumName, String regex, int numberOfModerators, int passwordEffectTime) throws PermissionDeniedException, ForumAlreadyExistException;

	/**
	 * Create a subforum in the specified forum.
	 * The Policy will be derived from the forum
	 */
	void createSubforum(int sessionId, String subforumName) throws PermissionDeniedException, SubForumAlreadyExistException, SessionNotFoundException;

	/**
	 * register a user to the specified forum
	 */
	void register(String forumName, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException;

	/**
	 * Login to the specified forum
	 * Returns session id
	 */
	int login(String forumName, String userName, String password) throws InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException, ForumNotFoundException;


	/**
	 * Logout from the current session
	 */
	void logout(int sessionId) throws SessionNotFoundException;

	/**
	 * Reply to a specific message
	 */
	void addReply(int sessionId, int srcMessageId, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException;

	/**
	 * Create a new thread in the specified subforum
	 */
	int createNewThread(int sessionId, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException;

	/**
	 * submit a complaint about a moderator
	 */
	void reportModerator(int sessionId, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException, SessionNotFoundException;


	/**
	 * Get authentication string
	 */
	String getUserAuthString(String forumName, String username, String password) throws InvalidUserCredentialsException, UserNotFoundException;


	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(int sessionId, int messageId) throws PermissionDeniedException, MessageNotFoundException, SessionNotFoundException;

	/**
	 * Set moderator for subforum
	 */
	void setModerator(int sessionId, String moderatorName) throws PermissionDeniedException, UserNotFoundException, SessionNotFoundException;

	/**
	 * guest entry usecase, return session id
	 */
	int guestEntry(String forumName) throws ForumNotFoundException;

	/**
	 * user type, connectionTime = the time the user was login
	 */
	void addUserType(int sessionId, String typeName, int seniority, int numOfMessages, int connectionTime) throws SessionNotFoundException;

	/**
	 * remove forum
	 */

	void removeForum(String username, String password, String forumName) throws ForumNotFoundException, PermissionDeniedException;

    void setPolicies(int sessionId, boolean isSecured, String regex, int numOfModerators, int passwordEffectTime) throws SessionNotFoundException;
	/**
	 * edit message
	 * @param sessionId - Id of current session
	 * @param messageId - Id of message
	 */
	void editMessage(int sessionId, int messageId, String title, String text) throws SessionNotFoundException;

	void removeModerator(int sessionId, String moderatorName) throws UserNotFoundException, SessionNotFoundException;

	String viewModeratorStatistics(int sessionsId) throws SessionNotFoundException;

	String viewSuperManagerStatistics(int sessionId) throws SessionNotFoundException;

	String viewSessions(int sessionId);

	ExMessageI getMessage(int sessionId, int messageId) throws SessionNotFoundException;

	Collection<ThreadI> getThreadsList(int sessionId) throws SessionNotFoundException;

	Tree getMessageList(int sessionId) throws SessionNotFoundException;

	/**
	 * Return the forum name of the current forum, according to the session id
	 * Return null if the sessionID does not exist or if no session was logged in during this session
	 */
	String getCurrentForumName(int sessionID) throws SessionNotFoundException;

	/**
	 * Return the currently logged user
	 * Return null if no user has logged in during the session
	 */
	String getCurrentUserName(int sessionID) throws SessionNotFoundException;

	/**
	 * Return true if the current user is an admin
	 */
	boolean isAdmin(int sessionID) throws SessionNotFoundException;

	ExSubForumI viewSubforum(int sessionId, String subforum) throws SubForumAlreadyExistException, SubForumNotFoundException, SessionNotFoundException;
	ExSubForumI viewSubforum(int sessionId) throws SessionNotFoundException;

	ExThreadI viewThread(int sessionId, String title) throws DoesNotComplyWithPolicyException, ThreadNotFoundException, SessionNotFoundException;

	ThreadI getCurrentThread(int sessionID) throws ThreadNotFoundException, SessionNotFoundException;

	void authanticateUser(String forum, String user1, String userAuthString) throws EmailNotAuthanticatedException, UserNotFoundException;
}
