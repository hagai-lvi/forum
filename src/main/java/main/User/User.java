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
    private int seniorityInDays;
    private int numOfMessages;
    private Vector<SubForumPermissionI> subForumsPermissions;
    private ForumPermissionI forumPermissions;
    private boolean isEmailAuthenticated;

    public User(String username, String password, String email, ForumPermissionI forumPermissions) {
        this.username = username;
        this.password = password;
        this.email    = email;
        signUpDate = new GregorianCalendar();
        seniorityInDays = 0;
        numOfMessages = 0;
        this.isEmailAuthenticated = false; //TODO should be false, set to true for testing purpose
        this.authString = SecureString.nextUserAuthString();
        this.subForumsPermissions = new Vector<>();
        this.forumPermissions = forumPermissions;
    }

    /**
     * @return whether this user has authenticated his email address
     */
    public boolean isEmailAuthnticated() {
        return false;
    }

    public void setAuthenticated(){
        isEmailAuthenticated = true;
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

    public int getSeniorityInDays() {
        return seniorityInDays;
    }

    public void setSeniorityInDays(int seniorityInDays) {
        this.seniorityInDays = seniorityInDays;
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
    public void createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException {
        forumPermissions.createSubForum(name);
    }

    @Override
    public void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException, SubForumDoesNotExsitsException {
        forumPermissions.deleteSubForum(toDelete);
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
    public void setAdmin(UserI admin) throws PermissionDeniedException {
        forumPermissions.setAdmin(admin);
    }

    @Override
    public void setPolicy(ForumPolicyI policy) throws PermissionDeniedException {
        forumPermissions.setPolicy(policy);
    }

    @Override
    public String viewStatistics(ForumI forum) throws PermissionDeniedException {
        //TODO
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void setModerator(SubForumI subForum, UserI moderator) throws PermissionDeniedException {
        //TODO
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
//            if(subForumsPermissions.elementAt(i).findSubforum(subForum.getName())){
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

    /**
     * This method is for testing purposes only, TODO need to think how to remove it
     */
    public void setSignUpDate(GregorianCalendar signUpDate){
        this.signUpDate = signUpDate;
    }
}
