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

    <c:forEach var="image" items="${imagesEncoded}">
        <img alt="sample" src="data:image/png;base64,${image}"/>
    </c:forEach>
</flatbook:layout>
