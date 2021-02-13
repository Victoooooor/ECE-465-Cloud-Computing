package ece465.node;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
        public client_handler(Socket client){
            this.client=client;
        }
        @Override
        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            try{
                in = new BufferedReader(new
                        InputStreamReader(client.getInputStream()));
                out = new
                        PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                System.err.println("client get Stream failed");
                System.exit(-1);
            }
            try {
                System.out.println("reading");
                String fromclient=in.readLine();
                System.out.println("From Client text: "+ fromclient);
                System.out.println("Done reading");
                out.println("this is server talking");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(){
        while(true){
            client_handler ch;
            try {
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
