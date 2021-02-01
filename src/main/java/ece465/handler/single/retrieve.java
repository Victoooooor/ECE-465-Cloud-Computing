package ece465.handler.single;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ece465.util.DBconnection;
import ece465.util.search;

public class retrieve {
    DataSource dbcp;
    private ConcurrentLinkedQueue<String> queue;
    private ConcurrentLinkedQueue<String> result;
    private String searchWord;
    private int nThreads;
    ExecutorService pool;

    public void startSearch(String searchWord, int nThreads) throws Exception {
        this.queue = queue;
        this.result = result;
        this.searchWord = searchWord;
        this.nThreads = nThreads;

        if(nThreads == 1){
            get g = new get(queue);
            this.queue = g.singleThreadRun(queue);
            search sc = new search(queue, result, searchWord, 1);
            sc.run();
            result = sc.getResult();
        } else if(nThreads > 1) {
            for (int i = 0; i < nThreads; i++) {
                if (i == 0) {
                    pool.execute(new get(queue));
                } else {
                    pool.execute(new search(queue, result, searchWord, nThreads - 1));
                }
                System.out.println("Thread #" + (i + 1) + " has been started");
                Thread.sleep(100);
            }
            pool.shutdown();
            pool.awaitTermination(0, TimeUnit.SECONDS);
        }
        for(String s : result){
            System.out.println(s);
        }
        //System.out.println(result.isEmpty());
    }

    public retrieve(DBconnection con_in, int nThreads){
        dbcp=  DBconnection.getDataSource();
        this.queue = new ConcurrentLinkedQueue<>();
        this.result = new ConcurrentLinkedQueue<>();
        pool = Executors.newFixedThreadPool(nThreads);
    }

}
