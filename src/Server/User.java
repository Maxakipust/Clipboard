package Server;

import Commands.Err;
import Commands.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class User {
    public String userName;
    public String passwordHash;
    List<Connection> connections;
    List<Thread> listeners;
    public User(String userName, String passwordHash){
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.connections = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    public void addConnection(Connection connection){
        connections.add(connection);
        Listener l = new Listener(this, connection);
        Thread thread = new Thread(l);
        thread.start();
        listeners.add(thread);
    }

    void dataReceived(Object input, Connection recievedConnection){
        if(input instanceof Message){
            for(Connection connection:connections){
                if(connection != recievedConnection){
                    try {
                        connection.sendObject(input);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if(input instanceof Err){
            Err err = (Err)input;
            err.print();
        }else{
            System.err.println(userName.toUpperCase()+": Received unknown data");
        }
    }


}
