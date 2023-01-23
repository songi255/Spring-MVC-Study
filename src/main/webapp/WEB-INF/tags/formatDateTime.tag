<%-- jstl 이 제공하는 날짜형식 tag 는 Java8의 LocalDateTime 은 지원하지 않는다.
 그래서 아래처럼 custom tag 파일을 사용해서 지정한 형식으로 출력하도록 한다. --%>

<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ tag import="java.time.format.DateTimeFormatter" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="value" required="true" type="java.time.temporal.TemporalAccessor" %>
<%@ attribute name="pattern" type="java.lang.String" %>
<%
    if (pattern == null) pattern = "yyyy-MM-dd";
%>
<%= DateTimeFormatter.ofPattern(pattern).format(value) %>