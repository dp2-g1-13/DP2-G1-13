<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="advertisements">

    <jsp:attribute name="customScript">
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBNGjohXXlwq4qcQE66tjVEnfXa5WqM-4c&libraries=places&language=en"></script>
        <script>$(document).ready(function() {
            initMap(<c:out value="${latitude}"/>, <c:out value="${longitude}"/>, 10);
            <c:set var="first" value="true"/>

            var advertisements = [
                    {
        <c:forEach items="${selections}" var="adv">
            <c:choose>
                <c:when test="${first != 'true'}">
                    ,{
                </c:when>
                <c:otherwise>
                    <c:set var="first" value="false"/>
                </c:otherwise>
            </c:choose>
                        title: "${adv.title} - <fmt:formatNumber type="number" minFractionDigits="2" value="${adv.pricePerMonth}"/> \u20AC",
                        position: new google.maps.LatLng(${adv.flat.address.latitude}, ${adv.flat.address.longitude})
                    }
        </c:forEach>
            ];
            createMarkers(advertisements);

        });
        </script>


    </jsp:attribute>
    <jsp:body>

        <h2>These are the results:</h2>

        <div id="map"></div>
        <br>
        <%@include file="/WEB-INF/jsp/advertisements/advertisementsListPanel.jsp"%>

    </jsp:body>

</flatbook:layout>
