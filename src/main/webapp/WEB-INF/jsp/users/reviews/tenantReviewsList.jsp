<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authentication var="principal" property="principal.username" />
<flatbook:layout pageName="tenantReviews">
<c:if test="${canCreate}">
	<div class="row">
		<div class="col-md-6">
			<spring:url value="/tenants/{tenantId}/reviews/new" var="tenantReviewUrl">
				<spring:param name="tenantId" value="${tenantId}" />
			</spring:url>
			<a role="button" class="btn btn-default btn-lg"
				href="${fn:escapeXml(tenantReviewUrl)}" aria-pressed="true">New
				Review</a>
		</div>
	</div>
</c:if>	
	<h2>These are the reviews of the user:</h2>
	<c:if test="${tenantReviews.size() > 0}">
		<c:forEach var="i" begin="0" end="${tenantReviews.size()-1}">
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-striped">
							<tr>
								<th>Description:</th>
								<td><c:out value="${tenantReviews.get(i).description}" /></td>
								<th>Rate:</th>
								<td><c:out value="${tenantReviews.get(i).rate}" /> of 5</td>
							</tr>
							<tr>
								<th>Creator:</th>
								<td><c:out value="${tenantReviews.get(i).creator.username}" /></td>
								<th>Creation date:</th>
								<td><flatbook:localDate
										date="${tenantReviews.get(i).creationDate}" pattern="dd/MM/yyyy" /></td>
							</tr>
						</table>
					</div>
					<c:if test="${tenantReviews.get(i).creator.username == principal}">
						<div class="row">
							<div class="col-md-6">
								<spring:url
									value="/tenants/{tenantId}/reviews/{tenantReviewId}/remove"
									var="tenantReviewRemoveUrl">
									<spring:param name="tenantId" value="${tenantId}" />
									<spring:param name="tenantReviewId"
										value="${tenantReviews.get(i).id}" />
								</spring:url>
								<a role="button" href="${fn:escapeXml(tenantReviewRemoveUrl)}"
									class="btn btn-default" aria-pressed="true">Remove</a>
							</div>
						</div>
					</c:if>
				</div>
			</div>
		</c:forEach>
	</c:if>
	<c:if test="${tenantReviews.size() == 0}">
		<p>There are no user reviews to show. Please create a new one to
			help other people find their perfect roommate!.</p>
	</c:if>
</flatbook:layout>
