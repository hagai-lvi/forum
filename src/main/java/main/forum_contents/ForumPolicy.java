package main.forum_contents;

import main.interfaces.*;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by victor on 4/11/2015.
 */
@Entity
public class ForumPolicy implements ForumPolicyI, SubForumPolicyI{

   // private Forum forum;
    @Transient
    private static final Logger logger = Logger.getLogger(ForumPolicy.class.getName());
    @Transient // TODO  - This is not a way to save collection, it will not be saved
    private String forbiddenWords[];
    private final int GOLDEN_USER_SENIORITY = 12; // The required seniority in months.
    private final int SILVER_USER_SENIORITY = 4;
    private boolean more_questions = false;
    private int maxModerators;
    private String passwordRegex;
    private int passwordEffectTime; //in days.
    private String validEmailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]+)$";

    public boolean isSecured() {
        return secured;
    }

    private boolean secured;

//TODO - define parameters according to requirements.
    public ForumPolicy(boolean isSecured, int maxModerators, String passwordRegex, int passwordEffectTime) {
        this.secured = isSecured;
        this.maxModerators = maxModerators;
        this.passwordRegex = passwordRegex;
        this.passwordEffectTime = passwordEffectTime;
        if (isSecured){
            more_questions = true;
        }

        //TODO - decide how to manage forbidden words.
        forbiddenWords = new String[2];
        forbiddenWords[0] = "stupid";
        forbiddenWords[1] = "dumb";
    }

    public ForumPolicy() {
    }

    @Override
    public boolean isValidPassword(String password) {
        return password.matches(passwordRegex);
    }

    @Override
    public boolean isValidEmailAddress(String email) {
        return email.matches(validEmailRegex);
    }

    @Override
    public boolean isPasswordInEffect(GregorianCalendar passwordDate){
        long currYear = GregorianCalendar.getInstance().get(Calendar.YEAR);
        long userYear = passwordDate.get(Calendar.YEAR);
        long currDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR);
        long userDay = passwordDate.getInstance().get(Calendar.DAY_OF_YEAR);
        long age = (currYear - userYear) * 365 + currDay - userDay;
        return age < passwordEffectTime;
    }

    @Override
    public boolean canAssignModerator(UserI user, SubForumI subforum){
        return
                (subforum.getModerators().size() < maxModerators)
                && isValidModerator(user);
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
    public boolean isValidAdmin(UserI admin){
        long currYear = GregorianCalendar.getInstance().get(Calendar.YEAR);
        long userYear = admin.getSignUpDate().get(Calendar.YEAR);
        long currMonth = GregorianCalendar.getInstance().get(Calendar.MONTH);
        long userMonth = admin.getSignUpDate().get(Calendar.MONTH);
        long seniority = (currYear - userYear) * 12 + currMonth - userMonth;
        return seniority > GOLDEN_USER_SENIORITY;
    }

    @java.lang.Override
    public boolean isValidModerator(UserI moderator){
        long currYear = GregorianCalendar.getInstance().get(Calendar.YEAR);
        long userYear = moderator.getSignUpDate().get(Calendar.YEAR);
        long currMonth = GregorianCalendar.getInstance().get(Calendar.MONTH);
        long userMonth = moderator.getSignUpDate().get(Calendar.MONTH);
        long seniority = (currYear - userYear) * 12 + currMonth - userMonth;
        return seniority > SILVER_USER_SENIORITY;
    }

    public boolean hasMoreAuthQuestions(){
        return more_questions;
    }

    @Override
    public boolean isValidMessage(String title, String text) {
        if (
                title == null ||
                text == null ||
                title.equals("") ||
                text.equals("")
                )
        {
            return false;
        }
        return true;
    }

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
