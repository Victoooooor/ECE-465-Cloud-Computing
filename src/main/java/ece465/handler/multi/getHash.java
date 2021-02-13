package ece465.handler.multi;

import ece465.util.DBconnection;
import ece465.util.fileInfo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class getHash {
    private ConcurrentLinkedQueue<fileInfo> result;
    private ArrayList<fileInfo> result_hash;
    private DBconnection con;
    public getHash(DBconnection con_in){
        this.result=null;
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
                    rs=stmt.executeQuery();
                    if(rs.next()){
                        int bid=rs.getInt(1);
                        stmt=conn.prepareStatement("select curr_hash from blocks where fid=?");
                        stmt.setInt(1,bid);
                        rs= stmt.executeQuery();
                        if(rs.next()){
                            String cur_hash=rs.getString(1);
                            current.setHash(cur_hash);
                        }
                    }
                    result_hash.add(current);
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

    public ArrayList<fileInfo> get(ConcurrentLinkedQueue<fileInfo> result){
        this.result=result;
        this.result_hash=new ArrayList<fileInfo>();
        int processors = Runtime.getRuntime().availableProcessors();
        ArrayList<Thread> threadList=new ArrayList<Thread>();
        for(int i=0;i<processors;i++){
            Thread T=new Thread(new getHash_single(con));
            threadList.add(T);
            T.start();
        }
        threadList.forEach(a-> {
            try {
                a.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return result_hash;
    }


}
