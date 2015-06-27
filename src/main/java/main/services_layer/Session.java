package main.services_layer;

import main.User.User;
import main.exceptions.ThreadNotFoundException;
import main.forum_contents.Forum;
import main.interfaces.ForumI;
import main.interfaces.SubForumI;
import main.interfaces.ThreadI;
import main.interfaces.UserI;

import java.util.Vector;

/**
 * Created by gabigiladov on 5/31/15.
 */
public class Session {
    private int sessionId;
    private String user;
    private String forum;
    private String subForum;
    private String thread;
    private Vector<String> history;


    public Session(int sessionId, String user) {
        this.sessionId = sessionId;
        this.user = user;
        this.thread = null;
        this.subForum = null;
        this.forum = null;
        history = new Vector<>();
    }

    public int getId(){
        return sessionId;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public ThreadI getThread() throws ThreadNotFoundException {
        if (thread == null){
            throw new NullPointerException("thread not found in session");
        }
        Forum f = Forum.load(forum);
        if(f == null) throw new ThreadNotFoundException();
        
        return findThread(f, subForum, thread);
    }

    private ThreadI findThread(Forum f, String subForum, String thread) {
        ThreadI result = null;
        SubForumI sub = f.getSubForums().get(subForum);
        result = sub.getThreads().get(thread);
        return result;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public ForumI getForum() {
        if (forum == null){
            throw new NullPointerException("forum " + forum + " not found in session.");
        }
        return Forum.load(forum);
    }

    public void setSubForum(String subforum) {
        this.subForum = subforum;
    }

    public SubForumI getSubForum() {
        if (subForum == null){
            throw new NullPointerException("subforum not found in session");
        }
        return Forum.load(forum).getSubForums().get(subForum);
    }

    public UserI getUser() {
        return User.getUserFromDB(user, forum);
    }

    public void addCommand(String command){
        history.add(command);
    }

    public Vector<String> getHistory(){
        return history;
    }
}
