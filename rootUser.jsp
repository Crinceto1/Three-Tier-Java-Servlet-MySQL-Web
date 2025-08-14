<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Root SQL Command Interface</title>
    <style>
        body {
            background-color: black;
            color: white;
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 20px;
        }

        h1 {
            color: yellow;
            font-size: 30px;
            margin-bottom: 10px;
        }

        h2 {
            color: limegreen;
            font-size: 22px;
            margin-bottom: 30px;
        }

        .description span {
            color: red;
        }

        textarea {
            width: 80%;
            height: 200px;
            background-color: blue;
            color: white;
            font-size: 16px;
            padding: 10px;
            border: 2px solid white;
            resize: none;
        }

        .buttons {
            margin: 20px 0;
        }

        .buttons input {
            font-size: 16px;
            font-weight: bold;
            padding: 10px 20px;
            margin: 0 10px;
            border: none;
            cursor: pointer;
            border-radius: 5px;
        }

        .execute {
            background-color: green;
            color: white;
        }

        .reset {
            background-color: red;
            color: white;
        }

        .clear {
            background-color: orange;
            color: black;
        }

        .results-heading {
            margin-top: 40px;
            font-size: 18px;
            font-weight: bold;
            color: white;
            border-top: 1px solid white;
            padding-top: 10px;
        }

        .results-box {
            color: lightgreen;
            font-family: monospace;
            margin-top: 10px;
        }
        table {
            margin: 0 auto;
            border-collapse: collapse;
            border: 2px solid white;
            color: white;
        }

        table th, table td {
            border: 1px solid white;
            padding: 8px 12px;
            text-align: center;
        }

    </style>
</head>
<body>
    <h1>Welcome to the Spring 2025 Project 4 Enterprise System</h1>
    <h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>

    <div class="description">
        You are connected to the Project 4 Enterprise System database as a <span>root-level</span> user.<br>
        Please enter any SQL query or update command in the box below.
    </div>

    <!-- Update the form to point to ExecuteSQLServlet -->
    <form method="post" action="ExecuteSQLServlet">
        <textarea name="sqlCommand" placeholder="Enter SQL: command here..."></textarea>

        <div class="buttons">
            <input type="submit" value="Execute Command" class="execute">
            <input type="reset" value="Reset Form" class="reset">
            <input type="button" value="Clear Results" class="clear" onclick="document.getElementById('results').innerHTML = '';">
        </div>
    </form>

    <!-- Display SQL Execution Results -->
    <div class="results-heading">Execution Results:</div>
    <div id="results" class="results-box">
        <!-- Dynamically updated results from ExecuteSQLServlet will appear here -->
        <%
            // Check if there are any results (for example, if ExecuteSQLServlet forwarded results)
            String results = (String) request.getAttribute("executionResults");
            if (results != null) {
                out.println(results); // Display results here
            }
        %>
    </div>
</body>
</html>
