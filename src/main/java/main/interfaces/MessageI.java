package main.interfaces;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface MessageI {

	/**
	 * @return the user that created this message
	 */
	UserI getUser();

    /**
     * Add a reply to this message
     */
    void reply(MessageI reply);

    String printSubTree(int detph);

    void removeMessage();
}
