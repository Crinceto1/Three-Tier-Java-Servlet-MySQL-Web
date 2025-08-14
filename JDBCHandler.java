import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCHandler {
    private static final Logger logger = Logger.getLogger(JDBCHandler.class.getName());
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public JDBCHandler(String userType) throws SQLException {
        // Determine connection parameters based on user type
        switch (userType.toLowerCase()) {
            case "client":
                this.dbUrl = "jdbc:mysql://localhost:3306/project4";
                this.dbUser = "client";
                this.dbPassword = "client";
                break;
            case "dataentry":
                this.dbUrl = "jdbc:mysql://localhost:3306/project4";
                this.dbUser = "dataentry";
                this.dbPassword = "dataentry";
                break;
            case "theaccountant":
                this.dbUrl = "jdbc:mysql://localhost:3306/project4";
                this.dbUser = "theaccountant";
                this.dbPassword = "theaccountant";
                break;
            case "systemapp":
                this.dbUrl = "jdbc:mysql://localhost:3306/credentialsDB";
                this.dbUser = "systemapp";
                this.dbPassword = "systemapp";
                break;
            case "root":
            default:
                this.dbUrl = "jdbc:mysql://localhost:3306/project4";
                this.dbUser = "root";
                this.dbPassword = "C@stellon1209";
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}