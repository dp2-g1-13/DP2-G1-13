<%@ tag body-content="empty" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@ tag import="org.springframework.samples.flatbook.model.DBImage" %>
<%@ tag import="java.util.Collection" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="images" required="true" rtexprvalue="true" type="java.util.Collection" %>

<%
    Collection<DBImage> colImages = (Collection<DBImage>) jspContext.getAttribute("images");
    jspContext.setAttribute("images", colImages);
%>


