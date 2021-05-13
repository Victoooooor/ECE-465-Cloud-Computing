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
        File ff=new File("saved_list.txt");
        if(!ff.exists()){
            System.err.println("No peerlist file, nothing loaded");
            return;
        }
        try(FileReader fr=new FileReader(ff); BufferedReader br=new BufferedReader(fr);){
            StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters
            String line;
            String[] lines;
            while((line=br.readLine())!=null)
            {
                lines=line.split(":");
                nodelist.add(new peer(lines[0],Integer.parseInt(lines[1])));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } {

        }


          //reads the file
          //creates a buffering character input stream

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
                    long start = System.currentTimeMillis();
                    long finish = System.currentTimeMillis();
                    while(finish - start<10000){//timeout in 10s
                        if(in.available()>0){
                            results.add(in.readUTF());
                            System.err.println("adding");
                            break;
                        }
                        Thread.sleep(2);
                        finish=System.currentTimeMillis();
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void register(String ip, Integer port){
        if(ip.equals("0.0.0.0")){
            return;
        }
        nodelist.add(new peer(ip,port));
        for (peerlist.peer peer : nodelist) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("saved_list.txt"));){
                writer.write(peer.ip+":"+peer.port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public ArrayList<String> broadcastbk (String message,int num_threads){
        ConcurrentLinkedQueue<peer> con_list=new ConcurrentLinkedQueue<>(nodelist);
        ConcurrentLinkedQueue<String> results=new ConcurrentLinkedQueue<>();
        if(num_threads==0){
            num_threads = Math.min(Runtime.getRuntime().availableProcessors(), nodelist.size());
        }
        ArrayList<Thread> threadlist=new ArrayList<>();
        for(int i =0; i<num_threads;i++){
            threadlist.add(new Thread(new subclient_bk(con_list,results,message)));
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
        for (peerlist.peer peer : nodelist) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("saved_list.txt"));){
                writer.write(peer.ip+":"+peer.port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class subclient_bk implements Runnable{
        ConcurrentLinkedQueue<peer> con_list;
        ConcurrentLinkedQueue<String> results;
        String message;
        public subclient_bk(ConcurrentLinkedQueue<peer> con_list,ConcurrentLinkedQueue<String> results,String message){
            this.con_list=con_list;
            this.results=results;
            this.message=message;
        }
        @Override
        public void run() {
            System.out.println("con_list.size() = " + con_list.size());
            peer current;
            while((current=con_list.poll())!=null){
                try(Socket s=new Socket(current.ip, current.port);
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()))){

                    out.writeUTF(this.message);
                    out.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
