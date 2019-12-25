package Commands;

import java.io.Serializable;

public class Err implements Serializable {
    String data;
    public Err(String data){
        this.data = data;
    }

    public void print(){
        System.err.println(data);
    }
}
