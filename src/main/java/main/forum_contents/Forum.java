package main.forum_contents;

import main.User.User;
import main.User.UserPermission;
import main.Utils.GmailSender;
import main.exceptions.*;
import main.interfaces.*;
import org.apache.log4j.Logger;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by hagai on 07/04/15.
 */
public class Forum implements ForumI {

    public static final String PERMISSION_REGULAR = "REGULAR";
    public static final String GUEST_USER_NAME = "Guest user";
    public static final String PERMISSION_GUEST = "GUEST";
    public static final String PERMISSION_ADMIN = "ADMINISTRATOR";
    private String forum_name;
    private ForumPolicyI policy;
    private HashMap<String, SubForumI> _subForums = new HashMap<>();
    private HashMap<String, UserI> _users = new HashMap<>();
    private HashMap<String, UserType> _userTypes = new HashMap<String, UserType>();
    private UserI guest = null;
    private UserI admin = null;
    private static Logger logger = Logger.getLogger(Forum.class.getName());



    public Forum(String name, ForumPolicyI policy){
        this.policy = policy;
        this.guest = new User(GUEST_USER_NAME, "no_pass", "nomail@nomail.com");
        this.admin = new User("Forum Admin", "zubur123", "forumadmin@nomail.com");
        addAllSubforumsToUser(guest, PERMISSION_GUEST);
        addAllSubforumsToUser(admin, PERMISSION_ADMIN);
        this._users.put("Guest", this.guest);
        this._users.put("Admin", this.admin);
        this.forum_name = name;
    }

    public void setAdmin(UserI admin){
        this.admin = admin;
    }

    public String viewStatistics(){
        return "No statistics yet";
    }

    //TODO remove
    public Forum getForum(){
        return this;
    }

    public String getName(){
        return this.forum_name;
    }
    @Override
    public HashMap<String, SubForumI> getSubForums(){ return _subForums;}


    @Override
    public SubForumI createSubForum(String name) throws SubForumAlreadyExistException {
        if (_subForums.containsKey(name)){
            throw new SubForumAlreadyExistException(name,this);
        }

        SubForumI subForum = new SubForum(name,  this.policy.getSubforumPolicy());
        _subForums.put(name, subForum);
        for (UserI user: _users.values()){
            UserPermission permission;
            if (user.getUsername().equals(GUEST_USER_NAME)){
                permission = new UserPermission(PERMISSION_GUEST, this, subForum);
            }
            else{
                permission = new UserPermission(PERMISSION_REGULAR, this, subForum);
            }
            user.addSubForumPermission(permission);
        }
        return subForum;
    }

    public void deleteSubForum(SubForumI subforum) throws SubForumDoesNotExsitsException {
        if (!_subForums.containsKey(subforum.getName())){
            throw new SubForumDoesNotExsitsException();
        }
        _subForums.remove(subforum.getName());
    }

    private void addAllSubforumsToUser(UserI user, String perm){
        for (SubForumI sub: _subForums.values()){
            user.addSubForumPermission(new UserPermission(perm, this, sub));
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

            //throw new InvalidUserCredentialsException(); TODO    ---> uncomment if victor does the checking.
        }
        // we are done with protective programing, time to do work.
        User new_user = new User(userName, password, eMail);
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
        return;
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

    public HashMap<String, SubForumI> get_subForums() {
        return _subForums;
    }

    public void set_subForums(HashMap<String, SubForumI> _subForums) {
        this._subForums = _subForums;
    }

    public HashMap<String, UserI> get_users() {
        return _users;
    }

    public void set_users(HashMap<String, UserI> _users) {
        this._users = _users;
    }

    public HashMap<String, UserType> get_userTypes() {
        return _userTypes;
    }

    public void set_userTypes(HashMap<String, UserType> _userTypes) {
        this._userTypes = _userTypes;
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

    public Forum(){}
}
