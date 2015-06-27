package main.interfaces;

import main.User.Permissions;
import main.exceptions.*;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface SubForumPermissionI {

	Permissions getPermission();

	/**
	 * create a thread in the subforum
	 */
	ThreadI createThread(String user, String title, String text) throws PermissionDeniedException, DoesNotComplyWithPolicyException;
	/**
	 * reply to a specific message
	 */
	int replyToMessage(MessageI original, String user, String title, String text) throws PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException;

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws PermissionDeniedException, ModeratorDoesNotExistsException;

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(String deleter, String thread, MessageI message) throws PermissionDeniedException, MessageNotFoundException;

	/**
	 * Edit an existing message.
	 */
	 void editMessage(ThreadI thread, int originalMessage, String title, String text, String user) throws MessageNotFoundException, PermissionDeniedException;

	 /**
	 * view threads
	 */

	ThreadI[] getThreads();

	/**
	 * Set moderator for subforum
	 */
	void setModerator(UserI moderator) throws PermissionDeniedException, ForumNotFoundException, CloneNotSupportedException;

	/**
	 * Get related subforum
	 */
	SubForumI getSubForum();

	void setPermission(Permissions permission);

	boolean isModerator();

	boolean canReply() throws PermissionDeniedException;

	boolean canAddThread() throws PermissionDeniedException;

	void removeModerator(String moderatorName);
}
