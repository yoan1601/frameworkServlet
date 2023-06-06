<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="objets.*" %>
<%
    Employe e = (Employe) request.getAttribute("employe");
    int nbAppels = (int) request.getAttribute("nbAppels");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>TODO supply a title</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h3>Ok les gars GG!</h3>
        <h6>Nom : <%= e.getNom() %></h6>
        <h6>age : <%= e.getAge() %></h6>
        <h6>Nombre d'appels : <%= nbAppels %></h6>
    </body>
</html>
