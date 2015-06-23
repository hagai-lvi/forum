package main.services_layer;

import data_structures.Tree;
import main.User.User;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumPolicy;
import main.interfaces.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hagai_lvi on 4/11/15.
 */
     public class Facade implements FacadeI {
	private static final String SUPER_ADMIN_USERNAME = "ADMIN";
	private static final String SUPER_ADMIN_PASSWORD = "ADMIN";
	private static FacadeI theFacade;
	private Collection<Session> openSessions;
	private int sessionCounter;
	private static final int GUEST_SESSION_ID = -2;
	private Facade(){
		initialize();
	}


	@Override
	public void initialize() {
		//TODO - how to initialize?
		openSessions = new ArrayList<>();
		sessionCounter = 0;
	}

	@Override
	public ArrayList<String> getForumList() {
		return Forum.getForumList();
	}

	@Override
	public Collection<SubForumI> getSubForumList(int sessionId) throws SessionNotFoundException {
		return findSession(sessionId).getForum().getSubForums();
	}

	public Collection<Session> getSessions() {
		return this.openSessions;
	}
	@Override
	public void addForum(String username, String password, String forumName, boolean isSecured, String regex, int numberOfModerators, int passLife) throws PermissionDeniedException, ForumAlreadyExistException {
		if (username.equals(SUPER_ADMIN_USERNAME) && password.equals(SUPER_ADMIN_PASSWORD)){
			ForumI forum = findForum(forumName);
			if(forum != null) throw new ForumAlreadyExistException("Forum already exist");
			ForumPolicyI policy = new ForumPolicy(isSecured, numberOfModerators, regex, passLife);
			forum = new Forum(forumName, policy);
		}
		else {
			throw new PermissionDeniedException(MessageFormat.format("user {0} is not authorized to add a forum.", username));
		}

	}

	@Override
	public void addSubforum(int sessionId, String subforumName) throws PermissionDeniedException, SubForumAlreadyExistException, SessionNotFoundException, ForumNotFoundException, SubForumDoesNotExistException {
		Session currentSession = findSession(sessionId);
		SubForumI subforum = currentSession.getUser().createSubForum(subforumName);
		currentSession.setSubForum(subforum);
	}

	@Override
	public void register(String forumName, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, DoesNotComplyWithPolicyException {
		ForumI currentForum = findForum(forumName);
		if(currentForum == null) throw new ForumNotFoundException("Forum not found");
		currentForum.register(userName, password, email);
	}


	@Override
	public int login(String forumName, String userName, String password) throws InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException, ForumNotFoundException {
		ForumI currentForum = findForum(forumName);
		if(currentForum == null) throw new ForumNotFoundException("Forum not found");
		UserI currentUser = currentForum.login(userName, password);
		Session currentSession = new Session(sessionCounter, currentUser); //create a new session
		currentSession.setForum(currentForum);
		openSessions.add(currentSession);
		sessionCounter++;
		return sessionCounter - 1;
	}

	@Override
	public void logout(int sessionId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		openSessions.remove(current);
	}

	@Override
	public void addReply(int sessionId, int srcMessageId, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException, SubForumDoesNotExistException {
		Session currentSession = findSession(sessionId);
		currentSession.getUser().replyToMessage(currentSession.getSubForum().getTitle(), currentSession.getThread().getMessages().find(srcMessageId), title, body);

	}

	@Override
	public void addThread(int sessionId, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException, SubForumDoesNotExistException {
		Session currentSession = findSession(sessionId);
		currentSession.getUser().createThread(new ForumMessage(currentSession.getUser(), srcMessageTitle, srcMessageBody), currentSession.getSubForum().getTitle());
	}

	@Override
	public void reportModerator(int sessionId, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException, SessionNotFoundException {
		Session current = findSession(sessionId);
		current.getSubForum().reportModerator(moderatorUserName, reportMessage, current.getUser());
	}

	@Override
	public String getUserAuthString(String forumName, String username, String password) throws InvalidUserCredentialsException, UserNotFoundException {
		UserI current = findUser(username, forumName);
		return current.getUserAuthString();
	}

	private UserI findUser(String username, String forumName) {
		return User.getUserFromDB(username, forumName);
	}


	@Override
	public void deleteMessage(int sessionId, int messageId) throws PermissionDeniedException, MessageNotFoundException, SessionNotFoundException, SubForumDoesNotExistException {
		Session current = findSession(sessionId);
		current.getUser().deleteMessage(current.getThread().getMessages().find(messageId), current.getSubForum().getTitle());
	}

	@Override
	public void setModerator(int sessionId, String moderatorName) throws PermissionDeniedException, UserNotFoundException, SessionNotFoundException, SubForumNotFoundException {
		Session current = findSession(sessionId);
		current.getUser().setModerator(current.getSubForum(), findUser(moderatorName, current.getForum().getName()));
	}

	@Override
	public int guestEntry(String forumName) throws ForumNotFoundException {
		ForumI current = findForum(forumName);
		if(current == null) throw new ForumNotFoundException("Forum not found");
		Session currentSession = new Session(sessionCounter, null);
		currentSession.setForum(current);
		openSessions.add(currentSession);
		return GUEST_SESSION_ID;
	}

	@Override
	public void addUserType(int sessionId, String typeName, int seniority, int numOfMessages, int connectionTime) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		current.getForum().addUserType(typeName, seniority, numOfMessages, connectionTime);
	}

	@Override
	public void removeForum(String username, String password, String forumName) throws ForumNotFoundException, PermissionDeniedException{
		if (username.equals(SUPER_ADMIN_USERNAME) && password.equals(SUPER_ADMIN_PASSWORD)) {
			Forum.delete(forumName);
		}else{
			throw new PermissionDeniedException("Unauthorized removal of a forum");
		}
	}

	@Override
	public void setPolicies(int sessionId, boolean isSecured, String regex, int numOfModerators, int passLife) throws SessionNotFoundException, PermissionDeniedException, ForumNotFoundException {
		Session current = findSession(sessionId);
		current.getUser().setPolicy(new ForumPolicy(isSecured, numOfModerators, regex, passLife));
	}

	@Override
	public void editMessage(int sessionId, int messageId, String title, String text) throws SessionNotFoundException, MessageNotFoundException, SubForumDoesNotExistException {
		Session current = findSession(sessionId);
		current.getUser().editMessage(current.getSubForum().getTitle(), current.getThread(), messageId, title, text);
	}

	@Override
	public void removeModerator(int sessionId, String moderatorName) throws UserNotFoundException, SessionNotFoundException, SubForumDoesNotExistException {
		Session current = findSession(sessionId);
		current.getUser().removeModerator(current.getSubForum().getTitle(), moderatorName);
	}

	@Override
	public String viewModeratorStatistics(int sessionsId) throws SessionNotFoundException {
		Session current = findSession(sessionsId);
		return current.getForum().viewStatistics();
	}

	@Override
	public String viewSuperManagerStatistics(int sessionId) throws SessionNotFoundException {
		return  null;
		//TODO - not implemented.
	}

	@Override
	public String viewSessions(int sessionId) {
		StringBuilder res = new StringBuilder();
		for (Session s : openSessions){
			res.append("Session ");
			res.append(s.getId());
			res.append(" [USER: ");
			res.append(s.getUser().getUsername());
			res.append("] [FORUM: ");
			res.append(s.getForum().getName());
			res.append("] [SUB-FORUM: ");
			res.append(s.getSubForum().getTitle());
			res.append("] [THREAD: ");
			res.append(s.getThread().getTitle());
			res.append("]\n");
		}
		return res.toString();
	}

	@Override
	public ExMessageI getMessage(int sessionId, int messageId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		ThreadI thread = current.getThread();
		Tree messages = thread.getMessages();
		return messages.find(messageId);
	}

	@Override
	public Collection<ThreadI> getThreadsList(int sessionId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		return current.getSubForum().getThreads();

	}

	@Override
	public Tree getMessageList(int sessionId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		return current.getThread().getMessages();
	}

	@Override
	public String getCurrentForumName(int sessionID) throws SessionNotFoundException {
		Session current = findSession(sessionID);
		return current.getForum().getName();
	}

	@Override
	public String getCurrentUserName(int sessionID) throws SessionNotFoundException {
		Session current = findSession(sessionID);
		return current.getUser().getUsername();
	}

	@Override
	public boolean isAdmin(int sessionID) throws SessionNotFoundException {
		Session current = findSession(sessionID);
		return current.getUser().isAdmin();
	}

	@Override
	public ExSubForumI viewSubforum(int sessionId, String subforum) throws SubForumAlreadyExistException, SubForumNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionId);
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
		return current.getSubForum();
	}

	@Override
	public ExThreadI viewThread(int sessionId, String title) throws DoesNotComplyWithPolicyException, ThreadNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionId);
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
		ThreadI thread = current.getThread();
		if (thread == null){
			throw new ThreadNotFoundException();
		}
		return thread;
	}

	@Override
	public void authenticateUser(String forum, String username, String userAuthString) throws EmailNotAuthanticatedException, UserNotFoundException {
		UserI user = findUser(username, forum);
		if(user == null) throw new UserNotFoundException("User not found");
		if (!user.getUserAuthString().equals(userAuthString)){
			throw new EmailNotAuthanticatedException();
		}
		user.setAuthenticated();
	}

	@Override
	public boolean isMessageFromCurrentUser(int sessionId, int messageId) throws SessionNotFoundException {
		Session currentSession = findSession(sessionId);
		UserI user = currentSession.getUser();
		MessageI message = currentSession.getThread().getMessages().find(messageId);
		return user.isOwnerOfMessage(message);
	}

	//TODO current.getSubForum might be null, for example, before the user entered to a specific subforum
	@Override
	public String getCurrentUserStatus(int sessionId) throws SessionNotFoundException, SubForumDoesNotExistException {
		Session current = findSession(sessionId);
		return current.getUser().getStatus(current.getSubForum().getTitle());
	}

	@Override
	public void setAdmin(String username, String password, String newAdmin, String forumname) throws UserNotFoundException, PermissionDeniedException, ForumNotFoundException {
		User admin = User.getUserFromDB(username, forumname);
		if(admin == null) throw new UserNotFoundException("User not found");
		User user = User.getUserFromDB(newAdmin, forumname);
		if(user == null) throw new UserNotFoundException("User not found");
		admin.setAdmin(user);
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

	private ForumI findForum(String forumName) {
		return Forum.load(forumName);
	}

	//TODO - get permission of current user.
}
