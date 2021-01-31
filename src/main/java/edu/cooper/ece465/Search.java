package edu.cooper.ece465;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Search implements Runnable {

    private ConcurrentLinkedQueue<File> queue;
    private ConcurrentLinkedQueue<String> result;
    private String searchWord;
    private String homeDir;
    private final int nThreads;

    ExecutorService pool;

    public boolean isRoot(File f) {
        File roots[] = File.listRoots();
        for (File i : roots) {
            if (f.getAbsolutePath().equals(i.getAbsolutePath()))
                return true;
        }
        return false;
    }

    public Search(String homeDir, String searchWord, final int nThreads){
        queue = new ConcurrentLinkedQueue<File>();
        result = new ConcurrentLinkedQueue<String>();
        this.homeDir = homeDir;
        this.searchWord = searchWord;
        this.nThreads = nThreads;
        homeDir = new File(homeDir).getAbsolutePath();
    }
    public Search(ConcurrentLinkedQueue<File> queue, ConcurrentLinkedQueue<String> result, String searchWord, final int nThreads) {
        this.queue = queue;
        this.result = result;
        this.searchWord = searchWord;
        this.nThreads = nThreads;
        this.homeDir = queue.peek().getAbsolutePath();
    }

    @Override
    public void run() {
        System.out.println("Starting search from: " + homeDir);
        //LocalTime start = LocalTime.now();
        //single thread
        if(nThreads == 1){
            File home = new File(homeDir);
            if (!home.isDirectory()) {
                return;
            }
            queue.add(home);
            while (queue.isEmpty() == false) {
                File current = queue.remove();
                if (current.getAbsolutePath().toLowerCase().contains(searchWord.toLowerCase()))
                    result.add(current.getAbsolutePath());
                if (current.isDirectory() || isRoot(current)) {
                    File childern[] = current.listFiles();
                    if(childern != null)
                        for (File c : childern)
                            queue.add(c);
                }
            }
        }
        //multi thread
        else if(nThreads > 1){
            try {
                while (queue.isEmpty() == false) {
                    File current;
                    synchronized (queue) {
                        current = queue.remove();
                    }
                    if (current.getAbsolutePath().toLowerCase()
                            .contains(searchWord.toLowerCase()))
                        synchronized (result) {
                            result.add(current.getAbsolutePath());
                        }
                    if (current.isDirectory() || isRoot(current)) {
                        File childern[] = current.listFiles();
                        if (childern != null)
                            for (File c : childern)
                                synchronized (queue) {
                                    queue.add(c);
                                }
                    }
                    //Thread.sleep(100);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //LocalTime end = LocalTime.now();
        for(String s : result){
            System.out.println(s);
        }

        //System.out.println("Run time: " + Duration.between(start, end).toSeconds() + " s");
    }
}
