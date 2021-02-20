package ece465.node;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class peerlist {//to implement node to node connection, consensus, central node lookup
    private ArrayList<peer> nodelist;

    private class peer{
        private String ip;
        private Integer port;
        public peer(String ip, Integer port){
            this.ip=ip;
            this.port=port;
        }
        public String getip(){
            return this.ip;
        }
        public Integer getport(){
            return this.port;
    }
    }

    private class subclient implements Runnable{
        ConcurrentLinkedQueue<peer> con_list;
        public subclient(ConcurrentLinkedQueue<peer> con_list){
            this.con_list=con_list;
        }
        @Override
        public void run() {

        }
    }

    public void register(String ip, Integer port){
        nodelist.add(new peer(ip,port));
    }
    public void broadcast (String message){
        ConcurrentLinkedQueue<peer> con_list=new ConcurrentLinkedQueue<>(nodelist);

        nodelist.forEach(node->{

        });
    }
}
