package ece465.node;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class client {
    Socket servers;
    DataInputStream in;
    DataOutputStream out;

    public client(String ip, int port){
        try {
            servers=new Socket(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void send(String content){

        try {
            in = new DataInputStream(new BufferedInputStream(servers.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(servers.getOutputStream()));
            out.writeUTF(content);
            out.flush();
            System.out.println("Write to server done");
            System.out.println("Response from server: "+in.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void stop(){
        try {
            servers.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
