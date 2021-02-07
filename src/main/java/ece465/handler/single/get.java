package ece465.handler.single;

import ece465.util.DBconnection;
import ece465.util.fileInfo;

import java.util.concurrent.ConcurrentLinkedQueue;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class get implements Runnable {
    DataSource dbcp;
    public final ConcurrentLinkedQueue<fileInfo> queue;

    get(ConcurrentLinkedQueue<fileInfo> queue){
        this.dbcp = DBconnection.getDataSource();
        this.queue = queue;
    }

    public ConcurrentLinkedQueue<fileInfo> singleThreadRun(ConcurrentLinkedQueue<fileInfo> queue) {
        Connection conn = null;
        try {
            conn = dbcp.getConnection();
        } catch (SQLException err) {
            System.err.println("Connection Error.");
            err.printStackTrace();
        }
        String query = "SELECT fid, fname FROM files;";
        assert conn != null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(query);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        try {
            assert stmt != null;
            ResultSet data = stmt.executeQuery(query);
            while(data.next()){
                fileInfo f = new fileInfo(data.getInt("fid"), data.getString("fname"));
                queue.add(f);
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
        String query = "SELECT fid, fname FROM files;";
        assert conn != null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(query);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        try {
            assert stmt != null;
            ResultSet data = stmt.executeQuery(query);
            while(data.next()){
                synchronized (queue) {
                    fileInfo f = new fileInfo(data.getInt("fid"), data.getString("fname"));
                    queue.add(f);
                }
            }
            //System.out.println(queue.isEmpty());
        } catch (SQLException throwable) {
            System.err.println("Execution Error.");
            throwable.printStackTrace();
        }
    }
}
