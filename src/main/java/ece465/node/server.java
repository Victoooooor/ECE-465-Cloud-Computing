package ece465.node;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import ece465.service.Json.*;
public class server {
    private static ServerSocket server;
    public server(int portnum){
        try {
            server = new ServerSocket(portnum);
        } catch (IOException e) {
            System.err.println("Server port non available: "+portnum);
            e.printStackTrace();
        }
    }

    private static class client_handler implements Runnable{
        private Socket client;
        private DataInputStream in = null;
        private DataOutputStream out = null;

        public client_handler(Socket client){
            this.client=client;
        }
        @Override
        public void run() {

            try{
                in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
                out = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
            } catch (IOException e) {
                System.err.println("client get Stream failed");
                System.exit(-1);
            }
            try {
                System.out.println("reading");
                String fromclient=in.readUTF();
                System.out.println("From Client text: "+ fromclient);
                ArrayList<readJson.returnInfo> read = readJson.read(fromclient);
                readJson.returnInfo Info=read.get(0);
                System.out.println(Info.filename);
                System.out.println("Done reading");
                out.writeUTF("this is server talking");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
                out.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(){
        while(true){
            client_handler ch;
            try {
                System.out.println("waiting");
                ch=new client_handler(server.accept());
                Thread T=new Thread(ch);
                T.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void stop(){
        try {
             server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}