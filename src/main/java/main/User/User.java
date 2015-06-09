package main.User;

import main.Utils.SecureString;
import main.exceptions.*;
import main.forum_contents.ForumMessage;
import main.interfaces.*;
import org.hibernate.annotations.ColumnTransformer;

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
    private String username;
    //@Type(type="encryptedString")

    @Column(columnDefinition= "LONGBLOB")
    @ColumnTransformer(
            read="AES_DECRYPT(password, 'yourkey')",
            write="AES_ENCRYPT(?, 'yourkey')")
    private String password;
    private GregorianCalendar passwordCreationDate;
    private String email;
    private GregorianCalendar signUpDate;
    private int seniorityInDays;
    private int numOfMessages;
    @OneToMany(targetEntity = UserSubforumPermission.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<SubForumPermissionI> subForumsPermissions;
    @OneToOne(targetEntity = UserForumPermission.class, cascade = CascadeType.ALL)
    private ForumPermissionI forumPermissions;
    private boolean isEmailAuthenticated = true;
    private String secQ;
    private String secA;

    public User(String username, String password, String email, ForumPermissionI forumPermissions) {
        this.username = username;
        this.password = password;
        this.passwordCreationDate = new GregorianCalendar();
        this.email    = email;
        signUpDate = new GregorianCalendar();
        seniorityInDays = 0;
        numOfMessages = 0;
        this.isEmailAuthenticated = false;
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
    public boolean isEmailAuthenticated() {
        return isEmailAuthenticated;
    }



    public void setAuthenticated(){
        isEmailAuthenticated = true;
    }
    /**
         Get the list of all of the subforums of this user
     */
    @Override
    public Collection<SubForumPermissionI> getSubForumsPermissions() {
        return subForumsPermissions;
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

    public GregorianCalendar getSignUpDate() {
        return signUpDate;
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
        subForumPermission.replyToMessage(original,new ForumMessage(this, msgTitle, msgBody));
    }

    @Override
    public void reportModerator(SubForumI subforum, String moderatorUsername, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException {
        for(int i = 0; i < subForumsPermissions.size(); i++) {
            if(((Vector<SubForumPermissionI>)subForumsPermissions).elementAt(i).subForumExists(subforum.getTitle())){
                ((Vector<SubForumPermissionI>)subForumsPermissions).elementAt(i).reportModerator(moderatorUsername, reportMessage, this);
                break;
            }
        }
    }

    @Override
    public void deleteMessage(MessageI message, SubForumPermissionI subForumPermission)
            throws PermissionDeniedException, MessageNotFoundException {
        subForumPermission.deleteMessage(message, this.username);
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
        //TODO - not implemented
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void setModerator(SubForumI subForum, UserI moderator) throws PermissionDeniedException, SubForumNotFoundException {

        for(int i = 0; i < subForumsPermissions.size(); i++) {
            boolean found = ((Vector<SubForumPermissionI>)subForumsPermissions).elementAt(i).subForumExists(subForum.getTitle());
            if(found){
                ((Vector<SubForumPermissionI>)subForumsPermissions).elementAt(i).setModerator(moderator);
                return;
            }
        }
        throw new SubForumNotFoundException();
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

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    public Integer getId() {
        return id;
    }

    @Override
    public void updatePasswordCreationDate(){
        passwordCreationDate = new GregorianCalendar();
    }

    @Override
    public GregorianCalendar getPasswordCreationDate(){
        return passwordCreationDate;
    }

    @Override
    public void setSecurityQuestion(String quest) {
        this.secQ = quest;
    }

    @Override
    public void setSecurityAnswer(String ans) {
        this.secA = ans;
    }

    @Override
    public boolean isAdmin() {
        return forumPermissions.isAdmin();
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
