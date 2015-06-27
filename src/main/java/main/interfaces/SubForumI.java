package main.interfaces;

import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.exceptions.ModeratorDoesNotExistsException;
import main.exceptions.PermissionDeniedException;

import java.util.Map;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface SubForumI extends ExSubForumI{

	Map<String, UserI> getModerators();

	/**
	 * create a thread in the sub-forum
	 */
	ThreadI addThread(String user, String title, String text) throws DoesNotComplyWithPolicyException;
	/**
	 * reply to a specific message
	 */
	int replyToMessage(MessageI original, String user, String title, String text) throws MessageNotFoundException, DoesNotComplyWithPolicyException;

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws ModeratorDoesNotExistsException;

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 * @param thread
	 * @param message
	 */
	void deleteMessage(String thread, MessageI message) throws MessageNotFoundException;

	void setModerator(UserI mod);

	/**
	 * Return the threads in this sub-forum
	 */
	Map<String, ThreadI> getThreads();

	void removeModerator(String mod);

	void editMessage(ThreadI thread, int originalMessage, String text, String title, String user) throws MessageNotFoundException, PermissionDeniedException;

	public int getMessagesCount();

	String viewStatistics();
}
