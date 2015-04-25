package main.forum_contents;

import data_structures.Tree;
import main.exceptions.MessageNotFoundException;
import main.exceptions.NodeNotFoundException;
import main.interfaces.MessageI;
import main.interfaces.ThreadI;

import javax.persistence.*;

/**
 * Created by hagai on 07/04/15.
 */
@Entity
public class ForumThread implements ThreadI{

    @Transient //TODO : hagai, It's a mess to persist this kind of tree, we could use default implementation which is supported by hibernate
    private Tree<MessageI> messages;


    public ForumThread(MessageI initialMessage){
        messages = new Tree<>(initialMessage);
    }


    @Override
    public MessageI getRootMessage() {
        return messages.getRoot();
    }

    public void printThread(){
        throw new RuntimeException("Not yet implemented");
//        System.out.println(threadTree.printSubTree(0));
    }

    @Override
    public void addReply(MessageI reply, MessageI original) throws MessageNotFoundException {
        try {
            messages.add(reply, original);
            original.addReply(reply);
        } catch (NodeNotFoundException e) {
            //TODO fix the exception
            throw new MessageNotFoundException(original, null);
        }
    }

    @Override
    public boolean contains(MessageI message){
        return messages.findNode(message) != null;
    }

    @Override
    public void remove(MessageI message) {
        messages.remove(message);
    }

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
