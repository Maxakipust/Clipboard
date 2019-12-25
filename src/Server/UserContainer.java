package Server;

import Commands.Login;
import Commands.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class UserContainer {
    Map<String, User> users = new HashMap<>();

    public UserContainer(){
        try {
            addUser("Admin", "Admin");
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    public User getUser(String name, String password) throws UserNotFoundException, WrongPasswordException {
        if(users.containsKey(name)){
            User u = users.get(name);
            if(Crypto.hashPassword(password).equals(u.passwordHash)){
                return u;
            }else{
                throw new WrongPasswordException();
            }
        }
        throw new UserNotFoundException();
    }

    public User addUser(String name, String password) throws UserAlreadyExistsException {
        if(users.containsKey(name)){
            throw new UserAlreadyExistsException();
        }
        User user = new User(name, Crypto.hashPassword(password));
        users.put(name, user);
        return user;
    }

    public void handleUser(Login loginInfo, Connection connection) throws IOException {
        User user;
        try{
            //log in
            user = getUser(loginInfo.username, loginInfo.password);
            connection.sendObject(new Message("logged in successfully"));
        }catch (UserNotFoundException unf){
            //create user
            try {
                user = addUser(loginInfo.username, loginInfo.password);
                connection.sendObject(new Message("created new user"));
            }catch (UserAlreadyExistsException ex){
                //user exists
                connection.sendObject(new Error("error logging in"));
                return;
            }
        } catch (WrongPasswordException e) {
            connection.sendObject(new Error("wrong password"));
            return;
        }
        user.addConnection(connection);

    }

    class UserNotFoundException extends Exception {

    }
    class WrongPasswordException extends Exception {

    }
    class UserAlreadyExistsException extends Exception {

    }
}
