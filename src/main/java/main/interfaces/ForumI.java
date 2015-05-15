package main.interfaces;

import main.exceptions.*;
import main.forum_contents.UserType;

import java.util.Collection;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ForumI {


	/**
	 * @return the subforums in this forum
	 */
	Collection<SubForumI> getSubForums();

	/**
	 * Create a subforum in this forum
	 */
	SubForumI createSubForum(String name) throws SubForumAlreadyExistException;

	String getName();

	void deleteSubForum(SubForumI todelete) throws SubForumDoesNotExsitsException;
	/**
	 * Register a user to this forum
	 */
	UserI register(String userName, String password, String eMail) throws UserAlreadyExistsException, InvalidUserCredentialsException;

	/**
	 * send an authentication email to the given email
	 */
	void sendAuthenticationEMail(UserI user);

	/**
	 * @return a user if the login was successful or throws an Exception
	 * if there was a problem in the login
	 */
	UserI login(String username, String password) throws InvalidUserCredentialsException, PasswordNotInEffectException, EmailNotAuthanticatedException;

	/**
	 * Return an 'empty' guest user
	 */
	UserI guestLogin();


	/**
	 * Log out the user
	 */
	void logout(UserI user);

	/**
	 * Set the policy of this forum
	 */
	void setPolicy(ForumPolicyI policy);

	/**
	 * return the list of users that are registered to this forum
	 */
	Collection<UserI> getUserList();

	/**
	 * manage forum user types
	 */
	Collection<UserType> getUserTypes();

	void addUserType(String type);

	boolean removeUserType(String type);

	void setAdmin(UserI admin);

	String viewStatistics();

	boolean enterUserAuthenticationString(UserI user, String auth_string);
}
