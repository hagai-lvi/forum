package controller;

//import data_structures.TreeNode;

import main.exceptions.*;
import main.interfaces.*;
import main.services_layer.Facade;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
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

	/**
	 * Shows a facade with all the available forums in the system
	 */
	@RequestMapping(value = "/facade",method = RequestMethod.GET)
	public void showFacade(ModelMap model) {
		logger.info("showFacade request");
		FacadeI f = Facade.getFacade();
		model.addAttribute("forumList", f.getForumList());
	}

	/**
	 * Send a request to create a new forum in the system
	 */
	@RequestMapping(value = "/addForum",method = RequestMethod.POST)
	public void addForum(ModelMap model, String forumName, int numOfModerators, String passRegex) throws PermissionDeniedException, ForumAlreadyExistException {
		logger.info("addForum request");
		FacadeI f = Facade.getFacade();
		f.addForum(ADMIN_PASS, ADMIN_USER, forumName, passRegex, numOfModerators); //TODO get credentials from the user
		model.addAttribute("forumName", forumName);
	}


	/**
	 * Send a request to create a new sub forum in the currently used forum
	 */
	@RequestMapping(value = "/addSubforum",method = RequestMethod.POST)
	public void addSubforum(ModelMap model, HttpSession session, String subforumName) throws SubForumAlreadyExistException, PermissionDeniedException {
		logger.info("addSubforum request");
		FacadeI f = Facade.getFacade();
		Integer sessionId = (Integer) session.getAttribute(SESSION_ID_ATTR);
		f.createSubforum(sessionId, subforumName);
		model.addAttribute("subforumName", subforumName);
	}


	/**
	 * Shows a login/register page
	 */
	@RequestMapping(value = "/login_page",method = RequestMethod.POST)
	public void loginForm(ModelMap model, String forum) {
		logger.info("loginForm request");
		model.addAttribute("forumName", forum);
	}


	/**
	 * redirects to the current forum home page after a login
	 */
	@RequestMapping(value = "forum_homepage",method = RequestMethod.POST)
	public String loginToForum(ModelMap model, HttpSession session, String username, String password, String forumName)
			throws InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException {
		logger.info("loginToForum request");
		FacadeI facade = Facade.getFacade();
		Integer sessionID = facade.login(forumName, username, password);
		session.setAttribute(SESSION_ID_ATTR, sessionID);
		session.setAttribute(SESSION_ID_ATTR, sessionID);
		preperaForumHomepageModel(model, facade, session);
		return "forum_homepage";
	}

	/**
	 * Redirects to the forum home page, assumes that a user has already logged in
	 */
	@RequestMapping(value = "forum_homepage",method = RequestMethod.GET)
	public String showForumHomepage(ModelMap model, HttpSession session) throws InvalidUserCredentialsException {
		logger.info("showForumHomepage request");
		FacadeI facade = Facade.getFacade();
		preperaForumHomepageModel(model, facade, session);
		return "forum_homepage";
	}

	/**
	 * Register a user to the current forum
	 */
	@RequestMapping(value = "register",method = RequestMethod.POST)
	public String register(ModelMap model, String username, String password, String email, String forumName)
			throws UserAlreadyExistsException, InvalidUserCredentialsException {
		logger.info("register request");
		FacadeI facade = Facade.getFacade();
		facade.register(forumName, username, password, email);//TODO - auto login after registraion
		model.addAttribute("forumName", forumName);
		return "login_page";
	}

	/**
	 * Allows to clear all data
	 */
	@RequestMapping(value = "reset",method = RequestMethod.GET)
	public String resetSystem() throws PermissionDeniedException, ForumAlreadyExistException, PasswordNotInEffectException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, SubForumAlreadyExistException {
		logger.info("resetSystem request");
		Facade.dropAllData();
		FacadeI f = Facade.getFacade();
		f.addForum("ADMIN", "ADMIN", "A", ".*", 5);
		int sessionID = f.login("A", "ADMIN", "ADMIN");
		f.createSubforum(sessionID, "A sf1");
		f.createSubforum(sessionID, "A sf2");
		f.createSubforum(sessionID, "A sf3");

		f.addForum("ADMIN", "ADMIN", "B", ".*", 5);
		sessionID = f.login("B", "ADMIN", "ADMIN");
		f.createSubforum(sessionID, "B sf1");
		f.createSubforum(sessionID, "B sf2");
		f.createSubforum(sessionID, "B sf3");

		f.addForum("ADMIN", "ADMIN", "C", ".*", 5);
		sessionID = f.login("C", "ADMIN", "ADMIN");
		f.createSubforum(sessionID, "C sf1");
		f.createSubforum(sessionID, "C sf2");
		f.createSubforum(sessionID, "C sf3");
		return "redirect:/facade";
	}

	private void preperaForumHomepageModel(ModelMap model, FacadeI facade, HttpSession session) {
		logger.info("preperaForumHomepageModel request");
		int sessionID = (int) session.getAttribute(SESSION_ID_ATTR);
		String forumName = facade.getCurrentForumName(sessionID);
		String userName = facade.getCurrentUserName(sessionID);
		boolean isAdmin = facade.isAdmin(sessionID);
		facade.getSubForumList(sessionID);
		model.addAttribute("forumName", forumName);
		model.addAttribute("user", userName);
//		model.addAttribute("numberOfSubforums", facade.getSubForumList(user).size());TODO
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("subforumsList", facade.getSubForumList(sessionID));
	}


	/**
	 * redirects to the current forum home page after a login
	 */
	@RequestMapping(value = "subforum_homepage",method = RequestMethod.POST)
	public String showSubforumHomepage(ModelMap model, HttpSession session, String subforumName) throws InvalidUserCredentialsException, SubForumAlreadyExistException {
		logger.info("showSubforumHomepage request");
		FacadeI facade = Facade.getFacade();
		preperaSubforumHomepageModel(model, facade, session, subforumName);
		return "subforum_homepage";
	}

	/**
	 * Called when the user is already logged in and the subforum is listed in the httpsession
	 */
	@RequestMapping(value = "subforum_homepage",method = RequestMethod.GET)
	public String refreshSubforumHomepage(ModelMap model, HttpSession session) throws InvalidUserCredentialsException, SubForumAlreadyExistException {
		logger.info("refreshSubforumHomepage request");
		FacadeI facade = Facade.getFacade();

		preperaSubforumHomepageModel(model, facade, session, null);
//		ForumI forum = (ForumI) session.getAttribute(SESSION_FORUM_ATTR);
//		User user = (User) session.getAttribute(SESSION_USER_ATTR);
//		SubForumPermissionI subforum = (SubForumPermissionI) session.getAttribute(SESSION_SUBFORUM_ATTR);
//		preperaSubforumHomepageModel(model, facade, subforum, user);
		return "subforum_homepage";
	}
	private void preperaSubforumHomepageModel(ModelMap model, FacadeI facade, HttpSession session, String subforumName) throws SubForumAlreadyExistException {
		logger.info("preperaSubforumHomepageModel request");
		int sessionID = (int) session.getAttribute(SESSION_ID_ATTR);
		ExSubForumI subForum;
		if(subforumName != null && !subforumName.equals("")) {
			subForum = facade.viewSubforum(sessionID, subforumName);
		}else {
			subForum = facade.viewSubforum(sessionID);
		}
		Collection<? extends ExThreadI> threads = subForum.getThreads();
		model.addAttribute("subforumName", subforumName);
		model.addAttribute("user", facade.getCurrentUserName(sessionID));
//		model.addAttribute("numberOfthreads", threads.length);//TODO
//		model.addAttribute("isModerator", user.isAdmin()); //TODO
		model.addAttribute("threadsList", threads);

	}


	/**
	 * Send a request to create a new sub forum in the currently used forum
	 */
	@RequestMapping(value = "/addThread",method = RequestMethod.POST)
	public void addThread(ModelMap model, HttpSession session, String srcMsgTitle, String srcMsgBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
		logger.info("addThread request");
		FacadeI f = Facade.getFacade();
		int sessionID = (int) session.getAttribute(SESSION_ID_ATTR);
		f.createNewThread(sessionID, srcMsgTitle, srcMsgBody);
		model.addAttribute("threadTitle", srcMsgTitle);
	}

	/**
	 * Called when the user is already logged in and the subforum is listed in the httpsession
	 */
	@RequestMapping(value = "thread_view",method = RequestMethod.GET)
	public String showThread(ModelMap model, HttpSession session, String threadID) throws DoesNotComplyWithPolicyException {
		FacadeI facade = Facade.getFacade();
		int sessionID = (int) session.getAttribute(SESSION_ID_ATTR);
		ExThreadI thread = facade.viewThread(sessionID, threadID);
		model.addAttribute("thread", thread);
		model.addAttribute("node", thread.getMessages().getRoot());
		return "thread_view";
	}

	@RequestMapping(value = "thread_view",method = RequestMethod.POST)
	public String addMessageAndShowThread(ModelMap model, HttpSession session, String newMsgTitle, String newMsgBody,
										  int messageID) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException {
		int sessionID = getSessionID(session);
		FacadeI facade = Facade.getFacade();
		facade.addReply(sessionID, messageID, newMsgTitle, newMsgBody);
		ThreadI thread = facade.getCurrentThread(sessionID);
		model.addAttribute("thread", thread);
		model.addAttribute("node", thread.getMessages().getRoot());
		return "thread_view";
	}

	private int getSessionID(HttpSession session) {
		return (int) session.getAttribute(SESSION_ID_ATTR);
	}

	//
//	//TODO remove, for testing only
//	@RequestMapping(value = "abc",method = RequestMethod.GET)
//	public static String showThread(ModelMap model, HttpServletRequest request){
//		model.addAttribute("node", TreeNode.getNodeTree());
//		return "thread_view";
//	}
//
	@RequestMapping(value = "/reply_to_message",method = RequestMethod.POST)
	public String addReplyMessage(ModelMap model, HttpSession session, int messageID) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
		model.addAttribute("messageID", messageID);
		return "reply_to_message";
	}
//
//
//	@ExceptionHandler(Exception.class)
//	public ModelAndView handleError(HttpServletRequest req, Exception exception) {
//		logger.error("Request: " + req.getRequestURL() + " raised an exception", exception);
//
//		ModelAndView mav = new ModelAndView();
//		mav.addObject("error_message", exception.getMessage());
//		mav.addObject("exception", exception);
//		mav.addObject("url", req.getRequestURL());
//		mav.setViewName("error");
//		return mav;
//	}
}
