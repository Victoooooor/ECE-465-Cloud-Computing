package ece465.handler.multi;

import ece465.util.DBconnection;
import ece465.util.fileInfo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class getHash {
    private ConcurrentLinkedQueue<fileInfo> result;
    private DBconnection con;
    public getHash(DBconnection con_in,ConcurrentLinkedQueue<fileInfo> result){
        this.result=result;
        this.con=con_in;
    }
    private class getHash_single implements Runnable{
        private final DataSource dbcp;
        private PreparedStatement stmt;
        private Connection conn=null;
        private ResultSet rs;
        private fileInfo current;
        public getHash_single(DBconnection con_in){
            dbcp=con_in.getDataSource();
        }
        @Override
        public void run() {
            while((current=result.poll())!=null){
                try {
                    conn=dbcp.getConnection();
                    stmt=conn.prepareStatement("select bid from files where fid=?");
                    stmt.setInt(1,current.getFid());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }finally {
                    try {
                        conn.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

            }
        }
    }

}
