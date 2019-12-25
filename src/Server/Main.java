package Server;

public class Main {
    public static void main(String[] args) {
        UserContainer uc = new UserContainer();
        Server s = new Server(3000, uc);
        s.start();
    }
}
