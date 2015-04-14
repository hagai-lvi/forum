package main.interfaces;

import main.User.User;
import main.forum_contents.SubForum;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ForumPolicyI {


    /**
     * Check if the given password matches the regex that was given to {@link #setPasswordRegex(String)}
     */
	boolean isValidPassword(String password);


    SubForumPolicyI getSubforumPolicy();
    /**
     * Each member in this forum must have a password that matches the given regex
     */
	void setPasswordRegex(String regex);

    /**
     * Each subforum under this forum can have at most numOfModerators moderators
     */
    void setMaxModerators(int numOfModerators);

    /**
     * Checks whether a given user is a valid manager according to policy.
     * @param manager - the designated manager.
     */
    boolean isValidAdmin(User manager);

    /**
     * Checks whether a given user is a valid moderator according to policy.
     * @param moderator - the designated moderator.
     */
    boolean isValidModerator(User moderator);
}
