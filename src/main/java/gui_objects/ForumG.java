package gui_objects;

import java.io.Serializable;

/**
 * Created by hagai_lvi on 6/5/15.
 */
public class ForumG implements Serializable{
	private String name;
	private String regex;
	private int numOfModerators;
	private UserG user;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public int getNumOfModerators() {
		return numOfModerators;
	}

	public void setNumOfModerators(int numOfModerators) {
		this.numOfModerators = numOfModerators;
	}

	public UserG getUser() {
		return user;
	}

	public void setUser(UserG user) {
		this.user = user;
	}
}
