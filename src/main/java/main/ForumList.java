package main;

import main.interfaces.ForumI;

import java.util.List;

/**
 * Created by hagai_lvi on 4/23/15.
 */
public class ForumList {
	private List<? extends ForumI> forums;


	public List<? extends ForumI> getForums() {
		return forums;
	}

	public void setForums(List<? extends ForumI> forums) {
		this.forums = forums;
	}
}
