<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="requests">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#startDate").datepicker({dateFormat: 'dd/mm/yy'});
            });
            $(function () {
                $("#finishDate").datepicker({dateFormat: 'dd/mm/yy'});
            });
        </script>
    </jsp:attribute>

    <jsp:body>
    <h2>
        <c:if test="${requestForm['new']}">New </c:if> Request
    </h2>

    <form:form modelAttribute="requestForm" class="form-horizontal" id="add-request-form">
        <div class="form-group has-feedback">
            <flatbook:textAreaField name="description" label="Description"/>
            <flatbook:inputField name="startDate" label="Start date"/>
            <flatbook:inputField name="finishDate" label="Finish date"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${requestForm['new']}">
                        <button class="btn btn-default" type="submit">Add Request</button>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </form:form>
    </jsp:body>
</flatbook:layout>
