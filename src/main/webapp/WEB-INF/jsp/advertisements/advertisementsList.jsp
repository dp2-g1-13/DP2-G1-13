<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="advertisements">

    <h2>These are the results:</h2>

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

</flatbook:layout>
