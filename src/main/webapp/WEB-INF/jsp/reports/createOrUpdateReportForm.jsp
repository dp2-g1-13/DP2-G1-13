<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="report">
<jsp:body>
    <h2>New Report</h2>
    <form:form modelAttribute="report" class="form-horizontal" id="add-report-form">
        <div class="form-group has-feedback">
        	<flatbook:textAreaField name="reason" label="Reason"/>
        	<flatbook:hidden name="creationDate"/>
        	<flatbook:hidden name="sender"/>
        	<flatbook:hidden name="receiver"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<button class="btn btn-default" type="submit">Create Report</button>
            </div>
        </div>
    </form:form>
</jsp:body>
</flatbook:layout>
