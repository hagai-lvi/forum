package main.forum_contents;

import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import main.interfaces.MessageI;
import main.interfaces.UserI;
import main.User.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * Created by hagai_lvi on 4/11/15.
 */
@Entity
public class ForumMessage implements MessageI {

	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	private UserI writingUser;
	@JsonView(NativeGuiController.class)
	private String messageText;
	@JsonView(NativeGuiController.class)
	private String messageTitle;
	private Date writingTime;
	@OneToMany(targetEntity = ForumMessage.class, cascade = CascadeType.ALL)
	private List<MessageI> replies;


	public ForumMessage(UserI user, String messageTitle, String messageText){
		this.writingUser = user;
		this.messageText = messageText;
		this.messageTitle = messageTitle;
		writingTime = new Date();
		replies = new ArrayList<>();
	}

	public ForumMessage() {
	}

	@Override
	public void editText(/*UserI user,*/ String newText)/* throws PermissionDeniedException */{
		/*if (user != this.writingUser){
			throw new PermissionDeniedException("User can't edit message");
		}*/ //Why should this be checked here?
		//Todo resolve check permissions issue here.
		this.messageText = newText;
	}

	@Override
	public void editTitle(String title) {
		this.messageTitle = title;
	}

	@Override
	public String getUser() {
		return writingUser.getUsername();
	}
	@Override
	public Date getDate()  { return writingTime; }

	@Override
	public String printSubTree(){
		String ans = this.messageText;
		for (MessageI m: replies){
			ans += "--> " + m.getMessageText();
		}
		return ans;
	}

	@Override
	public String getMessageText(){

		return messageText;
	}

	@Override
	public String getMessageTitle(){
		return messageTitle;
	}


	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	public int getId() {
		return id;
	}

	@Override
	public Collection<MessageI> getReplies() {
		return replies;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void addReply(MessageI reply){
		this.replies.add(reply);
	}



}
