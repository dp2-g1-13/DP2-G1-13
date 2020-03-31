<%@ page import="org.springframework.samples.flatbook.model.enums.RequestStatus" %>
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
                        <sec:authorize access="hasAnyAuthority('admin', 'HOST')">
                        <tr>
                            <th>Username</th>
                            <td><c:out value="${tenants.get(i).username}"/></td>
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
                        <spring:url value="/advertisements/{advertisementId}" var="advertisementUrl">
                            <spring:param name="advertisementId" value="${advIds.get(i)}"/>
                        </spring:url>
                        <a role="button" href="${fn:escapeXml(advertisementUrl)}" class="btn btn-default" aria-pressed="true">See details</a>
                    </div>
                    </sec:authorize>
                    <sec:authorize access="hasAnyAuthority('HOST', 'admin')">
                        <c:if test="${requests.get(i).status == RequestStatus.PENDING}">
                        <div class="col-md-4" align="center">
                            <spring:url value="/advertisements/{advertisementId}/requests/{requestId}/accept" var="acceptRequestUrl">
                                <spring:param name="advertisementId" value="${advId}"/>
                                <spring:param name="requestId" value="${requests.get(i).id}"/>
                            </spring:url>
                            <a role="button" href="${fn:escapeXml(acceptRequestUrl)}" class="btn btn-success" aria-pressed="true">Accept request</a>
                        </div>
                        <div class="col-md-4" align="center">
                            <spring:url value="/advertisements/{advertisementId}/requests/{requestId}/reject" var="rejectRequestUrl">
                                <spring:param name="advertisementId" value="${advId}"/>
                                <spring:param name="requestId" value="${requests.get(i).id}"/>
                            </spring:url>
                            <a role="button" href="${fn:escapeXml(rejectRequestUrl)}" class="btn btn-danger" aria-pressed="true">Reject request</a>
                        </div>
                        <div class="col-md-4" align="center">
                            <spring:url value="/messages/{username}" var="messageUrl">
                                <spring:param name="username" value="${tenants.get(i).username}"/>
                            </spring:url>
                            <a role="button" href="${fn:escapeXml(messageUrl)}" class="btn btn-info" aria-pressed="true">Send message</a>
                        </div>
                        </c:if>
                    </sec:authorize>
                </div>
            </div>
        </div>
    </c:forEach>
    </c:if>

</flatbook:layout>
