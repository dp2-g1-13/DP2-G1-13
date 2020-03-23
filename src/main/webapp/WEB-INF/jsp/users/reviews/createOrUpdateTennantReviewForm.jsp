<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="tennantReview">
<jsp:body>
    <h2>New Tennant Review</h2>
    <form:form modelAttribute="tennantReview" class="form-horizontal" id="add-tennantReview-form">
        <div class="form-group has-feedback">
        	<flatbook:textAreaField name="description" label="Description"/>
        	<flatbook:number name="rate" label = "Rate" max="5"  min="0"/>
        	<flatbook:hidden name="creationDate"/>
        	<flatbook:hidden name="creator"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<button class="btn btn-default" type="submit">Create Tennant Review</button>
            </div>
        </div>
    </form:form>
</jsp:body>
</flatbook:layout>
