package main.interfaces;

import data_structures.Tree;
import main.exceptions.*;
import main.services_layer.Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;

/**
 * Created by hagai_lvi on 4/11/15.
 */
public interface FacadeI {
	/**
	 * Initialize
	 */
	void initialize();

	/**
	 * Get the list of forums in the system
	 */
	ArrayList<String> getForumList();

	/**
	 * Get the list of subforums in the specified forum
	 */
	Map<String, SubForumI> getSubForumList(int sessionId) throws SessionNotFoundException;

	/**
	 * Add a forum to the system, requires login with permissions
	 */
	void addForum(String username, String password, String forumName, boolean isSecured, String regex, int numberOfModerators, int passwordEffectTime) throws PermissionDeniedException, ForumAlreadyExistException;

	/**
	 * Create a subforum in the specified forum.
	 * The Policy will be derived from the forum
	 */
	void addSubforum(int sessionId, String subforumName) throws PermissionDeniedException, SubForumAlreadyExistException, SessionNotFoundException, ForumNotFoundException, SubForumDoesNotExistException;

	/**
	 * register a user to the specified forum
	 */
	void register(String forumName, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, DoesNotComplyWithPolicyException;

	/**
	 * Login to the specified forum
	 * Returns session id
	 */
	int login(String forumName, String userName, String password) throws InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException, ForumNotFoundException;

	Collection<Session> getSessions();

	/**
	 * Logout from the current session
	 */
	void logout(int sessionId) throws SessionNotFoundException;

	/**
	 * Reply to a specific message
	 */
	int addReply(int sessionId, int srcMessageId, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException, SubForumDoesNotExistException, ThreadNotFoundException;

	/**
	 * Create a new thread in the specified subforum
	 */
	int addThread(int sessionId, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException, SubForumDoesNotExistException;

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
	void deleteMessage(int sessionId, int messageId) throws PermissionDeniedException, MessageNotFoundException, SessionNotFoundException, SubForumDoesNotExistException, ThreadNotFoundException;

	/**
	 * Set moderator for subforum
	 */
	void setModerator(int sessionId, String moderatorName) throws PermissionDeniedException, UserNotFoundException, SessionNotFoundException, SubForumNotFoundException, ForumNotFoundException, CloneNotSupportedException;

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

    void setPolicies(int sessionId, boolean isSecured, String regex, int numOfModerators, int passwordEffectTime) throws SessionNotFoundException, PermissionDeniedException, ForumNotFoundException;
	/**
	 * edit message
	 * @param sessionId - Id of current session
	 * @param messageId - Id of message
	 */
	void editMessage(int sessionId, int messageId, String title, String text) throws SessionNotFoundException, MessageNotFoundException, SubForumDoesNotExistException, ThreadNotFoundException, PermissionDeniedException;

	void removeModerator(int sessionId, String moderatorName) throws UserNotFoundException, SessionNotFoundException, SubForumDoesNotExistException;

	String viewModeratorStatistics(int sessionsId) throws SessionNotFoundException, PermissionDeniedException;

	String viewSuperManagerStatistics(String username, String password) throws PermissionDeniedException;

	String viewSessions(int sessionId) throws ThreadNotFoundException;

	ExMessageI getMessage(int sessionId, int messageId) throws SessionNotFoundException, ThreadNotFoundException;

	Map<String, ThreadI> getThreadsList(int sessionId) throws SessionNotFoundException;

	Tree getMessageList(int sessionId) throws SessionNotFoundException, ThreadNotFoundException;

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

	/**
	 * Return true if the current user is an admin
	 */
	boolean isGuest(int sessionID) throws SessionNotFoundException;

	ExSubForumI viewSubforum(int sessionId, String subforum) throws SubForumNotFoundException, SessionNotFoundException;
	ExSubForumI viewSubforum(int sessionId) throws SessionNotFoundException;

	ExThreadI viewThread(int sessionId, String title) throws DoesNotComplyWithPolicyException, ThreadNotFoundException, SessionNotFoundException;

	ExThreadI viewThread(int sessionId) throws DoesNotComplyWithPolicyException, ThreadNotFoundException, SessionNotFoundException;

	ThreadI getCurrentThread(int sessionID) throws ThreadNotFoundException, SessionNotFoundException;

	void authenticateUser(String forum, String user1, String userAuthString) throws EmailNotAuthanticatedException, UserNotFoundException;

	boolean isMessageFromCurrentUser(int sessionId, int messageId) throws SessionNotFoundException, ThreadNotFoundException;

	String getCurrentUserForumStatus(int sessionId) throws SessionNotFoundException, SubForumDoesNotExistException;

	String getCurrentUserSubForumStatus(int sessionId) throws SessionNotFoundException, SubForumDoesNotExistException;

	void setAdmin(String username, String password, String newAdmin, String forumname) throws UserNotFoundException, PermissionDeniedException, ForumNotFoundException, CloneNotSupportedException;

	Vector<String> getMessagesToSuperAdmin();

	void addMessageToSuperAdmin(String message);
}
