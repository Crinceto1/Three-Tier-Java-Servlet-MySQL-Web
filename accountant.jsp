<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CNT 4714 Spring 2025 Project 4 - Accountant</title>
    <style>
        body {
            background-color: black;
            color: white;
            font-family: Arial, sans-serif;
            text-align: center;
        }

        h1 {
            color: yellow;
        }

        h2 {
            color: lime;
        }

        .container {
            background-color: #222;
            margin: 30px auto;
            padding: 30px;
            border-radius: 15px;
            width: 70%;
            box-shadow: 0 0 15px #555;
        }

        .radio-option {
            text-align: left;
            margin: 10px 0;
            font-size: 18px;
        }

        .radio-option input {
            margin-right: 10px;
        }

        .radio-option a {
            color: dodgerblue;
            text-decoration: none;
        }

        .radio-option a:hover {
            text-decoration: underline;
        }

        .button-group {
            margin: 20px 0;
        }

        .button-group input {
            font-size: 16px;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            margin: 0 10px;
            cursor: pointer;
        }

        .execute {
            background-color: green;
            color: white;
        }

        .clear {
            background-color: red;
            color: white;
        }

        .results {
            margin-top: 40px;
            border-top: 1px solid gray;
            padding-top: 10px;
        }
    </style>
</head>
<body>
    <h1>Welcome to the Spring 2025 Project 4 Enterprise System</h1>
    <h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>

    <p>You are connected to the Project 4 Enterprise System database as an <strong style="color:red;">accountant-level</strong> user.<br>
    Please select the operation you would like to perform from the list below.</p>

    <form action="ExecuteSQLServlet" method="post">
        <div class="container">
            <div class="radio-option">
                <input type="radio" name="operation" value="maxStatus" required>
                <a href="#">Get The Maximum Status Value of All Suppliers</a>
            </div>
            <div class="radio-option">
                <input type="radio" name="operation" value="totalWeight">
                <a href="#">Get The Total Weight Of All Parts</a>
            </div>
            <div class="radio-option">
                <input type="radio" name="operation" value="totalShipments">
                <a href="#">Get The Total Number of Shipments</a>
            </div>
            <div class="radio-option">
                <input type="radio" name="operation" value="mostWorkers">
                <a href="#">Get The Name And Number Of Workers Of The Job With The Most Workers</a>
            </div>
            <div class="radio-option">
                <input type="radio" name="operation" value="supplierStatus">
                <a href="#">List The Name and Status Of Every Supplier</a>
            </div>

            <div class="button-group">
                <input type="submit" class="execute" value="Execute Command">
                <input type="reset" class="clear" value="Clear Results">
            </div>
        </div>

        <div class="results">
            <h3>Execution Results:</h3>
            <div id="executionResults">
                <%= request.getAttribute("executionResults") != null ? request.getAttribute("executionResults") : "" %>
            </div>
        </div>
    </form>
</body>
</html>
