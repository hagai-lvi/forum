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


    public ForumThread(MessageI initialMessage){
        messages = new Tree(initialMessage);
//        this.Save();
    }

    public ForumThread() {
    }

    @Override
    public String getTitle() {
        return getRootMessage().getMessageTitle();
    }

    @Override
    public void editMessage(MessageI originalMessage, MessageI newMessage) throws MessageNotFoundException {
        if (newMessage == null){
            throw new MessageNotFoundException(newMessage.getId());
        }
        MessageI msg = messages.findNode(originalMessage);

        if (msg == null){
            throw new MessageNotFoundException(originalMessage.getId());
        }
        msg.editTitle(newMessage.getMessageTitle());
        msg.editText(newMessage.getMessageText());
    }

    public Tree getMessages(){
        return messages;
    }


    @Override
    public MessageI getRootMessage() {
        return messages.getRoot().data;
    }

    @Override
    public void addReply(MessageI reply, MessageI original) throws MessageNotFoundException {
        try {
            messages.add(reply, original); //TODO - what to do with tree and array of replies?
            original.addReply(reply);
        } catch (NodeNotFoundException | NullPointerException e) {
            throw new MessageNotFoundException(original.getId());
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
