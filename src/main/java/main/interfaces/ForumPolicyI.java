package main.interfaces;

import java.util.GregorianCalendar;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ForumPolicyI {


    /**
     * Check if the given password matches the regex that was given to {@link #setPasswordRegex(String)}
     */
	boolean isValidPassword(String password);

    boolean isValidEmailAddress(String email);

    /**
     * Check whether the password date is in effect according to policy.
     * @param passwordDate - the date the user's password was created.
     */
    boolean isPasswordInEffect(GregorianCalendar passwordDate);

    /**
     * Check whether the given user can be a moderator of the given subforum.
     * @param user - The designated moderator
     * @param subforum - The target subforum.
     */
    boolean canAssignModerator(UserI user, SubForumI subforum);


    boolean hasMoreAuthQuestions();
    /**
     * Returns the SubForum portion of the forum policy.
     */
    SubForumPolicyI getSubforumPolicy();

    /**
     * Each member in this forum must have a password that matches the given regex
     */
	void setPasswordRegex(String regex);

    /**
     * Each sub-forum under this forum can have at most numOfModerators moderators
     */
    void setMaxModerators(int numOfModerators);

    /**
     * Checks whether a given user is a valid manager according to policy.
     * @param admin - the designated manager.
     */
    boolean isValidAdmin(UserI admin);

    /**
     * Checks whether a given user is a valid moderator according to policy.
     * @param moderator - the designated moderator.
     */
    boolean isValidModerator(UserI moderator);

    /**
     * Check whether the current forum is a secured forum.
     */
    boolean isSecured();

}
