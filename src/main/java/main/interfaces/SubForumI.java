package main.interfaces;

import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.exceptions.ModeratorDoesNotExistsException;

import java.util.Collection;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface SubForumI {

	/**
	 * create a thread in the subforum
	 */
	void creatThread(MessageI message) throws DoesNotComplyWithPolicyException;

	String getName();

	/**
	 * reply to a specific messgae
	 */
	void replyToMessage(MessageI original, MessageI reply) throws MessageNotFoundException, DoesNotComplyWithPolicyException;

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws ModeratorDoesNotExistsException;

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(MessageI message, UserI requestingUser);

	void setModerator(UserI mod);

	/**
	 * Return the threads in this subforum
	 */
	Collection<ThreadI> getThreads();


}
