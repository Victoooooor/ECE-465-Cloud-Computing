package ece465.handler.multi;

import ece465.util.DBconnection;
import ece465.handler.single.hash;
import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;

import org.apache.commons.io.IOUtils;
public class store_consumer implements Runnable{
    private final pipeline mypipe;
    private final DataSource dbcp;
    private FileInputStream inputStream;
    private BufferedInputStream BuffedIn;
    private String temp;
    private String SHA;
    private PreparedStatement stmt;
    private Connection conn;
    private final int thrsh=3;
    private int count;
    private hash ha;
    ResultSet rs;
    public store_consumer(pipeline inpipe, DBconnection con_in){
        mypipe=inpipe;
        dbcp=con_in.getDataSource();
        count=0;
        ha = new hash();
    }

    @Override
    public void run() {
        while((temp=mypipe.fetch())!=""){
            try {
                SHA=ha.gethash(temp);
                inputStream = new FileInputStream(temp);
                BuffedIn = new BufferedInputStream(inputStream);
                conn=dbcp.getConnection();
                while(true){
                    try{
                        stmt = conn.prepareStatement("INSERT INTO files (fname, stored) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
                        count=0;
                        stmt.setString(1,temp);
                        stmt.setBinaryStream(2,BuffedIn);
                        try {
                            stmt.executeUpdate();
                            rs=stmt.getGeneratedKeys();
                            if(rs.next()){
                                int fid=rs.getInt(1);//get fid for future expansion
                                stmt=conn.prepareStatement("INSERT INTO blocks (curr_hash, fid) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
                                stmt.setString(1,SHA);
                                stmt.setInt(2,fid);
                                stmt.executeUpdate();
                                rs=stmt.getGeneratedKeys();
                                if(rs.next()){
                                    int bid=rs.getInt(1);
                                    stmt=conn.prepareStatement("UPDATE files SET bid=? WHERE fid=?;");
                                    stmt.setInt(1,bid);
                                    stmt.setInt(2,fid);
                                    stmt.executeUpdate();
                                }
                            }
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
