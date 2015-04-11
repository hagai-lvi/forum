package main.User;

import main.interfaces.SubForumPermissionI;
import main.interfaces.UserI;

import java.util.Collection;

/**
 * Created by gabigiladov on 4/11/15.
 */
public class User implements UserI {

    private String username;
    private String password;
    private Collection<SubForumPermissionI> subForumsPermissions;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return whether this user has authenticated his email address
     */
    public boolean isEmailAuthnticated() {
        return false;
    }

    /**
    ﬂ * Get the list of all of the subforums of this user
     */
    public Collection<SubForumPermissionI> getSubForumPermission() {
        return null;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
