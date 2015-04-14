package main.interfaces;

import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;

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
	void reportModerator(String moderatorUsername, String reportMessage, UserI reporter);

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(MessageI message, UserI requesting_user);
}
