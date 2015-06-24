package main.forum_contents;

import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import data_structures.Tree;
import main.Persistancy.PersistantObject;
import main.exceptions.MessageNotFoundException;
import main.exceptions.NodeNotFoundException;
import main.interfaces.MessageI;
import main.interfaces.ThreadI;

import javax.persistence.*;

/**
 * Created by hagai on 07/04/15.
 */
@Entity
public class ForumThread extends PersistantObject implements ThreadI{

    @OneToOne(targetEntity = Tree.class, cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    //@Transient
    @JsonView(NativeGuiController.class)
    private Tree messages;


    public ForumThread(String user, String title, String text){
        messages = new Tree(new ForumMessage(user, title, text));
//        this.Save();
    }

    public ForumThread() {
    }

    public int getMessagesCount(){
        return messages.getMessagesCount();
    }

    @Override
    public String getTitle() {
        return getRootMessage().getMessageTitle();
    }

    @Override
    public void editMessage(MessageI originalMessage, String title, String newMessage) throws MessageNotFoundException {
        if (newMessage == null){
            throw new MessageNotFoundException(title);
        }
        MessageI msg = messages.findNode(originalMessage);

        if (msg == null){
            throw new MessageNotFoundException(originalMessage.getMessageTitle());
        }
        msg.editTitle(title);
        msg.editText(newMessage);
    }

    public Tree getMessages(){
        return messages;
    }


    @Override
    public MessageI getRootMessage() {
        return messages.getRoot().data;
    }

    @Override
    public MessageI addReply(MessageI original, String title, String text, String user) throws MessageNotFoundException {
        try {
            ForumMessage reply = new ForumMessage(user, title, text);
            messages.add(reply, original);
            original.addReply(reply);
            return reply;
        } catch (NodeNotFoundException | NullPointerException e) {
            throw new MessageNotFoundException(original.getMessageTitle());
        }
//        this.Save();
    }

    @Override
    public boolean contains(MessageI message){
        return messages.findNode(message) != null;
    }

    @Override
    public void remove(MessageI message) {
        messages.remove(message);
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
