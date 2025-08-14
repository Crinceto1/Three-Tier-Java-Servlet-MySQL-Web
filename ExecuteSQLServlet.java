import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/ExecuteSQLServlet")
public class ExecuteSQLServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ExecuteSQLServlet.class.getName());
    private JDBCHandler jdbcHandler;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userType = (String) session.getAttribute("userType");

        if (userType == null) {
            userType = "client";
            session.setAttribute("userType", userType);
        }

        try {
            jdbcHandler = new JDBCHandler(userType);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error initializing JDBCHandler", e);
            request.setAttribute("executionResults", "<p>Error: Database connection failed.</p>");
            forwardToJsp(request, response);
            return;
        }

        String action = request.getParameter("action");
        String operation = request.getParameter("operation");
        String sqlQuery = request.getParameter("sqlCommand"); // Note: changed to lowercase to match form parameter

        try {
            String resultsHtml = "";
            if ("InsertSupplier".equals(action)) {
                resultsHtml = insertSupplier(request);
            } else if ("InsertPart".equals(action)) {
                resultsHtml = insertPart(request);
            } else if ("InsertJob".equals(action)) {
                resultsHtml = insertJob(request);
            } else if ("InsertShipment".equals(action)) {
                resultsHtml = insertShipment(request);
            } else if (operation != null && "theaccountant".equalsIgnoreCase(userType)) {
                resultsHtml = handleAccountantOperation(operation);
            } else if (sqlQuery != null && !sqlQuery.trim().isEmpty()) {
                resultsHtml = executeCustomQuery(sqlQuery, userType);
            }

            request.setAttribute("executionResults", resultsHtml);
        } catch (SQLException e) {
            String errorMessage = "<p>Error executing SQL query: " + e.getMessage() + "</p>";
            request.setAttribute("executionResults", errorMessage);
            logger.log(Level.SEVERE, "SQL execution error", e);
        }

        forwardToJsp(request, response);
    }

    private String handleAccountantOperation(String operation) throws SQLException {
        try (Connection conn = jdbcHandler.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = null;

            switch (operation) {
                case "maxStatus":
                    try (CallableStatement stmt1 = conn.prepareCall("{CALL Get_The_Maximum_Status_Of_All_Suppliers()}")) {
                        rs = stmt1.executeQuery();
                        if (rs.next()) {
                            return "<p>The maximum status value of all suppliers is: <strong>" +
                                    rs.getInt("Maximum_Status_Of_All_Suppliers") + "</strong></p>";
                        }
                    }
                    break;

                case "totalWeight":
                    try (CallableStatement stmt2 = conn.prepareCall("{CALL Get_The_Sum_Of_All_Parts_Weights()}")) {
                        rs = stmt2.executeQuery();
                        if (rs.next()) {
                            return "<p>The total weight of all parts is: <strong>" +
                                    rs.getInt("Sum_Of_All_Part_Weights") + "</strong></p>";
                        }
                    }
                    break;

                case "totalShipments":
                    try (CallableStatement stmt3 = conn.prepareCall("{CALL Get_The_Total_Number_Of_Shipments()}")) {
                        rs = stmt3.executeQuery();
                        if (rs.next()) {
                            return "<p>The total number of shipments is: <strong>" +
                                    rs.getInt("The_Total_Number_Of_Shipments") + "</strong></p>";
                        }
                    }
                    break;

                case "mostWorkers":
                    try (CallableStatement stmt4 = conn.prepareCall("{CALL Get_The_Name_Of_The_Job_With_The_Most_Workers()}")) {
                        rs = stmt4.executeQuery();
                        if (rs.next()) {
                            return "<p>The job with the most workers is: <strong>" +
                                    rs.getString("jname") + "</strong> with <strong>" +
                                    rs.getInt("numworkers") + "</strong> workers</p>";
                        }
                    }
                    break;

                case "supplierStatus":
                    try (CallableStatement stmt5 = conn.prepareCall("{CALL List_The_Name_And_Status_Of_All_Suppliers()}")) {
                        rs = stmt5.executeQuery();
                        StringBuilder sb = new StringBuilder();
                        sb.append("<h4>Supplier Names and Status:</h4>");
                        sb.append("<table border='1'><tr><th>Supplier Name</th><th>Status</th></tr>");
                        while (rs.next()) {
                            sb.append("<tr><td>").append(rs.getString("sname"))
                                    .append("</td><td>").append(rs.getInt("status"))
                                    .append("</td></tr>");
                        }
                        sb.append("</table>");
                        return sb.toString();
                    }

                default:
                    return "<p>Invalid operation selected</p>";
            }

            return "<p>No results found</p>";
        }
    }



    private String executeCustomQuery(String sqlQuery, String userType) throws SQLException {
        // First check if it's a SELECT query
        boolean isSelect = sqlQuery.trim().toUpperCase().startsWith("SELECT");


        try (Connection conn = jdbcHandler.getConnection();
             Statement stmt = conn.createStatement()) {

            if (isSelect) {
                ResultSet rs = stmt.executeQuery(sqlQuery);
                return formatResultSet(rs);
            } else {
                int rowsAffected = stmt.executeUpdate(sqlQuery);
                return "<p>Command executed successfully. Rows affected: " + rowsAffected + "</p>";
            }
        }
    }

    private String formatResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'><tr>");

        // Column headers
        for (int i = 1; i <= columnCount; i++) {
            sb.append("<th>").append(metaData.getColumnName(i)).append("</th>");
        }
        sb.append("</tr>");

        boolean hasRows = false;
        while (rs.next()) {
            hasRows = true;
            sb.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                sb.append("<td>").append(rs.getString(i) != null ? rs.getString(i) : "NULL").append("</td>");
            }
            sb.append("</tr>");
        }

        if (!hasRows) {
            sb.append("<tr><td colspan='").append(columnCount).append("'>No results found.</td></tr>");
        }


        sb.append("</table>");
        return sb.toString();
    }

    // Method to insert Supplier data
    private String insertSupplier(HttpServletRequest request) throws SQLException {
        String snum = request.getParameter("snum");
        String sname = request.getParameter("sname");
        String status = request.getParameter("status");
        String city = request.getParameter("city");

        String sql = "INSERT INTO Suppliers (snum, sname, status, city) VALUES (?, ?, ?, ?)";
        try (Connection conn = jdbcHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, snum);
            pstmt.setString(2, sname);
            pstmt.setString(3, status);
            pstmt.setString(4, city);

            int rowsAffected = pstmt.executeUpdate();
            return "<p>Supplier record inserted successfully. Rows affected: " + rowsAffected + "</p>";
        }
    }

    // Method to insert Part data
    private String insertPart(HttpServletRequest request) throws SQLException {
        String pnum = request.getParameter("pnum");
        String pname = request.getParameter("pname");
        String color = request.getParameter("color");
        String weight = request.getParameter("weight");
        String city = request.getParameter("city");

        String sql = "INSERT INTO Parts (pnum, pname, color, weight, city) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = jdbcHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pnum);
            pstmt.setString(2, pname);
            pstmt.setString(3, color);
            pstmt.setString(4, weight);
            pstmt.setString(5, city);

            int rowsAffected = pstmt.executeUpdate();
            return "<p>Part record inserted successfully. Rows affected: " + rowsAffected + "</p>";
        }
    }

    // Method to insert Job data
    private String insertJob(HttpServletRequest request) throws SQLException {
        String jnum = request.getParameter("jnum");
        String jname = request.getParameter("jname");
        String numworkers = request.getParameter("numworkers");
        String city = request.getParameter("city");

        String sql = "INSERT INTO Jobs (jnum, jname, numworkers, city) VALUES (?, ?, ?, ?)";
        try (Connection conn = jdbcHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, jnum);
            pstmt.setString(2, jname);
            pstmt.setString(3, numworkers);
            pstmt.setString(4, city);

            int rowsAffected = pstmt.executeUpdate();
            return "<p>Job record inserted successfully. Rows affected: " + rowsAffected + "</p>";
        }
    }

    // Method to insert Shipment data
    private String insertShipment(HttpServletRequest request) throws SQLException {
        String snum = request.getParameter("snum");
        String pnum = request.getParameter("pnum");
        String jnum = request.getParameter("jnum");
        String quantity = request.getParameter("quantity");

        String sql = "INSERT INTO Shipments (snum, pnum, jnum, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = jdbcHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, snum);
            pstmt.setString(2, pnum);
            pstmt.setString(3, jnum);
            pstmt.setString(4, quantity);

            int rowsAffected = pstmt.executeUpdate();
            return "<p>Shipment record inserted successfully. Rows affected: " + rowsAffected + "</p>";
        }
    }

    private void forwardToJsp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userType = (String) session.getAttribute("userType");

        String targetJsp;

        switch (userType != null ? userType.toLowerCase() : "") {
            case "root":
                targetJsp = "/rootUser.jsp";
                break;
            case "client":
                targetJsp = "/clientUser.jsp";
                break;
            case "dataentry":
                targetJsp = "/dataEntry.jsp";
                break;
            case "theaccountant":
                targetJsp = "/accountant.jsp";
                break;
            default:
                targetJsp = "/errorpage.html";
                break;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(targetJsp);
        dispatcher.forward(request, response);
    }
}
