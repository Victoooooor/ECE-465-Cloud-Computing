package ece465.handler.single;

import ece465.util.DBconnection;
import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class fetch {
    private final DataSource dbcp;
    private PreparedStatement stmt;
    private Connection conn=null;
    private ResultSet rs;
    private Path fname;

    private File file;
    private InputStream input;

    public fetch(DBconnection con_in){
        dbcp=con_in.getDataSource();
    }
    public void getfile(final int fid,DataOutputStream out){
        try {
            conn=dbcp.getConnection();
            stmt=conn.prepareStatement("select * from files where fid=?");
            stmt.setInt(1, fid);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
//                fname = Paths.get(rs.getString("fname"));
//                file= new File("."+File.separator+"temp"+File.separator+fname.getFileName());
//                System.out.println(fname.getFileName());
                input = rs.getBinaryStream("stored");
                out.writeLong(input.available());
                out.flush();
                int count;
                byte[] buffer = new byte[8192];
                while ((count=input.read(buffer)) > 0) {
                    out.write(buffer,0,count);
                    out.flush();
                }
            }
        } catch (IOException | SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                conn.close();
                input.close();
            } catch (Exception e) {}
        }
    }
}
