package ece465.handler.single;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ece465.util.connection;

public class store {
    DataSource dbcp;
    public store(connection con_in){ dbcp=  con_in.getDataSource(); }

    public int add(File inFile) throws IOException, SQLException {
        FileInputStream inputStream=null;
        Connection conn = null;
        try {
            inputStream= new FileInputStream(inFile);
        } catch (FileNotFoundException e) {
            System.err.println("From File -> InputStream Error.");
            e.printStackTrace();
        }
        try {
            conn = dbcp.getConnection();
        } catch (SQLException err) {
            System.err.println("Connection Error.");
            err.printStackTrace();
            return -1;
        }
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO files (fname, stored) VALUES (?, ?);");
        stmt.setString(1,inFile.getAbsolutePath());
        stmt.setBinaryStream(2,inputStream);
        try {
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            System.err.println("Execution Error.");
            throwables.printStackTrace();
        }
        return 1;

    }
}
