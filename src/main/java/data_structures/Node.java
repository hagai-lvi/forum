package data_structures;

import main.Persistancy.PersistantObject;
import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import main.forum_contents.ForumMessage;
import main.interfaces.MessageI;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xkcd on 6/2/2015.
 */
@Entity(name = "Node")
public class Node extends PersistantObject{


    @OneToOne(targetEntity = ForumMessage.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@Transient
    @JsonView(NativeGuiController.class)
    public MessageI data;

    @ManyToOne(targetEntity = Node.class,  cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    //@Transient
    public Node parent;

    public List<Node> getChildren() {
        return children;
    }

    @OneToMany(targetEntity = Node.class,  cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    //@Transient
    @JsonView(NativeGuiController.class)
    public List<Node> children;

    public Node(MessageI data, Node parent) {
        this.data = data;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public Node() {
    }

    public void addChild(MessageI child) {
        Node toAdd = new Node(child, this);
        children.add(toAdd);
    }

    /**
     * Find a node that contains the specified data. return null if non found
     */
    public Node findChild(MessageI data) {
        if (data == null) {
            return null;
        }
        if (this.data.getId() == data.getId()) {
            return this;
        }

        Node res;
        for (Node node : children) {
            res = node.findChild(data);
            if (res != null) {
                return res;
            }
        }

        return null;
    }

    public MessageI getData() {
        return data;
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return data.getId();
    }

    public void setId(int id) {
        this.id = id;
    }
}
