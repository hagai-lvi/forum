package main.interfaces;

import java.util.Collection;

/**
 * Created by victor on 5/15/2015 for ${Class}.
 */
public interface ExMessageI {
    int getId();
    Collection<MessageI> getReplies();
    String getMessageText();
}
