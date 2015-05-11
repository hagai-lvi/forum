package main.interfaces;

import main.exceptions.PermissionDeniedException;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface MessageI {

   // void editText(UserI user, String newText) throws PermissionDeniedException;

    void editText(/*UserI user,*/ String newText)/* throws PermissionDeniedException */;

    /**
	 * @return the user that created this message
	 */
	String getUser();


    /**
     * @return the text content of the message
     */
    String getMessageText();

    /**
     * @return the title of the meassage
     */
    String getMessageTitle();

    /**
     * @return the message details along with the details of all replies
     */
    String printSubTree();

    /**
     * Deletes the message from the forum along with all replies to it
     */
    void removeMessage();

    void addReply(MessageI reply);
}
