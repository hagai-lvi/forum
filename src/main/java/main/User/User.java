package main.User;

import main.interfaces.ForumI;
import main.interfaces.SubForumPermissionI;
import main.interfaces.UserI;

import java.util.Collection;
import java.util.GregorianCalendar;

/**
 * Created by gabigiladov on 4/11/15.
 */
public class User implements UserI {

    private String username;
    private String password;
    private String email;
    private GregorianCalendar signUpDate;
    private int seniority_in_days;
    private int numOfMessages;
    private Collection<SubForumPermissionI> subForumsPermissions;

    public void setUsername(String username) {
        this.username = username;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email    = email;
        signUpDate = new GregorianCalendar();
        seniority_in_days = 0;
        numOfMessages = 0;
    }

    public void addForum(ForumI forum){
        return; // TODO : add
    }

    /**
     * @return whether this user has authenticated his email address
     */
    public boolean isEmailAuthnticated() {
        return false;
    }

    /**
    ﬂ * Get the list of all of the subforums of this user
     */
    public Collection<SubForumPermissionI> getSubForumPermission() {
        return null;
    }

    public String getUsername(){

        return username;
    }

    public String getPassword(){

        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GregorianCalendar getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(GregorianCalendar signUpDate) {
        this.signUpDate = signUpDate;
    }

    public int getSeniority_in_days() {
        return seniority_in_days;
    }

    public void setSeniority_in_days(int seniority_in_days) {
        this.seniority_in_days = seniority_in_days;
    }

    public int getNumOfMessages() {
        return numOfMessages;
    }

    public void setNumOfMessages(int numOfMessages) {
        this.numOfMessages = numOfMessages;
    }

    public Collection<SubForumPermissionI> getSubForumsPermissions() {
        return subForumsPermissions;
    }

    public void setSubForumsPermissions(Collection<SubForumPermissionI> subForumsPermissions) {
        this.subForumsPermissions = subForumsPermissions;
    }
}
