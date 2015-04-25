package main.services_layer;

import main.forum_contents.Forum;
import main.interfaces.FacadeI;
import main.interfaces.ForumI;
import main.interfaces.MessageI;
import main.interfaces.SubForumI;
import main.exceptions.*;
import main.forum_contents.ForumMessage;
import main.interfaces.*;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by hagai_lvi on 4/11/15.
 */
public class Facade implements FacadeI {
	private static Facade theFacade = new Facade();

	private Facade(){
		//TODO remove, for demo only
		addForum(new Forum("A",null));
		addForum(new Forum("B",null));
		addForum(new Forum("C",null));
	}

	private HashMap<String,ForumI> forums = new HashMap<>();

	@Override
	public ForumI getForumByName(String forumName){
		return forums.get(forumName);
	}

	@Override
	public Collection<ForumI> getForumList() {
		return forums.values();
	}

	@Override
	public Collection<SubForumPermissionI> getSubForumList(UserI user) {
		return user.getSubForumsPermissions();
	}

	@Override
	public void addForum(ForumI toAdd) {
		//TODO shouldn't be a part of the facade, super-admin only
		forums.add(toAdd);
	}

	@Override
	public void createSubforum(String subforumName, UserI user) throws PermissionDeniedException, SubForumAlreadyExistException {
		user.createSubForum(subforumName);
	}

	@Override
	public void register(ForumI forum, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException {
		forum.register(userName, password, email);
	}

	@Override
	public UserI login(ForumI forum, String userName, String password) throws InvalidUserCredentialsException {
		return forum.login(userName, password);
	}

	@Override
	public void logout(ForumI forum, UserI user) {
		forum.logout(user);
	}

	@Override
	public void addReply(UserI user, SubForumPermissionI subForumPermission, MessageI src, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException {
		user.replyToMessage(subForumPermission, src, title, body);
	}

	@Override
	public void createNewThread(UserI user, SubForumPermissionI subForumPermission, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
		//TODO
		user.createThread(new ForumMessage(null, user, srcMessageBody, srcMessageTitle), subForumPermission);//move the message creation to the user
	}

	@Override
	public void reportModerator(UserI user, SubForumI subforum, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException {
		user.reportModerator(subforum, moderatorUserName, reportMessage);
	}

	public static FacadeI getFacade(){
		return theFacade;
	}

	public static FacadeI dropAllData(){
		theFacade = new Facade();
		return theFacade;
	}
}
