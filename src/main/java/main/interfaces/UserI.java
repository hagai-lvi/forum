package main.interfaces;

import java.util.Collection;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface UserI {

	/**
	 * @return whether this user has authenticated his email address
	 */
	boolean isEmailAuthnticated();

	/**
	 * Get the list of all of the subforums of this user
	 */
	Collection<SubForumPermissionI> getSubForumPermission();

    String getUsername();

    String getPassword();

	String getEmail();

	String getUserAuthString();
}
