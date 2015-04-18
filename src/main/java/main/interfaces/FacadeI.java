package main.interfaces;

import java.util.Collection;

/**
 * Created by hagai_lvi on 4/11/15.
 */
public interface FacadeI {

	/**
	 * Get the list of forums in the system
	 * @return
	 */
	Collection<ForumI> getForumList();

	/**
	 * Add a forum to the system, requires login with permissions
	 * TODO who checks the permissions
	 */
	void addForum(ForumI toAdd);

	/**
	 * Add a subforum to the specified forum
	 */
	void addSubforum(ForumI forum, SubForumI subforum);

	/**
	 * register a user to the specified forum
	 */
	void register(ForumI forum, String userName, String password, String email);

	/**
	 * Login to the specified forum
	 * TODO what should happen?
	 */
	void login(ForumI forum, String userName, String password );

	/**
	 * TODO what should happen?
	 */
	void logout();

	/**
	 * Reply to a specific message
	 */
	void addReply(MessageI src, String title, String body);

	/**
	 * Create a new thread in the specified subforum
	 */
	void createNewThread(SubForumI subforum, String srcMessageTitle, String srcMessageBody);

}
