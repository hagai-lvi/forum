package controller;//package controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.servlet.http.HttpSession;
//import java.util.Date;
//
//@Controller
//@RequestMapping("/hello")
//public class HelloController {
//
//	@RequestMapping(method = RequestMethod.GET)
//	public void printWelcome(ModelMap model, HttpSession session) {
//		if (session.getAttribute("set") == null){
//			model.addAttribute("message", "Forum Hello World");
//			session.setAttribute("set", new Date(System.currentTimeMillis()).toString());
//		}
//		else{
//			model.addAttribute("message", session.getAttribute("set"));
//		}
//	}
//
//}