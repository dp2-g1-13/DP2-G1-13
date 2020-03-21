<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="tasks">
<jsp:body>
    <h2>
        <c:if test="${task['new']}">New </c:if> Task
    </h2>

    <form:form modelAttribute="task" class="form-horizontal" id="add-task-form">
        <div class="form-group has-feedback">
        	<flatbook:hidden name="id"/>
            <flatbook:hidden name="creator"/>
            <flatbook:hidden name="creationDate"/>
            <flatbook:hidden name="status"/>
            <flatbook:inputField name="title" label="Title"/>
            <flatbook:textAreaField name="description" label="Description"/>
            <flatbook:selectField name="asignee" label="Asignee" names="${roommates}" size="4"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${task['new']}">
                        <button class="btn btn-default" type="submit">Create Task</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Task</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</jsp:body>
</flatbook:layout>
