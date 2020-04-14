<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="tenantReview">
<jsp:body>
    <h2><c:if test="${tenantReview['new']}">New </c:if>Tenant Review</h2>
    <form:form modelAttribute="tenantReview" class="form-horizontal" id="add-tenantReview-form">
        <div class="form-group has-feedback">
        	<flatbook:textAreaField name="description" label="Description"/>
        	<flatbook:number name="rate" label = "Rate" max="5"  min="0"/>
        	<flatbook:hidden name="creationDate"/>
        	<flatbook:hidden name="creator"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<c:choose>
                    <c:when test="${tenantReview['new']}">
                        <button class="btn btn-default" type="submit">Create Tenant Review</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Tenant Review</button>
                    </c:otherwise>
            	</c:choose>
            </div>
        </div>
    </form:form>
</jsp:body>
</flatbook:layout>
