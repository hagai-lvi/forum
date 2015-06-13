package main.interfaces;

import data_structures.Tree;
import main.exceptions.MessageNotFoundException;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ThreadI extends ExThreadI{

	MessageI getRootMessage();

	public Tree getMessages();


	void addReply(MessageI reply, MessageI original) throws MessageNotFoundException;

	boolean contains(MessageI message);

	void remove(MessageI message);

	String getTitle();

	void editMessage(MessageI originalMessage, MessageI newMessage) throws MessageNotFoundException;
}
