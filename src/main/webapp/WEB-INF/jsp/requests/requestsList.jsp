<%@ page import="org.springframework.samples.flatbook.model.enums.RequestStatus" %>
<%@ page import="java.time.LocalDate" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<flatbook:layout pageName="requests">

    <h2>These are the results:</h2>

    <c:if test="${requests.size() > 0}">
    <c:forEach var="i" begin="0" end="${requests.size()-1}">
        <div class="panel panel-default">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <sec:authorize access="hasAnyAuthority('ADMIN', 'HOST')">
                        <tr>
                            <th>Username</th>
                            <td>
                                <spring:url value="/users/{user}" var="userPage">
                                    <spring:param name="user" value="${tenants.get(i).username}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(userPage)}"><c:out value="${tenants.get(i).username}"/></a>
                            </td>
                        </tr>
                        </sec:authorize>
                        <tr>
                            <th>Description</th>
                            <td><c:out value="${requests.get(i).description}"/></td>
                        </tr>
                        <tr>
                            <th>Status</th>
                            <td><c:out value="${requests.get(i).status}"/></td>
                        </tr>
                        <tr>
                            <th>Start date</th>
                            <td><flatbook:localDate date="${requests.get(i).startDate}" pattern="dd/MM/yyyy"/></td>
                        </tr>
                        <tr>
                            <th>Finish date</th>
                            <td><flatbook:localDate date="${requests.get(i).finishDate}" pattern="dd/MM/yyyy"/></td>
                        </tr>
                        <tr>
                            <th>Creation date</th>
                            <td><flatbook:localDatetime date="${requests.get(i).creationDate}"/></td>
                        </tr>
                    </table>
                </div>
                <div class="row">
                    <sec:authorize access="hasAuthority('TENANT')">
                    <div class="col-md-6">
                    <c:choose>
                    <c:when test="${advIds.get(i)!=null}"> 
                        <spring:url value="/advertisements/{advId}" var="advUrl">
                            <spring:param name="advId" value="${advIds.get(i)}"/>
                        </spring:url>
                        <a role="button" href="${fn:escapeXml(advUrl)}" class="btn btn-default" aria-pressed="true">See details</a>
                    </c:when>
                    <c:otherwise>
                    	The advertisement that you request for has been deleted.
                    </c:otherwise>
                    </c:choose>
                    </div>
                    </sec:authorize>
                    <sec:authorize access="hasAnyAuthority('HOST', 'ADMIN')">
                    <div align="center" class="row">
                        <c:choose>
                            <c:when test="${requests.get(i).status == RequestStatus.PENDING}">
                                <div class="col-md-6">
                                    <spring:url value="/flats/{flatId}/requests/{requestId}/accept" var="acceptRequestUrl">
                                        <spring:param name="flatId" value="${flatId}"/>
                                        <spring:param name="requestId" value="${requests.get(i).id}"/>
                                    </spring:url>
                                    <a role="button" href="${fn:escapeXml(acceptRequestUrl)}" class="btn btn-success" aria-pressed="true">Accept request</a>
                                </div>
                                <div class="col-md-6" align="center">
                                    <spring:url value="/flats/{flatId}/requests/{requestId}/reject" var="rejectRequestUrl">
                                        <spring:param name="flatId" value="${flatId}"/>
                                        <spring:param name="requestId" value="${requests.get(i).id}"/>
                                    </spring:url>
                                    <a role="button" href="${fn:escapeXml(rejectRequestUrl)}" class="btn btn-danger" aria-pressed="true">Reject request</a>
                                </div>
                            </c:when>
                            <c:when test="${requests.get(i).status == RequestStatus.ACCEPTED}">
                                <c:choose>
                                    <c:when test="${requests.get(i).startDate.isAfter(LocalDate.now())}">
                                        <div class="col-md-12" align="center">
                                            <spring:url value="/flats/{flatId}/requests/{requestId}/cancel" var="cancelRequestUrl">
                                                <spring:param name="flatId" value="${flatId}"/>
                                                <spring:param name="requestId" value="${requests.get(i).id}"/>
                                            </spring:url>
                                            <a role="button" href="${fn:escapeXml(cancelRequestUrl)}" class="btn btn-danger" aria-pressed="true">Cancel request</a>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="col-md-12" align="center">
                                            <spring:url value="/flats/{flatId}/requests/{requestId}/conclude" var="concludeRequestUrl">
                                                <spring:param name="flatId" value="${flatId}"/>
                                                <spring:param name="requestId" value="${requests.get(i).id}"/>
                                            </spring:url>
                                            <a role="button" href="${fn:escapeXml(concludeRequestUrl)}" class="btn btn-warning" aria-pressed="true">Conclude request</a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                        </c:choose>
                        </div>
                    </sec:authorize>
                </div>
            </div>
        </div>
    </c:forEach>
    </c:if>

</flatbook:layout>
