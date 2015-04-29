package main.forum_contents;

import main.User.User;
import main.User.UserForumPermission;
import main.User.UserSubforumPermission;
import main.Utils.GmailSender;
import main.exceptions.InvalidUserCredentialsException;
import main.exceptions.SubForumAlreadyExistException;
import main.exceptions.SubForumDoesNotExsitsException;
import main.exceptions.UserAlreadyExistsException;
import main.interfaces.*;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hagai on 07/04/15.
 */

@Entity
public class Forum implements ForumI {

    //TODO use enum permissions, fix all occurences of 'Guest' or 'Admin' in the code
    public static final String PERMISSION_REGULAR = "REGULAR";
    public static final String GUEST_USER_NAME = "Guest user";
    public static final String PERMISSION_GUEST = "GUEST";
    public static final String PERMISSION_ADMIN = "ADMINISTRATOR";
    public static final String ADMIN_USERNAME = "ADMIN";
    public static final String ADMIN_PASSWORD = "ADMIN";
    private String forum_name;

    @OneToOne(targetEntity = ForumPolicy.class)
    private ForumPolicyI policy;

    @OneToMany(targetEntity = SubForum.class, cascade = CascadeType.ALL)
    @MapKey(name="_name")
    private Map<String, SubForumI> _subForums = new HashMap<>();

    @OneToMany(targetEntity = User.class, cascade = CascadeType.ALL)
    private Map<String, UserI> _users = new HashMap<>();

    @OneToMany(targetEntity =  UserType.class, cascade = CascadeType.ALL)
    private Map<String, UserType> _userTypes = new HashMap<>();

    @OneToOne(targetEntity = User.class)

    private UserI guest;

    @OneToOne(targetEntity = User.class)
    private UserI admin;
    private static final Logger logger = Logger.getLogger(Forum.class.getName());



    public Forum(String name, ForumPolicyI policy){
        this.policy = policy;
        initGuest();
        initAdmin();//TODO should be initialized?
        addAllSubforumsToUser(guest, PERMISSION_GUEST);
        addAllSubforumsToUser(admin, PERMISSION_ADMIN);
        this._users.put("Guest", this.guest);
        this._users.put(this.admin.getUsername(), this.admin);
        this.forum_name = name;
    }

    public Forum() {
    }

    private void initAdmin() {
        ForumPermissionI adminPermission =
                UserForumPermission.createUserForumPermissions(UserForumPermission.PERMISSIONS.PERMISSIONS_ADMIN, this);
        this.admin = new User(ADMIN_USERNAME, ADMIN_PASSWORD, "forumadmin@nomail.com", adminPermission);
    }

    private void initGuest() {
        ForumPermissionI guestPermission =
                UserForumPermission.createUserForumPermissions(UserForumPermission.PERMISSIONS.PERMISSIONS_GUEST, this);
        this.guest = new User(GUEST_USER_NAME, "no_pass", "nomail@nomail.com", guestPermission);
    }

    @Override
    public void setAdmin(UserI admin){
        this.admin = admin;
    }

    @Override
    public String viewStatistics(){
        return "No statistics yet";
    }

    //TODO remove
    public Forum getForum(){
        return this;
    }

    @Override
    public String getName(){
        return this.forum_name;
    }

    @Override
    public Collection<SubForumI> getSubForums(){ return _subForums.values();}


    @Override
    public SubForumI createSubForum(String name) throws SubForumAlreadyExistException {
        if (_subForums.containsKey(name)){
            throw new SubForumAlreadyExistException(name,this);
        }

        SubForumI subForum = new SubForum(name,  this.policy.getsubforumpolicy());
        _subForums.put(name, subForum);
        for (UserI user: _users.values()){
            UserSubforumPermission permission;
            if (user.getUsername().equals(GUEST_USER_NAME)){
                permission = new UserSubforumPermission(PERMISSION_GUEST, this, subForum);
            }
            else{
                permission = new UserSubforumPermission(PERMISSION_REGULAR, this, subForum);
            }
            user.addSubForumPermission(permission);
        }
        return subForum;
    }

    @Override
    public void deleteSubForum(SubForumI subforum) throws SubForumDoesNotExsitsException {
        if (!_subForums.containsKey(subforum.getName())){
            throw new SubForumDoesNotExsitsException();
        }
        _subForums.remove(subforum.getName());
    }

    private void addAllSubforumsToUser(UserI user, String perm){
        for (SubForumI sub: _subForums.values()){
            user.addSubForumPermission(new UserSubforumPermission(perm, this, sub));
        }
    }

    @Override
    public User register(String userName, String password, String eMail) throws UserAlreadyExistsException, InvalidUserCredentialsException {
        // Protective Programing
        if (userName.equals("") || userName == null || password.equals("") || password == null || eMail.equals("") || eMail == null)
            throw new InvalidUserCredentialsException();
        if (_users.containsKey(userName)){
            throw new UserAlreadyExistsException(userName);
        }
        if (!policy.isValidPassword(password)){

            throw new InvalidUserCredentialsException(); //TODO    ---> uncomment if victor does the checking.
        }
        // we are done with protective programing, time to do work.
        ForumPermissionI userPermissions = UserForumPermission.
                createUserForumPermissions(UserForumPermission.PERMISSIONS.PERMISSIONS_USER,this);
        User new_user = new User(userName, password, eMail, userPermissions);
        addAllSubforumsToUser(new_user, PERMISSION_REGULAR);
        //sendAuthenticationEMail(new_user);    --> uncomment to actually send mails
        _users.put(userName, new_user);
        return new_user;
    }

    @Override
    public void sendAuthenticationEMail(UserI user) {
        String topic = "Authentication Email For: " + user.getUsername();
        String body = "Hello, " + user.getUsername() + " \n This is your authentication token : \n";
        body += user.getUserAuthString();
        try {
            GmailSender.sendFromGMail(new String[]{user.getEmail()}, topic, body);
        }
        catch(Exception e){
            logger.error("Problem sending auth mail");
        }
    }

    public boolean enterUserAuthenticationString(User user, String auth_string){
        try{
            if (user.getUserAuthString().equals(auth_string)){
                user.setAuthenticated();
                return true;
            }
        }
        catch (Throwable e){
            logger.error("Problem authenticating user - gave null string for example");
            return false;
        }
        return false;
    }

    public UserI get_admin_user(){
        return this.admin;
    }

    @Override
    public UserI login(String username, String password) throws InvalidUserCredentialsException {
        if (_users.containsKey(username) &&
                _users.get(username).getPassword().equals(password)){
            return _users.get(username);
        }
        else {
            throw new InvalidUserCredentialsException();
        }
    }

    @Override
    public UserI guestLogin() {
        return guest; // guest was intialized on start
    }

    @Override
    public void logout(UserI user) {
        //what should happen?  --> nothing.
    }

    @Override
    public void setPolicy(ForumPolicyI policy) {
        this.policy = policy;
    }

    @Override
    public Collection<UserI> getUserList() {
        return _users.values();
    }

    @Override
    public Collection<UserType> getUserTypes() {
        return _userTypes.values();
    }

    @Override
    public void addUserType(String type) {
        this._userTypes.put(type, new UserType(type));
    }

    @Override
    public boolean removeUserType(String type) {
        return false;
    }

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return getName();
    }

    public String getForum_name() {
        return forum_name;
    }

    public void setForum_name(String forum_name) {
        this.forum_name = forum_name;
    }

    public ForumPolicyI getPolicy() {
        return policy;
    }

    public UserI getGuest() {
        return guest;
    }

    public void setGuest(UserI guest) {
        this.guest = guest;
    }

    public UserI getAdmin() {
        return admin;
    }

}
