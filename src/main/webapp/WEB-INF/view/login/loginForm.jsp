<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-01-23
  Time: 오후 1:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="for" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title><spring:message code="login.title" /> </title>
</head>
<body>
    <form:form modelAttribute="loginCommand">
        <form:errors />
        <p>
            <label><spring:message code="email" /></label>
            <form:input path="email" />
            <form:errors path="email" />
        </p>
        <p>
            <label><spring:message code="password" /> </label>
            <form:password path="password" />
            <form:errors path="password" />
        </p>
        <p>
            <label><spring:message code="rememberEmail" />
                <form:checkbox path="rememberEmail" />
            </label>
        </p>
        <input type="submit" value="<spring:message code="login.btn" /> ">
    </form:form>
</body>
</html>
