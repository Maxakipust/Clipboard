package Commands;

import java.io.Serializable;

public class Message implements Serializable {
    public String data;
    public Message(String data){
        this.data = data;
    }
}


