<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="messages">

    <h2>
        Active conversations
    </h2>
    <table id="messagesList" class="table table-striped">
        <thead>
        <tr>
            <th>User</th>
            <th>Message</th>
            <th></th>
            
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${messages}" var="mess">
            <tr>
                <td>
                	<spring:url value="/messages/{username}" var="conversationUrl">
                        <spring:param name="username" value="${mess.key}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(conversationUrl)}"><c:out value="${mess.key}"/></a>
                </td>
                <td>
                   <div class="messagelist" > <c:out value="${mess.value[0].body}"/></div>
                </td>
                <td>
                	<flatbook:localDatetime date="${mess.value[0].creationMoment}"/>
                </td> 
            </tr>
        </c:forEach>
        </tbody>
    </table>

</flatbook:layout>
