<%@ page import="org.springframework.samples.flatbook.model.DBImage" %>
<%@ page import="java.util.Collection" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="flats">

    <h2>Flat information</h2>

    <table class="table table-striped">
        <tr>
            <th>Description</th>
            <td><b><c:out value="${flat.description}"/></b></td>
        </tr>
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
    </table>

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
                    <flatbook:image data="${image.data}" fileType="${image.fileType}" cssClass="img-responsive center-block" width="250" height="250"/>
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

</flatbook:layout>
