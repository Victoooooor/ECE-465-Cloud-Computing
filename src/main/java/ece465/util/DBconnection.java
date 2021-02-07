package ece465.util;

import javax.sql.DataSource;
import io.github.cdimascio.dotenv.Dotenv;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBconnection {
    Dotenv dotenv = Dotenv.load();
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String USER = "user1";
    private final String PASS;
    private final String DB_URL;
    private static HikariDataSource DBCP;
    public DBconnection() {
        Dotenv dotenv = Dotenv.load();
        PASS=dotenv.get("DB_PASS");
        DB_URL=dotenv.get("DB_URL");
        try{
            DBCP = new HikariDataSource();
            DBCP.setDriverClassName(JDBC_DRIVER);
            DBCP.setJdbcUrl(DB_URL);
            DBCP.setUsername(USER);
            DBCP.setPassword(PASS);
            DBCP.setMinimumIdle(5);
            DBCP.setMaximumPoolSize(500);
            DBCP.setLoginTimeout(1);
            DBCP.setAutoCommit(true);
            DBCP.setConnectionTimeout(5000);
            DBCP.setIdleTimeout(30000);
            DBCP.setMaxLifetime(120000);
        } catch (Exception err) {
            System.err.println("HikariCP Connection Error.");
            err.printStackTrace();
        }
        try {
            PreparedStatement stmt=DBCP.getConnection().prepareStatement("SET GLOBAL max_allowed_packet=4294967296;");
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            System.err.println("Set Max Packet Error.");
            throwables.printStackTrace();
        }

    }
    public static DataSource getDataSource() {return (DataSource) DBCP;}
}
