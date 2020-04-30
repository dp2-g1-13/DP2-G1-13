<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="flats">

    <div class="row">
    <div class="col-md-6">
        <h2>These are your flats:</h2>
    </div>
     <div align="right" class="col-md-6">
        <spring:url value="/flats/new" var="newFlatUrl" />
        <a role="button" href="${fn:escapeXml(newFlatUrl)}" class="btn btn-default" aria-pressed="true">New flat</a>
    </div>
    </div>
    <br>
    <c:if test="${flats.size()>0}">
    <c:forEach var="i" begin="0" end="${flats.size()-1}">
        <div class="row">
            <div class="panel panel-default">
                <div class="panel-heading"><h4>Flat in <c:out value="${flats.get(i).address.address}"/>, <c:out value="${flats.get(i).address.city}"/></h4></div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12">
                            <p class="resp"><c:out value="${flats.get(i).description}"/></p>
                        </div>
                    </div>
                    <br>
                    <div class="row" align="center">
                        <div class="col-md-4">
                            <spring:url value="/flats/{flatId}" var="flatUrl">
                                <spring:param name="flatId" value="${flats.get(i).id}"/>
                            </spring:url>
                            <a role="button" href="${fn:escapeXml(flatUrl)}" class="btn btn-default" aria-pressed="true">See details</a>
                        </div>
                        <div class="col-md-4">
                        <c:if test="${advIds.get(i) != null}">
                            <spring:url value="/advertisements/{advId}" var="advUrl">
                                <spring:param name="advId" value="${advIds.get(i)}"/>
                            </spring:url>
                            <a role="button" href="${fn:escapeXml(advUrl)}" class="btn btn-default" aria-pressed="true">See advertisement</a>
                        </c:if>
                        </div>
                        <div class="col-md-4">
                            <spring:url value="/flats/{flatId}/requests/list" var="requestsUrl">
                                <spring:param name="flatId" value="${flats.get(i).id}"/>
                            </spring:url>
                            <a role="button" href="${fn:escapeXml(requestsUrl)}" class="btn btn-default" aria-pressed="true">See requests</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
    </c:if>

</flatbook:layout>
