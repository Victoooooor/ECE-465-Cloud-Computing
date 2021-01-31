package edu.cooper.ece465;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.concurrent.*;

public class singleThreadedSearch {

    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        System.out.print("Enter a starting directory: ");
        String homeDir= sc.nextLine();
        System.out.print("Enter search keyword: ");
        String searchWord= sc.nextLine();

        Search search = new Search(homeDir, searchWord, 1);

        LocalTime start = LocalTime.now();
        search.run();

        LocalTime end = LocalTime.now();

        for(String s: search.getResult()){
            System.out.println(s);
        }

        System.out.println("Run time: " + Duration.between(start,end).toSeconds() + " s");
    }
}
