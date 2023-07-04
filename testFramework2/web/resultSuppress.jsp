<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="objets.*, java.util.ArrayList" %>

<%
    String[] deleted =(String []) request.getAttribute("toSuppres");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>TODO supply a title</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div>Session deleted</div>
        <ul>
            <% for(String del : deleted) { %>
                <li>
                    <%= del %>
                </li>
                <% } %>
        </ul>
        <div><a href="accueil.jsp">retour</a></div>
        <div><a href="logout.do">deconnexion</a></div>
    </body>
</html>
