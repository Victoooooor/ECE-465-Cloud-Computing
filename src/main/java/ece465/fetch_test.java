package ece465;
import java.io.File;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import ece465.handler.multi.fetch_multi;
import ece465.handler.multi.store_consumer;
import ece465.handler.single.retrieve;
import ece465.util.DBconnection;
import ece465.util.fileInfo;

public class fetch_test {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a search word: ");
        String searchWord = sc.nextLine();
        System.out.print("Enter number of threads: ");
        final int nThreads = sc.nextInt();

        LocalTime start = LocalTime.now();

        DBconnection DB_con = new DBconnection();
        retrieve RT = new retrieve(DB_con);
        RT.startSearch(searchWord, nThreads);
        ConcurrentLinkedQueue<fileInfo> result=RT.getResult();
        new File("temp").mkdirs();
        new Thread(new fetch_multi(DB_con,result)).start();
        new Thread(new fetch_multi(DB_con,result)).start();
        new Thread(new fetch_multi(DB_con,result)).start();
        new Thread(new fetch_multi(DB_con,result)).start();
        LocalTime end = LocalTime.now();

        System.out.println("Run time: " + Duration.between(start, end).toMillis() + " ms");

    }
}
