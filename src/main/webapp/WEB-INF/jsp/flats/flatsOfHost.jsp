<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="flats">

    <div class="row">
        <h2>These are your flats:</h2>
    </div>

    <c:forEach items="${flats}" var="flat">
        <div class="row">
            <div class="panel panel-default">
                <div class="panel-heading"><h4>Flat in <c:out value="${flat.address.address}"/>, <c:out value="${flat.address.city}"/></h4></div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-6">
                            <p class="resp"><c:out value="${flat.description}"/></p>
                        </div>
                        <div class="col-md-6">
                            <spring:url value="/flats/{flatId}" var="flatUrl">
                                <spring:param name="flatId" value="${flat.id}"/>
                            </spring:url>
                            <a role="button" href="${fn:escapeXml(flatUrl)}" class="btn btn-default" aria-pressed="true">See details</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>

    <div class="row">
        <spring:url value="/flats/new" var="newFlatUrl" />
        <a role="button" href="${fn:escapeXml(newFlatUrl)}" class="btn btn-default" aria-pressed="true">New flat</a>
    </div>

</flatbook:layout>
