package ece465.node;

import ece465.service.Json.*;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class client {
    private Socket servers;
    private Path fname;
    public client(){
    }


    private class receiver implements Runnable{
        ConcurrentLinkedQueue<readJson.returnInfo> fetch_queue;
        public receiver(ConcurrentLinkedQueue<readJson.returnInfo> tofetch){
            this.fetch_queue=tofetch;
        }
        @Override
        public void run() {
            readJson.returnInfo current=null;
            Path filename;
            File FF;
            while((current=fetch_queue.poll())!=null){
                filename=Paths.get(current.filename);
                FF = new File("." + File.separator + "client_temp" + File.separator + filename.getFileName());
                try(Socket serv=new Socket(current.ip,current.port);
                    DataInputStream server_in = new DataInputStream(new BufferedInputStream(serv.getInputStream()));
                    DataOutputStream server_out = new DataOutputStream(new BufferedOutputStream(serv.getOutputStream()));
                    FileOutputStream savefile=new FileOutputStream(FF)) {

                    server_out.writeUTF(fetchJsonWriter.generateJson(current));
                    server_out.flush();
                    long fsize=server_in.readLong();
                    byte[] buffer = new byte[8192];
                    int count=0;
                    while (fsize>0&&(count=server_in.read(buffer,0,(int)Math.min(fsize,8192))) > 0) {//this structure is for future expansion, do not simplify
                        savefile.write(buffer,0,count);
                        fsize-=count;
                    }

                } catch (UnknownHostException e) {
                    System.err.println("Error connecting server in receiver");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public String send(Socket target, String content){
        this.servers=target;
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(servers.getInputStream()));
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(servers.getOutputStream()))){
            out.writeUTF(content);
            out.flush();
            System.out.println("Write to server done");
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            this.stop();
        }
        return null;
    }

    public void receive(ArrayList<readJson.returnInfo> tofetch,int num_threads){
        new File("client_temp").mkdirs();
        ConcurrentLinkedQueue<readJson.returnInfo> queueing = new ConcurrentLinkedQueue<>(tofetch);
        if(num_threads==0){
            num_threads = Math.min(Runtime.getRuntime().availableProcessors(), tofetch.size());
        }
        for(int i =0; i<num_threads;i++){
            new Thread(new receiver(queueing)).start();
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
