package main.interfaces;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface ForumPolicyI {


    /**
     * Check if the given password matches the regex that was given to {@link #setPasswordRegex(String)}
     */
	boolean isValidPassword(String password);

    /**
     * Each member in this forum must have a password that matches the given regex
     */
	void setPasswordRegex(String regex);

    /**
     * Each subforum under this forum can have at most numOfModerators moderators
     */
    void setMaxModerators(int numOfModerators);



}
