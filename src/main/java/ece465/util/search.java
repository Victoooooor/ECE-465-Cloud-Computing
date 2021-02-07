package ece465.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public class search implements Runnable {

    private ConcurrentLinkedQueue<String> queue;
    private ConcurrentLinkedQueue<String> result;
    private final String searchWord;
    private final int nThreads;

    ExecutorService pool;

    public search(ConcurrentLinkedQueue<String> queue, ConcurrentLinkedQueue<String> result, String searchWord, final int nThreads) {
        this.queue = queue;
        //System.out.println(queue.isEmpty());
        this.result = result;
        this.searchWord = searchWord;
        this.nThreads = nThreads;
    }

    public ConcurrentLinkedQueue<String> getResult() {
        return result;
    }

    @Override
    public void run() {
        //System.out.println("Starting search...");
        //single thread
        if (nThreads == 1) {
            while (!queue.isEmpty()) {
                String current = queue.remove();
                if (current.toLowerCase().contains(searchWord.toLowerCase()))
                    result.add(current);
            }
        }
        //multi thread
        else if (nThreads > 1) {
            try {
                synchronized (queue) {
                    while (!queue.isEmpty()) {
                        String current;
                        synchronized (queue) {
                            current = queue.remove();
                            //System.out.println("current" + current);
                        }

                        if (current.toLowerCase().contains(searchWord.toLowerCase()))
                            synchronized (result) {
                                result.add(current);
                            }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
