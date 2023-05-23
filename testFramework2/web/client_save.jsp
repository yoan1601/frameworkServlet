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
        <h3>Enregistre un client</h3>
        <form action="client_save.do" method="post" enctype="multipart/form-data">
            <p><input type="text" name="nom" id="nom" placeholder="nom" required></p>
            <p><input type="date" name="dateNaissance" id="dateNaissance" placeholder="dateNaissance" required></p>
            <p><input type="file" name="badge" id="badge" placeholder="badge" required></p>
            <p><input type="submit" value="save"></p>
        </form>
    </body>
</html>
