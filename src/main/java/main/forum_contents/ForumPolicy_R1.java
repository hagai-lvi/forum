package main.forum_contents;

import main.interfaces.ForumPolicyI;
import main.interfaces.UserI;

import java.util.logging.Logger;

/**
 * Created by victor on 4/11/2015.
 */
public class ForumPolicy_R1 implements ForumPolicyI {

    private static Logger logger = Logger.getLogger(ForumPolicy_R1.class.getName());

    private final int GOLDEN_USER_SENIORITY = 12; // The required seniority in months.
    private final int SILVER_USER_SENIORITY = 4;

    private int maxModerators;
    private String passwordRegex;

    public ForumPolicy_R1() {
        this.maxModerators = 1;
        this.passwordRegex = "[a-zA-Z][a-zA-Z0-9]^7";
    }

    public ForumPolicy_R1(int maxModerators, String passwordRegex) {
        this.maxModerators = maxModerators;
        this.passwordRegex = passwordRegex;
    }

    @java.lang.Override
    public boolean isValidPassword(String password) {
        /* TODO */
        return false;
    }

    @java.lang.Override
    public void setPasswordRegex(String regex) {
        logger.warning("password regex modified.");
        this.passwordRegex = regex;
    }

    @java.lang.Override
    public void setMaxModerators(int numOfModerators) {
        logger.warning("maximum number of moderators modified.");
        this.maxModerators = numOfModerators;
    }

    @java.lang.Override
    public boolean isValidManager(UserI manager){
        /* TODO */
        return false;
    }

    @java.lang.Override
    public boolean isValidModerator(UserI moderator){
        /* TODO */
        return false;
    }
}
