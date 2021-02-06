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

    File file;
    FileOutputStream output;
    InputStream input;
    public fetch(DBconnection con_in){
        dbcp=con_in.getDataSource();
    }
    public void getfile(final int fid){
        try {
            conn=dbcp.getConnection();
            stmt=conn.prepareStatement("select * from files where fid=?");
            stmt.setInt(1, fid);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                fname = Paths.get(rs.getString("fname"));
                file= new File("."+File.separator+"temp"+File.separator+fname);
                output = new FileOutputStream(file);
                System.out.println(fname.getFileName());
                input = rs.getBinaryStream("stored");
                byte[] buffer = new byte[input.available()];
                while (input.read(buffer) > 0) {
                    output.write(buffer);
                }
            }
        } catch (IOException | SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                conn.close();
                output.close();
                input.close();
            } catch (SQLException | IOException e) {}
        }
    }
}
