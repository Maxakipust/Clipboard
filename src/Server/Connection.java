package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.oos = new ObjectOutputStream(socket.getOutputStream());
    }

    public void sendObject(Object obj) throws IOException {
        oos.writeObject(obj);
    }
    public Object listenForObject() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }
}
