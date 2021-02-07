package ece465.handler.single;

import ece465.util.DBconnection;

import java.util.concurrent.ConcurrentLinkedQueue;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class get implements Runnable {
    DataSource dbcp;
    public ConcurrentLinkedQueue<String> queue;

    get(ConcurrentLinkedQueue<String> queue){
        dbcp=  DBconnection.getDataSource();
        this.queue = queue;
    }

    public ConcurrentLinkedQueue<String> singleThreadRun(ConcurrentLinkedQueue<String> queue){
        Connection conn = null;
        try {
            conn = dbcp.getConnection();
        } catch (SQLException err) {
            System.err.println("Connection Error.");
            err.printStackTrace();
        }
        String query = "SELECT fname FROM files;";
        assert conn != null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(query);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        try {
            ResultSet data = stmt.executeQuery(query);
            while(data.next()){
                    queue.add(data.getString("fname"));
            }
            //System.out.println(queue.isEmpty());
        } catch (SQLException throwable) {
            System.err.println("Execution Error.");
            throwable.printStackTrace();
        }
        return queue;
    }

    @Override
    public void run() {
        Connection conn = null;
        try {
            conn = dbcp.getConnection();
        } catch (SQLException err) {
            System.err.println("Connection Error.");
            err.printStackTrace();
        }
        String query = "SELECT fname FROM files;";
        assert conn != null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(query);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        try {
            ResultSet data = stmt.executeQuery(query);
            while(data.next()){
                synchronized (queue) {
                    queue.add(data.getString("fname"));
                }
            }
            //System.out.println(queue.isEmpty());
        } catch (SQLException throwable) {
            System.err.println("Execution Error.");
            throwable.printStackTrace();
        }
    }
}
