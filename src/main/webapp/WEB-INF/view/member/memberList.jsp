<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%-- custom tag 를 경로 매핑해서 가져왔다! --%>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>회원 조회</title>
</head>
<body>
    <form:form modelAttribute="cmd">
    <p>
        <%-- input 의 from 과 to 는 Command 객체에 매핑된다. @DateTimeFormat 이 적용되있으므로, 해당 패턴으로 값이 출력된다. --%>
        <label>from: <form:input path="from" /></label>
        <form:errors path="from" />
        ~
        <label>to:<form:input path="to" /></label>
        <form:errors path="to" />
        <input type="submit" value="조회">
    </p>
    </form:form>
    
    <c:if test="${! empty members}">
    <table>
        <tr>
            <th>아이디</th><th>이메일</th>
            <th>이름</th><th>가입일</th>
        </tr>
        <c:forEach var="mem" items="${members}">
        <tr>
            <td>${mem.id}</td>
            <td><a href="<c:url value="/members/${mem.id}"/>">
                ${mem.email}</a></td>
            <td>${mem.name}</td>
            <%-- custom tag 사용 --%>
            <td><tf:formatDateTime value="${mem.registerDateTime }"
                                   pattern="yyyy-MM-dd" /></td>
        </tr>
        </c:forEach>
    </table>
    </c:if>
</body>
</html>
