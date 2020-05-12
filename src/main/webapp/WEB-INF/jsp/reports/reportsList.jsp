<%@ page import="org.springframework.samples.flatbook.model.enums.TaskStatus" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags"%>
<flatbook:layout pageName="reports">

		<h2>Reports 
		<c:if test="${username != null}">received by ${username}</c:if>
		</h2>
			<div class="panel panel-default">
       			 <div class="panel-body overflow">
       			 <c:if test="${reports.size() == 0}">
					<p>There are no reports to show.</p>
				 </c:if>
       			 <c:forEach items="${reports}" var="report">
       			 <div class="panel panel-default">
                        <div class="panel-body">
					<div class="table-responsive">
						<table class="table table-striped">
							<tr>
								<th>Reason:</th>
								<td><c:out value="${report.reason}" /></td>
							</tr>
							<tr>
								<th>Fecha:</th>
								<td><c:out value="${report.creationDate}" /></td>
							</tr>
							<tr>
								<th>Sender:</th>
								<td><spring:url value="/users/{user}" var="senderUrl">
                                        	<spring:param name="user" value="${report.sender.username}" />
									</spring:url>
                               		<a href="${fn:escapeXml(senderUrl)}"
                                   	aria-pressed="true">${report.sender.username}</a>
                                </td>
							</tr>
							<c:if test="${username == null}">
								<tr>
									<th>Receiver:</th>
									<td><spring:url value="/users/{user}" var="receiverUrl">
                                         	<spring:param name="user" value="${report.receiver.username}" />
										</spring:url>
                                  		<a href="${fn:escapeXml(receiverUrl)}"
                                     	aria-pressed="true">${report.receiver.username}</a>
                                     </td>
								</tr>
							</c:if>
						</table>
					</div>
					<div>
						<spring:url value="/reports/{id}/delete" var="deleteReport">
							<spring:param name="id" value="${report.id}" />
						</spring:url>
						<a role="button" href="${fn:escapeXml(deleteReport)}"
							class="btn btn-default" aria-pressed="true">Delete</a>
					</div>
					</div>
					</div>
					</c:forEach>
				</div>
			</div>
			
</flatbook:layout>
