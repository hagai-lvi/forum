package main.forum_contents;

import main.exceptions.PermissionDeniedException;
import main.interfaces.MessageI;
import main.interfaces.UserI;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.*;


/**
 * Created by hagai_lvi on 4/11/15.
 */
public class ForumMessage implements MessageI {

	ForumMessage reply_message;
	UserI writingUser;
	String messageText;
	String messageTitle;
	Date writingTime;
	ArrayList<MessageI> replays;
	boolean isDeleted = false;


	public ForumMessage(ForumMessage reply_to, UserI user, String messageText, String messageTitle){
		this.writingUser = user;
		this.messageText = messageText;
		this.reply_message = reply_to;
		this.messageTitle = messageTitle;
		writingTime = new Date();
		replays = new ArrayList<MessageI>();
	}

	public void editText(UserI user, String newText) throws PermissionDeniedException {
		if (user != this.writingUser){
			throw new PermissionDeniedException("User can't edit message");
		}
		this.messageText = newText;
	}


	@Override
	public UserI getUser() {
		return writingUser;
	}
	public Date getDate()  { return writingTime; }

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
		ans = "--> " + this.messageText;
		for (MessageI m: replays){
			ans += "\n " + m.printSubTree(depth+1);
		}
		return ans;
	}

	public void removeMessage(){
		this.isDeleted = true;
	}

	public String getMessageText(){

		return messageText;
	}

	public String getMessageTitle(){
		return messageTitle;
	}


}
