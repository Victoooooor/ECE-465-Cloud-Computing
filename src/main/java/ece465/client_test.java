package ece465;
import ece465.node.*;
public class client_test {
    public static void main(String[] args) {
        client c= new client("127.0.0.1",4666);
        c.start();
    }
}