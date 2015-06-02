package main.interfaces;

import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.exceptions.ModeratorDoesNotExistsException;

import java.util.Collection;
import java.util.Map;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface SubForumI extends ExSubForumI{

	Map<String, UserI> getModerators();

	/**
	 * create a thread in the sub-forum
	 */
	ThreadI createThread(MessageI message) throws DoesNotComplyWithPolicyException;

	String getName();

	/**
	 * reply to a specific message
	 */
	void replyToMessage(MessageI original, MessageI reply) throws MessageNotFoundException, DoesNotComplyWithPolicyException;

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws ModeratorDoesNotExistsException;

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(MessageI message, String requestingUser) throws MessageNotFoundException;

	void setModerator(UserI mod);

	/**
	 * Return the threads in this sub-forum
	 */
	Collection<ThreadI> getThreads();

	void removeModerator(UserI mod);
}
