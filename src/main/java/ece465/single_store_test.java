package ece465;

import ece465.handler.multi.pipeline;
import ece465.handler.multi.store_consumer;
import ece465.handler.multi.store_producer;
import ece465.handler.single.store;
import ece465.util.DBconnection;

public class single_store_test {
    public static void main(String[] args) {
        DBconnection DB_con=new DBconnection();
        store WT=new store(DB_con);
        pipeline mypipe=new pipeline();
        store_producer pp=new store_producer(".\\",mypipe);
        new Thread(pp).start();
        new Thread(new store_consumer(mypipe,DB_con)).start();
    }
}
