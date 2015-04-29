package controller;

import main.User.User;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.*;
import main.services_layer.Facade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by hagai_lvi on 4/18/15.
 */
@Controller
public class MyController {

	public static final String SESSION_USER_ATTR = "user";
	public static final String SESSION_FORUM_ATTR = "forum";
	public static final String SESSION_SUBFORUM_ATTR = "subforum";

	/**
	 * Shows a facade with all the available forums in the system
	 */
	@RequestMapping(value = "/facade",method = RequestMethod.GET)
	public void showFacade(ModelMap model) {
		FacadeI f = Facade.getFacade();
		model.addAttribute("forumList", f.getForumList());
	}

	/**
	 * Send a request to create a new forum in the system
	 */
	@RequestMapping(value = "/addForum",method = RequestMethod.POST)
	public void addForum(ModelMap model, String forumName, int numOfModerators, String passRegex) {
		FacadeI f = Facade.getFacade();
		ForumPolicyI policy = new ForumPolicy(numOfModerators, passRegex);
		f.addForum(new Forum(forumName, policy));
		model.addAttribute("forumName", forumName);
	}


	/**
	 * Send a request to create a new sub forum in the currently used forum
	 */
	@RequestMapping(value = "/addSubforum",method = RequestMethod.POST)
	public void addSubforum(ModelMap model, HttpSession session, String subforumName) throws SubForumAlreadyExistException, PermissionDeniedException {
		FacadeI f = Facade.getFacade();
		UserI user = (UserI) session.getAttribute(SESSION_USER_ATTR);
		f.createSubforum(subforumName, user);
		model.addAttribute("subforumName", subforumName);
	}


	/**
	 * Shows a login/register page
	 */
	@RequestMapping(value = "/login_page",method = RequestMethod.POST)
	public void loginForm(ModelMap model, HttpSession session, String forum) {
		model.addAttribute("forumName", forum);
		FacadeI facade = Facade.getFacade();
		session.setAttribute(SESSION_FORUM_ATTR, facade.getForumByName(forum));
	}


	/**
	 * redirects to the current forum home page after a login
	 */
	@RequestMapping(value = "forum_homepage",method = RequestMethod.POST)
	public String loginToForum(ModelMap model, HttpSession session, String username, String password) throws InvalidUserCredentialsException {
		FacadeI facade = Facade.getFacade();
		ForumI forum = (ForumI) session.getAttribute(SESSION_FORUM_ATTR);
		UserI user = facade.login(forum, username, password); //TODO handle exception thrown from login
		session.setAttribute(SESSION_USER_ATTR, user);
		preperaForumHomepageModel(model, facade, forum, user);
		return "forum_homepage";
	}

	/**
	 * Redirects to the forum home page, assumes that a user has already logged in
	 */
	@RequestMapping(value = "forum_homepage",method = RequestMethod.GET)
	public String showForumHomepage(ModelMap model, HttpSession session) throws InvalidUserCredentialsException {
		FacadeI facade = Facade.getFacade();
		ForumI forum = (ForumI) session.getAttribute(SESSION_FORUM_ATTR);
		UserI user = (UserI) session.getAttribute(SESSION_USER_ATTR);
		preperaForumHomepageModel(model, facade, forum, user);
		return "forum_homepage";
	}

	/**
	 * Register a user to the current forum
	 */
	@RequestMapping(value = "register",method = RequestMethod.POST)
	public String register(ModelMap model, HttpSession session, String username, String password, String email)
			throws UserAlreadyExistsException, InvalidUserCredentialsException {
		FacadeI facade = Facade.getFacade();
		ForumI forum = (ForumI) session.getAttribute("forum");
		UserI user = facade.register(forum, username, password, email); //TODO handle exception from register
		session.setAttribute(SESSION_USER_ATTR, user);
		preperaForumHomepageModel(model, facade, forum, user);
		return "forum_homepage";
	}

	/**
	 * Allows to clear all data
	 */
	@RequestMapping(value = "reset",method = RequestMethod.GET)
	public String resetSystem(){
		Facade.dropAllData();
		FacadeI f = Facade.getFacade();

		ForumPolicyI p = new ForumPolicy(10,".*");
		f.addForum(new Forum("A", p));

		p = new ForumPolicy(10,".*");
		f.addForum(new Forum("B", p));

		p = new ForumPolicy(10,".*");
		f.addForum(new Forum("C", p));
		return "redirect:/facade";
	}

	private void preperaForumHomepageModel(ModelMap model, FacadeI facade, ForumI forum, UserI user) {
		model.addAttribute("forumName", forum);
		model.addAttribute("user", user.getUsername());
		model.addAttribute("numberOfSubforums", facade.getSubForumList(user).size());
		model.addAttribute("isAdmin", user.isAdmin());
		model.addAttribute("subforumsList", forum.getSubForums());
	}


	/**
	 * redirects to the current forum home page after a login
	 */
	@RequestMapping(value = "subforum_homepage",method = RequestMethod.POST)
	public String showSubforumHomepage(ModelMap model, HttpSession session, String subforumName) throws InvalidUserCredentialsException {
		FacadeI facade = Facade.getFacade();
		ForumI forum = (ForumI) session.getAttribute(SESSION_FORUM_ATTR);
		User user = (User) session.getAttribute(SESSION_USER_ATTR);
		SubForumPermissionI subforum = facade.getSubforumByName(user, subforumName);
		session.setAttribute(SESSION_SUBFORUM_ATTR, subforum);
		preperaSubforumHomepageModel(model, facade, subforum, user);
		return "subforum_homepage";
	}

	private void preperaSubforumHomepageModel(ModelMap model, FacadeI facade, SubForumPermissionI subforum, User user) {
		ThreadI[] threads = subforum.getThreads();
		model.addAttribute("subforumName", subforum.getSubForum().getName());
		model.addAttribute("user", user.getUsername());
		model.addAttribute("numberOfthreads", threads.length);
//		model.addAttribute("isModerator", user.isAdmin()); TODO
		model.addAttribute("threadsList", threads);

	}


	/**
	 * Send a request to create a new sub forum in the currently used forum
	 */
	@RequestMapping(value = "/addThread",method = RequestMethod.POST)
	public void addThread(ModelMap model, HttpSession session, String srcMsgTitle, String srcMsgBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
		FacadeI f = Facade.getFacade();
		UserI user = (UserI) session.getAttribute(SESSION_USER_ATTR);
		SubForumPermissionI subforum = (SubForumPermissionI)session.getAttribute(SESSION_SUBFORUM_ATTR);
		f.createNewThread(user, subforum,srcMsgTitle, srcMsgBody);
		model.addAttribute("threadTitle", srcMsgTitle);
	}

}
