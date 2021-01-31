package edu.cooper.ece465;

import java.sql.*;

public class databaseSetup {
    public static void main(String[] args) {
        try{
            String myDriver = "org.apache.derby.jdbc.ClientDriver";
            String myUrl = "jdbc:mysql://localhost:3307";
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con=DriverManager.getConnection(myUrl,"root","diuleiloumo");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("CREATE DATABASE IF NOT EXISTS ece465");
            //while(rs.next())
                //System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
            con.close();
        }catch(Exception e){ System.out.println(e);}
    }

}
