<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="objets.*, java.util.Date, java.time.LocalDate, java.time.format.DateTimeFormatter" %>
<%
    Employe e = (Employe) request.getAttribute("employe");
    Integer id = Integer.valueOf(request.getAttribute("id").toString());
    String salut = request.getAttribute("salut").toString();
    Date date = (Date) request.getAttribute("date");
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
        <h3>Fiche employe</h3>
        <h6>salutation : <%=  salut %></h6>
        <h6>Id :  <%= id %></h6>
        <h6>Nom : <%= e.getNom() %></h6>
        <h6>age : <%= e.getAge() %></h6>
        <h6>date : <%= date %></h6>
        <div><a href="logout.do">deconnexion</a></div>
    </body>
</html>
