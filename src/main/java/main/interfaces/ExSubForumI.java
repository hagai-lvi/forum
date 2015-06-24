package main.interfaces;

import java.util.Collection;
import java.util.Map;

/**
 * Created by gabigiladov on 5/13/15.
 */
public interface ExSubForumI {
    String getTitle();

    Map<String, ThreadI> getThreads();
}
