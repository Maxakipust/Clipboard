package Commands;

import java.io.Serializable;

public class Login implements Serializable {
    public String username;
    public String password;
    public Login(String username, String password){
        this.username = username;
        this.password = password;
    }
}
