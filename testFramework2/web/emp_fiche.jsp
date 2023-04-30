<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="objets.*" %>
<%
    Employe e = (Employe) request.getAttribute("employe");
    Integer id = Integer.valueOf(request.getAttribute("id").toString());
%>

<!DOCTYPE html>
<html>
    <head>
        <title>TODO supply a title</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h3>Fiche employe</h3>
        <h6>Id :  <%= id %></h6>
        <h6>Nom : <%= e.getNom() %></h6>
        <h6>age : <%= e.getAge() %></h6>
    </body>
</html>
