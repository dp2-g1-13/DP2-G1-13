<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags"%>

<flatbook:layout pageName="report">
	<jsp:body>
    <div class="row">
	    <div class="panel panel-default">
	    	<div class="panel-heading">
					<h2>Requests</h2>
				</div>
		        <div class="panel-body">
		            <table class="table table-striped">
		            <tr>
		                <th>Number Of Requests</th>
		                <td><c:out value="${statistics.numberOfRequests}" /></td>
		            </tr>
		            <tr>
		                <th>Ratio Of Accepted Requests</th>
		                <td><c:out value="${statistics.ratioOfAcceptedRequests}" /></td>
		            </tr>
		            <tr>
		                <th>Ratio Of Rejected Requests</th>
		                <td><c:out value="${statistics.ratioOfRejectedRequests}" /></td>
		            </tr>
		           <tr>
		                <th>Ratio Of Canceled Requests</th>
		                <td><c:out value="${statistics.ratioOfCanceledRequests}" /></td>
		            </tr>
		            <tr>
		                <th>Ratio Of Finished Requests</th>
		                <td><c:out value="${statistics.ratioOfFinishedRequests}" /></td>
		            </tr>
		            </table>
		       </div>
	      </div>
     	 </div>
        <br>
    	<div class="row">
    	<div class="panel panel-default">
	    	<div class="panel-heading">
					<h2>Flats</h2>
				</div>
		        <div class="panel-body">
		            <table class="table table-striped">
		            <tr>
		                <th>Number Of Flats</th>
		                <td><c:out value="${statistics.numberOfFlats}" /></td>
		            </tr>
		            <tr>
		                <th>Number Of Advertisements</th>
		                <td><c:out value="${statistics.numberOfAdvertisements}" /></td>
		            </tr>
		            <tr>
		                <th>Ratio Of Flats With Advertisement</th>
		                <td><c:out value="${statistics.ratioOfFlatsWithAdvertisement}" /></td>
		            </tr>
		            </table>
		            
		            <div class="col-xs-6 panel panel-default">
				    	<div class="panel-heading">
							<h2>Best Reviewed Flats</h2>
						</div>
					        <div class="panel-body">
					            <table class="table table-striped">
					            	<c:set var="contador" value="${1}" />
					              	<c:forEach items="${statistics.topThreeBestReviewedFlats}" var="flat">
						             	<tr>
											<th><c:out value="${contador}º" /></th>
								            <c:set var="contador" value="${contador+1}" />
								            <td>
									            <spring:url value="/flats/{id}" var="flatUrl">
				                               		<spring:param name="id" value="${flat.id}" />
				                                </spring:url>
				                                <a href="${fn:escapeXml(flatUrl)}" aria-pressed="true">Id: ${flat.id}</a>
											</td>
										</tr>
					        		</c:forEach>
					        	</table>
					       </div>
					   </div>
					   
					   <div class="col-xs-6 panel panel-default">
				    	<div class="panel-heading">
							<h2>Worst Reviewed Flats</h2>
						</div>
					        <div class="panel-body">
					            <table class="table table-striped">
					            	<c:set var="contador" value="${1}" />
					              	<c:forEach items="${statistics.topThreeWorstReviewedFlats}" var="flat">
						             	<tr>
											<th><c:out value="${contador}º" /></th>
								            <c:set var="contador" value="${contador+1}" />
								            <td>
									            <spring:url value="/flats/{id}" var="flatUrl">
				                               		<spring:param name="id" value="${flat.id}" />
				                                </spring:url>
				                                <a href="${fn:escapeXml(flatUrl)}" aria-pressed="true">Id: ${flat.id}</a>
											</td>
										</tr>
					        		</c:forEach>
					        	</table>
					       </div>
					   </div>
			      </div>
		       </div>
		   </div>
		   <br>
    	<div class="row">
    	<div class="panel panel-default">
	    	<div class="panel-heading">
				<h2>Users</h2>
				</div>
		        <div class="panel-body">
		            <table class="table table-striped">
		            <tr>
		                <th>Number Of Users</th>
		                <td><c:out value="${statistics.numberOfUsers}" /></td>
		            </tr>
		            </table>
		            
		            <div class="col-xs-6 panel panel-default">
				    	<div class="panel-heading">
							<h2>Best Reviewed Tenants</h2>
						</div>
					        <div class="panel-body">
					            <table class="table table-striped">
					            	<c:set var="contador" value="${1}" />
					              	<c:forEach items="${statistics.topThreeBestReviewedTenants}" var="user">
						             	<tr>
											<th><c:out value="${contador}º" /></th>
								            <c:set var="contador" value="${contador+1}" />
								            <td>
									            <spring:url value="/users/{id}" var="userUrl">
				                               		<spring:param name="id" value="${user.username}" />
				                                </spring:url>
				                                <a href="${fn:escapeXml(userUrl)}" aria-pressed="true">Username: ${user.username}</a>
											</td>
										</tr>
					        		</c:forEach>
					        	</table>
					       </div>
					   </div>
					   
					   <div class="col-xs-6 panel panel-default">
				    	<div class="panel-heading">
							<h2>Best Reviewed Hosts</h2>
						</div>
					        <div class="panel-body">
					            <table class="table table-striped">
					            	<c:set var="contador" value="${1}" />
					              	<c:forEach items="${statistics.topThreeBestReviewedHosts}" var="user">
						             	<tr>
											<th><c:out value="${contador}º" /></th>
								            <c:set var="contador" value="${contador+1}" />
								            <td>
									            <spring:url value="/users/{id}" var="userUrl">
				                               		<spring:param name="id" value="${user.username}" />
				                                </spring:url>
				                                <a href="${fn:escapeXml(userUrl)}" aria-pressed="true">Username: ${user.username}</a>
											</td>
										</tr>
					        		</c:forEach>
					        	</table>
					       </div>
			      		</div>
			      		
			      		<div class="col-xs-4 panel panel-default">
				    	<div class="panel-heading">
							<h2>Worst Reviewed Tenants</h2>
						</div>
					        <div class="panel-body">
					            <table class="table table-striped">
					            	<c:set var="contador" value="${1}" />
					              	<c:forEach items="${statistics.topThreeBestReviewedTenants}" var="user">
						             	<tr>
											<th><c:out value="${contador}º" /></th>
								            <c:set var="contador" value="${contador+1}" />
								            <td>
									            <spring:url value="/users/{id}" var="userUrl">
				                               		<spring:param name="id" value="${user.username}" />
				                                </spring:url>
				                                <a href="${fn:escapeXml(userUrl)}" aria-pressed="true">Username: ${user.username}</a>
											</td>
										</tr>
					        		</c:forEach>
					        	</table>
					       </div>
			      		</div>
			      		
			      		<div class="col-xs-4 panel panel-default">
				    	<div class="panel-heading">
							<h2>Worst Reviewed Hosts</h2>
						</div>
					        <div class="panel-body">
					            <table class="table table-striped">
					            	<c:set var="contador" value="${1}" />
					              	<c:forEach items="${statistics.topThreeWorstReviewedHosts}" var="user">
						             	<tr>
											<th><c:out value="${contador}º" /></th>
								            <c:set var="contador" value="${contador+1}" />
								            <td>
									            <spring:url value="/users/{id}" var="userUrl">
				                               		<spring:param name="id" value="${user.username}" />
				                                </spring:url>
				                                <a href="${fn:escapeXml(userUrl)}" aria-pressed="true">Username: ${user.username}</a>
											</td>
										</tr>
					        		</c:forEach>
					        	</table>
					       </div>
			      		</div>
			      		
			      		<div class="col-xs-4 panel panel-default">
				    	<div class="panel-heading">
							<h2>Most Reported Users</h2>
						</div>
					        <div class="panel-body">
					            <table class="table table-striped">
					            	<c:set var="contador" value="${1}" />
					              	<c:forEach items="${statistics.topThreeMostReportedUsers}" var="user">
						             	<tr>
											<th><c:out value="${contador}º" /></th>
								            <c:set var="contador" value="${contador+1}" />
								            <td>
									            <spring:url value="/users/{id}" var="userUrl">
				                               		<spring:param name="id" value="${user.username}" />
				                                </spring:url>
				                                <a href="${fn:escapeXml(userUrl)}" aria-pressed="true">Username: ${user.username}</a>
											</td>
										</tr>
					        		</c:forEach>
					        	</table>
					       </div>
			      		</div>
		       </div>
		   </div>
</jsp:body>
</flatbook:layout>
