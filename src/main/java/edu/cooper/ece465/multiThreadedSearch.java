package edu.cooper.ece465;

import java.util.*;
import java.io.File;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class multiThreadedSearch {

    private ConcurrentLinkedQueue<File> queue;
    private ConcurrentLinkedQueue<String> result;
    private String searchWord;
    private String homeDir;
    private final int nThreads;

    ExecutorService pool;

    public multiThreadedSearch(String homeDir, String searchWord, final int nThreads) {
        this.homeDir = homeDir;
        this.searchWord = searchWord;
        this.nThreads = nThreads;
        queue = new ConcurrentLinkedQueue<File>();
        result = new ConcurrentLinkedQueue<String>();

        pool = Executors.newFixedThreadPool(nThreads);
        queue.add(new File(homeDir));
    }

    public void startSearch() throws Exception {
        for (int i = 0; i < nThreads; i++) {
            pool.execute(new Search(queue, result, searchWord, nThreads));
            System.out.println("Thread #" + (i + 1) + " has been started");
            Thread.sleep(10);
        }
        pool.shutdown();
        pool.awaitTermination(00, TimeUnit.SECONDS);
    }

    public ConcurrentLinkedQueue<String> getResult() {
        return result;
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a starting directory: ");
        String homeDir = sc.nextLine();
        System.out.print("Enter search keyword: ");
        String searchWord = sc.nextLine();
        System.out.print("Enter number of threads: ");
        final int nTheards = sc.nextInt();

        LocalTime start = LocalTime.now();

        multiThreadedSearch search = new multiThreadedSearch(homeDir, searchWord, nTheards);
        search.startSearch();

        LocalTime end = LocalTime.now();

        ConcurrentLinkedQueue<String> result = search.getResult();
        for (String s : result) {
            System.out.println(s);
        }

        System.out.println("Run time: " + Duration.between(start, end).toMillis() + " ms");
    }
}
