package controller;

import main.forum_contents.Forum;
import main.interfaces.FacadeI;
import main.services_layer.Facade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
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
		FacadeI f = Facade.getFacade();
			model.addAttribute("message", "Facade controller");
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
	public void login(ModelMap model, @ModelAttribute Forum forum) {
		model.addAttribute("forumName", forum.getName());
	}


}
