package ece465.node;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import ece465.handler.multi.getHash;
import ece465.handler.single.retrieve;
import ece465.service.Json.*;
import ece465.service.threaded_store;
import ece465.util.DBconnection;
import ece465.util.fileInfo;
import ece465.handler.single.fetch;
public class server {
    private static ServerSocket server;
    private DBconnection DB_con;
    private retrieve RT;
    private ConcurrentLinkedQueue<fileInfo> result;
    private ConcurrentLinkedQueue<String> requestID_queue;
    private getHash HAS;
    private peerlist peers;
    public server(int portnum){
        try {
            server = new ServerSocket(portnum);
            DB_con = new DBconnection();
            RT = new retrieve(DB_con);
            HAS = new getHash(DB_con);
            peers=new peerlist();
            requestID_queue = new ConcurrentLinkedQueue<>();
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
                allread:for(readJson.returnInfo Info:read) {
                    System.out.println(Info.action);
                    synchronized (requestID_queue){
                        if(!requestID_queue.contains(Info.requestID)){
                            if(requestID_queue.size() < 128){
                                requestID_queue.add(Info.requestID);
                            } else{
                                synchronized (requestID_queue) {
                                    requestID_queue.poll();
                                    requestID_queue.add(Info.requestID);
                                }
                            }
                        }else {
                            return;
                        }
                    }

                    switch (Info.action) {
                        case 0://search by filename
                            System.out.println("Search file: " + Info.filename);
                            try {
                                RT.startSearch(Info.filename, 0);
                                result = RT.getResult();
                                System.out.println("Search Done");
                                ArrayList<String> netresult=peers.broadcast(fromclient,0);
                                ArrayList<readJson.returnInfo> fromnet=new ArrayList<>();
                                netresult.forEach(f->{
                                    fromnet.addAll(readJson.read(f));
                                });
                                String fromlocal=searchReturnJsonWriter.generateJson(HAS.get(result), server.getInetAddress().toString().split("/")[1], server.getLocalPort());
                                fromnet.addAll(readJson.read(fromlocal));
                                out.writeUTF(readJsonWriter.generateJson(fromnet));
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
                            fetch ft = new fetch(DB_con);
                            ft.getfile(Info.fid, out);
                            break;
                        case 3://save to db
                            threaded_store.store(DB_con, read, 0);
                            out.writeUTF("Write to DB done");
                            out.flush();
                            break allread;
                        case 4://broadcast message
                            System.out.println("peers.nodelist.size() = "+peers.nodelist.size());
                            peers.broadcastbk(fromclient, 0);
                            if(peers.nodelist.size() < 32) {
                                peers.register(read.get(0).ip, read.get(0).port);
                                String message = returnMsgJsonWriter.generateJson(server.getInetAddress().toString().split("/")[1], server.getLocalPort());
                                peers.returnMsg(read.get(0).ip, read.get(0).port,message);
                            }
                            break;
                        case 5://return message
                            if(peers.nodelist.size() < 8) {
                                peers.register(read.get(0).ip, read.get(0).port);
                            }else{
                                String message = confirmationMsgJsonWriter.generateJson(server.getInetAddress().toString().split("/")[1], server.getLocalPort());
                                peers.confirmationMsg(server.getInetAddress().toString().split("/")[1], server.getLocalPort(),message);
                            }
                            break;
                        case 6://delete
                            peers.delete(read.get(0).ip, read.get(0).port);
                            break;
                        default:
                            ;
                            break;
                    }
                    System.out.println("Done processing request");
                }

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