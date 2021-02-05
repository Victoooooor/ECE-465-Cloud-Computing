package ece465.handler.multi;

import ece465.util.DBconnection;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
public class store_consumer implements Runnable{
    private final pipeline mypipe;
    private final DataSource dbcp;
    private FileInputStream inputStream;
    private BufferedInputStream BuffedIn;
    private String temp;
    private PreparedStatement stmt;
    private Connection conn;
    private final int thrsh=3;
    private int count;
    public store_consumer(pipeline inpipe, DBconnection con_in){
        mypipe=inpipe;
        dbcp=con_in.getDataSource();
        count=0;
    }

    @Override
    public void run() {
        while((temp=mypipe.fetch())!=""){
            try {
                inputStream = new FileInputStream(temp);
                BuffedIn = new BufferedInputStream(inputStream);
                conn=dbcp.getConnection();
                while(true){
                    try{
                        stmt = conn.prepareStatement("INSERT INTO files (fname, stored) VALUES (?, ?);");
                        count=0;
                        stmt.setString(1,temp);
                        stmt.setBinaryStream(2,BuffedIn);
                        try {
                            stmt.executeUpdate();
                        } catch (SQLException e) {
                            System.err.println("Execution Error.");
                            e.printStackTrace();
                        }
                        break;
                    } catch (SQLException e) {
                        System.err.println("Connection Error.");
                        e.printStackTrace();
                        if(count>=thrsh)    System.exit(-1);
                        count++;
                    }
                }
            }catch (Exception e) {
                System.err.println("From String -> InputStream Error.");
                e.printStackTrace();
            }finally {
                try {
                    inputStream.close();
                    BuffedIn.close();
                    conn.close();
                } catch (IOException | SQLException e) {}
            }

        }
    }
}
