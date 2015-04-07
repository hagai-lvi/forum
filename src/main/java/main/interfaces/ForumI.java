package main.interfaces;

import main.exceptions.InvalidUserCredentialsException;
import main.exceptions.SubForumAlreadyExistException;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ForumI {


	/**
	 * Create a subforum in this forum
	 */
	SubForumI createSubForum(String name) throws SubForumAlreadyExistException;

	/**
	 * Register a user to this forum
	 */
	UserI register(String userName, String password, String eMail);

	/**
	 * send an authentication email to the given email
	 */
	void sendAuthenticationEMail(UserI user);

	/**
	 * @return a user if the login was successful or throws an Exception
	 * if there was a problem in the login
	 */
	UserI login(String username, String password) throws InvalidUserCredentialsException;

	/**
	 * Return an 'empty' guest user
	 */
	UserI guestLogin();


	/**
	 * Log out the user
	 */
	void logout(UserI user);
}
