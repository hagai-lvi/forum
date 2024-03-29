package controller;

//import data_structures.TreeNode;

import main.exceptions.*;
import main.interfaces.ExSubForumI;
import main.interfaces.ExThreadI;
import main.interfaces.FacadeI;
import main.interfaces.ThreadI;
import main.services_layer.Facade;
import main.services_layer.Session;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hagai_lvi on 4/18/15.
 */
@Controller
public class WebController {
	private Logger logger = Logger.getLogger(this.getClass());

	public static final String SESSION_ID_ATTR = "session_id";


	public static final String ADMIN_USER = "ADMIN";// TODO remove
	public static final String ADMIN_PASS = "ADMIN";// TODO remove

	@RequestMapping(value = "report_moderator", method = RequestMethod.POST)
	public void reportModerator(HttpSession session, HttpServletResponse response, String moderatorUserName, String reportMessage) throws Exception{
		FacadeI facade = getFacade();
		try {
			facade.reportModerator(getSessionID(session), moderatorUserName, reportMessage);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			logger.warn(e);
			throw e;
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@RequestMapping(value = "view_session_detailes", method = RequestMethod.GET)
	public String showSessionDetailes(ModelMap model, Integer sessionID){
		Collection<Session> sessions = getFacade().getSessions();
		for (Session s: sessions){
			if (s.getId() == sessionID) {
				model.addAttribute("sessionEntries", s.getHistory());
				return "view_session_detailes";
			}
		}
		return "view_session_detailes";
	}

	@RequestMapping(value = "superAdminDashboard")
	public String showSuperAdminDashboard(ModelMap model) throws PermissionDeniedException, SubForumDoesNotExistException {
		Collection<Session> sessions = getFacade().getSessions();
		model.addAttribute("sessions", sessions);
		model.addAttribute("messages", getFacade().getMessagesToSuperAdmin());
		model.addAttribute("statistics",getFacade().viewSuperManagerStatistics("ADMIN", "ADMIN"));
		ArrayList<String> forumList = getFacade().getForumList();
		model.addAttribute("forumList", forumList);
		return "superAdminDashboard";
	}

	@RequestMapping(value = "deleteForum")
	public String removeForum(String username, String password, String forumName) throws ForumNotFoundException, PermissionDeniedException {
		getFacade().removeForum(username,password, forumName);
		return "redirect:/superAdminDashboard";
	}

	/**
	 * Shows a facade with all the available forums in the system
	 */
	@RequestMapping(value = "facade",method = RequestMethod.GET)
	public String showFacade(ModelMap model) {
		logger.info("showFacade request");
		FacadeI f = getFacade();
		model.addAttribute("forumList", f.getForumList());
		return "facade";
	}
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws SessionNotFoundException {
		Facade.getFacade().logout(getSessionID(session));
		session.removeAttribute(SESSION_ID_ATTR);
		return "redirect:/facade";
	}

	/**
	 * Send a request to create a new forum in the system
	 */
	@RequestMapping(value = "addForum",method = RequestMethod.POST)
	public String addForum(ModelMap model, String username, String password, String forumName, int numOfModerators, String passRegex,
						   boolean isSecured, int passwordEffectTime) throws PermissionDeniedException, ForumAlreadyExistException, ForumNotFoundException {
		logger.info("addForum request");
		FacadeI f = getFacade();
		f.addForum(username, password, forumName, isSecured, passRegex, numOfModerators, passwordEffectTime);
		model.addAttribute("forumName", forumName);
		return "addForum";
	}


	/**
	 * Send a request to create a new sub forum in the currently used forum
	 */
	@RequestMapping(value = "addSubforum",method = RequestMethod.POST)
	public String addSubforum(ModelMap model, HttpSession session, String subforumName) throws SubForumAlreadyExistException, PermissionDeniedException, SessionNotFoundException, ForumNotFoundException, SubForumDoesNotExistException {
		logger.info("addSubforum request");
		FacadeI f = getFacade();
		Integer sessionId = (Integer) session.getAttribute(SESSION_ID_ATTR);
		f.addSubforum(sessionId, subforumName);
		model.addAttribute("subforumName", subforumName);
		return "addSubforum";
	}


	/**
	 * Shows a login/register page
	 */
	@RequestMapping(value = "login_page",method = RequestMethod.POST)
	public String loginForm(ModelMap model, String forum) {
		logger.info("loginForm request");
		model.addAttribute("forumName", forum);
		return "login_page";
	}

	@RequestMapping(value = "guest_forum_homepage", method = RequestMethod.POST)
	public String guestLogin(ModelMap model, HttpSession session, String forumName) throws ForumNotFoundException, SessionNotFoundException {
		logger.info("loginToForum request");
		FacadeI facade = getFacade();
		Integer sessionID = facade.guestEntry(forumName);
		session.setAttribute(SESSION_ID_ATTR, sessionID);
		preperaForumHomepageModel(model, facade, session, "Guest");
		return "forum_homepage";
	}

	/**
	 * redirects to the current forum home page after a login
	 */
	@RequestMapping(value = "forum_homepage",method = RequestMethod.POST)
	public String loginToForum(ModelMap model, HttpSession session, String username, String password, String forumName)
			throws InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException, ForumNotFoundException, SessionNotFoundException, SubForumDoesNotExistException, PermissionDeniedException {
		logger.info("loginToForum request");
		FacadeI facade = getFacade();
		Integer sessionID = facade.login(forumName, username, password);
		session.setAttribute(SESSION_ID_ATTR, sessionID);
		String status = facade.getCurrentUserForumStatus(sessionID);
		preperaForumHomepageModel(model, facade, session, status);
		if (getFacade().isAdmin(getSessionID(session))){
			model.addAttribute("statistics",getFacade().viewModeratorStatistics(getSessionID(session)));
		}
		return "forum_homepage";
	}

	@RequestMapping(value = "edit_message", method = RequestMethod.POST)
	public String editMessage(HttpSession session, int messageID,String newTitle, String newBody) throws SessionNotFoundException, SubForumDoesNotExistException, MessageNotFoundException, ThreadNotFoundException, PermissionDeniedException {
		FacadeI facade = getFacade();
		facade.editMessage(getSessionID(session), messageID, newTitle, newBody);
		return "redirect:/thread_view";
	}

	@RequestMapping(value = "delete_message", method = RequestMethod.POST)
	public String deleteMessage(HttpSession session, int messageID) throws PermissionDeniedException, SessionNotFoundException, SubForumDoesNotExistException, MessageNotFoundException, ThreadNotFoundException {
		FacadeI facade = getFacade();
		facade.deleteMessage(getSessionID(session), messageID);
		return "redirect:/thread_view";
	}

	/**
	 * Redirects to the forum home page, assumes that a user has already logged in
	 */
	@RequestMapping(value = "forum_homepage",method = RequestMethod.GET)
	public String showForumHomepage(ModelMap model, HttpSession session) throws InvalidUserCredentialsException, SessionNotFoundException, SubForumDoesNotExistException {
		logger.info("showForumHomepage request");
		FacadeI facade = getFacade();
		int sessionID = (int) session.getAttribute(SESSION_ID_ATTR);
		String status = facade.getCurrentUserForumStatus(sessionID);
		preperaForumHomepageModel(model, facade, session, status);
		return "forum_homepage";
	}

	/**
	 * Register a user to the current forum
	 */
	@RequestMapping(value = "register",method = RequestMethod.POST)
	public String register(ModelMap model, String username, String password, String email, String forumName)
			throws UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, DoesNotComplyWithPolicyException {
		logger.info("register request");
		FacadeI facade = getFacade();
		facade.register(forumName, username, password, email);
		model.addAttribute("forumName", forumName);
		return "login_page";
	}
	/**
	 * Allows to clear all data
	 */
	@RequestMapping(value = "reset",method = RequestMethod.GET)
	public String resetSystem() throws PermissionDeniedException, ForumAlreadyExistException, PasswordNotInEffectException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SubForumAlreadyExistException, ForumNotFoundException, SessionNotFoundException, SubForumDoesNotExistException {
		logger.info("resetSystem request");
		Facade.dropAllData();
		FacadeI f = getFacade();
		f.addForum("ADMIN", "ADMIN", "A", false, ".*", 5, 365);
		int sessionID = f.login("A", "ADMIN", "ADMIN");
		f.addSubforum(sessionID, "A sf1");
		f.addSubforum(sessionID, "A sf2");
		f.addSubforum(sessionID, "A sf3");
		f.logout(sessionID);

		f.addForum("ADMIN", "ADMIN", "B", false, ".*", 5, 365);
		sessionID = f.login("B", "ADMIN", "ADMIN");
		f.addSubforum(sessionID, "B sf1");
		f.addSubforum(sessionID, "B sf2");
		f.addSubforum(sessionID, "B sf3");
		f.logout(sessionID);

		f.addForum("ADMIN", "ADMIN", "C", false, ".*", 5, 365);
		sessionID = f.login("C", "ADMIN", "ADMIN");
		f.addSubforum(sessionID, "C sf1");
		f.addSubforum(sessionID, "C sf2");
		f.addSubforum(sessionID, "C sf3");
		f.logout(sessionID);
		return "redirect:/facade";
	}

	private void preperaForumHomepageModel(ModelMap model, FacadeI facade, HttpSession session, String userStatus) throws SessionNotFoundException {
		logger.info("preperaForumHomepageModel request");
		int sessionID = (int) session.getAttribute(SESSION_ID_ATTR);
		String forumName = facade.getCurrentForumName(sessionID);
		String userName = facade.getCurrentUserName(sessionID);
		boolean isAdmin = facade.isAdmin(sessionID);
		facade.getSubForumList(sessionID);
		model.addAttribute("forumName", forumName);
		model.addAttribute("user", userName);
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("subforumsList", facade.getSubForumList(sessionID).values());
		model.addAttribute("userStatus", userStatus);
	}

	@RequestMapping(value = "addModerator", method = RequestMethod.POST)
	public void addModeratorToSubforum(HttpSession session,
									   String moderatorName, HttpServletResponse response) throws IOException, SubForumNotFoundException, PermissionDeniedException, UserNotFoundException, ForumNotFoundException, CloneNotSupportedException, SessionNotFoundException, TooManyModeratorsException {
		FacadeI facade = getFacade();
		try {
			facade.setModerator(getSessionID(session), moderatorName);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (Exception e) {
			logger.warn("exception thrown in addModeratorToSubforum", e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			throw e;
		}
	}
	@RequestMapping(value = "removeModerator", method = RequestMethod.POST)
	public void removeModerator(HttpSession session,
									   String moderatorName, HttpServletResponse response) throws Exception{
		FacadeI facade = getFacade();
		try {
			facade.removeModerator(getSessionID(session), moderatorName);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (Exception e) {
			logger.warn("exception thrown in removeModerator", e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			throw e;
		}
	}

	private FacadeI getFacade() {
		return Facade.getFacade();
	}


	/**
	 * redirects to the current forum home page after a login
	 */
	@RequestMapping(value = "subforum_homepage",method = RequestMethod.POST)
	public String showSubforumHomepage(ModelMap model, HttpSession session, String subforumName) throws InvalidUserCredentialsException, SubForumAlreadyExistException, SubForumNotFoundException, SessionNotFoundException, SubForumDoesNotExistException {
		logger.info("showSubforumHomepage request");
		FacadeI facade = getFacade();
		preperaSubforumHomepageModel(model, facade, session, subforumName);
		return "subforum_homepage";
	}

	/**
	 * Called when the user is already logged in and the subforum is listed in the httpsession
	 */
	@RequestMapping(value = "subforum_homepage",method = RequestMethod.GET)
	public String refreshSubforumHomepage(ModelMap model, HttpSession session) throws InvalidUserCredentialsException, SubForumAlreadyExistException, SubForumNotFoundException, SessionNotFoundException, SubForumDoesNotExistException {
		logger.info("refreshSubforumHomepage request");
		FacadeI facade = getFacade();

		preperaSubforumHomepageModel(model, facade, session, null);
//		ForumI forum = (ForumI) session.getAttribute(SESSION_FORUM_ATTR);
//		User user = (User) session.getAttribute(SESSION_USER_ATTR);
//		SubForumPermissionI subforum = (SubForumPermissionI) session.getAttribute(SESSION_SUBFORUM_ATTR);
//		preperaSubforumHomepageModel(model, facade, subforum, user);
		return "subforum_homepage";
	}
	private void preperaSubforumHomepageModel(ModelMap model, FacadeI facade, HttpSession session, String subforumName) throws SubForumAlreadyExistException, SessionNotFoundException, SubForumNotFoundException, SubForumDoesNotExistException {
		logger.info("preperaSubforumHomepageModel request");
		int sessionID = (int) session.getAttribute(SESSION_ID_ATTR);
		ExSubForumI subForum;
		if(subforumName != null && !subforumName.equals("")) {
			subForum = facade.viewSubforum(sessionID, subforumName);
		}else {
			subForum = facade.viewSubforum(sessionID);
		}
		Collection<? extends ExThreadI> threads = subForum.getThreads().values();
		model.addAttribute("subforumName", subforumName);
		model.addAttribute("user", facade.getCurrentUserName(sessionID));
		model.addAttribute("userStatus", facade.getCurrentUserSubForumStatus(sessionID));
		model.addAttribute("isAdmin", facade.isAdmin(sessionID)); //TODO
		model.addAttribute("threadsList", threads);

	}


	/**
	 * Send a request to create a new sub forum in the currently used forum
	 */
	@RequestMapping(value = "addThread",method = RequestMethod.POST)
	public void addThread(ModelMap model, HttpSession session, String srcMsgTitle, String srcMsgBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException, SubForumDoesNotExistException {
		logger.info("addThread request");
		FacadeI f = getFacade();
		int sessionID = (int) session.getAttribute(SESSION_ID_ATTR);
		f.addThread(sessionID, srcMsgTitle, srcMsgBody);
		model.addAttribute("threadTitle", srcMsgTitle);
	}

	/**
	 * Called when the user is already logged in and the subforum is listed in the httpsession
	 */
	@RequestMapping(value = "thread_view",method = RequestMethod.GET)
	public String showThread(ModelMap model, HttpSession session, String threadID) throws DoesNotComplyWithPolicyException, ThreadNotFoundException, SessionNotFoundException {
		FacadeI facade = getFacade();
		int sessionID = (int) session.getAttribute(SESSION_ID_ATTR);
		ExThreadI thread;
		if (threadID != null){
			thread =  facade.viewThread(sessionID, threadID);
		}
		else{
			thread = facade.viewThread(sessionID);
		}
		model.addAttribute("thread", thread);
		model.addAttribute("node", thread.getMessages().getRoot());
		model.addAttribute("isGuest", facade.isGuest(sessionID));
		return "thread_view";
	}

	@RequestMapping(value = "thread_view",method = RequestMethod.POST)
	public String addMessageAndShowThread(ModelMap model, HttpSession session, String newMsgTitle, String newMsgBody,
										  int messageID) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException, ThreadNotFoundException, SubForumDoesNotExistException {
		int sessionID = getSessionID(session);
		FacadeI facade = getFacade();
		facade.addReply(sessionID, messageID, newMsgTitle, newMsgBody);
		ThreadI thread = facade.getCurrentThread(sessionID);
		model.addAttribute("thread", thread);
		model.addAttribute("node", thread.getMessages().getRoot());
		model.addAttribute("isGuest", facade.isGuest(sessionID));
		return "thread_view";
	}

	private int getSessionID(HttpSession session) {
		return (int) session.getAttribute(SESSION_ID_ATTR);
	}

	@RequestMapping(value = "reply_to_message",method = RequestMethod.POST)
	public String addReplyMessage(ModelMap model, HttpSession session, int messageID) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
		model.addAttribute("messageID", messageID);
		return "reply_to_message";
	}


	@RequestMapping(value = "auth", method = RequestMethod.POST)
	public String authUser(HttpServletResponse response, String forumName, String username, String auth_string) throws EmailNotAuthanticatedException, UserNotFoundException, IOException {
		FacadeI facade = getFacade();
		try {
			facade.authenticateUser(forumName, username, auth_string);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			logger.warn(e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			throw e;

		}
		return "facade";
	}



	@RequestMapping(value = "viewForumStatistics", method = RequestMethod.POST)
	public String viewForumStatistics(HttpServletResponse response, HttpSession session) throws EmailNotAuthanticatedException, UserNotFoundException, IOException, SessionNotFoundException, SubForumDoesNotExistException, PermissionDeniedException {
		FacadeI facade = getFacade();
		String res = facade.viewModeratorStatistics(getSessionID(session));
		return "facade";
	}




	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest req, Exception exception) {
		logger.error("Request: " + req.getRequestURL() + " raised an exception", exception);

		ModelAndView mav = new ModelAndView();
		mav.addObject("error_message", exception.getMessage());
//		mav.addObject("exception", exception);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("error");
		return mav;
	}
}
