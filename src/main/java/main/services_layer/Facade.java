package main.services_layer;

import main.forum_contents.Forum;
import main.interfaces.FacadeI;
import main.interfaces.ForumI;
import main.interfaces.MessageI;
import main.interfaces.SubForumI;

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
	public Collection<SubForumI> getSubForumList(ForumI forum) {
		return null;
	}

	@Override
	public void addForum(ForumI toAdd) {
		forums.put(toAdd.getName(), toAdd);
	}

	@Override
	public void addSubforum(ForumI forum, SubForumI subforum) {

	}

	@Override
	public void register(ForumI forum, String userName, String password, String email) {

	}

	@Override
	public void login(ForumI forum, String userName, String password) {

	}

	@Override
	public void logout() {

	}

	@Override
	public void addReply(MessageI src, String title, String body) {

	}

	@Override
	public void createNewThread(SubForumI subforum, String srcMessageTitle, String srcMessageBody) {

	}

	@Override
	public void reportModerator(String moderatorUserName, String reportMessage) {

	}

	public static FacadeI getFacade(){
		return theFacade;
	}
}
