import java.sql.*;
import jakarta.servlet.ServletContext;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationJDBCHandler {
    private static final Logger logger = Logger.getLogger(AuthenticationJDBCHandler.class.getName());

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 8+ driver
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    // Constructor for credentialsDB connection
    public AuthenticationJDBCHandler(ServletContext context) throws SQLException {
        dbUrl = context.getInitParameter("db.url"); // Hardcode credentialsDB for authentication
        dbUser = context.getInitParameter("db.user");
        dbPassword = context.getInitParameter("db.password");
    }

    public Connection getConnection() throws SQLException {
        // Connect specifically to credentialsDB for authentication
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public boolean authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM usercredentials WHERE login_username = ? AND login_password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // If the user exists, return true
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error authenticating user", e);
            throw e;
        }
    }
}
