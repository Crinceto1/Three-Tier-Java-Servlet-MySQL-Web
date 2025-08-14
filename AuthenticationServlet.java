/* Name: Angelo Castellon
Course: CNT 4714 – Spring 2025 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: April 23, 2025
*/
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AuthenticationServlet")
public class AuthenticationServlet extends HttpServlet {

    private AuthenticationJDBCHandler jdbcHandler;

    @Override
    public void init() throws ServletException {
        try {
            // Initialize the specialized AuthenticationJDBCHandler
            jdbcHandler = new AuthenticationJDBCHandler(getServletContext());
        } catch (SQLException e) {
            throw new ServletException("Error initializing AuthenticationJDBCHandler", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username").toLowerCase().trim();
        String password = request.getParameter("password");

        try {
            // Authenticate the user using the specialized JDBC handler
            boolean isAuthenticated = jdbcHandler.authenticateUser(username, password);

            if (isAuthenticated) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("userType", username);

                // Redirect based on the username
                switch (username) {
                    case "root":
                        response.sendRedirect("rootUser.jsp");
                        break;
                    case "client":
                        response.sendRedirect("clientUser.jsp");
                        break;
                    case "dataentry":
                        response.sendRedirect("dataEntry.jsp");
                        break;
                    case "theaccountant":
                        response.sendRedirect("accountant.jsp");
                        break;
                    default:
                        response.sendRedirect("errorpage.html");
                }
            } else {
                response.sendRedirect("errorpage.html");
            }
        } catch (SQLException e) {
            throw new ServletException("Database error during authentication", e);
        }
    }
}

