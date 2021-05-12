package ece465.node;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class peerlist {//to implement node to node connection, consensus, central node lookup
    public ArrayList<peer> nodelist;
    public peerlist(ArrayList<peer> copy){
        this.nodelist=copy;
    }
    public peerlist(){
        this.nodelist=new ArrayList<>();
    }
    private class peer{
        protected String ip;
        protected Integer port;
        public peer(String ip, Integer port){
            this.ip=ip;
            this.port=port;
        }
    }

    private class subclient implements Runnable{
        ConcurrentLinkedQueue<peer> con_list;
        ConcurrentLinkedQueue<String> results;
        String message;
        public subclient(ConcurrentLinkedQueue<peer> con_list,ConcurrentLinkedQueue<String> results,String message){
            this.con_list=con_list;
            this.results=results;
            this.message=message;
        }
        @Override
        public void run() {
            peer current;
            while((current=con_list.poll())!=null){
                try(Socket s=new Socket(current.ip, current.port);
                    DataInputStream in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()))){

                    out.writeUTF(this.message);
                    out.flush();
                    results.add(in.readUTF());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void register(String ip, Integer port){
        nodelist.add(new peer(ip,port));
    }
    public ArrayList<String> broadcast (String message,int num_threads){
        ConcurrentLinkedQueue<peer> con_list=new ConcurrentLinkedQueue<>(nodelist);
        ConcurrentLinkedQueue<String> results=new ConcurrentLinkedQueue<>();
        if(num_threads==0){
            num_threads = Math.min(Runtime.getRuntime().availableProcessors(), nodelist.size());
        }
        ArrayList<Thread> threadlist=new ArrayList<>();
        for(int i =0; i<num_threads;i++){
            threadlist.add(new Thread(new subclient(con_list,results,message)));
            threadlist.get(i).start();
        }
        threadlist.forEach(f->{
            try {
                f.join();
            } catch (InterruptedException e) {
                System.err.println("thread.join err");
                e.printStackTrace();
            }
        });
        String[] temp=new String[results.size()];
        return new ArrayList<>(Arrays.asList(results.toArray(temp)));
    }
    public void returnMsg (String ip, Integer port, String message){
        try(Socket s=new Socket(ip, port);
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()))){

            out.writeUTF(message);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void confirmationMsg (String ip, Integer port, String message){
        try(Socket s=new Socket(ip, port);
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()))){

            out.writeUTF(message);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void delete (String ip, Integer port){
        peer p = new peer(ip, port);
        nodelist.remove(p);
    }
}
