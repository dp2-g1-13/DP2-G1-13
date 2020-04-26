<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="persons">

    <h2>
        ${username}
    </h2>
        <div class="form-group">
            <spring:url value="/reviews/new?tenantId={tenantId}" var="tenantReviewUrl">
				<spring:param name="tenantId" value="${username}" />
			</spring:url>
			<a role="button" class="btn btn-default btn-lg"
				href="${fn:escapeXml(tenantReviewUrl)}" aria-pressed="true">New
				Review</a>
        </div>

</flatbook:layout>
