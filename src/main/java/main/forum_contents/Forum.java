package main.forum_contents;

import main.User.User;
import main.Utils.GmailSender;
import main.exceptions.InvalidUserCredentialsException;
import main.exceptions.SubForumAlreadyExistException;
import main.exceptions.UserAlreadyExistsException;
import main.interfaces.ForumI;
import main.interfaces.ForumPolicyI;
import main.interfaces.SubForumI;
import main.interfaces.UserI;
import org.apache.log4j.Logger;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by hagai on 07/04/15.
 */
public class Forum implements ForumI {

    private ForumPolicyI policy;
    private HashMap<String, SubForumI> _subForums = new HashMap<>();
    private HashMap<String, UserI> _users = new HashMap<>();
    private User guest = null;
    private static Logger logger = Logger.getLogger(Forum.class.getName());

    public Forum(ForumPolicyI policy) {
        this.policy = policy;
        this.guest = new User("Guest user", "no_pass", "nomail@nomail.com");
        this.guest.addForum(this);
    }


    @Override
    public SubForumI createSubForum(String name) throws SubForumAlreadyExistException {
        if (_subForums.containsKey(name)){
            throw new SubForumAlreadyExistException(name,this);
        }

        SubForumI subForum = new SubForum(name);
        _subForums.put(name,subForum);
        return subForum;
    }

    @Override
    public User register(String userName, String password, String eMail) throws UserAlreadyExistsException, InvalidUserCredentialsException {
        // Protective Programing
        if (userName.equals("") || userName == null || password.equals("") || password == null || eMail.equals("") || eMail == null)
            throw new InvalidUserCredentialsException();
        if (_users.containsKey(userName)){
            throw new UserAlreadyExistsException(userName);
        }
        // we are done with protective programing, time to do work.
        User new_user = new User(userName, password, eMail);
        new_user.addForum(this);  // Gabi said this will be the logic
        sendAuthenticationEMail(new_user);
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
            System.out.println("Had error "+e.toString());
        }
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
    public String[] getUserTypes() {
        return new String[0];
    }

    @Override
    public void addUserType(String type) {

    }

    @Override
    public boolean removeUserType(String type) {
        return false;
    }
}
