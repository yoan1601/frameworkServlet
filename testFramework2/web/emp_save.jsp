<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="objets.*" %>

<!DOCTYPE html>
<html>
    <head>
        <title>TODO supply a title</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <form action="emp_save.do" method="post">
            <input type="text" name="nom" id="nom" placeholder="nom" required>
            <input type="number" name="age" id="age" placeholder="age" required>
            <input type="submit" value="save">
        </form>
    </body>
</html>
