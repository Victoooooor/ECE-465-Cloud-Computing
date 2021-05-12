package ece465;
import ece465.node.*;
import ece465.service.Json.*;

import java.awt.print.PrinterGraphics;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class client_test {
    public static void main(String[] args) {
        ece465.node.client c= null;
        c = new ece465.node.client();
        boolean exit = false;
        while(!exit) {
            String search_result = null;
            Scanner sc = new Scanner(System.in);

            System.out.println("Please select an action to perform: ");
            System.out.println("1 - Store files to database.");
            System.out.println("2 - Search files from database");
            System.out.println("3 = Exit");

            Integer option = sc.nextInt();

            switch (option){
                case 1:
                    System.out.println("Please enter pathname for the files you want to store, " +
                            "separated by newline character (one file per line), press enter again to finish.");
                    ArrayList<String> listing=new ArrayList<>();
                    listing.clear();
                    String fp = null;
                    fp = sc.nextLine();
                    while(!(fp = sc.nextLine()).isBlank()) {
                        listing.add(fp);
                    }
                    System.out.println(listing);
                    try {
                        c.send(new Socket("0.0.0.0", 4666), storerequestWriter.generateJson(listing));
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Please enter a search word: ");
                    String searchword = scanner.nextLine();
                    System.out.println(searchword);
                    try {
                        search_result = c.send(new Socket("0.0.0.0", 4666), searchJsonWriter.generateJson(searchword));
                        System.out.println("search result: " + search_result);
                        ArrayList<readJson.returnInfo> returned = readJson.read(search_result);

                        System.out.println("Pleas select the files you want to retrieve, " +
                                "separated by newline character (one file per line), press enter again to finish.");
                        for (int i = 0; i < returned.size(); i++) {
                            System.out.println((i + 1) + " - " + returned.get(i).filename);
                        }
                        String fn;
                        ArrayList<readJson.returnInfo> fetching = new ArrayList<>();
                        fn = scanner.nextLine();
                        int n = Integer.parseInt(fn);
                        fetching.add(returned.get(n-1));
                        while (!(fn = scanner.nextLine()).isBlank()) {
                            n = Integer.parseInt(fn);
                            fetching.add(returned.get(n - 1));
                        }
                        for(readJson.returnInfo a: fetching){
                            System.out.println(a.filename);
                        }
                        c.receive(fetching, 0);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    exit = true;
                    break;
            }
            /*
            try {
                //ArrayList<String> listing=new ArrayList<>();
                //listing.add(".\\");
                //c.send(new Socket("0.0.0.0",4666), storerequestWriter.generateJson(listing));
                search_result = c.send(new Socket("0.0.0.0", 4666), searchJsonWriter.generateJson(".j"));
                System.out.println("search result: " + search_result);
                ArrayList<readJson.returnInfo> returned = readJson.read(search_result);
                c.receive(returned, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
             */
        }
        c.stop();
    }
}