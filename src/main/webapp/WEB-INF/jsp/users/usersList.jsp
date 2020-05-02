<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="messages">
		<div align="center" class="row">
			<div align="left" class="col-xs-6">
			    <h2>
			        Users
			    </h2>
	    	</div>
			<div align="right" class="col-xs-6">
				<a role="button" class="btn btn-default"
					href="${pageContext.request.contextPath}/users/list?show=banned"
					aria-pressed="true">Banned Users</a>
				<a role="button" class="btn btn-default"
					href="${pageContext.request.contextPath}/users/list?show=active"
					aria-pressed="true">Active Users</a>
			</div>
		</div>
		<br>
    <div class="over">
    <table class="table table-striped">
        <thead>
        <tr>
            <th>User</th>
            <th>Status</th>
            <th>Type</th>
        </tr>
        </thead>
        <tbody>
        <c:set var="i" value="0"/>
        <c:forEach items="${users}" var="user">
            <tr>
                <td>
                	<spring:url value="/users/{user}" var="userUrl">
                        <spring:param name="user" value="${user.username}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(userUrl)}"><c:out value="${user.username}"/></a>
                </td>
                <td>
                  	<c:if test="${user.enabled}">Active</c:if>
                  	<c:if test="${!user.enabled}">Banned</c:if>
                </td>
                <td>
                  	<c:out value="${authorities.get(i).authority}"/>
                </td>
            </tr>
            <c:set var="i" value="${i+1}"/>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${users.size() == 0}">
			<p>There are no users to show.</p>
	</c:if>
    </div>
    
</flatbook:layout>
