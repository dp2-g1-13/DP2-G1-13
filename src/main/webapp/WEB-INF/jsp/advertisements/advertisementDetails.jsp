<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<flatbook:layout pageName="advertisements">
    <div class="container">
        <div class="row">
            <h2>Flat in <c:out value="${advertisement.flat.address.city}"/>: <c:out value="${advertisement.title}"/></h2>
        </div>

		<div class="row">
			<div class="col-md-6">
				<spring:url value="/flats/{flatId}/reviews/list"
					var="flatReviewListUrl">
					<spring:param name="flatId" value="${advertisement.flat.id}" />
				</spring:url>
				<a role="button" class="btn btn-default btn-lg"
					href="${fn:escapeXml(flatReviewListUrl)}" aria-pressed="true">Reviews
					of the flat</a>
			</div>
		</div>

		<sec:authorize access="isAuthenticated()">
            <sec:authentication property="principal" var="user"/>
            <div class="row" align="center">
                <sec:authorize access="hasAnyAuthority('TENANT')">
                    <c:if test="${!hasFlat}">
                        <div class="col-md-1">
                            <spring:url value="/advertisements/{advId}/requests/new" var="requestUrl">
                                <spring:param name="advId" value="${advertisement.id}"/>
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
        </sec:authorize>
        <br><br>

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
        <div class="row">
            <div class="panel panel-default">
                <div class="panel-heading"><h3>Flat information</h3></div>
                <div class="panel-body">
                    <p><c:out value="${advertisement.flat.description}"/></p>
                </div>
                <table class="table table-striped">
                    <tr>
                        <th>Square meters</th>
                        <td><c:out value="${advertisement.flat.squareMeters}"/></td>
                    </tr>
                    <tr>
                        <th>Number of rooms</th>
                        <td><c:out value="${advertisement.flat.numberRooms}"/></td>
                    </tr>
                    <tr>
                        <th>Number of baths</th>
                        <td><c:out value="${advertisement.flat.numberBaths}"/></td>
                    </tr>
                    <tr>
                        <th>Available services</th>
                        <td><c:out value="${advertisement.flat.availableServices}"/></td>
                    </tr>
                    <tr>
                        <th>Address</th>
                        <td><c:out value="${advertisement.flat.address.address}"/></td>
                    </tr>
                    <tr>
                        <th>City</th>
                        <td><c:out value="${advertisement.flat.address.city}"/></td>
                    </tr>
                    <tr>
                        <th>Postal code</th>
                        <td><c:out value="${advertisement.flat.address.postalCode}"/></td>
                    </tr>
                    <tr>
                        <th>Country</th>
                        <td><c:out value="${advertisement.flat.address.country}"/></td>
                    </tr>
                    <tr>
                        <th>Host</th>
                        <td><c:out value="${host}"/></td>
                    </tr>
                </table>
            </div>
        </div>

        <div class="row">
            <div id="carouselImages" class="carousel slide" data-ride="carousel">
            <ol class="carousel-indicators">
                <c:forEach var="i" begin="0" end="${images.size()-1}">
                    <c:choose>
                        <c:when test="${i == 0}">
                            <li data-target="#carouselImages" data-slide-to="${i}" class="active"></li>
                        </c:when>
                        <c:otherwise>
                            <li data-target="#carouselImages" data-slide-to="${i}"></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

            </ol>
            <div class="carousel-inner">

                <c:set var="firstImage" value="true"/>
                <c:forEach var="image" items="${images}">
                <c:choose>
                <c:when test="${firstImage == 'true'}">
                <c:set var="firstImage" value="false"/>
                <div class="item active">
                    </c:when>
                    <c:otherwise>
                    <div class="item">
                        </c:otherwise>
                        </c:choose>
                        <flatbook:image data="${image.data}" fileType="${image.fileType}" cssClass="img-responsive center-block" width="350" height="350"/>
                    </div>
                    </c:forEach>
                </div>
                <a class="left carousel-control" href="#carouselImages" data-slide="prev">
                    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="right carousel-control" href="#carouselImages" data-slide="next">
                    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                    <span class="sr-only">Next</span>
                </a>
            </div>
            </div>
        </div>
        <br>

    <%@include file="/WEB-INF/jsp/reviews/listReviews.jsp"%>

        <div class="row">
            <div class="panel panel-success"><p>It was created on <flatbook:localDate date="${advertisement.creationDate}" pattern="dd/MM/yyyy"/> and has <c:out value="${advertisement.requests.size()}"/> requests.</p></div>
        </div>
    </div>
</flatbook:layout>
