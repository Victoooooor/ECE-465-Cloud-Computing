package ece465.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

public class search implements Runnable {

    Logger LOG = Logger.getLogger(search.class);

    private final ConcurrentLinkedQueue<fileInfo> queue;
    private final ConcurrentLinkedQueue<fileInfo> result;
    private final String searchWord;
    private final int nThreads;

    public search(ConcurrentLinkedQueue<fileInfo> queue, ConcurrentLinkedQueue<fileInfo> result, String searchWord, final int nThreads) {
        LOG.debug("search CONSTRUCTOR");
        this.queue = queue;
        this.result = result;
        this.searchWord = searchWord;
        this.nThreads = nThreads;
        LOG.debug("search CONSTRUCTOR - DONE");
    }

    public ConcurrentLinkedQueue<fileInfo> getResult() {
        return result;
    }

    @Override
    public void run() {
        LOG.debug("search.run()");
        //single thread
        if (nThreads == 1) {
            LOG.debug("search.run() - single-thread");
            while (!queue.isEmpty()) {
                fileInfo current = queue.remove();
                String currentFilename = current.getFilename();
                if (currentFilename.toLowerCase().contains(searchWord.toLowerCase()))
                    result.add(current);
            }
        }
        //multi thread
        else if (nThreads > 1) {
            LOG.debug("search.run() - multi-thread");
            try {
                synchronized (queue) {
                    while (!queue.isEmpty()) {
                        fileInfo current;
                        synchronized (queue) {
                            current = queue.remove();
                        }
                        String currentFilename = current.getFilename();
                        if (currentFilename.toLowerCase().contains(searchWord.toLowerCase())) {
                            synchronized (result) {
                                result.add(current);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LOG.debug("search.run - DONE");
    }
}
