package main.forum_contents;

/**
 * Created by Gila on 21/04/2015.
 */
public class Example {
    private String username;
    private String password;

    public void setUsername(String newU){username = newU;}
    public void setPassword(String pass) {password=pass;}

    public String getUsername(){return username;}
    public String getPassword(){return password;}

    public Example(){
        username = "gilgil";
        password = "1111";
    }

    public Example(String user, String pass){
        username=user;
        password=pass;
    }
}
