package ece465.handler.single;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

import ece465.util.DBconnection;
import ece465.util.fileInfo;
import ece465.util.search;

public class retrieve {

    Logger LOG = Logger.getLogger(retrieve.class);

    DataSource dbcp;
    private ConcurrentLinkedQueue<fileInfo> queue;
    private ConcurrentLinkedQueue<fileInfo> result;
    private final int nThreads;
    ExecutorService pool;

    public void startSearch(String searchWord) throws Exception {
        LOG.debug("Clearing queue");
        queue.clear();
        result.clear();
        LOG.debug("Clearing result - DONE");

        if(nThreads == 1){

            get g = new get(queue);
            this.queue = g.singleThreadRun(queue);

            search sc = new search(queue, result, searchWord, 1);
            sc.run();
            result = sc.getResult();
            LOG.debug("Getting result from the search() object - DONE");
        } else if(nThreads > 1) {
            LOG.debug("Multi-thread - retrieve");

            LOG.debug("Starting " + nThreads + " threads");
            for (int i = 0; i < nThreads; i++) {
                if (i == 0) {
                    pool.execute(new get(queue));
                } else {
                    pool.execute(new search(queue, result, searchWord, nThreads - 1));
                }
                System.out.println("Thread #" + (i + 1) + " has been started");
                Thread.sleep(5);
            }
            LOG.debug("ALL " + nThreads + "have been started");

            pool.shutdown();
            pool.awaitTermination(0, TimeUnit.SECONDS);

        }
    }

    public retrieve(DBconnection com_in, int nThreads){
        LOG.debug("retrieve CONSTRUCTOR");
        this.nThreads = nThreads;
        dbcp = com_in.getDataSource();
        this.queue = new ConcurrentLinkedQueue<>();
        this.result = new ConcurrentLinkedQueue<>();
        pool = Executors.newFixedThreadPool(nThreads);
        LOG.debug("retrieve CONSTRUCT0R - DONE");
    }

    public ConcurrentLinkedQueue<fileInfo> getResult(){
        return result;
    }

}
