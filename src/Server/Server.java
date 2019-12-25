package Server;

import Commands.Login;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket server;
    UserContainer uc;
    public Server(int port, UserContainer uc){
        this.uc = uc;
        try{
            server = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        System.out.println("MAIN: Server.Server started");
        while(true) {
            try {
                System.out.println("MAIN: Waiting for client");
                Connection connection = new Connection(server.accept());
                System.out.println("MAIN: client accepted");
                Object loginInfoObj = connection.listenForObject();
                if (loginInfoObj instanceof Login) {
                    Login loginInfo = (Login) loginInfoObj;
                    uc.handleUser(loginInfo, connection);
                } else {
                    connection.socket.close();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
