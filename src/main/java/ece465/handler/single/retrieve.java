package ece465.handler.single;

import javax.sql.DataSource;
import java.util.concurrent.*;

import org.apache.log4j.Logger;

import ece465.util.DBconnection;
import ece465.util.fileInfo;
import ece465.util.search;

public class retrieve {

    Logger LOG = Logger.getLogger(retrieve.class);

    DataSource dbcp;
    private ConcurrentLinkedQueue<fileInfo> queue;
    private ConcurrentLinkedQueue<fileInfo> result;
    private int nThreads;
    ExecutorService pool;

    public void startSearch(String searchWord,int threads) throws Exception {
        queue.clear();
        result.clear();
        this.nThreads=threads;
        if(nThreads == 0){
            int processors = Runtime.getRuntime().availableProcessors();
            System.out.println("Processors = " + processors);
            pool = Executors.newFixedThreadPool(processors);
        }else {
            pool = Executors.newFixedThreadPool(nThreads);
        }
        if(this.nThreads == 1){

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

            /*
            for(fileInfo f : result){
                System.out.println(f.getFid() + "\t" + f.getFilename());
            }
             */

        }
    }

    public retrieve(DBconnection com_in){
        LOG.debug("retrieve CONSTRUCTOR");
        dbcp = com_in.getDataSource();
        this.queue = new ConcurrentLinkedQueue<>();
        this.result = new ConcurrentLinkedQueue<>();
        LOG.debug("retrieve CONSTRUCT0R - DONE");
    }

    public ConcurrentLinkedQueue<fileInfo> getResult(){
        return result;
    }

}
