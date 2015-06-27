package main.forum_contents;

import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import main.Persistancy.PersistantObject;
import main.User.Permissions;
import main.User.User;
import main.User.UserForumPermission;
import main.Utils.GmailSender;
import main.exceptions.*;
import main.interfaces.*;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by hagai on 07/04/15.
 */

@Entity
public class Forum extends PersistantObject implements ForumI{

    public static final String GUEST_USER_NAME = "Guest user";
    public static final String ADMIN_USERNAME = "ADMIN";
    public static final String ADMIN_PASSWORD = "ADMIN";


    @Id
    @JsonView(NativeGuiController.class)
    private String forum_name;

    @ManyToOne(targetEntity = ForumPolicy.class, cascade = {CascadeType.ALL},  fetch= FetchType.EAGER)
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

   // @OneToMany(targetEntity = User.class, cascade = CascadeType.ALL, fetch= FetchType.EAGER)
   // private Map<String, UserI> admins = new HashMap<>();

    @Transient
    //private static HibernatePersistancyAbstractor pers;
    private static final Logger logger = Logger.getLogger(Forum.class.getName());



    public Forum(String name, ForumPolicyI policy){
        this.policy = policy;
        this.forum_name = name;
        initGuest();
        initAdmin();
        addAllSubforumsToUser(guest);
        addAllSubforumsToUser(User.getUserFromDB(ADMIN_USERNAME, forum_name));
        this._users.put("Guest", this.guest);
       //this._users.put(this.admin.getUsername(), this.admin);
        //this.pers = HibernatePersistancyAbstractor.getPersistanceAbstractor();
        this.Save();
       // Update();
    }

    public Forum() {  // needed here for hibernate
    }

    private void initAdmin() {
        ForumPermissionI userPermissions = UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_SUPERADMIN, forum_name);
        User newUser = new User(ADMIN_USERNAME, ADMIN_PASSWORD, "admin@gmail.com", userPermissions);
        newUser.setAuthenticatedAdmin();
        addAllSubforumsToUser(newUser);
        _users.put(ADMIN_USERNAME, newUser);
       // admins.put(ADMIN_USERNAME,newUser);

    }

    private void initGuest() {
        ForumPermissionI guestPermission =
                UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_GUEST, forum_name);
        this.guest = new User(GUEST_USER_NAME, "no_pass", "nomail@nomail.com", guestPermission);
    }

    @Override
    public void setAdmin(UserI admin){
        _users.replace(admin.getUsername(), admin);
        for (SubForumI subForum: _subForums.values()){
            admin.addSubForumPermission(subForum.getTitle());
        }
        Update();
    }

    @Override
    public String viewStatistics(){
        return "Number of messages: " + getNumOfMessages();

    }


    private int getNumOfMessages(){
        int sum =0;
        for (SubForumI s : _subForums.values()){
            sum+=s.getMessagesCount();
        }
        return sum;
    }


    @Override
    public String getName(){
        return this.forum_name;
    }

    @Override
    public Map<String, SubForumI> getSubForums(){ return
            _subForums;
    }


    @Override
    public SubForumI addSubForum(String subForumName) throws SubForumAlreadyExistException {
        if (_subForums.containsKey(subForumName)){
            throw new SubForumAlreadyExistException(subForumName,this);
        }
        SubForumI subForum = new SubForum(subForumName, this.policy.getSubforumPolicy());
        _subForums.put(subForumName, subForum);

        for (UserI user: _users.values()) {
            if (user != null ) {
                user.addSubForumPermission(subForumName);
            }
        }
        Update();
       return subForum;
    }

    @Override
    public void deleteSubForum(String subforum) throws SubForumDoesNotExistException {
        if (!_subForums.containsKey(subforum)){
            throw new SubForumDoesNotExistException();
        }
        _subForums.remove(subforum);
        Update();
    }

    private void addAllSubforumsToUser(UserI user){
        for (SubForumI sub: _subForums.values()){
            user.addSubForumPermission(sub.getTitle());
        }
    }

    @Override
    public UserI register(String userName, String password, String eMail) throws UserAlreadyExistsException, InvalidUserCredentialsException, DoesNotComplyWithPolicyException {
        // Protective Programing
        if (userName == null || userName.equals("") || password == null || password.equals("") || eMail == null || eMail.equals(""))
            throw new InvalidUserCredentialsException("invalid user credentials.");
        if (User.getUserFromDB(userName, forum_name) != null){
            throw new UserAlreadyExistsException(userName);
        }
        if (!policy.isValidPassword(password)){
            throw new DoesNotComplyWithPolicyException("password does not comply with forum policy.");
        }
        if (!policy.isValidEmailAddress(eMail)){
            throw new InvalidUserCredentialsException("e-mail does not comply with forum policy.");
        }

        ForumPermissionI userPermissions = UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_USER, forum_name);
        UserI newUser = new User(userName, password, eMail, userPermissions);
        addAllSubforumsToUser(newUser);
        sendAuthenticationEMail(newUser);
        _users.put(userName, newUser);
        Update();
        return newUser;
    }

    @Override
    public void sendAuthenticationEMail(UserI user) {
        String topic = "Authentication Email For: " + user.getUsername();
        String body = "Hello, " + user.getUsername() + " \n This is your authentication token : \n";
        body += user.getUserAuthString();
        GmailSender.sendFromGMail(new String[]{user.getEmail()}, topic, body);
    }

    public boolean enterUserAuthenticationString(UserI user, String auth_string) throws InvalidUserCredentialsException {
            if (user.getUserAuthString().equals(auth_string)){
                user.setAuthenticated();
                _users.replace(user.getUsername(), user);
                Update();
                return true;
            } else {
                throw new InvalidUserCredentialsException("Invalid authentication string");
            }
    }

    @Override
    public UserI getGuest() {
        return guest;
    }

    @Override
    public UserI login(String username, String password) throws InvalidUserCredentialsException, NeedMoreAuthParametersException, EmailNotAuthanticatedException, PasswordNotInEffectException {
        UserI user = User.getUserFromDB(username, forum_name);
        if (user == null){
            throw new InvalidUserCredentialsException("User is not registered");
        }
        if (user.getPassword().equals(password)){
            if (policy.hasMoreAuthQuestions()){
                throw new NeedMoreAuthParametersException();
            }
            if (!user.isEmailAuthenticated()){
                throw new EmailNotAuthanticatedException();
            }
            if (!policy.isPasswordInEffect(user.getPasswordCreationDate())){
                throw new PasswordNotInEffectException();
            }
            return user;
        }
        else {
            throw new InvalidUserCredentialsException("invalid username or password");
        }
    }

    @Override
    public UserI guestLogin() {
        return guest; // guest was initialized on start
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
        //TODO - yet not implemented.
        return false;
    }


//    public void Save(){    // save the forum to the database
//        pers.save(this);
//    }

    public static Forum load(String forum_name){
        return (Forum)pers.load(Forum.class, forum_name);
    }

    public static void delete(String forum_name) throws ForumNotFoundException {

        Forum forum = load(forum_name);
        if(forum == null) throw new ForumNotFoundException("Forum not found");
        if (forum.policy != null) {
            forum.policy = null;
            forum.Update();
        }
        pers.Delete(forum);

    }

    public static ArrayList<String> getForumList(){
        String sql = "SELECT forum_name from forum";
        return (ArrayList<String>)pers.executeQuery(sql);
    }
  //  public void saveOrUpdate(){    // save the forum to the database
  //      pers.saveOrUpdate(this);
   // }

    //public void Update(){    // save the forum to the database
    //    pers.Update(this);
   // }

}
