<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="flats">
    <h2>
        <c:if test="${flat['new']}">New </c:if> Flat
    </h2>

    <form:form modelAttribute="flat" class="form-horizontal" id="add-flat-form" enctype="multipart/form-data">
        <div class="form-group has-feedback">
            <flatbook:textAreaField name="description" label="Description"/>
            <flatbook:inputField name="squareMeters" label="Square meters"/>
            <flatbook:inputField name="numberRooms" label="Number of rooms"/>
            <flatbook:inputField name="numberBaths" label="Number of baths"/>
            <flatbook:inputField name="availableServices" label="Available services"/>
            <flatbook:inputField name="address.address" label="Address"/>
            <flatbook:inputField name="address.city" label="City"/>
            <flatbook:inputField name="address.postalCode" label="Postal code"/>
            <flatbook:inputField name="address.country" label="Country"/>
            <flatbook:multipartField name="images" label="Images" multiple="true"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${flat['new']}">
                        <button class="btn btn-default" type="submit">Add Flat</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Flat</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>

    <c:if test="${!flat['new'] and images.size() >= 1}">
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
                <div class="row">
                    <flatbook:image data="${image.data}" fileType="${image.fileType}" cssClass="img-responsive center-block" width="350" height="350"/>
                </div>
                <div class="row" align="center">
                    <spring:url value="/flats/{flatId}/images/{imageId}/delete" var="deleteImageUrl">
                        <spring:param name="flatId" value="${flat.id}"/>
                        <spring:param name="imageId" value="${image.id}"/>
                    </spring:url>
                    <a role="button" href="${fn:escapeXml(deleteImageUrl)}" class="btn btn-danger" aria-pressed="true">Delete image</a>
                </div>
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
    </c:if>
</flatbook:layout>
