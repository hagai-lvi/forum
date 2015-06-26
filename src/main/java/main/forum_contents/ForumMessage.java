package main.forum_contents;

import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import main.interfaces.MessageI;

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

	//@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	private String writingUser;
	@JsonView(NativeGuiController.class)
	private String messageText;
	@JsonView(NativeGuiController.class)
	private String messageTitle;
	private Date writingTime;
	@OneToMany(targetEntity = ForumMessage.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<MessageI> replies;


	public ForumMessage(String user, String messageTitle, String messageText){
		this.writingUser = user;
		this.messageText = messageText;
		this.messageTitle = messageTitle;
		writingTime = new Date();
		replies = new ArrayList<>();
		this.id = ForumThread.GENERATED_ID++;
	}

	public ForumMessage() {
	}

	@Override
	public void editText(/*UserI user,*/ String newText)/* throws PermissionDeniedException */{
		this.messageText = newText;
	}

	@Override
	public void editTitle(String title) {
		this.messageTitle = title;
	}

	@Override
	public String getUser() {
		return writingUser;
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
