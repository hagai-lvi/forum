package data_structures;

import main.Persistancy.PersistantObject;
import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import main.forum_contents.ForumMessage;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xkcd on 6/2/2015.
 */
@Entity(name = "node")
public class Node extends PersistantObject{


    @OneToOne(targetEntity = ForumMessage.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@Transient
    @JsonView(NativeGuiController.class)
    public ForumMessage data;

    @ManyToOne(targetEntity = Node.class,  cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    //@Transient
    public Node parent;

    @OneToMany(targetEntity = Node.class,  cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    //@Transient
    @JsonView(NativeGuiController.class)
    public List<Node> children;

    public Node(ForumMessage data, Node parent) {
        this.data = data;
        this.parent = parent;
        this.children = new ArrayList<>();
    //    this.Save();
    }

    public Node() {
    }

    public void addChild(ForumMessage child) {
        Node toAdd = new Node(child, this);
        children.add(toAdd);
    //    this.Save();
    }

    /**
     * Find a node that contains the specified data. return null if non found
     */
    public Node findChild(ForumMessage data) {

        if (this.data.equals(data)) {
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

    public ForumMessage getData() {
        return data;
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
