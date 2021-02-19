package ece465.node;

import java.util.ArrayList;

public class peerlist {//to implement node to node connection, consensus, central node lookup
    private ArrayList<peer> nodelist;
    public class peer{
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
}
