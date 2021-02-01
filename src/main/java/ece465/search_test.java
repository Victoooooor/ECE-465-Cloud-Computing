package ece465;
import java.io.*;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Scanner;

import ece465.handler.single.retrieve;
import ece465.util.DBconnection;
import ece465.handler.single.store;
public class search_test {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a search word: ");
        String searchWord = sc.nextLine();
        System.out.print("Enter number of threads: ");
        final int nThreads = sc.nextInt();

        LocalTime start = LocalTime.now();

        DBconnection DB_con=new DBconnection();
        retrieve RT = new retrieve(DB_con, nThreads);
        RT.startSearch(searchWord, nThreads);

        LocalTime end = LocalTime.now();

        System.out.println("Run time: " + Duration.between(start, end).toMillis() + " ms");

    }
}
