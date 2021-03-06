<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="name" required="true" rtexprvalue="true" description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<flatbook:menuItem active="${name eq 'home'}" url="/" title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Home</span>
				</flatbook:menuItem>
                <sec:authorize access="hasAuthority('HOST')">
				    <flatbook:menuItem active="${name eq 'flats'}" url="/flats/list" title="find my flats">
					    <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					    <span>Find my flats</span>
				    </flatbook:menuItem>
                </sec:authorize>

				<sec:authorize access="hasAnyAuthority('TENANT', 'HOST')">
				<flatbook:menuItem active="${name eq 'messages'}" url="/messages/list" title="messages">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Messages</span>
				</flatbook:menuItem>
				</sec:authorize>

				<sec:authorize access="hasAnyAuthority('ADMIN')">
					<flatbook:menuItem active="${name eq 'statistics'}" url="/statistics" title="view the statistics">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Statistics</span>
					</flatbook:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAnyAuthority('ADMIN')">
					<flatbook:menuItem active="${name eq 'reports'}" url="/reports/list" title="view the reports">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Reports</span>
					</flatbook:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAnyAuthority('ADMIN')">
					<flatbook:menuItem active="${name eq 'userList'}" url="/users/list" title="view the users">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>User List</span>
					</flatbook:menuItem>
				</sec:authorize>
			</ul>




			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Login</a></li>
					<li class="dropdown"><a href="/users/new" > <span
							class="glyphicon glyphicon-user"></span> Register
					</a>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <span
							class="glyphicon glyphicon-user"></span> <strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />" class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<sec:authorize access="hasAnyAuthority('HOST','TENANT')">
							<li class="divider"></li>

							<li>
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="/users/<sec:authentication property="name"/>" class="btn btn-primary btn-block">My User Page</a> 
												<a href="/users/<sec:authentication property="name"/>/edit" class="btn btn-primary btn-block">My Profile</a> 
												<a href="/users/<sec:authentication property="name"/>/editPassword"	class="btn btn-danger btn-block">Change Password</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							</sec:authorize>
						</ul></li>
						
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
