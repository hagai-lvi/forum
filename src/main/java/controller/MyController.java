package controller;

import main.exceptions.InvalidUserCredentialsException;
import main.exceptions.UserAlreadyExistsException;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.FacadeI;
import main.interfaces.ForumI;
import main.interfaces.ForumPolicyI;
import main.interfaces.UserI;
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

	@RequestMapping(value = "/facade",method = RequestMethod.GET)
	public void printWelcome(ModelMap model, HttpSession session) {

		FacadeI f = Facade.getFacade();
		model.addAttribute("forumList", f.getForumList());
	}

	@RequestMapping(value = "/facade",method = RequestMethod.POST)
	public void afterLogin(ModelMap model, HttpSession session) {

		FacadeI f = Facade.getFacade();
		model.addAttribute("size", f.getForumList().size());
		model.addAttribute("forumList", f.getForumList());
	}

	@RequestMapping(value = "/addForum",method = RequestMethod.POST)
	public void add(ModelMap model, String forumName) {
		FacadeI f = Facade.getFacade();
		f.addForum(new Forum(forumName, null));
		model.addAttribute("forumName", forumName);
	}


	@RequestMapping(value = "/login_page",method = RequestMethod.POST)
	public void loginPage(ModelMap model, HttpSession session, String forum) {
		model.addAttribute("forumName", forum);
		FacadeI facade = Facade.getFacade();
		session.setAttribute(SESSION_FORUM_ATTR, facade.getForumByName(forum));
	}


	@RequestMapping(value = "forum_homepage",method = RequestMethod.POST)
	public String showHomepage(ModelMap model, HttpSession session, String username, String password) throws InvalidUserCredentialsException {
		FacadeI facade = Facade.getFacade();
		ForumI forum = (ForumI) session.getAttribute(SESSION_FORUM_ATTR);
		UserI user = facade.login(forum, username, password); //TODO handle exception thrown from login
		session.setAttribute(SESSION_USER_ATTR, user);
		preperaForumHomepageModel(model, facade, forum, user);
		return "forum_homepage";
	}

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
		f.addForum(new Forum("A",p));

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
	}


}
