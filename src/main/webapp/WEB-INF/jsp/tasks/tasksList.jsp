<%@ page import="org.springframework.samples.flatbook.model.enums.TaskStatus" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags"%>
<flatbook:layout pageName="tasks">
	<div class="row">
		<div class="col-md-6">
			<a role="button" class="btn btn-default btn-lg"
				href="${pageContext.request.contextPath}/tasks/new"
				aria-pressed="true">New Task</a>
		</div>
	</div>
	<br>
	<h2>These are your tasks:</h2>
	<c:if test="${tasks.size() > 0}">
	<div class="panel-body overflow">
		<c:forEach var="i" begin="0" end="${tasks.size()-1}">
			<div class="panel panel-default">
			<c:if test="${tasks.get(i).status == TaskStatus.TODO}">
				<div class="panel-body task-todo">
			</c:if>
			<c:if test="${tasks.get(i).status == TaskStatus.INPROGRESS}">
				<div class="panel-body task-ip">
			</c:if>
			<c:if test="${tasks.get(i).status == TaskStatus.DONE}">
				<div class="panel-body task-done">
			</c:if>
					<div class="table-responsive">
						<table class="table table-striped">
							<tr>
								<th>Title:</th>
								<td><c:out value="${tasks.get(i).title}" /></td>
								<th>Description:</th>
								<td><c:out value="${tasks.get(i).description}" /></td>
							</tr>
							<tr>
								<th>Assignee:</th>
								<td><c:out value="${tasks.get(i).asignee.username}" /></td>
								<th>Status:</th>
								<td><c:out value="${tasks.get(i).status}" /></td>
							</tr>
							<tr>
								<th>Creator:</th>
								<td><c:out value="${tasks.get(i).creator.username}" /></td>
								<th>Creation date:</th>
								<td><flatbook:localDate date="${tasks.get(i).creationDate}"
										pattern="dd/MM/yyyy" /></td>
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
						<spring:url value="/tasks/{taskId}/delete" var="taskRemoveUrl">
							<spring:param name="taskId" value="${tasks.get(i).id}" />
						</spring:url>
						<a role="button" href="${fn:escapeXml(taskRemoveUrl)}"
							class="btn btn-default" aria-pressed="true">Delete</a>
					</div>
				</div>
			</div>
		</div>
		</c:forEach>
		</div>
	</c:if>
	<c:if test="${tasks.size() == 0}">
		<p>There are no tasks to show. Please create a new task to begin.</p>
	</c:if>

</flatbook:layout>
