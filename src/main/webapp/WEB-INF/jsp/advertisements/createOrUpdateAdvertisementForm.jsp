<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="advertisements">
    <h2>
    <c:if test="${formAdvertisement['new']}">New </c:if> Advertisement
    </h2>

    <form:form modelAttribute="formAdvertisement" class="form-horizontal" id="add-advertisement-form">
        <div class="form-group has-feedback">
            <flatbook:inputField name="title" label="Title"/>
            <flatbook:textAreaField name="description" label="Description"/>
            <flatbook:inputField name="requirements" label="Requirements"/>
            <flatbook:inputField name="pricePerMonth" label="Price per month (in Euros)"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${formAdvertisement['new']}">
                        <button class="btn btn-default" type="submit">Add Advertisement</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Advertisement</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</flatbook:layout>
