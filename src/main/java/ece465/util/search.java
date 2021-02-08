package ece465.util;

import java.util.concurrent.ConcurrentLinkedQueue;

public class search implements Runnable {

    private final ConcurrentLinkedQueue<fileInfo> queue;
    private final ConcurrentLinkedQueue<fileInfo> result;
    private final String searchWord;
    private final int nThreads;

    public search(ConcurrentLinkedQueue<fileInfo> queue, ConcurrentLinkedQueue<fileInfo> result, String searchWord, final int nThreads) {
        this.queue = queue;
        //System.out.println(queue.isEmpty());
        this.result = result;
        this.searchWord = searchWord;
        this.nThreads = nThreads;
    }

    public ConcurrentLinkedQueue<fileInfo> getResult() {
        return result;
    }

    @Override
    public void run() {
        //System.out.println("Starting search...");
        //single thread
        if (nThreads == 1) {
            while (!queue.isEmpty()) {
                fileInfo current = queue.remove();
                String currentFilename = current.getFilename();
                if (currentFilename.toLowerCase().contains(searchWord.toLowerCase()))
                    result.add(current);
            }
        }
        //multi thread
        else if (nThreads > 1) {
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

    }
}