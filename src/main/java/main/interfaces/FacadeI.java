package main.interfaces;

import data_structures.Tree;
import main.exceptions.*;
import main.forum_contents.Forum;

import java.util.ArrayList;
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
	Collection<SubForumI> getSubForumList(int sessionId);

	/**
	 * Add a forum to the system, requires login with permissions
	 * TODO who checks the permissions
	 */
	void addForum(String username, String password, String forumName, String regex, int numberOfModerators) throws PermissionDeniedException, ForumAlreadyExistException;

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
	int login(String forumName, String userName, String password) throws InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException;


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

	void removeForum(String username, String password, String forumName) throws ForumNotFoundException, PermissionDeniedException;

    void setPolicies(int sessionId, String regex, int numOfModerators);
	/**
	 * edit message
	 * @param sessionId - Id of current session
	 * @param messageId - Id of message
	 */
	void editMessage(int sessionId, int messageId, String title, String text);

	void removeModerator(int sessionId, String moderatorName);

	String viewModeratorStatistics(int sessionsId);

	String viewSuperManagerStatistics(int sessionId);

	String viewSessions(int sessionId);

	ExMessageI getMessage(int sessionId, int messageId);

	Collection<ThreadI> getThreadsList(int sessionId);

	Tree getMessageList(int sessionId);

	void viewSubforum(int sessionId, String subforum) throws SubForumAlreadyExistException;

	void viewThread(int sessionId, String title) throws DoesNotComplyWithPolicyException;
}
