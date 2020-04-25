<%@page import="java.lang.Integer" %>

<sec:authorize access="hasAnyAuthority('HOST','TENANT', 'admin')">
    <sec:authentication var="principal" property="principal.username" />
</sec:authorize>

<c:if test="${reviews.size() > 0}">
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-heading"><h3>Reviews</h3></div>
            <div class="panel-body">
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
                                            <td><c:out value="${reviews.get(i).creator.username}" /></td>
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
                                                    <spring:url
                                                            value="/flats/{flatId}/reviews/{flatReviewId}/edit"
                                                            var="reviewEditUrl">
                                                        <spring:param name="flatId" value="${advertisement.flat.id}" />
                                                        <spring:param name="flatReviewId"
                                                                      value="${reviews.get(i).id}" />
                                                    </spring:url>
                                                    <spring:url
                                                            value="/flats/{flatId}/reviews/{flatReviewId}/remove"
                                                            var="reviewRemoveUrl">
                                                        <spring:param name="flatId" value="${advertisement.flat.id}" />
                                                        <spring:param name="flatReviewId"
                                                                      value="${reviews.get(i).id}" />
                                                    </spring:url>
                                                </c:when>
                                                <c:otherwise>
                                                    <spring:url
                                                            value="/tenants/{tenantId}/reviews/{tenantReviewId}/edit"
                                                            var="reviewEditUrl">
                                                        <spring:param name="tenantId" value="${tenantId}" />
                                                        <spring:param name="tenantReviewId"
                                                                      value="${reviews.get(i).id}" />
                                                    </spring:url>
                                                    <spring:url
                                                            value="/tenants/{tenantId}/reviews/{tenantReviewId}/remove"
                                                            var="reviewRemoveUrl">
                                                        <spring:param name="tenantId" value="${tenantId}" />
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
            </div>
        </div>
    </div>
</c:if>
