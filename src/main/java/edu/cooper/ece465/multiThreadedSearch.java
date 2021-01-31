package edu.cooper.ece465;

import java.io.File;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class multiThreadedSearch {

    public ConcurrentLinkedQueue<File> queue;
    public ConcurrentLinkedQueue<String> result;
    private String searchWord;
    private String homeDir;
    private final int nThreads;

    ExecutorService pool;

    public multiThreadedSearch(String homeDir, String searchWord, final int nThreads){
        this.homeDir = homeDir;
        this.searchWord = searchWord;
        this.nThreads = nThreads;
        queue = new ConcurrentLinkedQueue<File>();
        result = new ConcurrentLinkedQueue<String>();

        pool = Executors.newFixedThreadPool(nThreads);
        queue.add(new File(homeDir));
    }

    public void startSearch() throws Exception {
        LocalTime start = LocalTime.now();
        for (int i = 0; i < nThreads; i++) {
            pool.execute(new Search(queue, result, searchWord, nThreads));
            System.out.println("Thread #" + (i + 1) + " has been started");
            Thread.sleep(10);
        }
        pool.shutdown();
        pool.awaitTermination(00, TimeUnit.SECONDS);
        LocalTime end = LocalTime.now();
        System.out.println("Run time: " + Duration.between(start, end).toSeconds() + " s");
    }

    public static void main(String[] args) throws Exception {
        multiThreadedSearch search = new multiThreadedSearch("D:\\", "ECE465testingTarget", 20);
        //LocalTime start = LocalTime.now();

        search.startSearch();

        //LocalTime end = LocalTime.now();

        //System.out.println("Run time: " + Duration.between(start,end).toMillis() + " ms");
    }
}
