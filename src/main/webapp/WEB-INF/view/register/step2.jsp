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

                <%-- path에 해당하는 code 를 가진 error message 를 출력한다.
                    path 를 지정하지 않으면 Global error 를 대상으로 출력한다. --%>
                <form:errors path="email" />

                <%-- Validator에서 프로퍼티에 error code를 여러개 추가할 수 있었는데, 그런 경우 form:errors 는 전부 출력한다.
                    element는 에러메시지를 띄울 tag. 기본 span 이다.
                    delimiter는 구분자 tag. 기본 <br/> 이다.
                --%>
                <form:errors path="email" element="div" delimiter="" />
            </label>
        </p>
        <p>
            <label>
                name : <br>
                <form:input path="name"/>
                <form:errors path="name" />
            </label>
        </p>
        <p>
            <label>
                password : <br>
                <form:password path="password"/>
                <form:errors path="password" />
            </label>
        </p>
        <p>
            <label>
                password confirm : <br>
                <form:password path="confirmPassword"/>
                <form:errors path="confirmPassword" />
            </label>
        </p>
        <input type="submit" value="가입 완료">
    </form:form>
    <%-- 이 외 나머지 spring form 태그들은 생략한다. 궁금하면 308쪽 참고하자. (어차피 JSP 말고 thymeleaf 쓸건데.. --%>
</body>
</html>
