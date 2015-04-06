package main.interfaces;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface SubForumI {

	/**
	 * create a thread in the subforum
	 */
	void creatThread(MessageI message);

	/**
	 * reply to a specific messgae
	 */
	void replyToMessage(MessageI original, MessageI reply);

	/**
	 * Allows a user to report a moderator
	 */
	void reportModerator(String moderatorUsername, String reportMessage, UserI reporter);

	/**
	 * Delete a specific message if the message was create by the user that sent this request
	 */
	void deleteMessage(MessageI message);
}