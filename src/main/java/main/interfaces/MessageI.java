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
     * @return the text content of the message
     */
    String getMessageText();

    /**
     * @return the title of the meassage
     */
    String getMessageTitle();

    /**
     * @return the message details along with the details of all replies up to depth
     */
    String printSubTree(int detph);

    /**
     * Deletes the message from the forum along with all replies to it
     */
    void removeMessage();
}
