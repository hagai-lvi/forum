package main.forum_contents;

import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import main.Persistancy.HibernatePersistancyAbstractor;
import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.User.UserSubforumPermission;
import main.Utils.GmailSender;
import main.exceptions.*;
import main.interfaces.*;
import org.apache.log4j.Logger;
import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by hagai on 07/04/15.
 */

@Entity
public class Forum implements ForumI {

    //TODO use enum permissions, fix all occurences of 'Guest' or 'Admin' in the code
    public static final String GUEST_USER_NAME = "Guest user";
    public static final String ADMIN_USERNAME = "ADMIN";
    public static final String ADMIN_PASSWORD = "ADMIN";


    @Id
    @JsonView(NativeGuiController.class)
    private String forum_name;
    @OneToOne(targetEntity = ForumPolicy.class, cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    private ForumPolicyI policy;

    @OneToMany(targetEntity = SubForum.class, cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @MapKey(name="_name")
    private Map<String, SubForumI> _subForums = new HashMap<>();

    @OneToMany(targetEntity = User.class, cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    private Map<String, UserI> _users = new HashMap<>();

    @OneToMany(targetEntity =  UserType.class, cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    private Map<String, UserType> _userTypes = new HashMap<>();

    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch= FetchType.EAGER)

    private UserI guest;

    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    private UserI admin;

    @Transient
    private HibernatePersistancyAbstractor pers;
    private static final Logger logger = Logger.getLogger(Forum.class.getName());



    public Forum(String name, ForumPolicyI policy){
        this.policy = policy;
        initGuest();
        initAdmin();//TODO should be initialized?
        addAllSubforumsToUser(guest, Permissions.PERMISSIONS_GUEST);
        addAllSubforumsToUser(admin, Permissions.PERMISSIONS_ADMIN);
        this._users.put("Guest", this.guest);
        this._users.put(this.admin.getUsername(), this.admin);
        this.forum_name = name;
        this.pers = HibernatePersistancyAbstractor.getPersistanceAbstractor();
        this.saveOrUpdate();
    }

    public Forum() {  // needed here for hibernate
    }

    private void initAdmin() {
        ForumPermissionI adminPermission =
                UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_ADMIN, this);
        this.admin = new User(ADMIN_USERNAME, ADMIN_PASSWORD, "forumadmin@nomail.com", adminPermission);
    }

    private void initGuest() {
        ForumPermissionI guestPermission =
                UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_GUEST, this);
        this.guest = new User(GUEST_USER_NAME, "no_pass", "nomail@nomail.com", guestPermission);
    }

    @Override
    public void setAdmin(UserI admin){
        this.admin = admin;
        Update();
    }

    @Override
    public String viewStatistics(){
        return "No statistics yet";
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

        SubForumI subForum = new SubForum(name,  this.policy.getSubforumPolicy());
        _subForums.put(name, subForum);
        for (UserI user: _users.values()){
            UserSubforumPermission permission;
            if (user.getUsername().equals(GUEST_USER_NAME)){
                permission = new UserSubforumPermission(Permissions.PERMISSIONS_GUEST, this, subForum);
            }
            else{
                permission = new UserSubforumPermission(Permissions.PERMISSIONS_USER, this, subForum);
            }
            user.addSubForumPermission(permission);
        }
        Update();
        return subForum;
    }

    @Override
    public void deleteSubForum(SubForumI subforum) throws SubForumDoesNotExsitsException {
        if (!_subForums.containsKey(subforum.getName())){
            throw new SubForumDoesNotExsitsException();
        }
        _subForums.remove(subforum.getName());
        Update();
    }

    private void addAllSubforumsToUser(UserI user, Permissions perm){
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

            //throw new InvalidUserCredentialsException(); TODO    ---> uncomment if victor does the checking.
        }
        // we are done with protective programing, time to do work.
        ForumPermissionI userPermissions = UserForumPermission.
                createUserForumPermissions(Permissions.PERMISSIONS_USER,this);
        User new_user = new User(userName, password, eMail, userPermissions);
        addAllSubforumsToUser(new_user, Permissions.PERMISSIONS_USER);
        //sendAuthenticationEMail(new_user);    --> uncomment to actually send mails
        _users.put(userName, new_user);
        Update();
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

    public boolean enterUserAuthenticationString(UserI user, String auth_string){
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
    public UserI login(String username, String password) throws InvalidUserCredentialsException, NeedMoreAuthParametersException {
        if (_users.containsKey(username) &&
                _users.get(username).getPassword().equals(password)){

            if (policy.hasMoreAuthQuestions()){
                throw new NeedMoreAuthParametersException();
            }

            return _users.get(username);
        }
        else {
            throw new InvalidUserCredentialsException();
        }
    }

    public UserI login(String username, String password, List<String> more_answers) throws InvalidUserCredentialsException{
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
        Update();
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
    public void addUserType(String typeName, int seniority, int numOfMessages, int connectionTime){
        //TODO new parameters
        this._userTypes.put(typeName, new UserType(typeName));
        Update();
    }

    @Override
    public boolean removeUserType(String type) {
        return false;
    }




    public void save(){    // save the forum to the database
        pers.save(this);
    }

    public void load(String forum_name){
        pers.load(Forum.class, forum_name);
    }

    public void saveOrUpdate(){    // save the forum to the database
        pers.saveOrUpdate(this);
    }

    public void Update(){    // save the forum to the database
        pers.Update(this);
    }

}
