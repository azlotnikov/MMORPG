<%@ page import="MonsterQuest.Auth" %>
<%--
  Created by IntelliJ IDEA.
  Auth: razoriii
  Date: 25.02.14
  Time: 16:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
%>
<%
    Auth.doAction(request, response);
%>
