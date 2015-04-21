package main.services_layer;

import main.exceptions.*;
import main.forum_contents.ForumMessage;
import main.forum_contents.SubForum;
import main.interfaces.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hagai_lvi on 4/11/15.
 */
public class Facade implements FacadeI {
	private static Facade theFacade = new Facade();

	private Facade(){}

	private Collection<ForumI> forums = new ArrayList<>();

	@Override
	public Collection<ForumI> getForumList() {
		return forums;
	}

	@Override
	public Collection<SubForumI> getSubForumList(ForumI forum) {
		return forum.getSubForums().values();
		//TODO forum.subforums should return collection and not hashmap
	}

	@Override
	public void addForum(ForumI toAdd) {
		//TODO shouldn't be a part of the facade, super-admin only
		forums.add(toAdd);
	}

	@Override
	public void createSubforum(ForumI forum, String subforumName, UserI user) throws PermissionDeniedException {
		user.createSubForum(subforumName, forum);
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
	public void addReply(UserI user, SubForumI subForum, MessageI src, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException {
		user.replyToMessage(subForum,src,title, body);

	}

	@Override
	public void createNewThread(UserI user, SubForumI subforum, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
		user.createThread(new ForumMessage(null, null, null, null), subforum);//move the message creation to the user
	}

	@Override
	public void reportModerator(UserI user, SubForumI subforum, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException {
		user.reportModerator(subforum, moderatorUserName, reportMessage);
	}

	public static FacadeI getFacade(){
		return theFacade;
	}
}
