package main.services_layer;

import data_structures.Tree;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumPolicy;
import main.forum_contents.ForumThread;
import main.interfaces.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by hagai_lvi on 4/11/15.
 */
     public class Facade implements FacadeI {
	private static final String ADMIN_USERNAME = "ADMIN";
	private static final String ADMIN_PASSWORD = "ADMIN";
	private static FacadeI theFacade;
	private ArrayList<UserI> users;
	private Collection<Session> openSessions;
	private ArrayList<ForumI> forums;
	private int sessionCounter;
	private static final int GUEST_SESSION_ID = -2;
	private Facade(){
		initilize();
	}


	@Override
	public void initilize() {
		//TODO
		openSessions =  new ArrayList<Session>();
		forums = new ArrayList<ForumI>();
		users = new ArrayList<UserI>();
		sessionCounter = 0;
	}

	@Override
	public ArrayList<ForumI> getForumList() {
		return forums;
	}

	@Override
	public Collection<SubForumI> getSubForumList(int sessionId) throws SessionNotFoundException {
		return findSession(sessionId).getForum().getSubForums();
	}

	@Override
	public void addForum(String username, String password, boolean isSecured, String forumName, String regex, int numberOfModerators, int passLife) throws PermissionDeniedException, ForumAlreadyExistException {
		if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)){
			for (ForumI forum : forums){
				if (forum.getName().equals(forumName)){
					throw new ForumAlreadyExistException(forumName);
				}
			}
			forums.add(new Forum(forumName, new ForumPolicy(isSecured, numberOfModerators, regex, passLife)));
		}
		else{
			throw new PermissionDeniedException(username + "is not an admin");
		}

	}

	@Override
	public void createSubforum(int sessionId, String subforumName) throws PermissionDeniedException, SubForumAlreadyExistException, SessionNotFoundException {
		Session current = findSession(sessionId);
		current.setSubForum(current.getForum().createSubForum(subforumName));
	}

	@Override
	public void register(String forumName, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException {
		ForumI current = findForum(forumName);
		if (current == null) return;
		UserI currentUser = current.register(userName, password, email);
		users.add(currentUser);
	}


	@Override
	public int login(String forumName, String userName, String password) throws InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException, ForumNotFoundException {
		ForumI current = findForum(forumName);
		UserI currentUser = current.login(userName, password);
		Session currentSession = new Session(sessionCounter, currentUser);
		currentSession.setForum(current);
		openSessions.add(currentSession);
		sessionCounter++;
		return sessionCounter-1;
	}

	@Override
	public void logout(int sessionId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		openSessions.remove(current);
	}

	@Override
	public void addReply(int sessionId, int srcMessageId, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException {
		Session session = findSession(sessionId);
		assert session != null;
		MessageI msg = session.getThread().getMessages().find(srcMessageId);
		UserI user = session.getUser();
		MessageI newMsg = new ForumMessage(user, title, body);
		//*TODO - There are 3 different ways to reply to a message, which to use?
		//A. session.getSubForum().replyToMessage(msg, newMsg);	 	- bypasses permissions
		//B. session.getThread().addReply(newMsg, msg);				- bypasses permissions and policy
		//C. session.getUser().replyToMessage(null, msg, newMsg); 	- how to get permissions?
	}

	@Override
	public int createNewThread(int sessionId, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		ForumMessage msg = new ForumMessage(current.getUser(), srcMessageTitle, srcMessageBody);
		ThreadI thread = current.getSubForum().createThread(msg);
		current.setThread(thread);
		return msg.getId();
	}

	@Override
	public void reportModerator(int sessionId, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException, SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		current.getSubForum().reportModerator(moderatorUserName, reportMessage, current.getUser());
	}

	@Override
	public String getUserAuthString(String forumName, String username, String password) throws InvalidUserCredentialsException, UserNotFoundException {
		UserI current = findUser(username);
		return current.getUserAuthString();
	}

	@Override
	public void deleteMessage(int sessionId, int messageId) throws PermissionDeniedException, MessageNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		MessageI msg = current.getThread().getMessages().find(messageId);
		UserI user = current.getUser();
		current.getThread().remove(msg);
		current.getSubForum().deleteMessage(msg, user.getUsername());
	}

	@Override
	public void setModerator(int sessionId, String moderatorName) throws PermissionDeniedException, UserNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		current.getSubForum().setModerator(findUser(moderatorName));
	}

	@Override
	public int guestEntry(String forumName) throws ForumNotFoundException {
		ForumI current = findForum(forumName);
		Session currentSession = new Session(sessionCounter, null);
		currentSession.setForum(current);
		openSessions.add(currentSession);
		return GUEST_SESSION_ID;
	}

	@Override
	public void addUserType(int sessionId, String typeName, int seniority, int numOfMessages, int connectionTime) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		current.getForum().addUserType(typeName, seniority, numOfMessages, connectionTime);
	}

	@Override
	public void removeForum(String username, String password, String forumName) throws ForumNotFoundException, PermissionDeniedException{
		if (username == ADMIN_USERNAME && password == ADMIN_PASSWORD) {
			forums.remove(findForum(forumName));
		}else{
			throw new PermissionDeniedException("Unauthorized removal of a forum");
		}
	}

	@Override
	public void setPolicies(int sessionId, boolean isSecured, String regex, int numOfModerators, int passLife) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		current.getForum().setPolicy(new ForumPolicy(isSecured, numOfModerators, regex, passLife));
	}

	@Override
	public void editMessage(int sessionId, int messageId, String title, String text) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		MessageI msg = current.getThread().getMessages().find(messageId);
		UserI user = current.getUser();
		msg.editText(text);
		msg.editTitle(title);
		//*TODO - Where should permissions be checked?
	}

	@Override
	public void removeModerator(int sessionId, String moderatorName) throws UserNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		current.getSubForum().removeModerator(findUser(moderatorName));
	}

	@Override
	public String viewModeratorStatistics(int sessionsId) throws SessionNotFoundException {
		Session current = findSession(sessionsId);
		assert current != null;
		return current.getForum().viewStatistics();
	}

	@Override
	public String viewSuperManagerStatistics(int sessionId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		return  null;
		//TODO - not implemented.
	}

	@Override
	public String viewSessions(int sessionId) {
		return null;
		//TODO - not implemented.
	}

	@Override
	public ExMessageI getMessage(int sessionId, int messageId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		ThreadI thread = current.getThread();
		assert thread != null;
		Tree messages = thread.getMessages();
		assert messages != null;
		ExMessageI message = messages.find( messageId);
		return message;
	}

	@Override
	public Collection<ThreadI> getThreadsList(int sessionId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		return current.getSubForum().getThreads();

	}

	@Override
	public Tree getMessageList(int sessionId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		return current.getThread().getMessages();
	}

	@Override
	public String getCurrentForumName(int sessionID) throws SessionNotFoundException {
		Session current = findSession(sessionID);
		assert current != null;
		return current.getForum().getName();
	}

	@Override
	public String getCurrentUserName(int sessionID) throws SessionNotFoundException {
		Session current = findSession(sessionID);
		assert current != null;
		return current.getUser().getUsername();
	}

	@Override
	public boolean isAdmin(int sessionID) throws SessionNotFoundException {
		Session current = findSession(sessionID);
		assert current != null;
		return current.getUser().isAdmin();
	}

	@Override
	public ExSubForumI viewSubforum(int sessionId, String subforum) throws SubForumAlreadyExistException, SubForumNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		Collection<SubForumI> subForums = current.getForum().getSubForums();
		for (SubForumI s: subForums){
			if (s.getTitle().equals(subforum)){
				current.setSubForum(s);
				return s;
			}
		}
		throw new SubForumNotFoundException();
	}

	@Override
	public ExSubForumI viewSubforum(int sessionId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		return current.getSubForum();
	}

	@Override
	public ExThreadI viewThread(int sessionId, String title) throws DoesNotComplyWithPolicyException, ThreadNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionId);
		assert current != null;
		Collection<ThreadI> threads = current.getSubForum().getThreads();
		for (ThreadI t: threads){
			if (t.getTitle().equals(title)){
				current.setThread(t);
				return t;
			}
		}
		throw new ThreadNotFoundException();
	}

	@Override
	public ThreadI getCurrentThread(int sessionID) throws ThreadNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionID);
		assert current != null;
		ThreadI thread = current.getThread();
		if (thread == null){
			throw new ThreadNotFoundException();
		}
		return thread;
	}

	@Override
	public void authanticateUser(String forum, String username, String userAuthString) throws EmailNotAuthanticatedException, UserNotFoundException {
		UserI user = findUser(username);
		if (!user.getUserAuthString().equals(userAuthString)){
			throw new EmailNotAuthanticatedException();
		}
		user.setAuthenticated();
	}


	public static FacadeI getFacade(){
		if (theFacade == null) {
			theFacade = new Facade();
		}
		return theFacade;
	}

	public static FacadeI dropAllData(){
		theFacade = new Facade();
		return theFacade;
	}

	private Session findSession(int sessionId) throws SessionNotFoundException {
		for (Session s: openSessions){
			if (s.getId() == sessionId){
				return s;
			}
		}
		throw new SessionNotFoundException();
	}

	private ForumI findForum(String forumName) throws ForumNotFoundException {
		for (ForumI f: forums){
			if (f.getName().equals(forumName)){
				return f;
			}
		}
		throw new ForumNotFoundException();
	}

	private UserI findUser(String name) throws UserNotFoundException {
		for (UserI user: users){
			if (user.getUsername().equals(name)){
				return user;
			}
		}
		throw new UserNotFoundException(name);
	}

}
