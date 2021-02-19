package ece465.node;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import ece465.handler.multi.getHash;
import ece465.handler.single.retrieve;
import ece465.service.Json.*;
import ece465.util.DBconnection;
import ece465.util.fileInfo;

public class server {
    private static ServerSocket server;
    private DBconnection DB_con;
    private retrieve RT;
    private ConcurrentLinkedQueue<fileInfo> result;
    private getHash HAS;
    public server(int portnum){
        try {
            server = new ServerSocket(portnum);
            DB_con = new DBconnection();
            RT = new retrieve(DB_con);
            HAS = new getHash(DB_con);
        } catch (IOException e) {
            System.err.println("Server port non available: "+portnum);
            e.printStackTrace();
        }
    }

    private class client_handler implements Runnable{
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
                read.forEach(Info->{
                    System.out.println(Info.action);
                    switch(Info.action){
                        case 0://search by filename
                            System.out.println("Search file: "+Info.filename);
                            try {
                                RT.startSearch(Info.filename,0);
                                result=RT.getResult();
                                System.out.println("Search Done");
                                out.writeUTF(searchReturnJsonWriter.generateJson(HAS.get(result), server.getInetAddress().toString().split("/")[1], server.getLocalPort()));
                                out.flush();
                            } catch (Exception e) {
                                System.err.println("Server Processing Search error");
                                e.printStackTrace();
                            }
                            break;
                        case 1://only client
                            ;
                            break;
                        case 2://fetch
                            System.out.println("Fetching: ");

                            break;
                        default:
                            ;
                            break;
                    }
                    System.out.println("Done processing request");
                });

            } catch (Exception e) {
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