<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<flatbook:layout pageName="advertisements">

    <jsp:attribute name="customScript">
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBNGjohXXlwq4qcQE66tjVEnfXa5WqM-4c&libraries=places&language=en"></script>
        <script>
            $(document).ready(function() {
                initMap(<c:out value="${flat.address.latitude}"/>, <c:out value="${flat.address.longitude}"/>, 18);
                createMarker(<c:out value="${flat.address.latitude}"/>, <c:out value="${flat.address.longitude}"/>);
            })
        </script>
    </jsp:attribute>
    <jsp:body>
		<sec:authorize access="isAuthenticated()">
        <sec:authentication property="principal" var="user"/>
        <div class="row" align="center">
            <sec:authorize access="hasAnyAuthority('TENANT')">
                <c:if test="${!hasFlat}">
                    <div class="col-md-1">
                        <spring:url value="/flats/{flatId}/requests/new" var="requestUrl">
                            <spring:param name="flatId" value="${advertisement.flat.id}"/>
                        </spring:url>
                        <c:choose>
                            <c:when test="${!requestMade}">
                                <a role="button" href="${fn:escapeXml(requestUrl)}" class="btn btn-primary" aria-pressed="true">Make a request!</a>
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="btn btn-primary" disabled>You have already made a request</button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </sec:authorize>
            <c:if test="${user.username == host}">
            <div class="col-md-6">
                <spring:url value="/advertisements/{advId}/edit" var="advertisementEditUrl">
                    <spring:param name="advId" value="${advertisement.id}"/>
                </spring:url>
                <a role="button" href="${fn:escapeXml(advertisementEditUrl)}" class="btn btn-default" aria-pressed="true">Edit advertisement</a>
            </div>
            <div class="col-md-6">
                <spring:url value="/advertisements/{advId}/delete" var="advertisementDeleteUrl">
                    <spring:param name="advId" value="${advertisement.id}"/>
                </spring:url>
                <a role="button" href="${fn:escapeXml(advertisementDeleteUrl)}" class="btn btn-danger" aria-pressed="true">Delete advertisement</a>
            </div>
            </c:if>

        </div>
    </sec:authorize><br>
        
    <div class="container">
        <div class="row">
            <h2>Flat in <c:out value="${advertisement.flat.address.city}"/>: <c:out value="${advertisement.title}"/></h2>
        </div>

        <div class="row">
            <table class="table table-striped">
                <tr>
                    <th>Description</th>
                    <td><c:out value="${advertisement.description}"/></td>
                </tr>
                <tr>
                    <th>Requirements</th>
                    <td><c:out value="${advertisement.requirements}"/></td>
                </tr>
                <tr>
                    <th>Price per month</th>
                    <td><fmt:formatNumber value="${advertisement.pricePerMonth}" currencySymbol="&euro;" maxFractionDigits="2" type="currency"/></td>
                </tr>
            </table>
        </div>

        <%@include file="/WEB-INF/jsp/flats/flatPanel.jsp" %>

        <div class="row">
            <div class="panel panel-success"><p>It was created on <flatbook:localDate date="${advertisement.creationDate}" pattern="dd/MM/yyyy"/> and has <c:out value="${advertisement.flat.requests.size()}"/> requests.</p></div>
        </div>
    </div>
    </jsp:body>
</flatbook:layout>
