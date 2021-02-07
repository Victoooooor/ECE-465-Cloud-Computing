package ece465;

import ece465.util.DBconnection;
import ece465.handler.single.store;
import ece465.handler.multi.*;
public class threaded_store_hash_test {
    public static void main(String[] args) {
        DBconnection DB_con=new DBconnection();
        store WT=new store(DB_con);
        pipeline mypipe=new pipeline();
        store_producer pp=new store_producer(".\\",mypipe);
        new Thread(pp).start();
        new Thread(new store_consumer(mypipe,DB_con)).start();
        new Thread(new store_consumer(mypipe,DB_con)).start();
        new Thread(new store_consumer(mypipe,DB_con)).start();
        new Thread(new store_consumer(mypipe,DB_con)).start();
        new Thread(new store_consumer(mypipe,DB_con)).start();
        new Thread(new store_consumer(mypipe,DB_con)).start();
    }
}
