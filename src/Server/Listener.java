package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Listener implements Runnable{
    public User u;
    public Connection connection;
    public Listener(User u, Connection connection){
        this.u = u;
        this.connection = connection;
    }

    private void listen(Connection s){
        try {
            while (true) {
                Object input = connection.listenForObject();
                u.dataReceived(input, connection);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        listen(this.connection);
    }
}
