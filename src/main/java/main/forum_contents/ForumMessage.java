package main.forum_contents;

import main.exceptions.IncorrectPermissionsException;
import main.exceptions.PermissionDeniedException;
import main.interfaces.MessageI;
import main.interfaces.UserI;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hagai_lvi on 4/11/15.
 */
public class ForumMessage implements MessageI {

	ForumMessage reply_message;
	UserI writing_user;
	String message_text;
	String message_title;
	Date writing_time;
	ArrayList<MessageI> replays;
	boolean isDeleted = false;


	public ForumMessage(ForumMessage reply_to, UserI user, String message_text, String message_title){
		this.writing_user = user;
		this.message_text = message_text;
		this.reply_message = reply_to;
		this.message_title = message_title;
		writing_time = new Date();
		replays = new ArrayList<MessageI>();
	}

	public void editText(UserI user, String new_text) throws PermissionDeniedException {
		if (user != this.writing_user){
			throw new PermissionDeniedException("User can't edit message");
		}
		this.message_text = new_text;
	}


	@Override
	public UserI getUser() {
		return writing_user;
	}
	public Date getDate()  { return writing_time; }

	@Override
	public void reply(MessageI reply) {
		this.replays.add(reply);
	}

	public ForumMessage get_replayed_message(){
		return reply_message;
	}

	public String printSubTree(int depth){
		if (isDeleted){
			return "";
		}
		String ans = "";
		for (int i = 0; i < depth ; i++){
			ans += "	";
		}
		ans = "--> " + this.message_text;
		for (MessageI m: replays){
			ans += "\n " + m.printSubTree(depth+1);
		}
		return ans;
	}

	public void removeMessage(){
		this.isDeleted = true;
	}
}
