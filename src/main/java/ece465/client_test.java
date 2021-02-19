package ece465;
import ece465.node.*;
import ece465.service.Json.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class client_test {
    public static void main(String[] args) {
        client c= null;
        c = new client();
        String search_result= null;
        try {
            //ArrayList<String> listing=new ArrayList<>();
            //listing.add(".\\");
            //c.send(new Socket("0.0.0.0",4666), storerequestWriter.generateJson(listing));
            search_result = c.send(new Socket("0.0.0.0",4666), searchJsonWriter.generateJson(".j"));
            System.out.println("search result: "+search_result);
            ArrayList<readJson.returnInfo> returned = readJson.read(search_result);
            c.receive(returned,0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        c.stop();
    }
}