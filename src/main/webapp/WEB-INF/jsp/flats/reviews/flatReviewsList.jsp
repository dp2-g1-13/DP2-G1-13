<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:if test="${principal != null}">
	<sec:authentication var="principal" property="principal.username" />
</c:if>	
<flatbook:layout pageName="flatReviews">
<c:if test="${canCreate}">
	<div class="row">
		<div class="col-md-6">
			<spring:url value="/flats/{flatId}/reviews/new" var="flatReviewUrl">
				<spring:param name="flatId" value="${flatId}" />
			</spring:url>
			<a role="button" class="btn btn-default btn-lg"
				href="${fn:escapeXml(flatReviewUrl)}" aria-pressed="true">New
				Review</a>
		</div>
	</div>
	<br>
</c:if>	
	<h2>These are the reviews of the flat:</h2>
	<c:if test="${flatReviews.size() > 0}">
		<c:forEach var="i" begin="0" end="${flatReviews.size()-1}">
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-striped">
							<tr>
								<th>Description:</th>
								<td><c:out value="${flatReviews.get(i).description}" /></td>
								<th>Rate:</th>
								<td><c:out value="${flatReviews.get(i).rate}" /> of 5</td>
							</tr>
							<tr>
								<th>Creator:</th>
								<td><c:out value="${flatReviews.get(i).creator.username}" /></td>
								<th>Creation date:</th>
								<td><flatbook:localDate
										date="${flatReviews.get(i).creationDate}" pattern="dd/MM/yyyy" /></td>
								<c:if test="${flatReviews.get(i).modifiedDate != null}">
									<th>Modification date:</th>
									<td><flatbook:localDate
										date="${flatReviews.get(i).modifiedDate}" pattern="dd/MM/yyyy" /></td>
								</c:if>
							</tr>
						</table>
					</div>
					<c:if test="${flatReviews.get(i).creator.username == principal}">
						<div class="row">
							<div class="col-md-6">
								<spring:url
									value="/flats/{flatId}/reviews/{flatReviewId}/edit"
									var="flatReviewEditUrl">
									<spring:param name="flatId" value="${flatId}" />
									<spring:param name="flatReviewId"
										value="${flatReviews.get(i).id}" />
								</spring:url>
								<a role="button" href="${fn:escapeXml(flatReviewEditUrl)}"
									class="btn btn-default" aria-pressed="true">Edit</a>
								<spring:url
									value="/flats/{flatId}/reviews/{flatReviewId}/remove"
									var="flatReviewRemoveUrl">
									<spring:param name="flatId" value="${flatId}" />
									<spring:param name="flatReviewId"
										value="${flatReviews.get(i).id}" />
								</spring:url>
								<a role="button" href="${fn:escapeXml(flatReviewRemoveUrl)}"
									class="btn btn-default" aria-pressed="true">Remove</a>
							</div>
						</div>
					</c:if>
				</div>
			</div>
		</c:forEach>
	</c:if>
	<c:if test="${flatReviews.size() == 0}">
		<p>There are no flat reviews to show. Please create a new one to
			help other people find their perfect flat!.</p>
	</c:if>
</flatbook:layout>
