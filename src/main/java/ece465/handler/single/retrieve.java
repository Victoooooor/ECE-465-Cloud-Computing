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
        LOG.debug("Clearing queue - DONE");

        LOG.debug("Clearing result");
        result.clear();
        LOG.debug("Clearing result - DONE");

        if(nThreads == 1){
            LOG.debug("Single-thread - retrieve");

            LOG.debug("Creating a new get() object");
            get g = new get(queue);
            LOG.debug("Creating a new get() object");

            LOG.debug("Getting fid and filename from database");
            this.queue = g.singleThreadRun(queue);
            LOG.debug("Getting fid and filename from database - DONE");

            LOG.debug("Creating new search() object");
            search sc = new search(queue, result, searchWord, 1);
            LOG.debug("Creating new search() object - DONE");

            LOG.debug("Single-thread file search - STARTED");
            sc.run();
            LOG.debug("Single-thread file search - DONE");

            LOG.debug("Getting result from the search() object");
            result = sc.getResult();
            LOG.debug("Getting result from the search() object - DONE");
        } else if(nThreads > 1) {
            LOG.debug("Multi-thread - retrieve");

            LOG.debug("Starting " + nThreads + " threads");
            for (int i = 0; i < nThreads; i++) {
                if (i == 0) {
                    LOG.debug("Starting thread 1 to execute get.run()");
                    pool.execute(new get(queue));
                    LOG.debug("Thread 1 - STARTED");
                } else {
                    LOG.debug("Starting thread " + i+1 + " to execute search.run()");
                    pool.execute(new search(queue, result, searchWord, nThreads - 1));
                    LOG.debug("Thread " + i+1 + " = STARTED");
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
