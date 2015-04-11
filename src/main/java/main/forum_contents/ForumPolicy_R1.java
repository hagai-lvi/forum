package main.forum_contents;

import main.User.User;
import main.interfaces.ForumPolicyI;
import main.interfaces.UserI;
import org.apache.log4j.Logger;
import org.junit.experimental.theories.FromDataPoints;

import java.util.GregorianCalendar;

/**
 * Created by victor on 4/11/2015.
 */
public class ForumPolicy_R1 implements ForumPolicyI {

    private Forum forum;
    private static Logger logger = Logger.getLogger(ForumPolicy_R1.class.getName());

    private final int GOLDEN_USER_SENIORITY = 12; // The required seniority in months.
    private final int SILVER_USER_SENIORITY = 4;

    private int maxModerators;
    private String passwordRegex;

    public ForumPolicy_R1(Forum forum, int maxModerators, String passwordRegex) {
        this.maxModerators = maxModerators;
        this.passwordRegex = passwordRegex;
        this.forum = forum;
    }

    @java.lang.Override
    public boolean isValidPassword(String password) {
        return password.matches(passwordRegex);
    }

    public int getMaxModerators() {
        return maxModerators;
    }

    public String getPasswordRegex() {
        return passwordRegex;
    }

    @java.lang.Override
    public void setPasswordRegex(String regex) {
        logger.warn("password regex changed");
        this.passwordRegex = regex;
    }

    @java.lang.Override
    public void setMaxModerators(int numOfModerators) {
        logger.warn("maximum number of moderators modified.");
        this.maxModerators = numOfModerators;
    }

    @java.lang.Override
    public boolean isValidManager(User manager){
        long currTime = GregorianCalendar.getInstance().getTime().getTime();
        long userTime = manager.getSignUpDate().getTime().getTime();
        long timeSinceRegistration = currTime - userTime;
        System.out.println(timeSinceRegistration);
        return true;

    }

    @java.lang.Override
    public boolean isValidModerator(User moderator){
        /* TODO */
        return false;
    }
}
