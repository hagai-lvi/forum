package data_structures;


import main.Persistancy.PersistantObject;
import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import main.exceptions.MessageNotFoundException;
import main.exceptions.NodeNotFoundException;
import main.exceptions.ThreadFinalMessageDeletedException;
import main.interfaces.MessageI;

import javax.persistence.*;
import java.util.List;

/**
 * A basic tree implementation
 */
@Entity
public class Tree extends PersistantObject{
    // TODO handle the case in which the root is null (might happen after calling remove())

    @OneToOne(targetEntity = Node.class, cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JsonView(NativeGuiController.class)
    private Node root;


    public Tree(MessageI rootData) {
        root = new Node(rootData, null);
    }

    public Tree() {
    }

    public void add(MessageI original, MessageI newMessage) throws NodeNotFoundException {
        Node ancestorNode = root.findChild(original);
        if (ancestorNode == null || newMessage == null){
            throw new NodeNotFoundException("Could not find ancestor " + original.toString() + " in the tree");
        }
        ancestorNode.addChild(newMessage);
    }

    public Node getRoot() {
        return root;
    }

    public MessageI findNode(MessageI data) {
        if (root == null){
            return null;
        }

        Node node = root.findChild(data);
        if (node == null){
            return null;
        }
        else{
            return node.data;
        }
    }

    public void remove(MessageI data) throws MessageNotFoundException, ThreadFinalMessageDeletedException {
        Node child = root.findChild(data);
        if (child == null){
            throw new MessageNotFoundException("Could not find message " + data.getMessageTitle());
        }
        if (child.getId() == root.getId()){
            root = null;
            throw new ThreadFinalMessageDeletedException();
        }
        else {
            child.parent.children.remove(child);
        }
    }

    public MessageI find(int id){
        return find(root, id);
    }
    private MessageI find(Node node, int id) {
        if (node.data.getId() == id) {
            return node.data;
        }
        else{
            for (Node child : root.children){
                MessageI msg = find(child, id);
                if (msg != null) {
                    return msg;
                }
            }
            return null;
        }
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getMessagesCount(Node node){
       if (node == null){
           return 0;
       }
        if (node.getChildren().size() == 0){
           return 1;
       }
        int c = 0;
        for (Node n : node.getChildren()){
            c = c + getMessagesCount(n);
        }
        return c + 1;
    }

    public int getMessagesCount(){
        if (root == null){
            return 0;
        }

        if (root.getChildren().size() == 0){
            return 1;
        }

        int c = 0;
        for (Node n : root.getChildren()){
            c = c + getMessagesCount(n);
        }
        return c+1;
    }

    public MessageI editNodeData(MessageI originalMessage, String title, String text) {
        if (root == null){
            return null;
        }
        if (root.getData().getId() == originalMessage.getId()){
            root.getData().editTitle(title);
            root.getData().editText(text);
            return root.getData();
        }
        for (Node n : root.getChildren()){
            MessageI newmsg;
            if ((newmsg = editNodeData(n.getData(), title, text)) != null){
                return newmsg;
            }
        }
        return null;
    }
}