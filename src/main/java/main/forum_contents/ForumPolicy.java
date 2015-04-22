package main.forum_contents;

import main.User.User;
import main.interfaces.ForumPolicyI;
import main.interfaces.MessageI;
import main.interfaces.SubForumPolicyI;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by victor on 4/11/2015.
 */
public class ForumPolicy implements ForumPolicyI, SubForumPolicyI{

   // private Forum forum;
    private static final Logger logger = Logger.getLogger(ForumPolicy.class.getName());
    private final String forbiddenWords[];
    private final int GOLDEN_USER_SENIORITY = 12; // The required seniority in months.
    private final int SILVER_USER_SENIORITY = 4;

    private int maxModerators;
    private String passwordRegex;
//TODO - define parameters according to  requirements.
    public ForumPolicy(int maxModerators, String passwordRegex) {
        this.maxModerators = maxModerators;
        this.passwordRegex = passwordRegex;
        forbiddenWords = new String[2];
        forbiddenWords[0] = "stupid";
        forbiddenWords[1] = "dumb";
    }

    @java.lang.Override
    public boolean isValidPassword(String password) {
        return password.matches(passwordRegex);
    }

    @Override
    public SubForumPolicyI getSubforumPolicy() {
        return this;
    }

    public int getMaxModerators() {
        return maxModerators;
    }

    public String getPasswordRegex() {
        return passwordRegex;
    }

    @java.lang.Override
    public void setPasswordRegex(String regex) {
        logger.warn("password regex changed from " + passwordRegex + " to " + regex + ".");
        this.passwordRegex = regex;
    }

    @java.lang.Override
    public void setMaxModerators(int numOfModerators) {
        logger.warn("maximum number of moderators changed from "+maxModerators+" to " +numOfModerators + ".");
        this.maxModerators = numOfModerators;
    }

    @java.lang.Override
    public boolean isValidAdmin(User admin){
        long currYear = GregorianCalendar.getInstance().get(Calendar.YEAR);
        long userYear = admin.getSignUpDate().get(Calendar.YEAR);
        long currMonth = GregorianCalendar.getInstance().get(Calendar.MONTH);
        long userMonth = admin.getSignUpDate().get(Calendar.MONTH);
        long seniority = (currYear - userYear) * 12 + currMonth - userMonth;
        return seniority > GOLDEN_USER_SENIORITY;
    }

    @java.lang.Override
    public boolean isValidModerator(User moderator){
        long currYear = GregorianCalendar.getInstance().get(Calendar.YEAR);
        long userYear = moderator.getSignUpDate().get(Calendar.YEAR);
        long currMonth = GregorianCalendar.getInstance().get(Calendar.MONTH);
        long userMonth = moderator.getSignUpDate().get(Calendar.MONTH);
        long seniority = (currYear - userYear) * 12 + currMonth - userMonth;
        return seniority > SILVER_USER_SENIORITY;
    }

    @Override
    public boolean isValidMessage(MessageI message) {
        boolean valid = true;
        for (String word : forbiddenWords){
            valid = valid & (!message.getMessageText().contains(word) & !message.getMessageTitle().contains(word));
            if (!valid) { break; }
        }
        return valid;
    }
}
