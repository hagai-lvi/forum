package main.interfaces;

import main.User.UserSubforumPermission;
import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.exceptions.ModeratorDoesNotExistsException;
import main.exceptions.PermissionDeniedException;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface SubForumPermissionI {

	/**
	 * create a thread in the subforum
	 */
	void createThread(MessageI message) throws PermissionDeniedException, DoesNotComplyWithPolicyException;

	/**
	 * reply to a specific message
	 */
	void replyToMessage(MessageI original, MessageI reply) throws PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException;

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws PermissionDeniedException, ModeratorDoesNotExistsException;

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(MessageI message, String deleter) throws PermissionDeniedException, MessageNotFoundException;

	/**
	 * Edit an existing message.
	 */
	 void editMessage(MessageI originalMessage, MessageI newMessage);

	 /**
	 * view threads
	 */

	ThreadI[] getThreads();

	/**
	 * Set moderator for subforum
	 */
	void setModerator(UserI moderator) throws PermissionDeniedException;

	/**
	 * Get related subforum
	 */
	SubForumI getSubForum();

	boolean findForum(String name);

	ThreadI getThreadById(long id);

	boolean isModerator();
}
