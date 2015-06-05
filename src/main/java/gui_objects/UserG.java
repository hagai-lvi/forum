package gui_objects;

import java.io.Serializable;

/**
 * Created by hagai_lvi on 6/5/15.
 *
 * A simple user pojo representation to be used by the communication layer
 */
public class UserG implements Serializable{
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
