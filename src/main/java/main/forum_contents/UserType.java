package main.forum_contents;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by xkcd2 on 4/15/2015.
 */
@Entity
public class UserType {

    private String name;

    public UserType(String name){
        this.name = name;
    }



    public UserType() {
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
