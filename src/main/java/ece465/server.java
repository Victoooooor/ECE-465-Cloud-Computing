package ece465;
import ece465.node.*;
public class server {
    public static void main(String[] args) {
        ece465.node.server s= new ece465.node.server(4567);
        s.start();

    }
}
