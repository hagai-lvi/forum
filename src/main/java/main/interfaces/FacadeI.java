package main.interfaces;

import main.exceptions.*;

import java.util.Collection;

/**
 * Created by hagai_lvi on 4/11/15.
 */
public interface FacadeI {

	/**
	 * Return a forum with the specified name, or null if no such forum exists
	 * TODO throw an exception if no such forum exists?
	 */
	ForumI getForumByName(String forumName);

	/**
	 * Get the list of forums in the system
	 */
	Collection<ForumI> getForumList();

	/**
	 * Get the list of subforums in the specified forum
	 */
	Collection<SubForumPermissionI> getSubForumList(UserI user);

	/**
	 * Add a forum to the system, requires login with permissions
	 * TODO who checks the permissions
	 */
	void addForum(ForumI toAdd);

	/**
	 * Create a subforum in the specified forum.
	 * The Policy will be derived from the forum
	 */
	void createSubforum(String subforumName, UserI user) throws PermissionDeniedException, SubForumAlreadyExistException;

	/**
	 * register a user to the specified forum
	 */
	void register(ForumI forum, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException;

	/**
	 * Login to the specified forum
	 * TODO what should happen?
	 */
	UserI login(ForumI forum, String userName, String password ) throws InvalidUserCredentialsException;


	/**
	 * TODO what should happen?
	 */
	void logout(ForumI forum, UserI user);

	/**
	 * Reply to a specific message
	 */
	void addReply(UserI user, SubForumPermissionI subforumPermissions, MessageI src, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException;

	/**
	 * Create a new thread in the specified subforum
	 */
	void createNewThread(UserI user, SubForumPermissionI subforumPermission, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException;

	/**
	 * submit a complaint about a moderator
	 */
	void reportModerator(UserI user,SubForumI subforum , String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException;

}
