package main.interfaces;

import java.util.Date;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface MessageI extends ExMessageI{

   // void editText(UserI user, String newText) throws PermissionDeniedException;

    void editText(/*UserI user,*/ String newText)/* throws PermissionDeniedException */;

    int getId();
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

    Date getDate();

    /**
     * @return the message details along with the details of all replies
     */
    String printSubTree();

    /**
     * Deletes the message from the forum along with all replies to it
     */

    void addReply(MessageI reply);

    void editTitle(String title);
}
