<%@ page
	import="org.springframework.samples.flatbook.model.enums.RequestStatus"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authentication var="principal" property="principal.username"/>
<flatbook:layout pageName="tasks">

	<h2>These are your tasks:</h2>
	<div class="row">
		<a role="button" class="btn btn-default btn-lg"
			href="${pageContext.request.contextPath}/tasks/new"
			aria-pressed="true">New Task</a>
	</div>
	<br>
	<c:if test="${tasks.size() > 0}">
		<c:forEach var="i" begin="0" end="${tasks.size()-1}">
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-striped">
							<tr>
								<th>Title</th>
								<td><c:out value="${tasks.get(i).title}" /></td>
							</tr>
							<tr>
								<th>Description</th>
								<td><c:out value="${tasks.get(i).description}" /></td>
							</tr>
							<tr>
								<th>Creation date</th>
								<td><flatbook:localDate date="${tasks.get(i).creationDate}"
										pattern="dd/MM/yyyy" /></td>
							</tr>
							<tr>
								<th>Creator</th>
								<td><c:out value="${tasks.get(i).creator.username}" /></td>
							</tr>
							<tr>
								<th>Assignee</th>
								<td><c:out value="${tasks.get(i).asignee.username}" /></td>
							</tr>
							<tr>
								<th>Status</th>
								<td><c:out value="${tasks.get(i).status}" /></td>
							</tr>
						</table>
					</div>
					<div class="row">
						<div class="col-md-6">
							<spring:url value="/tasks/{taskId}/edit" var="taskEditUrl">
								<spring:param name="taskId" value="${tasks.get(i).id}" />
							</spring:url>
							<a role="button" href="${fn:escapeXml(taskEditUrl)}"
								class="btn btn-default" aria-pressed="true">Edit</a>
							<c:if test="${tasks.get(i).creator.username == principal}">
								<spring:url value="/tasks/{taskId}/remove" var="taskRemoveUrl">
									<spring:param name="taskId" value="${tasks.get(i).id}" />
								</spring:url>
								<a role="button" href="${fn:escapeXml(taskRemoveUrl)}"
									class="btn btn-default" aria-pressed="true">Remove</a>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
	</c:if>

</flatbook:layout>
