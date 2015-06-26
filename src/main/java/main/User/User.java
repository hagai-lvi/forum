package main.User;

import main.Persistancy.PersistantObject;
import main.Utils.SecureString;
import main.exceptions.*;
import main.interfaces.*;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.util.*;

/**
 * Created by gabigiladov on 4/11/15.
 */
@Entity
public class User extends PersistantObject implements UserI, Cloneable {
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
    private Map<String, SubForumPermissionI> subForumsPermissions;
    @OneToOne(targetEntity = UserForumPermission.class, cascade = CascadeType.ALL)
    private ForumPermissionI forumPermissions;
    private boolean isEmailAuthenticated;
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
        this.subForumsPermissions = new HashMap<>();
        this.id = new UserForumID(username, forumPermissions.getForumName());
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
        Update();
    }
    /**
         Get the list of all of the subforums of this user
     */
    @Override
    public Map<String, SubForumPermissionI> getSubForumsPermissions() {
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
    public SubForumI createSubForum(String name) throws PermissionDeniedException, ForumNotFoundException, SubForumDoesNotExistException, SubForumAlreadyExistException {
        SubForumI subforum = forumPermissions.createSubForum(name);
        UserSubforumPermission newPerms = new UserSubforumPermission(forumPermissions.getPermission(), forumPermissions.getForum().getName(), name);
        subForumsPermissions.put(name, newPerms);
        this.Update();
        return subforum;
    }

    @Override
    public void deleteSubForum(String toDelete) throws PermissionDeniedException, SubForumDoesNotExistException, ForumNotFoundException {
        forumPermissions.deleteSubForum(toDelete);
    }

    @Override
    public ThreadI createThread(String title, String text, String subforum) throws PermissionDeniedException, DoesNotComplyWithPolicyException, SubForumDoesNotExistException {
        ThreadI threadI = findPermission(subforum).createThread(username, title, text);
        return threadI;
    }

    @Override
    public int replyToMessage(String subforum, MessageI original, String title, String text) throws PermissionDeniedException, MessageNotFoundException, DoesNotComplyWithPolicyException, SubForumDoesNotExistException {
        return findPermission(subforum).replyToMessage(original, username, title, text);
    }

    @Override
    public void reportModerator(SubForumI subforum, String moderatorUsername, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException {
        subforum.reportModerator(moderatorUsername, reportMessage, this);
    }

    @Override
    public void deleteMessage(String subforum, String thread, MessageI mes)
            throws PermissionDeniedException, MessageNotFoundException, SubForumDoesNotExistException {
        findPermission(subforum).deleteMessage(this.username,thread, mes);
    }

    @Override
    public void setAdmin(UserI admin) throws PermissionDeniedException, ForumNotFoundException, UserNotFoundException, CloneNotSupportedException {
        forumPermissions.setAdmin(admin);
    }

    @Override
    public void setPolicy(ForumPolicyI policy) throws PermissionDeniedException, ForumNotFoundException {
        forumPermissions.setPolicy(policy);
    }

    @Override
    public String viewStatistics() throws PermissionDeniedException {
        return null; //TODO - not yet implemented.
    }

    @Override
    public void setModerator(String subForum, UserI moderator) throws PermissionDeniedException, SubForumNotFoundException {
         boolean found = subForumsPermissions.containsKey(subForum);
            if(found){
                if(forumPermissions.isAdmin()) {
                    subForumsPermissions.get(subForum).setModerator(moderator);
                }
                else throw new PermissionDeniedException("Can not set moderator");
            }
        throw new SubForumNotFoundException();
    }

    @Override
    public void banModerator(SubForumI subForum, UserI moderatorToBan, long time) {
        throw new RuntimeException("Not yet implemented");
        //TODO
    }

    @Override
    public void addSubForumPermission(String subforum, SubForumPermissionI permission) {
        this.subForumsPermissions.put(subforum, permission);
    }

    public void setSubForumsPermissions(Map<String, SubForumPermissionI> subForumsPermissions) {
        this.subForumsPermissions = subForumsPermissions;
    }

    /**
     * This method is for testing purposes only, TODO need to think how to remove it
     */
    public void setSignUpDate(GregorianCalendar signUpDate){
        this.signUpDate = signUpDate;
    }

    @EmbeddedId
    private UserForumID id;

    public UserForumID getId() {
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

    @Override
    public boolean canReply(String subForum) throws SubForumDoesNotExistException, PermissionDeniedException {
        SubForumPermissionI permission = findPermission(subForum);
        return permission.canReply();
    }

    @Override
    public boolean canAddThread(String subForum) throws SubForumDoesNotExistException, PermissionDeniedException {
        SubForumPermissionI permission = findPermission(subForum);
        return permission.canAddThread();
    }

    @Override
    public void editMessage(String subforum, ThreadI thread, int messageId, String title, String text) throws SubForumDoesNotExistException, MessageNotFoundException {
        findPermission(subforum).editMessage(thread, messageId, title, text);
    }

    @Override
    public void removeModerator(String subforum, String moderatorName) throws SubForumDoesNotExistException {
        findPermission(subforum).removeModerator(moderatorName);
    }

    @Override
    public String getForumStatus() {
        if (forumPermissions.isAdmin()){
            return "Admin";
        }
        if (forumPermissions.isGuest()){
            return "Guest";
        }
        return "User";
    }

    @Override
    public String getSubForumStatus(String subForum) throws SubForumDoesNotExistException {
        Permissions perms = findPermission(subForum).getPermission();
        switch (perms){
            case PERMISSIONS_MODERATOR:
                return "Moderator";
            case PERMISSIONS_USER:
                return "User";
            case PERMISSIONS_GUEST:
                return "Guest";
        }
        return null;
    }

    @Override
    public boolean isOwnerOfMessage(MessageI message) {
        return message.getUser().equals(this.username);
    }

    @Override
    public void setAuthenticatedAdmin() {
        isEmailAuthenticated = true;
    }

    private void setForumPermissions(Permissions permissionsAdmin) throws ForumNotFoundException {
        forumPermissions = UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_ADMIN, forumPermissions.getForum().getName());
    }

  //  @Override
  //  public void becomeAdmin() {
  //      forumPermissions.becomeAdmin();
  //  }


    @Override
    public UserI cloneAs(Permissions permission) throws ForumNotFoundException, CloneNotSupportedException {
        Permissions temPerms = this.forumPermissions.getPermission();
        this.setForumPermissions(permission);
        UserI clone = (User)this.clone();//new User(this.username, this.password, this.email, clonePermissions);
        this.setForumPermissions(temPerms);
        return clone;
    }

    @Override
    public boolean isGuest() {
        return forumPermissions.isGuest();
    }

    private SubForumPermissionI findPermission(String subForum) throws SubForumDoesNotExistException {
        if(!subForumsPermissions.containsKey(subForum))
            throw new SubForumDoesNotExistException();
        return subForumsPermissions.get(subForum);
    }

    public void setId(UserForumID id) {
        this.id = id;
    }

    public static User getUserFromDB(String username, String forumname){
            return pers.load(User.class, new UserForumID(username, forumname));
    }



    public static void delete(String username, String forumname) throws UserNotFoundException {

        User user = getUserFromDB(username, forumname);
        if(user == null) throw new UserNotFoundException("User not found");
        if (user.subForumsPermissions != null) {
            user.subForumsPermissions.clear();
            user.subForumsPermissions = null;
            user.Update();
        }
        pers.Delete(user);

    }

}
