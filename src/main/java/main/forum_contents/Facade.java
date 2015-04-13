package main.forum_contents;

import main.interfaces.FacadeI;
import main.interfaces.ForumI;

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
	public boolean InitilizeSystem() {
		return false;
	}

	public static FacadeI getFacade(){
		return theFacade;
	}
}
