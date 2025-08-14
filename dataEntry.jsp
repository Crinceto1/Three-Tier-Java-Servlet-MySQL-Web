<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Data Entry Application</title>
    <style>
        body {
            background-color: black;
            color: white;
            font-family: Arial, sans-serif;
            text-align: center;
        }

        h1 { color: red; margin-top: 20px; }
        h2 { color: cyan; }

        .section {
            margin: 20px auto;
            width: 90%;
            border-top: 2px solid gray;
            padding-top: 10px;
        }

        table {
            margin: 0 auto;
            background-color: black;
            border: 2px solid yellow;
        }

        input[type="text"] {
            width: 100px;
            background-color: #d4b95c;
            border: 1px solid #aaa;
            color: black;
            font-weight: bold;
            text-align: center;
        }

        .btn-green {
            background-color: green;
            color: white;
            font-weight: bold;
            padding: 5px 10px;
            margin: 10px;
            border: none;
        }

        .btn-red {
            background-color: red;
            color: white;
            font-weight: bold;
            padding: 5px 10px;
            margin: 10px;
            border: none;
        }

        #results {
            margin-top: 20px;
            border-top: 4px solid yellow;
            padding: 10px;
            color: lightgreen;
        }
    </style>
</head>
<body>

<h1>Welcome to the Spring 2025 Project 4 Enterprise System</h1>
<h2>Data Entry Application</h2>
<p>
    You are connected to the Project 4 Enterprise System database as a
    <span style="color:red">data-entry-level</span> user.<br>
    Enter the data values in a form below to add a new record to the corresponding database table.
</p>

<!-- Supplier Insert Section -->
<div class="section">
    <h3>Suppliers Record Insert</h3>
    <form method="post" action="ExecuteSQLServlet">
        <input type="hidden" name="action" value="InsertSupplier" />
        <table>
            <tr>
                <td><input type="text" name="snum" placeholder="snum" required /></td>
                <td><input type="text" name="sname" placeholder="sname" required /></td>
                <td><input type="text" name="status" placeholder="status" required /></td>
                <td><input type="text" name="city" placeholder="city" required /></td>
            </tr>
        </table>
        <input type="submit" value="Enter Supplier Record Into Database" class="btn-green" />
        <input type="reset" value="Clear Form" class="btn-red" />
    </form>
</div>

<!-- Parts Insert Section -->
<div class="section">
    <h3>Parts Record Insert</h3>
    <form method="post" action="ExecuteSQLServlet">
        <input type="hidden" name="action" value="InsertPart" />
        <table>
            <tr>
                <td><input type="text" name="pnum" placeholder="pnum" required /></td>
                <td><input type="text" name="pname" placeholder="pname" required /></td>
                <td><input type="text" name="color" placeholder="color" required /></td>
                <td><input type="text" name="weight" placeholder="weight" required /></td>
                <td><input type="text" name="city" placeholder="city" required /></td>
            </tr>
        </table>
        <input type="submit" value="Enter Part Record Into Database" class="btn-green" />
        <input type="reset" value="Clear Form" class="btn-red" />
    </form>
</div>

<!-- Jobs Insert Section -->
<div class="section">
    <h3>Jobs Record Insert</h3>
    <form method="post" action="ExecuteSQLServlet">
        <input type="hidden" name="action" value="InsertJob" />
        <table>
            <tr>
                <td><input type="text" name="jnum" placeholder="jnum" required /></td>
                <td><input type="text" name="jname" placeholder="jname" required /></td>
                <td><input type="text" name="numworkers" placeholder="numworkers" required /></td>
                <td><input type="text" name="city" placeholder="city" required /></td>
            </tr>
        </table>
        <input type="submit" value="Enter Job Record Into Database" class="btn-green" />
        <input type="reset" value="Clear Form" class="btn-red" />
    </form>
</div>

<!-- Shipments Insert Section -->
<div class="section">
    <h3>Shipments Record Insert</h3>
    <form method="post" action="ExecuteSQLServlet">
        <input type="hidden" name="action" value="InsertShipment" />
        <table>
            <tr>
                <td><input type="text" name="snum" placeholder="snum" required /></td>
                <td><input type="text" name="pnum" placeholder="pnum" required /></td>
                <td><input type="text" name="jnum" placeholder="jnum" required /></td>
                <td><input type="text" name="quantity" placeholder="quantity" required /></td>
            </tr>
        </table>
        <input type="submit" value="Enter Shipment Record Into Database" class="btn-green" />
        <input type="reset" value="Clear Form" class="btn-red" />
    </form>
</div>

<!-- Results Section -->
<div id="results">
    <h3>Execution Results:</h3>
    <%= request.getAttribute("executionResults") != null ? request.getAttribute("executionResults") : "" %>
</div>

</body>
</html>
