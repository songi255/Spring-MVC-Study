<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-01-19
  Time: 오후 4:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원가입</title>
</head>
<body>
    <p>
        <%-- 아래와 같이 Command 객체를 이용해서 정보를 깔끔하게 표시할 수 있다. (Command 객체 첫글자만 소문자로 바꾼 이름의 변수이다.) --%>
        <strong>${registerRequest.name}님</strong>
        회원가입을 완료했습니다.
    </p>
    <%-- jstl을 사용해서 상대경로를 사용할 수 있다. --%>
    <a href="<c:url value='/main'/>">[첫 화면 이동]</a>
</body>
</html>
