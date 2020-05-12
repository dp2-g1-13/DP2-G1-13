<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="error">

    <spring:url value="/resources/images/error.png" var="petsImage"/>
    <img src="${petsImage}" alt="Error" height="200" width="200"/>
	<br>
	<br>
    <h2>Something happened...</h2>

    <p>${exception.message}</p>

</flatbook:layout>
