package ece465;
import java.io.*;
import java.sql.SQLException;
import java.util.Iterator;

import ece465.util.connection;
import ece465.handler.single.store;
public class main {
    public static void main(String[] args) {
        connection DB_con=new connection();
        store WT=new store(DB_con);
        String directory ="C:\\Users\\victo\\IdeaProjects\\BlockChain FileSystem\\jetbrains-toolbox-1.19.7784.exe";
        File dir = new File(directory);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                System.out.println(child.getAbsolutePath());
            }
        } else {
            try {
                WT.add(dir);
            } catch (SQLException | IOException e) {
                System.err.println("adding to sql Error.");
                e.printStackTrace();
            }
        }
    }
}
