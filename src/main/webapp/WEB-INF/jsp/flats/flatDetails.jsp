<%@ page import="org.springframework.samples.flatbook.model.DBImage" %>
<%@ page import="java.util.Collection" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<flatbook:layout pageName="flats">

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
    <div class="container">

            <sec:authentication var="user" property="principal.username" />
            <c:if test="${user == host}">
                <div class="row" >
                    <div align="left" class="col-md-4">
                        <spring:url value="/flats/{flatId}/edit" var="flatUrl">
                            <spring:param name="flatId" value="${flat.id}"/>
                        </spring:url>
                        <a role="button" href="${fn:escapeXml(flatUrl)}" class="btn btn-default" aria-pressed="true">Edit flat</a>
                    </div>
                    <c:if  test="${!existAd}">
                        <div align="center" class="col-md-4">
                            <spring:url value="/flats/{flatId}/advertisements/new" var="advertisementUrl">
                                <spring:param name="flatId" value="${flat.id}"/>
                            </spring:url>
                            <a role="button" href="${fn:escapeXml(advertisementUrl)}" class="btn btn-default" aria-pressed="true">Publish an ad</a>
                        </div>
                    </c:if>
                    <div align="right" class="col-md-4">
                        <spring:url value="/flats/{flatId}/delete" var="flatDeleteUrl">
                            <spring:param name="flatId" value="${flat.id}"/>
                        </spring:url>
                        <a role="button" href="${fn:escapeXml(flatDeleteUrl)}" class="btn btn-danger" aria-pressed="true" onclick="return confirm('Are you sure you want to delete this flat?')">Delete flat</a>
                    </div>
                </div>
                <br>
            </c:if>

        <%@include file="/WEB-INF/jsp/flats/flatPanel.jsp" %>

    </div>
    </jsp:body>

</flatbook:layout>
