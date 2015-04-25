package main.forum_contents;


import javax.persistence.Entity;
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

    @Id
    private String id;

    public UserType() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
