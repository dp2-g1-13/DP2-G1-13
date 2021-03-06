<%@page import="java.lang.Integer" %>

<sec:authorize access="hasAnyAuthority('HOST','TENANT', 'ADMIN')">
    <sec:authentication var="principal" property="principal.username" />
</sec:authorize>


<div class="row">
    <div class="panel panel-default">
        <div class="panel-heading"><h3>Reviews</h3></div>
        <div class="panel-body overflow">
        	<c:if test="${reviews.size() == 0}">
				<p>There are no reviews to show.</p>
			</c:if>
            <c:if test="${reviews.size() > 0}">
            <div class="panel-group">
                <c:forEach var="i" begin="0" end="${reviews.size()-1}">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <tr>
                                        <th>Description:</th>
                                        <td><c:out value="${reviews.get(i).description}" /></td>
                                    </tr>
                                    <tr>
                                        <th>Rate:</th>
                                        <td>
                                            <c:forEach var="j" begin="1" end="5">
                                                <c:choose>
                                                    <c:when test="${reviews.get(i).rate - Integer.valueOf(j) >= 0}">
                                                        <span class="glyphicon glyphicon-star"></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="glyphicon glyphicon-star-empty"></span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Creator:</th>
                                        <td><spring:url
                                                        value="/users/{user}"
                                                        var="creatorUrl">
                                                    <spring:param name="user"
                                                                  value="${reviews.get(i).creator.username}" />
												</spring:url>
                                  					 <a href="${fn:escapeXml(creatorUrl)}"
                                     		 		      aria-pressed="true">${reviews.get(i).creator.username}</a></td>
                                    </tr>
                                    <tr>
                                        <th>Creation date:</th>
                                        <td><flatbook:localDate
                                                date="${reviews.get(i).creationDate}" pattern="dd/MM/yyyy" /></td>
                                    </tr>
                                        <c:if test="${reviews.get(i).modifiedDate != null}">
                                            <tr>
                                                <th>Modification date:</th>
                                                <td><flatbook:localDate
                                                    date="${reviews.get(i).modifiedDate}" pattern="dd/MM/yyyy" /></td>
                                            </tr>
                                        </c:if>
                                </table>
                            </div>
                            <c:if test="${reviews.get(i).creator.username == principal}">
                                <div class="row">
                                    <div class="col-md-6">
                                        <c:choose>
                                            <c:when test="${reviews.get(i).getClass().simpleName.equals('FlatReview')}">
                                                <spring:url value="/reviews/{flatReviewId}/edit"
                                                        var="reviewEditUrl">
                                                    <spring:param name="flatReviewId"
                                                                  value="${reviews.get(i).id}" />
                                                </spring:url>
                                                <spring:url
                                                        value="/reviews/{flatReviewId}/delete"
                                                        var="reviewRemoveUrl">
                                                    <spring:param name="flatReviewId"
                                                                  value="${reviews.get(i).id}" />
                                                </spring:url>
                                            </c:when>
                                            <c:otherwise>
                                                <spring:url
                                                        value="/reviews/{tenantReviewId}/edit"
                                                        var="reviewEditUrl">
                                                    <spring:param name="tenantReviewId"
                                                                  value="${reviews.get(i).id}" />
                                                </spring:url>
                                                <spring:url
                                                        value="/reviews/{tenantReviewId}/delete"
                                                        var="reviewRemoveUrl">
                                                    <spring:param name="tenantReviewId"
                                                                  value="${reviews.get(i).id}" />
                                                </spring:url>
                                            </c:otherwise>
                                        </c:choose>

                                        <a role="button" href="${fn:escapeXml(reviewEditUrl)}"
                                           class="btn btn-default" aria-pressed="true">Edit</a>

                                        <a role="button" href="${fn:escapeXml(reviewRemoveUrl)}"
                                           class="btn btn-default" aria-pressed="true">Remove</a>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
            </c:if>
            <c:if test="${canCreateReview}">
            <br>
                <div class="row">
                    <div class="col-md-6">
                        <c:choose>
                            <c:when test="${flatId != null}">
                                <spring:url value="/reviews/new?flatId={flatId}" var="reviewUrl">
                                    <spring:param name="flatId" value="${flatId}" />
                                </spring:url>
                            </c:when>
                            <c:otherwise>
                                <spring:url value="/reviews/new?tenantId={tenantId}" var="reviewUrl">
                                    <spring:param name="tenantId" value="${tenantId}" />
                                </spring:url>
                            </c:otherwise>
                        </c:choose>
                        <a role="button" class="btn btn-default"
                           href="${fn:escapeXml(reviewUrl)}" aria-pressed="true">New Review</a>
                    </div>
                </div>
                <br>
            </c:if>
        </div>
    </div>
</div>

