<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="persons">
<sec:authentication var="myname" property="name"/>

        <div class="row">
		     <h2>
		        ${username}<c:if test="${!enabled}"> (BANNED)</c:if>
		    </h2>
		    
		     <sec:authorize access="hasAnyAuthority('ADMIN', 'TENANT', 'HOST')">
		    
	        <c:if test="${username != myname}">
	        <sec:authorize access="hasAnyAuthority('TENANT', 'HOST')">
	      			<c:if test="${enabled}">
			            <spring:url value="/messages/{username}" var="sendMessage">
							<spring:param name="username" value="${username}" />
						</spring:url>
						<a role="button" class="btn btn-default"
							href="${fn:escapeXml(sendMessage)}" aria-pressed="true">Send Message</a>
						<spring:url value="/reports/{username}/new" var="reportUrl">
							<spring:param name="username" value="${username}" />
						</spring:url>
						<a role="button" class="btn btn-default"
							href="${fn:escapeXml(reportUrl)}" aria-pressed="true">Report User</a>
					</c:if>
				</sec:authorize>
		   	</c:if>


            <sec:authorize access="hasAuthority('TENANT')">
                <c:choose>
                    <c:when test="${username == myname && myFlatId != null}">
                        <spring:url value="/flats/{myFlatId}" var="myFlat">
                            <spring:param name="myFlatId" value="${myFlatId}" />
                        </spring:url>
                        <a role="button" class="btn btn-default"
                            href="${fn:escapeXml(myFlat)}" aria-pressed="true">My flat</a>
                        <a role="button" class="btn btn-default" href="${pageContext.request.contextPath}/tasks/list" aria-pressed="true">My tasks</a>
                    </c:when>
                    <c:when test="${username == myname && myFlatId == null}">
                        <a role="button" class="btn btn-default" href="${pageContext.request.contextPath}/requests/list" aria-pressed="true">My requests</a>
                    </c:when>
                </c:choose>
            </sec:authorize>
            
           <sec:authorize access="hasAuthority('ADMIN')">
				<spring:url value="/reports/{username}/list" var="reportList">
					<spring:param name="username" value="${username}" />
				</spring:url>
				<a role="button" href="${fn:escapeXml(reportList)}"
					class="btn btn-default" aria-pressed="true">Report List</a>
				<c:choose>
					<c:when test="${enabled}">
						<spring:url value="/users/{username}/ban" var="desactivateUser">
							<spring:param name="username" value="${username}" />
						</spring:url>
						<a role="button" href="${fn:escapeXml(desactivateUser)}"
							class="btn btn-default" aria-pressed="true">Ban User</a>
					</c:when>
					<c:otherwise>
						<spring:url value="/users/{username}/unban" var="reactivateUser">
							<spring:param name="username" value="${username}" />
						</spring:url>
						<a role="button" href="${fn:escapeXml(reactivateUser)}"
							class="btn btn-default" aria-pressed="true">Unban User</a>
					</c:otherwise>
				</c:choose>
            </sec:authorize>
            
           	</sec:authorize>
        </div>
        
        <br>
        
        <c:if test="${reviews != null}">
        	<%@include file="/WEB-INF/jsp/reviews/reviewsList.jsp"%>
        </c:if>
        <c:if test="${selections != null}">
        <div class="row">
            <div class="panel panel-default">
       			 <div class="panel-heading"><h3>Advertisements</h3></div>
		         <c:if test="${selections.size() > 0}">
		        	<%@include file="/WEB-INF/jsp/advertisements/advertisementsListPanel.jsp"%>
		        </c:if>
	        </div>
	    </div>
        </c:if>
        
</flatbook:layout>
