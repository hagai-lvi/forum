package main.interfaces;

import java.util.Collection;

/**
 * Created by gabigiladov on 5/13/15.
 */
public interface ExSubForumI {
    String getTitle();

    Collection<? extends ExThreadI> getThreads();
}
