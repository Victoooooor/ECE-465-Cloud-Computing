package ece465.handler.single;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ece465.util.DBconnection;

public class store {
    DataSource dbcp;
    public store(DBconnection con_in){ dbcp=con_in.getDataSource(); }

    public int add(File inFile) throws IOException, SQLException {
        FileInputStream inputStream=null;
        Connection conn = null;
        try {
            inputStream= new FileInputStream(inFile);
        } catch (FileNotFoundException e) {
            System.err.println("From File -> InputStream Error.");
            e.printStackTrace();
            return -1;
        }
        try {
            conn = dbcp.getConnection();
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
        } catch (SQLException err) {
            System.err.println("Connection Error.");
            err.printStackTrace();
            return -1;
        }
        finally {
            conn.close();
        }


    }

    public int store(String name, InputStream inputStream) throws IOException, SQLException {
        Connection conn = null;

        try {
            conn = dbcp.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO files (fname, stored) VALUES (?, ?);");
            stmt.setString(1,name);
            stmt.setBinaryStream(2,inputStream);
            try {
                stmt.executeUpdate();
            } catch (SQLException throwables) {
                System.err.println("Execution Error.");
                throwables.printStackTrace();
            }
            return 1;
        } catch (SQLException err) {
            System.err.println("Connection Error.");
            err.printStackTrace();
            return -1;
        }
        finally {
            conn.close();
        }


    }
}
