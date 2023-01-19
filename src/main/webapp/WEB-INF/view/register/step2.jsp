<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-01-19
  Time: 오후 4:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- form:~~~ 사용하기 위한 taglib --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%-- spring:~~~ 사용하기 위한 taglib --%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="member.register" /></title>
</head>
<body>
    <h2>회원정보입력</h2>
    <%-- 아래처럼 Spring 이 제공하는 form:form 태그를 이용하면, command 객체에 바로 매핑해서 출력할 수 있어 매우 편리하다.
        아래는 오류 발생시 입력했던 값을 다시 넣어주는 예시이다. --%>
    <%-- Controller 에서 Model 객체에 Command 를 아래 사용할 key로 Attribute 로 넣어야 한다! --%>
    <%-- 4.3 까지는 commandName 이었다. modelAttribute 는 기본값은 "command" 이다. --%>
    <form:form action="step3" modelAttribute="registerRequest">
        <p>
            <label>
                email:<br>
                <%-- path에 해당하는 Command 의 property 를 form:input 의 value 로 사용한다. --%>
                <form:input path="email"/>
            </label>
        </p>
        <p>
            <label>
                name : <br>
                <form:input path="name"/>
            </label>
        </p>
        <p>
            <label>
                password : <br>
                <form:password path="password"/>
            </label>
        </p>
        <p>
            <label>
                password confirm : <br>
                <form:password path="confirmPassword"/>
            </label>
        </p>
        <input type="submit" value="가입 완료">
    </form:form>
    <%-- 이 외 나머지 spring form 태그들은 생략한다. 궁금하면 308쪽 참고하자. (어차피 JSP 말고 thymeleaf 쓸건데.. --%>
</body>
</html>
