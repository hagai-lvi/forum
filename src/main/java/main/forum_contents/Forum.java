package main.forum_contents;

import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import main.Persistancy.PersistantObject;
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
public class Forum extends PersistantObject implements ForumI{

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
    //private static HibernatePersistancyAbstractor pers;
    private static final Logger logger = Logger.getLogger(Forum.class.getName());



    public Forum(String name, ForumPolicyI policy){
        this.policy = policy;
        this.forum_name = name;
        initGuest();
        initAdmin();//TODO should be initialized?
        addAllSubforumsToUser(guest, Permissions.PERMISSIONS_GUEST);
        addAllSubforumsToUser(admin, Permissions.PERMISSIONS_ADMIN);
        this._users.put("Guest", this.guest);
        this._users.put(this.admin.getUsername(), this.admin);
        //this.pers = HibernatePersistancyAbstractor.getPersistanceAbstractor();
        this.Save();
    }

    public Forum() {  // needed here for hibernate
    }

    private void initAdmin() {
        ForumPermissionI adminPermission =
                UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_ADMIN, this);
        this.admin = new User(ADMIN_USERNAME, ADMIN_PASSWORD, "forumadmin@nomail.com", adminPermission);
        this.admin.setAuthenticated();
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
    public SubForumI createSubForum(String subForumName) throws SubForumAlreadyExistException {
        if (_subForums.containsKey(subForumName)){
            throw new SubForumAlreadyExistException(subForumName,this);
        }
        SubForumI subForum = new SubForum(subForumName, this.policy.getSubforumPolicy());
        _subForums.put(subForumName, subForum);
        for (UserI user: _users.values()){
            UserSubforumPermission permission;
            if (user.isAdmin()) {
                permission = new UserSubforumPermission(Permissions.PERMISSIONS_MODERATOR, this, subForum);
            } else{
                permission = new UserSubforumPermission(Permissions.PERMISSIONS_USER, this, subForum);
            }
            user.addSubForumPermission(permission);
        }
        Update();
        return subForum;
    }

    @Override
    public void deleteSubForum(SubForumI subforum) throws SubForumDoesNotExsitsException {
        if (!_subForums.containsKey(subforum.getTitle())){
            throw new SubForumDoesNotExsitsException();
        }
        _subForums.remove(subforum.getTitle());
        Update();
    }

    private void addAllSubforumsToUser(UserI user, Permissions perm){
        for (SubForumI sub: _subForums.values()){
            user.addSubForumPermission(new UserSubforumPermission(perm, this, sub));
        }
    }

    @Override
    public User register(String userName, String password, String eMail) throws UserAlreadyExistsException, InvalidUserCredentialsException, DoesNotComplyWithPolicyException {
        // Protective Programing
        if (userName == null || userName.equals("") || password == null || password.equals("") || eMail == null || eMail.equals(""))
            throw new InvalidUserCredentialsException();
        if (_users.containsKey(userName)){
            throw new UserAlreadyExistsException(userName);
        }
        if (!policy.isValidPassword(password)){
            throw new DoesNotComplyWithPolicyException("password does not comply with forum policy.");
        }
        if (!policy.isValidEmailAddress(eMail)){
            throw new DoesNotComplyWithPolicyException("e-mail does not comply with forum policy.");
        }

        ForumPermissionI userPermissions = UserForumPermission.createUserForumPermissions(Permissions.PERMISSIONS_USER, this);
        User newUser = new User(userName, password, eMail, userPermissions);
        addAllSubforumsToUser(newUser, Permissions.PERMISSIONS_USER);
        //sendAuthenticationEMail(new_user);    //TODO --> uncomment to actually send authentication mails!
        _users.put(userName, newUser);
        Update();
        return newUser;
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
    public UserI login(String username, String password) throws InvalidUserCredentialsException, NeedMoreAuthParametersException, EmailNotAuthanticatedException, PasswordNotInEffectException {
        if (_users.containsKey(username) &&
                 _users.get(username).getPassword().equals(password)){
            UserI user = _users.get(username);
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


//    public void Save(){    // save the forum to the database
//        pers.save(this);
//    }

    public static Forum load(String forum_name){
        return (Forum)pers.load(Forum.class, forum_name);
    }

//    public void saveOrUpdate(){    // save the forum to the database
//        pers.saveOrUpdate(this);
//    }

//    public void Update(){    // save the forum to the database
//        pers.Update(this);
//    }

}
