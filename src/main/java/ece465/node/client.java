package ece465.node;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class client {
    Socket servers;

    public client(String ip, int port){
        try {
            servers=new Socket(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void start(){
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new
                    InputStreamReader(servers.getInputStream()));
            out = new
                    PrintWriter(servers.getOutputStream(), true);
            out.println("this should be a json file");
            System.out.println("Write to server done");
            System.out.println("Response from server: "+in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
