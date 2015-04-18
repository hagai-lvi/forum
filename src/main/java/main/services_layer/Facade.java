package main.services_layer;

import main.interfaces.FacadeI;
import main.interfaces.ForumI;
import main.interfaces.MessageI;
import main.interfaces.SubForumI;

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
	public void addForum(ForumI toAdd) {
		forums.add(toAdd);
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

	public static FacadeI getFacade(){
		return theFacade;
	}
}