package main.interfaces;

import data_structures.Tree;
import main.exceptions.MessageNotFoundException;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ThreadI {

	Tree<MessageI> getMessages();

	MessageI getRootMessage();

	void addReply(MessageI reply, MessageI original) throws MessageNotFoundException;

	boolean contains(MessageI message);

	void remove(MessageI message);

	long getID();
}
