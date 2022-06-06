package Model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Records implements Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public int score;

    public Records(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return "Records{" +
                "name=" + String.valueOf(name) +
                ", score=" + String.valueOf(score) +
                '}';
    }

    public void restoreData() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("table.ser");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Records records = (Records) objectInputStream.readObject();
        System.out.println(records);
        objectInputStream.close();
    }
}
