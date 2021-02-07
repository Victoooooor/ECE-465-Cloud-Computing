package ece465;

import ece465.handler.multi.pipeline;
import ece465.handler.multi.store_consumer;
import ece465.handler.multi.store_producer;
import ece465.handler.single.store;
import ece465.util.DBconnection;

import java.time.Duration;
import java.time.LocalTime;

public class single_store_test {
    public static void main(String[] args) {
        DBconnection DB_con=new DBconnection();
        store WT=new store(DB_con);
        pipeline mypipe=new pipeline();
        store_producer pp;
        if(args.length == 0) {
            pp = new store_producer(".\\", mypipe);
        } else {
            pp = new store_producer(args[0], mypipe);
        }

        LocalTime start = LocalTime.now();
        new Thread(pp).start();
        new Thread(new store_consumer(mypipe,DB_con)).start();
        LocalTime end = LocalTime.now();

        System.out.println("Run time: " + Duration.between(start, end).toMillis() + " ms");
    }
}
