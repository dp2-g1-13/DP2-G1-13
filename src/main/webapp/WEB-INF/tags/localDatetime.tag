<%@ tag body-content="empty" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="date" required="true" type="java.time.LocalDateTime" %>


<fmt:parseDate value="${date}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${parsedDateTime}" />