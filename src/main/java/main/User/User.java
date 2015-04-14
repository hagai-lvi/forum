package main.User;

import main.Utils.SecureString;
import main.exceptions.*;
import main.interfaces.*;

import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * Created by gabigiladov on 4/11/15.
 */
public class User implements UserI {

    private String authString = null;
    private String username;
    private String password;
    private String email;
    private GregorianCalendar signUpDate;
    private int seniority_in_days;
    private int numOfMessages;
    private Vector<SubForumPermissionI> subForumsPermissions;
    private Vector<ForumPermissionI> forumsPermission;
    private boolean isAuthenticated;
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
        this.isAuthenticated = true; //TODO should be false, set to true for testing purpose
        this.authString = SecureString.nextUserAuthString();
        this.subForumsPermissions = new Vector<SubForumPermissionI>();
        this.forumsPermission = new Vector<ForumPermissionI>();
    }

    public void addForum(ForumI forum) throws PermissionDeniedException{
        forumsPermission.elementAt(0).addForum(forum);
    }

    /**
     * @return whether this user has authenticated his email address
     */
    public boolean isEmailAuthnticated() {
        return false;
    }

    public void setAuthenticated(){
        isAuthenticated = true;
    }
    /**
         Get the list of all of the subforums of this user
     */
    public Vector<SubForumPermissionI> getSubForumPermission() {

        return this.subForumsPermissions;
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

    public String getUserAuthString(){
        return this.authString;
    }

    @Override
    public Vector<SubForumPermissionI> viewSubForums() {
        return this.subForumsPermissions;
    }

    @Override
    public void createSubForum(String name, ForumI forum) throws PermissionDeniedException {
        for(int i = 0; i < forumsPermission.size(); i++) {
            if(forumsPermission.elementAt(i).findForum(forum.getName())){
                try {
                    forumsPermission.elementAt(i).createSubForum(name);
                    break;
                } catch (SubForumAlreadyExistException e) {
                    System.out.println("The SubForum is already exist!");;
                }
            }
        }
    }

    @Override
    public void deleteSubForum(SubForumI toDelete, ForumI forum) throws PermissionDeniedException{
        for(int i = 0; i < forumsPermission.size(); i++) {
            if(forumsPermission.elementAt(i).findForum(forum.getName())){
                forumsPermission.elementAt(i).deleteSubForum(toDelete);
                break;
            }
        }
    }

    @Override
    public void createThread(MessageI message, SubForumI subforum) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
        for(int i = 0; i < subForumsPermissions.size(); i++) {
            if(subForumsPermissions.elementAt(i).findForum(subforum.getName())){
                subForumsPermissions.elementAt(i).createThread(message);
                break;
            }
        }
    }

    @Override
    public void replyToMessage(SubForumI subforum, MessageI original, MessageI reply) throws PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException {
        for(int i = 0; i < subForumsPermissions.size(); i++) {
            if(subForumsPermissions.elementAt(i).findForum(subforum.getName())){
                subForumsPermissions.elementAt(i).replyToMessage(original, reply);
                break;
            }
        }
    }

    @Override
    public void reportModerator(SubForumI subforum, String moderatorUsername, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException {
        for(int i = 0; i < subForumsPermissions.size(); i++) {
            if(subForumsPermissions.elementAt(i).findForum(subforum.getName())){
                subForumsPermissions.elementAt(i).reportModerator(moderatorUsername, reportMessage, this);
                break;
            }
        }
    }

    @Override
    public void deleteMessage(MessageI message, SubForumI subforum) throws PermissionDeniedException {
        for(int i = 0; i < subForumsPermissions.size(); i++) {
            if(subForumsPermissions.elementAt(i).findForum(subforum.getName())){
                subForumsPermissions.elementAt(i).deleteMessage(message, this);
                break;
            }
        }
    }

    @Override
    public void setAdmin(UserI admin, ForumI forum) throws PermissionDeniedException {
        for(int i = 0; i < forumsPermission.size(); i++) {
            if(forumsPermission.elementAt(i).findForum(forum.getName())){
                forumsPermission.elementAt(i).setAdmin(admin);
                break;
            }
        }
    }

    @Override
    public void setPolicy(ForumI forum, ForumPolicyI policy) throws PermissionDeniedException {
        for(int i = 0; i < forumsPermission.size(); i++) {
            if(forumsPermission.elementAt(i).findForum(forum.getName())){
                forumsPermission.elementAt(i).setPolicy(policy);
                break;
            }
        }
    }

    @Override
    public String viewStatistics(ForumI forum) throws PermissionDeniedException {
        for(int i = 0; i < forumsPermission.size(); i++) {
            if(forumsPermission.elementAt(i).findForum(forum.getName())){
                return forumsPermission.elementAt(i).viewStatistics();
            }
        }
        return "Has no permission to view statistics";
    }

    @Override
    public void setModerator(SubForumI subForum, UserI moderator) throws PermissionDeniedException {
        for(int i = 0; i < subForumsPermissions.size(); i++) {
            if(subForumsPermissions.elementAt(i).findForum(subForum.getName())){
                subForumsPermissions.elementAt(i).setModerator(moderator);
                break;
            }
        }
    }

    @Override
    public void banModerator(SubForumI subForum, UserI moderatorToBan, long time) {
        throw new RuntimeException("Not yet implemented");
//        for(int i = 0; i < subForumsPermissions.size(); i++) {
//            if(subForumsPermissions.elementAt(i).findForum(subForum.getName())){
//                subForumsPermissions.elementAt(i).banModerator(moderatorToBan, time);
//                break;
//            }
//        }
    }

    @Override
    public void addSubForumPermission(SubForumPermissionI permission) {
        this.subForumsPermissions.add(permission);
    }

    public Vector<SubForumPermissionI> getSubForumsPermissions() {
        return subForumsPermissions;
    }

    public void setSubForumsPermissions(Vector<SubForumPermissionI> subForumsPermissions) {
        this.subForumsPermissions = subForumsPermissions;
    }
}
