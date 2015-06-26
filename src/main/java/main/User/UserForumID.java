package main.User;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by xkcd on 6/17/2015.
 */

@Embeddable
public class UserForumID implements Serializable{
    protected String username_id;
    protected String forumname_id;

    public UserForumID() {}

    public UserForumID(String uname, String forumname) {
        this.username_id = uname;
        this.forumname_id = forumname;
    }
    // equals, hashCode
}
