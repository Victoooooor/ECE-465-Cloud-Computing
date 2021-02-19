package ece465.service;

import ece465.handler.multi.pipeline;
import ece465.handler.multi.store_consumer;
import ece465.handler.multi.store_producer;
import ece465.handler.single.store;
import ece465.service.Json.readJson;
import ece465.util.DBconnection;

import java.util.ArrayList;

public class threaded_store {
    public static void store(DBconnection DB_con, ArrayList<readJson.returnInfo> flist,Integer num_threads) {
        ArrayList<String> dir=new ArrayList<>();
        flist.forEach(f->{dir.add(f.filename);});
        store WT=new store(DB_con);
        pipeline mypipe=new pipeline();
        store_producer pp=new store_producer(dir,mypipe);
        new Thread(pp).start();
        if(num_threads==0){
            num_threads = Math.max(Runtime.getRuntime().availableProcessors(), 1);
            num_threads=Math.min(num_threads, flist.size());
        }
        for(int i=0;i<num_threads;i++){
            new Thread(new store_consumer(mypipe,DB_con)).start();
        }
    }
}
