package Client;

import Commands.Err;
import Commands.Login;
import Commands.Message;

import java.awt.datatransfer.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.awt.Toolkit;

public class Client implements Runnable{
    Socket s;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    public Client(String ip, int port, String username, String password) throws IOException {
        s = new Socket(ip, port);
        System.out.println("CLIENT: connected");
        oos = new ObjectOutputStream(s.getOutputStream());
        Login l = new Login(username, password);
        oos.writeObject(l);
        System.out.println("CLIENT: sent login info");
        ois = new ObjectInputStream(s.getInputStream());
        Thread thread = new Thread(this);
        thread.start();

        Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(new FlavorListener() {
            @Override
            public void flavorsChanged(FlavorEvent e) {
                String data = null;
                try {
                    data = (String) Toolkit.getDefaultToolkit()
                            .getSystemClipboard().getData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                try {
                    oos.writeObject(new Message(data));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        while(true){
            Object input = null;
            try {
                input = ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(input instanceof Message){
                Message message = (Message) input;
                System.out.print("CLIENT: received data: ");
                System.out.println(message.data);
                setClipboard(message.data);

            }else if(input instanceof Err){
                Err error = (Err) input;
                error.print();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Client c = new Client("localhost", 3000, "Admin", "Admin");
    }

    public static void setClipboard(String clipboardData){
        StringSelection stringSelection = new StringSelection(clipboardData);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(true){
            String line = scanner.nextLine();
            try {
                oos.writeObject(new Message(line));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
