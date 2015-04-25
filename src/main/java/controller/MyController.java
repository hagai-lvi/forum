package controller;

import main.forum_contents.Forum;
import main.interfaces.FacadeI;
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

	@RequestMapping(value = "/facade",method = RequestMethod.GET)
	public void printWelcome(ModelMap model, HttpSession session) {

		if (session.getAttribute("user") != null){

			UserI user = (UserI) session.getAttribute("user");
			model.addAttribute("message", "logged in as " + user.getUsername());
		}
		else{
			model.addAttribute("message", "you are not logged in");
		}
		FacadeI f = Facade.getFacade();
		model.addAttribute("size", f.getForumList().size());
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


	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public void login(ModelMap model, String forum) {
		model.addAttribute("forumName", forum);
	}


}
