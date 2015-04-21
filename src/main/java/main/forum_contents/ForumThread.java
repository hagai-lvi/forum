package main.forum_contents;

import main.interfaces.MessageI;
import main.interfaces.ThreadI;
import javax.persistence.*;

/**
 * Created by hagai on 07/04/15.
 */
public class ForumThread implements ThreadI{
    MessageI threadTree;


    public ForumThread(MessageI initialMessage){
        threadTree = initialMessage;
    }

    @Override
    public MessageI getRootMessage() {
        return threadTree;
    }

    public void printThread(){
        System.out.println(threadTree.printSubTree(0));
    }
}
