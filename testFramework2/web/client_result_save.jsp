<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="objets.*" %>
<%
    Client c = (Client) request.getAttribute("client");
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
        <h3>Client saved</h3>
        <h6>Nom : <%= c.getNom() %></h6>
        <h6>Date de naissance : <%= c.getDateNaissance() %></h6>
        <h6>Intitule du badge : <%= c.getBadge().getName() %></h6>
        <p>
            <h6>inof sur les bytes du badge : </h6>
            <div style="width: 50vw; margin : 5vh auto; overflow-y: scroll; height: 50vh;">
                <% for(int i = 0; i < c.getBadge().getFile().length; i++) { %>
                    <span style="margin: auto 5vh;"><%= c.getBadge().getFile()[i] %></span>
                <% } %>
            </div>
        </p>
    </body>
</html>
