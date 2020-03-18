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
</flatbook:layout>
