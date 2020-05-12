<div class="row">
    <div class="panel panel-default">
        <div class="panel-heading"><h3>Flat information</h3></div>
        <div class="panel-body">
            <p><c:out value="${flat.description}"/></p>
        </div>
        <table class="table table-striped">
            <tr>
                <th>Square meters</th>
                <td><c:out value="${flat.squareMeters}"/></td>
            </tr>
            <tr>
                <th>Number of rooms</th>
                <td><c:out value="${flat.numberRooms}"/></td>
            </tr>
            <tr>
                <th>Number of baths</th>
                <td><c:out value="${flat.numberBaths}"/></td>
            </tr>
            <tr>
                <th>Available services</th>
                <td><c:out value="${flat.availableServices}"/></td>
            </tr>
            <tr>
                <th>Address</th>
                <td><c:out value="${flat.address.address}"/></td>
            </tr>
            <tr>
                <th>City</th>
                <td><c:out value="${flat.address.city}"/></td>
            </tr>
            <tr>
                <th>Postal code</th>
                <td><c:out value="${flat.address.postalCode}"/></td>
            </tr>
            <tr>
                <th>Country</th>
                <td><c:out value="${flat.address.country}"/></td>
            </tr>
            <tr>
                <th>Host</th>
                <td><spring:url value="/users/{user}" var="userPage">
                    <spring:param name="user" value="${host}"/>
                </spring:url>
                    <a role="button" href="${fn:escapeXml(userPage)}" aria-pressed="true">${host}</a></td>
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

<div class="row">
    <div id="map"></div>
</div>
<br>

<%@include file="/WEB-INF/jsp/reviews/reviewsList.jsp"%>

<div class="row">
            <div class="panel panel-default">
                <div class="panel-heading"><h3>Tenants</h3></div>
                <div class="panel-body">
                	<c:if test="${flat.tenants.size() == 0}">
						<p>There are no tenants in this flat.</p>
					</c:if>
					<c:if test="${flat.tenants.size() > 0}">
						<ul>
                        <c:forEach var="tenant" items="${flat.tenants}">
                            <spring:url value="/users/{user}" var="tenantPage">
                                <spring:param name="user" value="${tenant.username}"/>
                            </spring:url>
                            <li><a href="${fn:escapeXml(tenantPage)}" aria-pressed="true"><c:out value="${tenant.username}"/></a></li>
                        </c:forEach>
                    	</ul>
					</c:if>
                </div>
            </div>
        </div>

