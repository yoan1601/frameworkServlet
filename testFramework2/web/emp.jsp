<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="objets.*, java.util.ArrayList" %>

<%
    ArrayList <Employe> le = (ArrayList <Employe>) request.getAttribute("listeAllEmp");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>TODO supply a title</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div>Liste des employes</div>
        <ul>
            <%
                for(int i = 0; i < le.size(); i++) {
                    Employe e = le.get(i);
            %>
                <p><%= e.getNom() %></p>
            <%    }
            %>
        </ul>
    </body>
</html>
