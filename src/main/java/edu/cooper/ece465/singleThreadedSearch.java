package edu.cooper.ece465;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.*;

public class singleThreadedSearch {

    public static void main(String[] args) {
        Search search = new Search("D:\\", "ECE465testingTarget", 1);

        LocalTime start = LocalTime.now();

        search.run();

        LocalTime end = LocalTime.now();

        System.out.println("Run time: " + Duration.between(start,end).toSeconds() + " s");
    }
}
