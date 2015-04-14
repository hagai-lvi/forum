package main.interfaces;

import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.exceptions.PermissionDeniedException;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface SubForumPermissionI {

	/**
	 * create a thread in the subforum
	 */
	public void createThread(MessageI message) throws PermissionDeniedException, DoesNotComplyWithPolicyException;

	/**
	 * reply to a specific message
	 */
	void replyToMessage(MessageI original, MessageI reply) throws PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException;

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(String moderatorUsername, String reportMessage) throws PermissionDeniedException;

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(MessageI message) throws PermissionDeniedException;

	/**
	 * view threads
	 */

	ThreadI[] getThreads() throws PermissionDeniedException;

	/**
	 * Set moderator for subforum
	 */
	void setModerator(UserI moderator) throws PermissionDeniedException;

	/**
	 * Get related subforum
	 */
	SubForumI getSubForum();

	boolean findForum(String name);
}
