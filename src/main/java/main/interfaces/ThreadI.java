package main.interfaces;

import data_structures.Tree;
import main.exceptions.MessageNotFoundException;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ThreadI extends ExThreadI{

	MessageI getRootMessage();

	Tree getMessages();


	MessageI addReply(MessageI original, String title, String text, String user) throws MessageNotFoundException;

	boolean contains(MessageI message);

	void remove(MessageI message);

	String getTitle();

	void editMessage(MessageI originalMessage, String title, String newMessage) throws MessageNotFoundException;

	public int getMessagesCount();
}
