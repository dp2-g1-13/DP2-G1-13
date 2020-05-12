<div class="panel-body overflow">
<c:if test="${selections.size() == 0}">
	<p>There are no advertisements to show.</p>
</c:if>
<c:forEach items="${selections}" var="advertisement">
    <div class="panel panel-default">
        <div class="panel-heading"><h4><c:out value="${advertisement.title}" /></h4></div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-6">
                    <p><c:out value="${advertisement.description}"/></p>

                    <p><strong><fmt:formatNumber type="number" minFractionDigits="2" value="${advertisement.pricePerMonth}" /> &euro;</strong></p>
                </div>
                <div class="col-md-6">
                    <spring:url value="/advertisements/{advertisementId}" var="advertisementUrl">
                        <spring:param name="advertisementId" value="${advertisement.id}"/>
                    </spring:url>
                    <a role="button" href="${fn:escapeXml(advertisementUrl)}" class="btn btn-default" aria-pressed="true">See details</a>
                </div>
            </div>
        </div>
    </div>
</c:forEach>
</div>
