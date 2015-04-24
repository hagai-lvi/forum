package main.User;

import main.Utils.SecureString;
import main.exceptions.*;
import main.forum_contents.ForumMessage;
import main.forum_contents.SubForum;
import main.interfaces.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * Created by gabigiladov on 4/11/15.
 */
@Entity
public class User implements UserI {

    private String authString = null;
    @Id
    private String username;
    private String password;
    private String email;
    private GregorianCalendar signUpDate;
    private int seniorityInDays;
    private int numOfMessages;
    @OneToMany(targetEntity = UserSubforumPermission.class, cascade = CascadeType.ALL)
    private Collection<SubForumPermissionI> subForumsPermissions;
    @OneToOne(targetEntity = UserForumPermission.class, cascade = CascadeType.ALL)
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

    public User() {
    }

    /**
     * @return whether this user has authenticated his email address
     */
    @Override
    public boolean isEmailAuthnticated() {
        return false;
    }

    public void setAuthenticated(){
        isEmailAuthenticated = true;
    }
    /**
         Get the list of all of the subforums of this user
     */
    @Override
    public Vector<SubForumPermissionI> getSubForumsPermissions() {
        return (Vector<SubForumPermissionI>)this.subForumsPermissions;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
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

    @Override
    public String getUserAuthString(){
        return this.authString;
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
    public void createThread(MessageI message, SubForumPermissionI subForumPermission) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
        subForumPermission.createThread(message);
    }

    @Override
    public void replyToMessage(SubForumPermissionI subForumPermission, MessageI original, String msgTitle, String msgBody) throws PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException {
        subForumPermission.replyToMessage(original,new ForumMessage(original, this, msgBody, msgTitle));
    }

    @Override
    public void reportModerator(SubForumI subforum, String moderatorUsername, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException {
        for(int i = 0; i < subForumsPermissions.size(); i++) {
            if(((Vector<SubForumPermissionI>)subForumsPermissions).elementAt(i).findForum(subforum.getName())){
                ((Vector<SubForumPermissionI>)subForumsPermissions).elementAt(i).reportModerator(moderatorUsername, reportMessage, this);
                break;
            }
        }
    }

    @Override
    public void deleteMessage(MessageI message, SubForumPermissionI subForumPermission)
            throws PermissionDeniedException, MessageNotFoundException {
        subForumPermission.deleteMessage(message, this);
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
            if(((Vector<SubForumPermissionI>)subForumsPermissions).elementAt(i).findForum(subForum.getName())){
                ((Vector<SubForumPermissionI>)subForumsPermissions).elementAt(i).setModerator(moderator);
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
