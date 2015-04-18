package main.interfaces;

import java.util.Collection;

/**
 * Created by hagai_lvi on 4/11/15.
 */
public interface FacadeI {
	Collection<ForumI> getForumList();
	void addForum(ForumI toAdd);



}
